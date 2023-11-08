package opwvhk.intellij.avro_idl.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.NonEmptyInputValidator;
import com.intellij.psi.PsiDirectory;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class AvroIdlCreateFileFromAction extends CreateFileFromTemplateAction implements DumbAware {
	public AvroIdlCreateFileFromAction() {
		super("Avro IDL File", "Create a new Avro IDL file", AvroIdlIcons.AVSC_FILE);
	}

	@Override
	protected void buildDialog(@NotNull Project project,
	                           @NotNull PsiDirectory directory,
	                           @NotNull CreateFileFromTemplateDialog.Builder builder) {
		/*
		 * Note: the template names are prefixes. IntelliJ takes the first matching prefix. This means with the AsciiDoc plugin installed,
		 * the template name "Empty" matches the empty AsciiDoc template, not one of this template!
		 *
		 * To avoid this kind of bugs, all template files for this plugin have a plugin specific prefix.
		 */
		builder.setTitle("New Avro IDL File")
				// TODO: Enable schema templates when Avro supports the schema syntax (Avro 1.12.0)
				//.addKind("Empty Schema IDL", AvroIdlIcons.AVDL_FILE, "AvroIDL_EmptySchema")
				.addKind("Empty Protocol IDL", AvroIdlIcons.AVDL_FILE, "AvroIDL_EmptyProtocol")
				//.addKind("Example schema", AvroIdlIcons.AVDL_FILE, "AvroIDL_ExampleSchema")
				.addKind("Example protocol", AvroIdlIcons.AVDL_FILE, "AvroIDL_ExampleProtocol")
				.setValidator(new NonEmptyInputValidator());
	}

	@Override
	protected String getActionName(PsiDirectory directory, @NonNls @NotNull String newName,
	                               @NonNls String templateName) {
		return "Avro IDL File";
	}
}
