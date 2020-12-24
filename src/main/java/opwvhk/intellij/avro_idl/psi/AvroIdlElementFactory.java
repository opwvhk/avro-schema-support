package opwvhk.intellij.avro_idl.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import org.jetbrains.annotations.NotNull;

public class AvroIdlElementFactory {
	public static PsiElement createIdentifier(@NotNull Project project, @NotNull String name) {
		// Yes, in theory this can cause syntax errors. We're assuming the name has been vetted by AvroIdlNamesValidator
		final AvroIdlFile file = createFile(project, String.format("protocol %s {}", name));
		final AvroIdlProtocolDeclaration protocol = (AvroIdlProtocolDeclaration) file.getFirstChild();
		return protocol.getNameIdentifier();
	}

	public static AvroIdlFile createFile(Project project, String text) {
		String name = "dummy.avdl";
		return (AvroIdlFile) PsiFileFactory.getInstance(project).
			createFileFromText(name, AvroIdlFileType.INSTANCE, text);
	}
}
