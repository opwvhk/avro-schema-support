package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.LocalQuickFixOnPsiElement;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlAnnotatedNameIdentifierOwner;
import opwvhk.intellij.avro_idl.psi.AvroIdlSchemaProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvroIdlDuplicateAnnotationsInspectionTool extends BaseAvroIdlInspectionTool {

	@Override
	protected void visitElement(@NotNull PsiElement element, @NotNull ProblemsHolder holder,
								@NotNull LocalInspectionToolSession session) {
		if (element instanceof AvroIdlAnnotatedNameIdentifierOwner) {
			visitAnnotatedType((AvroIdlAnnotatedNameIdentifierOwner) element, holder);
		}
	}

	public void visitAnnotatedType(@NotNull AvroIdlAnnotatedNameIdentifierOwner element, @NotNull ProblemsHolder holder) {
		Map<String, List<AvroIdlSchemaProperty>> annotationsByName = new HashMap<>();
		for (AvroIdlSchemaProperty annotation : element.getSchemaPropertyList()) {
			final String annotationName = annotation.getName();
			annotationsByName.computeIfAbsent(annotationName, ignored -> new ArrayList<>()).add(annotation);
		}
		for (Map.Entry<String, List<AvroIdlSchemaProperty>> entry : annotationsByName.entrySet()) {
			List<AvroIdlSchemaProperty> annotationList = entry.getValue();
			if (annotationList.size() > 1) {
				final String annotationName = entry.getKey();
				final LocalQuickFix deleteAllButLast = new LocalQuickFixOnPsiElement(element) {
					@Override
					public @NotNull @IntentionName String getText() {
						return "Delete all @" + annotationName + " annotations except the last.";
					}

					@Override
					public void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement,
									   @NotNull PsiElement endElement) {
						final List<AvroIdlSchemaProperty> duplicates = new ArrayList<>();
						for (AvroIdlSchemaProperty schemaProperty : ((AvroIdlAnnotatedNameIdentifierOwner) startElement).getSchemaPropertyList()) {
							if (annotationName.equals(schemaProperty.getName())) {
								duplicates.add(schemaProperty);
							}
						}
						final List<AvroIdlSchemaProperty> toRemove = duplicates.subList(0, duplicates.size() - 1);
						for (AvroIdlSchemaProperty duplicateSchemaProperty : toRemove) {
							duplicateSchemaProperty.delete();
						}
					}

					@Override
					public @NotNull @IntentionFamilyName String getFamilyName() {
						return "Avro IDL";
					}
				};
				for (AvroIdlSchemaProperty duplicate : annotationList) {
					holder.registerProblem(duplicate, "Duplicate annotation (only the last will take effect)", deleteAllButLast);
				}
			}
		}
	}
}
