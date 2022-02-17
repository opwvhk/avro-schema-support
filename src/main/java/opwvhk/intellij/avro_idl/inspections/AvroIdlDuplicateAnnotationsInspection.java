package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlAnnotatedNameIdentifierOwner;
import opwvhk.intellij.avro_idl.psi.AvroIdlNamespaceProperty;
import opwvhk.intellij.avro_idl.psi.AvroIdlSchemaProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AvroIdlDuplicateAnnotationsInspection extends BaseAvroIdlInspection<AvroIdlAnnotatedNameIdentifierOwner> {
	public AvroIdlDuplicateAnnotationsInspection() {
		super(AvroIdlAnnotatedNameIdentifierOwner.class);
	}

	@Override
	protected void visitElement(@NotNull AvroIdlAnnotatedNameIdentifierOwner element, @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		Map<String, List<AvroIdlSchemaProperty>> annotationsByName = element.getSchemaPropertyList().stream().collect(
			Collectors.groupingBy(AvroIdlDuplicateAnnotationsInspection::getAnnotationName));
		annotationsByName.entrySet().removeIf(entry -> entry.getValue().size() <= 1);

		final String description = "Duplicate annotation (only the last will take effect)";
		annotationsByName.forEach((annotationName, annotationList) -> {
			final LocalQuickFix deleteAllButLast = new RemoveDuplicateAnnotationsQuickFix(element, annotationName);
			annotationList.forEach(duplicate -> holder.registerProblem(duplicate, description, deleteAllButLast));
		});
	}

	private static @Nullable String getAnnotationName(AvroIdlSchemaProperty annotation) {
		return annotation instanceof AvroIdlNamespaceProperty ? "namespace" : annotation.getName();
	}

	private static class RemoveDuplicateAnnotationsQuickFix extends SimpleAvroIdlQuickFixOnPsiElement<AvroIdlAnnotatedNameIdentifierOwner> {
		private final String annotationName;

		public RemoveDuplicateAnnotationsQuickFix(@NotNull AvroIdlAnnotatedNameIdentifierOwner annotatedElement, String annotationName) {
			super(annotatedElement, "Delete all @" + annotationName + " annotations except the last");
			this.annotationName = annotationName;
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
                              @NotNull AvroIdlAnnotatedNameIdentifierOwner element) {
			final List<AvroIdlSchemaProperty> duplicates = new ArrayList<>();
			for (AvroIdlSchemaProperty schemaProperty : element.getSchemaPropertyList()) {
				if (annotationName.equals(getAnnotationName(schemaProperty))) {
					duplicates.add(schemaProperty);
				}
			}
			final List<AvroIdlSchemaProperty> toRemove = duplicates.subList(0, duplicates.size() - 1);
			for (AvroIdlSchemaProperty duplicateSchemaProperty : toRemove) {
				duplicateSchemaProperty.delete();
			}
		}
	}
}
