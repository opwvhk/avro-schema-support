package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public abstract class BaseAvroIdlInspection<T extends PsiElement> extends LocalInspectionTool {

	private final Class<T> elementType;

	public BaseAvroIdlInspection(Class<T> elementType) {
		this.elementType = elementType;
	}

	protected abstract void visitElement(@NotNull T element, @NotNull ProblemsHolder holder,
	                                     @SuppressWarnings("unused") @NotNull LocalInspectionToolSession session);

	@Override
	@NotNull
	public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly,
	                                      @NotNull LocalInspectionToolSession session) {
		return new PsiElementVisitor() {
			@Override
			public void visitElement(@NotNull PsiElement element) {
				if (elementType.isAssignableFrom(element.getClass())) {
					BaseAvroIdlInspection.this.visitElement(elementType.cast(element), holder, session);
				}
			}
		};
	}
}
