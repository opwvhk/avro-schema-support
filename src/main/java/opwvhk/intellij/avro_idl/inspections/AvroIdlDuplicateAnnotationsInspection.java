package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import opwvhk.intellij.avro_idl.psi.AvroIdlAnnotatedNameIdentifierOwner;
import opwvhk.intellij.avro_idl.psi.AvroIdlSchemaProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvroIdlDuplicateAnnotationsInspection extends BaseAvroIdlInspection<AvroIdlAnnotatedNameIdentifierOwner> {
	public AvroIdlDuplicateAnnotationsInspection() {
		super(AvroIdlAnnotatedNameIdentifierOwner.class);
	}

	@Override
	protected void visitElement(@NotNull AvroIdlAnnotatedNameIdentifierOwner element, @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		Map<String, List<AvroIdlSchemaProperty>> annotationsByName = new HashMap<>();
		for (AvroIdlSchemaProperty annotation : element.getSchemaPropertyList()) {
			final String annotationName = annotation.getName();
			annotationsByName.computeIfAbsent(annotationName, ignored -> new ArrayList<>()).add(annotation);
		}
		for (Map.Entry<String, List<AvroIdlSchemaProperty>> entry : annotationsByName.entrySet()) {
			List<AvroIdlSchemaProperty> annotationList = entry.getValue();
			if (annotationList.size() > 1) {
				final String annotationName = entry.getKey();
				final LocalQuickFix deleteAllButLast = createQuickFix(element, "Delete all @" + annotationName + " annotations except the last",
					(project, file, elem) -> {
						final List<AvroIdlSchemaProperty> duplicates = new ArrayList<>();
						for (AvroIdlSchemaProperty schemaProperty : element.getSchemaPropertyList()) {
							if (annotationName.equals(schemaProperty.getName())) {
								duplicates.add(schemaProperty);
							}
						}
						final List<AvroIdlSchemaProperty> toRemove = duplicates.subList(0, duplicates.size() - 1);
						for (AvroIdlSchemaProperty duplicateSchemaProperty : toRemove) {
							duplicateSchemaProperty.delete();
						}
					});
				for (AvroIdlSchemaProperty duplicate : annotationList) {
					holder.registerProblem(duplicate, "Duplicate annotation (only the last will take effect)", deleteAllButLast);
				}
			}
		}
	}
}
