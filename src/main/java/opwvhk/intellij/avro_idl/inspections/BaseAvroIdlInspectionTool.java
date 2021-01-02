package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInspection.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.jetbrains.annotations.Nls.Capitalization.Sentence;

public abstract class BaseAvroIdlInspectionTool extends LocalInspectionTool {
	private final String inspectionName;
	private final String inspectionDescription;

	public BaseAvroIdlInspectionTool(@NotNull String inspectionName, @NotNull String inspectionDescription) {
		this.inspectionName = inspectionName;
		this.inspectionDescription = inspectionDescription;
	}

	protected abstract void visitElement(@NotNull PsiElement element, @NotNull ProblemsHolder holder, @NotNull LocalInspectionToolSession session);

	@Override
	public @Nls(capitalization = Sentence) @NotNull String getGroupDisplayName() {
		return AvroIdlLanguage.INSTANCE.getDisplayName();
	}

	@Override
	public @Nls(capitalization = Sentence) @NotNull String getDisplayName() {
		return inspectionName;
	}

	@Override
	public @Nls(capitalization = Sentence) @NotNull String getStaticDescription() {
		return inspectionDescription;
	}

	@Override
	public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly,
												   @NotNull LocalInspectionToolSession session) {
		return new PsiElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				BaseAvroIdlInspectionTool.this.visitElement(element, holder, session);
			}
		};
	}
}
