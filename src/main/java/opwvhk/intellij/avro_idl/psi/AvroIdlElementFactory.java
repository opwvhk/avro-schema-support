package opwvhk.intellij.avro_idl.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public class AvroIdlElementFactory {
	private final Project myProject;

	public AvroIdlElementFactory(@NotNull Project project) {
		myProject = project;
	}

	public @NotNull PsiElement createIdentifier(@NotNull String name) {
		// Yes, in theory this can cause syntax errors. We're assuming the name has been vetted by AvroIdlNamesValidator.
		final AvroIdlFile file = createDummyFile(String.format("protocol %s {}", name));
		final AvroIdlProtocolDeclaration protocol = (AvroIdlProtocolDeclaration) file.getFirstChild();
		final PsiElement nameIdentifier = protocol.getNameIdentifier();
		assert nameIdentifier != null;
		return nameIdentifier;
	}

	public @NotNull AvroIdlJsonStringLiteral createJsonStringLiteral(@NotNull String text) {
		final AvroIdlFile file = createDummyFile(String.format("protocol Foo { import idl \"%s\"; }", StringUtil.escapeStringCharacters(text)));
		final AvroIdlProtocolBody protocolBody = extractAvroIdlProtocolBody(file);
		final AvroIdlImportDeclaration avroIdlImportDeclaration = protocolBody.getImportDeclarationList().get(0);
		final AvroIdlJsonStringLiteral jsonStringLiteral = avroIdlImportDeclaration.getJsonStringLiteral();
		assert jsonStringLiteral != null;
		return jsonStringLiteral;
	}

	public @NotNull PsiComment createMultilineComment(@NotNull AvroIdlDocumentation documentation) {
		final String commentText = documentation.getDocComment().getText();
		final String commentContent = commentText.substring(3, commentText.length() - 2);
		final AvroIdlFile file = createDummyFile(String.format("/*%s*/ protocol Foo { }", commentContent));
		return (PsiComment)file.getFirstChild();
	}

	private @NotNull AvroIdlProtocolBody extractAvroIdlProtocolBody(AvroIdlFile file) {
		final AvroIdlProtocolDeclaration protocol = (AvroIdlProtocolDeclaration) file.getFirstChild();
		return requireNonNull(protocol.getProtocolBody());
	}

	public @NotNull AvroIdlNullableType makeOptional(@NotNull AvroIdlNullableType type) {
		if (type.isOptional()) {
			return type;
		}
		final AvroIdlFile file = createDummyFile(String.format("protocol Foo { record Bar { %s? field; } }", type.getText()));
		final AvroIdlProtocolBody protocolBody = extractAvroIdlProtocolBody(file);
		final AvroIdlRecordDeclaration recordDeclaration = (AvroIdlRecordDeclaration)protocolBody.getNamedSchemaDeclarationList().get(0);
		final AvroIdlFieldDeclaration fieldDeclaration = requireNonNull(recordDeclaration.getRecordBody()).getFieldDeclarationList().get(0);
		return (AvroIdlNullableType)fieldDeclaration.getType();
	}

	public @NotNull AvroIdlUnionType unionWithNull(@NotNull AvroIdlType type, boolean nullLast) {
		final AvroIdlFile file = createDummyFile("protocol Foo { record Bar { union { null, null } field; } }");
		final AvroIdlProtocolBody protocolBody = extractAvroIdlProtocolBody(file);
		final AvroIdlRecordDeclaration recordDeclaration = (AvroIdlRecordDeclaration)protocolBody.getNamedSchemaDeclarationList().get(0);
		final AvroIdlFieldDeclaration fieldDeclaration = requireNonNull(recordDeclaration.getRecordBody()).getFieldDeclarationList().get(0);
		final AvroIdlUnionType unionType = (AvroIdlUnionType)fieldDeclaration.getType();

		unionType.getTypeList().get(nullLast ? 0 : 1).replace(type);
		return unionType;
	}

	/**
	 * Create lightweight in-memory {@link AvroIdlFile} filled with {@code content}.
	 *
	 * @param content content of the file to be created
	 * @return created file
	 */
	public @NotNull AvroIdlFile createDummyFile(@NotNull String content) {
		return (AvroIdlFile) PsiFileFactory.getInstance(myProject).
			createFileFromText("dummy." + AvroIdlFileType.INSTANCE.getDefaultExtension(), AvroIdlFileType.INSTANCE, content);
	}
}
