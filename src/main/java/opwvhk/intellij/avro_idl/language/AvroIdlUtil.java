package opwvhk.intellij.avro_idl.language;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import groovy.json.StringEscapeUtils;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AvroIdlUtil {
	/**
	 * Searches a protocol for named types (record, enum, fixed) with a given name.
	 *
	 * @param protocol       a protocol
	 * @param name           the name to search for
	 * @param incompleteCode if the code we're searching for is incomplete (we should be more lenient)
	 * @return matching protocols
	 */
	@NotNull
	public static List<AvroIdlNamedSchemaDeclaration> findNamedTypes(AvroIdlProtocolDeclaration protocol, String name, boolean incompleteCode) {
		var availableTypes = findAllAvailableTypes(protocol);
		Stream<AvroIdlNamedSchemaDeclaration> namedTypes;
		if (name == null) {
			namedTypes = availableTypes.stream();
		} else if(incompleteCode) {
			namedTypes = availableTypes.stream().filter(namedType -> {
				final String declaredFullName = namedType.getFullName();
				return declaredFullName != null && declaredFullName.contains(name);
			});
		} else{
			namedTypes = availableTypes.stream().filter(namedType -> {
				final String fullName = namedType.getFullName();
				return name.equals(fullName);
			});
		}
		return namedTypes.collect(Collectors.toList());
	}

	@NotNull
	public static List<AvroIdlNamedSchemaDeclaration> findAllAvailableTypes(Project project) {
		final PsiManager psiManager = PsiManager.getInstance(project);
		final Set<AvroIdlProtocolDeclaration> protocols = FileTypeIndex.getFiles(AvroIdlFileType.INSTANCE, GlobalSearchScope.allScope(project)).stream()
			.map(virtualFile -> (AvroIdlFile) psiManager.findFile(virtualFile))
			.filter(Objects::nonNull)
			.flatMap(avroIdlFile -> PsiTreeUtil.getChildrenOfTypeAsList(avroIdlFile, AvroIdlProtocolDeclaration.class).stream())
			.collect(Collectors.toSet());
		return findAllAvailableTypes(protocols);
	}

	@NotNull
	public static List<AvroIdlNamedSchemaDeclaration> findAllAvailableTypes(AvroIdlProtocolDeclaration protocol) {
		return findAllAvailableTypes(Collections.singleton(protocol));
	}

	@NotNull
	private static List<AvroIdlNamedSchemaDeclaration> findAllAvailableTypes(@NotNull Set<AvroIdlProtocolDeclaration> protocols) {
		Set<String> protocolsSeen = new HashSet<>();
		Deque<AvroIdlProtocolDeclaration> protocolsToGo = new ArrayDeque<>(protocols);

		List<AvroIdlNamedSchemaDeclaration> availableTypes = new ArrayList<>();
		AvroIdlProtocolDeclaration protocolDef;
		while ((protocolDef = protocolsToGo.poll()) != null) {
			if (!protocolsSeen.add(protocolDef.getFullName())) {
				// We've already processed this protocol.
				continue;
			}
			final AvroIdlProtocolBody protocolBody = protocolDef.getProtocolBody();
			if (protocolBody == null) {
				continue;
			}
			availableTypes.addAll(protocolBody.getNamedSchemaDeclarationList());
			// TODO: Follow imports
		}
		return availableTypes;
	}

	public static String getJsonString(@Nullable AvroIdlJsonValue jsonValue) {
		return jsonValue == null ? null : stringValue(jsonValue.getStringLiteral());
	}

	@Nullable
	public static String stringValue(@Nullable PsiElement stringLiteral) {
		if (stringLiteral == null) {
			return null;
		} else {
			String escapedLiteralWithQuotes = stringLiteral.getText();
			String escapedLiteral = escapedLiteralWithQuotes.substring(1, escapedLiteralWithQuotes.length() - 1);
			return StringEscapeUtils.unescapeJavaScript(escapedLiteral);
		}
	}

	public static boolean hasJsonIntValueForInteger(@Nullable AvroIdlJsonValue jsonValue) {
		final PsiElement intLiteral = jsonValue == null ? null : jsonValue.getIntLiteral();
		if (intLiteral == null) {
			return false;
		}
		long value = Long.parseLong(intLiteral.getText());
		return value == (int) value; // If values don't match, value is larger than Integer.MAX_VALUE
	}

	public static Long getJsonIntValue(@Nullable AvroIdlJsonValue jsonValue) {
		final PsiElement intLiteral = jsonValue == null ? null : jsonValue.getIntLiteral();
		if (intLiteral == null) {
			return null;
		}
		return Long.parseLong(intLiteral.getText());
	}
}
