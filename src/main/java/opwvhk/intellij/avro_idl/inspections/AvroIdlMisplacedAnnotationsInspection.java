package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import opwvhk.intellij.avro_idl.TextBundle;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvroIdlMisplacedAnnotationsInspection extends BaseAvroIdlInspection<AvroIdlSchemaProperty> {
	public AvroIdlMisplacedAnnotationsInspection() {
		super(AvroIdlSchemaProperty.class);
	}

	@Override
	protected void visitElement(@NotNull AvroIdlSchemaProperty element,
	                            @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		RemoveAnnotationQuickFix quickFix = null;

		final PsiElement parent = element.getParent();
		final String name = element.getName();
		if (element instanceof AvroIdlNamespaceProperty) {
			if (!(parent instanceof AvroIdlProtocolDeclaration || parent instanceof AvroIdlNamedSchemaDeclaration)) {
				quickFix = new RemoveAnnotationQuickFix(element, "namespace");
			}
		} else if (name != null) {
			// This method needs the name, but annotations without a name are already parse errors anyway.
			final boolean isMisplaced = switch (name) {
				case "aliases" -> !(parent instanceof AvroIdlProtocolDeclaration ||
						parent instanceof AvroIdlNamedSchemaDeclaration ||
						parent instanceof AvroIdlVariableDeclarator);
				case "order" -> !(parent instanceof AvroIdlVariableDeclarator);
				case "logicalType" ->
						!(parent instanceof AvroIdlType) && !(parent instanceof AvroIdlNamedSchemaDeclaration);
				// These annotations have a meaning combination with @logicalType("decimal"), but may occur by themselves as well.
				default ->
					// Annotations with a different name are custom annotations and hence not checked.
						false;
			};
			if (isMisplaced) {
				quickFix = new RemoveAnnotationQuickFix(element, name);
			}
		}

		if (quickFix != null) {
			holder.registerProblem(element,
					TextBundle.message("inspection.misplaced.annotations.problem", quickFix.getAnnotationName()),
					quickFix);
		}
	}

	private static class RemoveAnnotationQuickFix extends SimpleAvroIdlQuickFixOnPsiElement<AvroIdlSchemaProperty> {
		private final String annotationName;

		public RemoveAnnotationQuickFix(@NotNull AvroIdlSchemaProperty element, @NotNull String annotationName) {
			super(element, TextBundle.message("inspection.misplaced.annotations.fix", annotationName));
			this.annotationName = annotationName;
		}

		@NotNull
		public String getAnnotationName() {
			return annotationName;
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
		                      @NotNull AvroIdlSchemaProperty element) {
			element.delete();
		}
	}
}
