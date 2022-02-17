package opwvhk.intellij.avro_idl.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class AvroIdlElementFactory {
	private final Project myProject;

	public AvroIdlElementFactory(@NotNull Project project) {
		myProject = project;
	}

	public @NotNull PsiElement createIdentifier(@NotNull String name) {
		// Yes, in theory this can cause syntax errors. We're assuming the name has been vetted by AvroIdlNamesValidator.
		final AvroIdlFile file = createDummyFile(String.format("protocol %s {}", name));
		final AvroIdlProtocolDeclaration protocol = (AvroIdlProtocolDeclaration)file.getFirstChild();
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

	private @NotNull AvroIdlProtocolBody extractAvroIdlProtocolBody(AvroIdlFile file) {
		final AvroIdlProtocolDeclaration protocol = (AvroIdlProtocolDeclaration)file.getFirstChild();
		return requireNonNull(protocol.getProtocolBody());
	}

	public @NotNull PsiComment createMultilineComment(@NotNull AvroIdlDocumentation documentation) {
		final String commentText = documentation.getDocComment().getText();
		final String commentContent = commentText.substring(3, commentText.length() - 2);
		final AvroIdlFile file = createDummyFile(String.format("/*%s*/ protocol Foo { }", commentContent));
		return (PsiComment)file.getFirstChild();
	}

	/**
	 * Create a namespace declaration and (if there is a named schema) a main schema declaration. The protocol must have a namespace.
	 * This method returns an in-memory file, of which you'll want to use all children.
	 *
	 * @param protocolDeclaration a protocol declaration
	 * @return an in-memory 'file' using the IDL schema syntax with a namespace declaration and optionally a main schema declaration
	 */
	public @NotNull AvroIdlFile createSchemaSyntaxHeader(@NotNull AvroIdlProtocolDeclaration protocolDeclaration) {

		String fullName = requireNonNull(protocolDeclaration.getFullName());
		int dotPos = fullName.lastIndexOf('.');
		String namespace = dotPos < 0 ? "" : fullName.substring(0, dotPos);

		String firstSchemaDeclaration = Stream.ofNullable(protocolDeclaration.getProtocolBody())
			.map(AvroIdlProtocolBody::getNamedSchemaDeclarationList)
			.flatMap(List::stream)
			.findFirst()
			.map(namedSchema -> {
				String schemaNamespace = AvroIdlPsiUtil.getNamespace(namedSchema);
				return (namespace.equals(schemaNamespace) ? "" : (schemaNamespace + ".")) + namedSchema.getName();
			})
			.orElse(null);

		StringBuilder buffer = new StringBuilder();
		if (!namespace.isEmpty()) {
			buffer.append(String.format("namespace %s;%n", namespace));
		}
		if (firstSchemaDeclaration != null) {
			buffer.append(String.format("// Optional: main schema (using it creates a .avsc equivalent file)%n"));
			buffer.append(String.format("schema %s;%n", firstSchemaDeclaration));
		}
		if (buffer.length() > 0) {
			buffer.append(String.format("%n"));
		}
		return createDummyFile(buffer);
	}

	public @NotNull AvroIdlProtocolDeclaration createDummyProtocol(@Nullable String namespace) {
		String protocolDefinition = "protocol Dummy { }";
		if (namespace != null) {
			protocolDefinition = String.format("@namespace(\"%s\")%n%s", namespace, protocolDefinition);
		}
		return (AvroIdlProtocolDeclaration)createDummyFile(protocolDefinition).getFirstChild();
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
	public @NotNull AvroIdlFile createDummyFile(@NotNull CharSequence content) {
		return (AvroIdlFile)PsiFileFactory.getInstance(myProject).
			createFileFromText("dummy." + AvroIdlFileType.INSTANCE.getDefaultExtension(), AvroIdlFileType.INSTANCE, content);
	}
}
