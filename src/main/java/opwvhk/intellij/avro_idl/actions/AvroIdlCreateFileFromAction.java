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
		super("Avro IDL File", "Create a new Avro IDL file", AvroIdlIcons.FILE);
	}

	@Override
	protected void buildDialog(@NotNull Project project,
	                           @NotNull PsiDirectory directory,
	                           CreateFileFromTemplateDialog.@NotNull Builder builder) {
		builder.setTitle("New Avro IDL File")
			.addKind("Empty IDL", AvroIdlIcons.FILE, "Empty")
			.addKind("Example protocol", AvroIdlIcons.FILE, "ExampleProtocol")
			.setValidator(new NonEmptyInputValidator());
	}

	@Override
	protected String getActionName(PsiDirectory directory, @NonNls @NotNull String newName, @NonNls String templateName) {
		return "Avro IDL File";
	}
}
