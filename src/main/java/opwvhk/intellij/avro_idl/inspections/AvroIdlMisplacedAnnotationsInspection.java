package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;

public class AvroIdlMisplacedAnnotationsInspection extends BaseAvroIdlInspection<AvroIdlSchemaProperty> {
	public AvroIdlMisplacedAnnotationsInspection() {
		super(AvroIdlSchemaProperty.class);
	}

	@Override
	protected void visitElement(@NotNull AvroIdlSchemaProperty element,
	                            @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		final PsiElement parent = element.getParent();
		final String name = element.getName();
		if (element instanceof AvroIdlNamespaceProperty) {
			if(!(parent instanceof AvroIdlProtocolDeclaration || parent instanceof AvroIdlNamedSchemaDeclaration)) {
				registerMisplacedAnnotationProblem(holder, element, "namespace");
			}
		} else if (name != null) {
			// This method needs the name, but annotations without a name are already parse errors anyway.
			final boolean isMisplaced;
			switch (name) {
				case "aliases":
					isMisplaced = !(parent instanceof AvroIdlProtocolDeclaration ||
						parent instanceof AvroIdlNamedSchemaDeclaration ||
						parent instanceof AvroIdlVariableDeclarator);
					break;
				case "order":
					isMisplaced = !(parent instanceof AvroIdlVariableDeclarator);
					break;
				case "logicalType":
					isMisplaced = !(parent instanceof AvroIdlType) && !(parent instanceof AvroIdlNamedSchemaDeclaration);
					break;
				case "precision":
				case "scale":
					// These annotations have a meaning combination with @logicalType("decimal"), but may occur by themselves as well.
				default:
					// Annotations with a different name are custom annotations and hence not checked.
					isMisplaced = false;
					break;
			}
			if (isMisplaced) {
				registerMisplacedAnnotationProblem(holder, element, name);
			}
		}
	}

	private void registerMisplacedAnnotationProblem(@NotNull ProblemsHolder holder, @NotNull AvroIdlSchemaProperty element, @NotNull String name) {
		LocalQuickFix removeAnnotation = createQuickFix(element, "Remove the @" + name + " annotation", (project, file, elem) -> elem.delete());
		holder.registerProblem(element, "The @" + name + " annotation has no effect here", removeAnnotation);
	}
}
