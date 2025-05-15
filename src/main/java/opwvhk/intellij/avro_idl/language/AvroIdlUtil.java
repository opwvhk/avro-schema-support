package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
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
import groovy.json.StringEscapeUtils;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import opwvhk.intellij.avro_idl.psi.*;
import org.apache.avro.Protocol;
import org.apache.avro.SchemaParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class AvroIdlUtil {
	private static final Logger LOG = Logger.getInstance(AvroIdlUtil.class);
	@NotNull
	public static final Key<Boolean> IS_ERROR_KEY = Key.create("isError");
	private static final boolean IS_JSON_AVAILABLE;
	static {
		boolean isJsonAvailable;
		try {
			Class.forName("com.intellij.json.JsonFileType");
			isJsonAvailable = true;
		} catch (ClassNotFoundException e) {
			isJsonAvailable = false;
		}
		IS_JSON_AVAILABLE = isJsonAvailable;
	}

	@NotNull
	public static List<AvroIdlNameIdentifierOwner> findDeclarationsInScope(@NotNull GlobalSearchScope scope) {

		// Choice:
		// Only schemas, fields, messages, and parameters are considered symbols.
		// Namespaces are not, as these can be specified in multiple locations.

		List<AvroIdlNameIdentifierOwner> result = new ArrayList<>();

		final PsiManager psiManager = PsiManager.getInstance(requireNonNull(scope.getProject()));
		FileTypeIndex.processFiles(AvroIdlFileType.INSTANCE, virtualFile -> {
			ifType(psiManager.findFile(virtualFile), AvroIdlFile.class)
					.flatMap(AvroIdlUtil::readNamedSchemas)
					.flatMap(namedSchema -> Stream.concat(Stream.of(namedSchema),
							ifType(namedSchema, AvroIdlRecordDeclaration.class)
									.map(AvroIdlRecordDeclaration::getRecordBody)
									.filter(Objects::nonNull)
									.map(AvroIdlRecordBody::getFieldDeclarationList)
									.flatMap(List::stream)
									.map(AvroIdlFieldDeclaration::getVariableDeclaratorList)
									.flatMap(List::stream)
					))
					.forEach(result::add);
			return true;
		}, scope);
		return result;
	}

	/**
	 * Read all named schemas from a file. Does not follow imports.
	 *
	 * @param idlFile an IDL file
	 * @return all named schema declarations in the file
	 */
	private static Stream<AvroIdlNamedSchemaDeclaration> readNamedSchemas(@NotNull AvroIdlFile idlFile) {
		return Stream.of(idlFile)
				.filter(PsiFile::isValid)
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
				.filter(PsiFile::isValid)
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

	public static <T, R extends T> Function<T, R> ifType(@NotNull Class<R> type) {
		return object -> type.isInstance(object) ? type.cast(object) : null;
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
					PsiManager psiManager = importDeclaration.getManager();
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

	@NotNull
	public static Stream<LookupElement> findAllSchemaNamesAvailableInIdl(AvroIdlFile idlFile, boolean errorsOnly,
	                                                                     @NotNull String currentNamespace) {
		return findAllSchemaNamesAvailableInIdl(idlFile, errorsOnly, currentNamespace, new SchemaParser());
	}

	@NotNull
	private static Stream<LookupElement> findAllSchemaNamesAvailableInIdl(AvroIdlFile idlFile, boolean errorsOnly,
	                                                                      @NotNull String currentNamespace,
	                                                                      @NotNull SchemaParser avroSchemaParser) {
		final Module module = ModuleUtil.findModuleForPsiElement(idlFile);
		return Stream.concat(
				readNamedSchemas(idlFile)
						.filter(namedSchema -> namedSchema.getFullName() != null)
						.filter(namedSchema -> !errorsOnly || namedSchema.isErrorType())
						.map(namedSchema -> lookupElement(namedSchema, currentNamespace)),
				readImports(idlFile)
						.flatMap(importDeclaration ->
								findAllSchemaNamesAvailableFromImport(module, importDeclaration, errorsOnly,
										currentNamespace, avroSchemaParser))
		);
	}

	@NotNull
	private static Stream<LookupElement> findAllSchemaNamesAvailableFromImport(Module module,
	                                                                           AvroIdlImportDeclaration importDeclaration,
	                                                                           boolean errorsOnly,
	                                                                           @NotNull String currentNamespace,
	                                                                           @NotNull SchemaParser avroSchemaParser) {
		AvroIdlImportType importTypeElement = importDeclaration.getImportType();
		final AvroIdlJsonStringLiteral importedFileReferenceElement = importDeclaration.getJsonStringLiteral();
		VirtualFile importedFile = findReferencedFile(module, importedFileReferenceElement);
		if (importTypeElement == null || importedFile == null) {
			return Stream.empty();
		}

		final IElementType importType = importTypeElement.getFirstChild().getNode().getElementType();
		final PsiManager psiManager = importDeclaration.getManager();
		if (importType == AvroIdlTypes.IDL) {
			return ifType(psiManager.findFile(importedFile), AvroIdlFile.class).flatMap(idlFile ->
					findAllSchemaNamesAvailableInIdl(idlFile, errorsOnly, currentNamespace, avroSchemaParser));
		} else if (importType == AvroIdlTypes.PROTOCOL && IS_JSON_AVAILABLE) {
			try (InputStream inputStream = importedFile.getInputStream()) {
				final Protocol protocol = Protocol.parse(inputStream);
				return protocol.getTypes().stream()
						.filter(schema -> !errorsOnly || schema.isError())
						.map(schema -> AvroIdlJsonUtil.createLookupElementForSchemaInJsonFile(currentNamespace, importedFileReferenceElement, psiManager,
								importedFile, schema));
			} catch (Exception e) {
				LOG.warn("Failed to read file " + importedFile.getCanonicalPath(), e);
			}
		} else if (importType == AvroIdlTypes.SCHEMA && IS_JSON_AVAILABLE) {
			try (InputStream inputStream = importedFile.getInputStream()) {
				avroSchemaParser.parse(inputStream);
				return avroSchemaParser.getParsedNamedSchemas().stream()
						.filter(schema -> !errorsOnly || schema.isError())
						.map(schema -> AvroIdlJsonUtil.createLookupElementForSchemaInJsonFile(currentNamespace, importedFileReferenceElement, psiManager,
								importedFile, schema));
			} catch (Exception e) {
				LOG.warn("Failed to read file " + importedFile.getCanonicalPath(), e);
			}
		}
		// Read failure.
		return Stream.empty();
	}

	private static VirtualFile findReferencedFile(Module module,
	                                              AvroIdlJsonStringLiteral importedFileReferenceElement) {
		if (importedFileReferenceElement == null) {
			return null;
		}

		String importedFileReference = getJsonString(importedFileReferenceElement);
		assert importedFileReference != null;
		return Optional.of(importedFileReferenceElement)
				.map(PsiElement::getContainingFile)
				.filter(PsiFile::isValid)
				.map(PsiFile::getVirtualFile)
				.filter(VirtualFile::isValid)
				.map(VirtualFile::getParent)
				.filter(VirtualFile::isValid)
				.map(vFile -> vFile.findFileByRelativePath(importedFileReference))
				.filter(VirtualFile::isValid)
				.or(() -> Stream.ofNullable(module)
						.map(ModuleRootManager::getInstance)
						.map(rootMgr -> rootMgr.orderEntries().classes().getRoots())
						.flatMap(Stream::of)
						.map(root -> root.findFileByRelativePath(importedFileReference))
						.filter(Objects::nonNull)
						.filter(VirtualFile::isValid)
						.findFirst()
				).orElse(null);
	}

	@NotNull
	private static LookupElement lookupElement(@NotNull AvroIdlNamedSchemaDeclaration schemaDeclaration,
	                                           @NotNull String currentNamespace) {
		final LookupElement lookupElement = lookupElement(schemaDeclaration,
				requireNonNull(schemaDeclaration.getName()),
				requireNonNull(schemaDeclaration.getFullName()), AvroIdlPsiUtil.getNamespace(schemaDeclaration),
				currentNamespace);
		lookupElement.putUserData(IS_ERROR_KEY, schemaDeclaration.isErrorType());
		return lookupElement;
	}

	@NotNull
	static LookupElement lookupElement(@NotNull PsiElement psiElement, @NotNull String schemaName,
	                                           @NotNull String schemaFullName,
	                                           @Nullable String namespace, @NotNull String currentNamespace) {
		if (namespace == null || namespace.isEmpty()) {
			return LookupElementBuilder.create(psiElement, schemaName);
		} else if (namespace.equals(currentNamespace)) {
			return LookupElementBuilder.create(psiElement, schemaName).withLookupString(schemaFullName)
					.withTypeText(namespace, AvroIdlIcons.getAvroIdlIcon(psiElement), false);
		} else {
			return LookupElementBuilder.create(psiElement, schemaFullName).withLookupString(schemaName)
					.withTypeText(namespace, AvroIdlIcons.getAvroIdlIcon(psiElement), false);
		}
	}

	@Nullable
	public static String getJsonString(@Nullable AvroIdlJsonValue jsonValue) {
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
