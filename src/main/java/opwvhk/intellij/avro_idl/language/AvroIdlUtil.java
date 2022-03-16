package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.json.psi.*;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import groovy.json.StringEscapeUtils;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.psi.*;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class AvroIdlUtil {
	private static final Logger LOG = Logger.getInstance(AvroIdlUtil.class);
	public static final @NotNull Key<Boolean> IS_ERROR_KEY = Key.create("isError");

	public static @NotNull List<NavigationItem> findNavigableNamedSchemasInProject(Project project) {
		List<NavigationItem> result = new ArrayList<>();

		final PsiManager psiManager = PsiManager.getInstance(project);
		FileTypeIndex.processFiles(AvroIdlFileType.INSTANCE, virtualFile -> {
			ifType(psiManager.findFile(virtualFile), AvroIdlFile.class)
				.flatMap(AvroIdlUtil::readNamedSchemas)
				.forEach(namedSchemaDeclaration -> {
					result.add((NavigationItem)namedSchemaDeclaration); // All AvroIdl PSI classes implement NavigationItem
				});
			return true;
		}, GlobalSearchScope.allScope(project));
		return result;
	}


	private static Stream<AvroIdlNamedSchemaDeclaration> readNamedSchemas(@NotNull AvroIdlFile idlFile) {
		return Stream.of(idlFile)
			.flatMap(avroIdlFile -> Stream.of(avroIdlFile.getChildren()))
			.flatMap(child -> Stream.concat(
				ifType(child, AvroIdlProtocolDeclaration.class)
					.map(AvroIdlProtocolDeclaration::getProtocolBody)
					.filter(Objects::nonNull)
					.map(AvroIdlProtocolBody::getNamedSchemaDeclarationList)
					.flatMap(List::stream),
				ifType(child, AvroIdlNamedSchemaDeclaration.class)
			));
	}

	private static Stream<AvroIdlImportDeclaration> readImports(@NotNull AvroIdlFile idlFile) {
		return Stream.of(idlFile)
			.flatMap(avroIdlFile -> Stream.of(avroIdlFile.getChildren()))
			.flatMap(child -> Stream.concat(
				ifType(child, AvroIdlProtocolDeclaration.class)
					.map(AvroIdlProtocolDeclaration::getProtocolBody)
					.filter(Objects::nonNull)
					.map(AvroIdlProtocolBody::getImportDeclarationList)
					.flatMap(List::stream),
				ifType(child, AvroIdlImportDeclaration.class)
			));
	}

	public static <T> Stream<T> ifType(@Nullable Object object, @NotNull Class<T> type) {
		return type.isInstance(object) ? Stream.of(type.cast(object)) : Stream.empty();
	}

	public static boolean hasMessages(@NotNull AvroIdlProtocolDeclaration protocolDeclaration) {
		final Module module = ModuleUtil.findModuleForPsiElement(protocolDeclaration);
		return hasMessages(module, protocolDeclaration.getProtocolBody());
	}

	public static boolean hasMessages(@Nullable Module module, @Nullable AvroIdlProtocolBody protocolBody) {
		if (protocolBody == null) {
			return false;
		}
		if (!protocolBody.getMessageDeclarationList().isEmpty()) {
			return true;
		} else {
			return protocolBody.getImportDeclarationList().stream().anyMatch(importDeclaration -> {
				AvroIdlImportType importTypeKeyword = importDeclaration.getImportType();
				VirtualFile importedFile = findReferencedFile(module, importDeclaration.getJsonStringLiteral());
				if (importTypeKeyword == null || importedFile == null) {
					return false;
				}
				IElementType importType = importTypeKeyword.getFirstChild().getNode().getElementType();
				if (importType == AvroIdlTypes.IDL) {
					PsiManager psiManager = PsiManager.getInstance(importDeclaration.getProject());
					return ifType(psiManager.findFile(importedFile), AvroIdlFile.class)
						.flatMap(importedIdlFile -> Stream.of(importedIdlFile.getChildren()))
						.flatMap(child -> ifType(child, AvroIdlProtocolDeclaration.class)
							.map(AvroIdlProtocolDeclaration::getProtocolBody)
							.filter(Objects::nonNull))
						.anyMatch(importedProtocolBody -> hasMessages(module, importedProtocolBody));
				} else if (importType == AvroIdlTypes.PROTOCOL) {
					try (InputStream inputStream = importedFile.getInputStream()) {
						final Protocol protocol = Protocol.parse(inputStream);
						return !protocol.getMessages().isEmpty();
					} catch (Exception e) {
						LOG.warn("Failed to read file " + importedFile.getCanonicalPath(), e);
					}
				}
				return false;
			});
		}
	}

	public static @NotNull Stream<LookupElement> findAllSchemaNamesAvailableInIdl(@NotNull AvroIdlFile idlFile) {
		return findAllSchemaNamesAvailableInIdl(idlFile, false, "");
	}

	public static @NotNull Stream<LookupElement> findAllSchemaNamesAvailableInIdl(AvroIdlFile idlFile, boolean errorsOnly, @NotNull String currentNamespace) {
		return findAllSchemaNamesAvailableInIdl(idlFile, errorsOnly, currentNamespace, new Schema.Parser());
	}

	private static @NotNull Stream<LookupElement> findAllSchemaNamesAvailableInIdl(AvroIdlFile idlFile, boolean errorsOnly, @NotNull String currentNamespace,
	                                                                               @NotNull Schema.Parser avroSchemaParser) {
		final Module module = ModuleUtil.findModuleForPsiElement(idlFile);
		return Stream.concat(
			readNamedSchemas(idlFile)
				.filter(namedSchema -> namedSchema.getFullName() != null)
				.filter(namedSchema -> !errorsOnly || namedSchema.isErrorType())
				.map(namedSchema -> lookupElement(namedSchema, currentNamespace)),
			readImports(idlFile)
				.flatMap(importDeclaration ->
					findAllSchemaNamesAvailableFromImport(module, importDeclaration, errorsOnly, currentNamespace, avroSchemaParser))
		);
	}

	private static @NotNull Stream<LookupElement> findAllSchemaNamesAvailableFromImport(Module module, AvroIdlImportDeclaration importDeclaration,
	                                                                                    boolean errorsOnly,
	                                                                                    @NotNull String currentNamespace,
	                                                                                    @NotNull Schema.Parser avroSchemaParser) {
		AvroIdlImportType importTypeElement = importDeclaration.getImportType();
		final AvroIdlJsonStringLiteral importedFileReferenceElement = importDeclaration.getJsonStringLiteral();
		VirtualFile importedFile = findReferencedFile(module, importedFileReferenceElement);
		if (importTypeElement == null || importedFile == null) {
			return Stream.empty();
		}

		final IElementType importType = importTypeElement.getFirstChild().getNode().getElementType();
		final PsiManager psiManager = PsiManager.getInstance(importDeclaration.getProject());
		if (importType == AvroIdlTypes.IDL) {
			return ifType(psiManager.findFile(importedFile), AvroIdlFile.class).flatMap(idlFile ->
				findAllSchemaNamesAvailableInIdl(idlFile, errorsOnly, currentNamespace, avroSchemaParser));
		} else if (importType == AvroIdlTypes.PROTOCOL) {
			try (InputStream inputStream = importedFile.getInputStream()) {
				final Protocol protocol = Protocol.parse(inputStream);
				return protocol.getTypes().stream()
					.filter(schema -> !errorsOnly || schema.isError())
					.map(schema -> createLookupElement(currentNamespace, importedFileReferenceElement, psiManager, importedFile, schema));
			} catch (Exception e) {
				LOG.warn("Failed to read file " + importedFile.getCanonicalPath(), e);
			}
		} else if (importType == AvroIdlTypes.SCHEMA) {
			try (InputStream inputStream = importedFile.getInputStream()) {
				avroSchemaParser.parse(inputStream);
				return avroSchemaParser.getTypes().values().stream()
					.filter(schema -> !errorsOnly || schema.isError())
					.map(schema -> createLookupElement(currentNamespace, importedFileReferenceElement, psiManager, importedFile, schema));
			} catch (Exception e) {
				LOG.warn("Failed to read file " + importedFile.getCanonicalPath(), e);
			}
		}
		// Read failure.
		return Stream.empty();
	}

	private static VirtualFile findReferencedFile(Module module, AvroIdlJsonStringLiteral importedFileReferenceElement) {
		if (importedFileReferenceElement == null) {
			return null;
		}

		String importedFileReference = getJsonString(importedFileReferenceElement);
		assert importedFileReference != null;
		return Optional.of(importedFileReferenceElement)
			.map(PsiElement::getContainingFile)
			.map(PsiFile::getVirtualFile)
			.map(VirtualFile::getParent)
			.map(vFile -> vFile.findFileByRelativePath(importedFileReference))
			.or(() -> Stream.ofNullable(module)
				.map(ModuleRootManager::getInstance)
				.map(rootMgr -> rootMgr.orderEntries().classes().getRoots())
				.flatMap(Stream::of)
				.map(root -> root.findFileByRelativePath(importedFileReference))
				.filter(Objects::nonNull)
				.findFirst()
			).orElse(null);
	}

	private static @NotNull LookupElement createLookupElement(@NotNull String namespace, AvroIdlJsonStringLiteral importedFileReferenceElement,
	                                                          PsiManager psiManager,
	                                                          VirtualFile importedFile, Schema schema) {
		final PsiFile psiProtocolFile = psiManager.findFile(importedFile);
		if (psiProtocolFile instanceof JsonFile) {
			final PsiElement[] elements = PsiTreeUtil.collectElements(psiProtocolFile,
				element -> isSchemaNameJsonStringLiteral(element, schema));
			if (elements.length > 0) {
				return lookupElement(elements[0], schema, namespace);
			}
		}
		return lookupElement(importedFileReferenceElement, schema, namespace);
	}

	private static boolean isSchemaNameJsonStringLiteral(@NotNull PsiElement element, @NotNull Schema schema) {
		if (element instanceof JsonStringLiteral &&
			element.getParent() instanceof JsonProperty &&
			element.getParent().getParent() instanceof JsonObject) {
			// All named schema names in an Avro schema are property values in an object
			final JsonProperty jsonProperty = (JsonProperty)element.getParent();
			final JsonObject jsonSchema = (JsonObject)jsonProperty.getParent();
			if (!"name".equals(jsonProperty.getName())) {
				return false;
			}
			final String textValue = ElementManipulators.getValueText(element);
			if (textValue.contains(".")) {
				return textValue.equals(schema.getFullName());
			} else if (textValue.equals(schema.getName())) {
				JsonElement parent = jsonSchema;
				while (parent != null && !(parent instanceof JsonFile)) {
					final JsonProperty namespaceProperty = jsonSchema.findProperty("namespace");
					if (namespaceProperty != null) {
						final JsonValue namespaceValue = namespaceProperty.getValue();
						return namespaceValue instanceof JsonStringLiteral &&
							ElementManipulators.getValueText(namespaceValue).equals(schema.getNamespace());
					}
					parent = (JsonElement)parent.getParent();
				}
			}
		}
		return false;
	}

	private static @NotNull LookupElement lookupElement(@NotNull PsiElement psiElement, @NotNull Schema schema, @NotNull String currentNamespace) {
		final String namespace = schema.getNamespace();
		final String schemaName = schema.getName();
		final String schemaFullName = schema.getFullName();
		final LookupElement lookupElement = lookupElement(psiElement, schemaName, schemaFullName, namespace, currentNamespace);
		lookupElement.putUserData(IS_ERROR_KEY, schema.getType() == Schema.Type.RECORD && schema.isError());
		return lookupElement;
	}

	private static @NotNull LookupElement lookupElement(@NotNull AvroIdlNamedSchemaDeclaration schemaDeclaration, @NotNull String currentNamespace) {
		final LookupElement lookupElement = lookupElement(schemaDeclaration, requireNonNull(schemaDeclaration.getName()),
			requireNonNull(schemaDeclaration.getFullName()), AvroIdlPsiUtil.getNamespace(schemaDeclaration), currentNamespace);
		lookupElement.putUserData(IS_ERROR_KEY, schemaDeclaration.isErrorType());
		return lookupElement;
	}

	private static @NotNull LookupElement lookupElement(@NotNull PsiElement psiElement, @NotNull String schemaName, @NotNull String schemaFullName,
	                                                    @NotNull String namespace, @NotNull String currentNamespace) {
		if (namespace.isEmpty()) {
			return LookupElementBuilder.create(psiElement, schemaName);
		} else if (namespace.equals(currentNamespace)) {
			return LookupElementBuilder.create(psiElement, schemaName).withLookupString(schemaFullName).withTypeText(namespace);
		} else {
			return LookupElementBuilder.create(psiElement, schemaFullName).withLookupString(schemaName).withTypeText(namespace);
		}
	}

	public static @Nullable String getJsonString(@Nullable AvroIdlJsonValue jsonValue) {
		if (jsonValue instanceof AvroIdlJsonStringLiteral) {
			final TextRange range = ElementManipulators.getValueTextRange(jsonValue);
			String escapedLiteral = range.substring(jsonValue.getText());
			return StringEscapeUtils.unescapeJavaScript(escapedLiteral);
		}
		return null;
	}

	public static Long getJsonIntValue(@Nullable AvroIdlJsonValue jsonValue) {
		final PsiElement intLiteral = jsonValue == null ? null : jsonValue.getIntLiteral();
		if (intLiteral == null) {
			return null;
		}
		return Long.parseLong(intLiteral.getText());
	}
}
