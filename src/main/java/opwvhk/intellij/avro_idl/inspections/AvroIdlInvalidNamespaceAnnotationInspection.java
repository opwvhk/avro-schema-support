package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInsight.daemon.impl.quickfix.RenameElementFix;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import opwvhk.intellij.avro_idl.language.AvroIdlAnnotator;
import opwvhk.intellij.avro_idl.naming.RenameFix;
import opwvhk.intellij.avro_idl.psi.AvroIdlAnnotatedNameIdentifierOwner;
import opwvhk.intellij.avro_idl.psi.AvroIdlNamespaceProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AvroIdlInvalidNamespaceAnnotationInspection extends BaseAvroIdlInspection<AvroIdlNamespaceProperty>{
	public AvroIdlInvalidNamespaceAnnotationInspection() {
		super(AvroIdlNamespaceProperty.class);
	}

	@Override
	protected void visitElement(@NotNull AvroIdlNamespaceProperty element,
	                            @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		final String name = element.getName();
		if (name != null && !AvroIdlAnnotator.VALID_IDENTIFIER_IN_STRING.test(name)) {
			PsiElement nameIdentifier = element.getNameIdentifier();
			holder.registerProblem(Objects.requireNonNull(nameIdentifier, "Bug: name identifier is null, but the name is not..."),
				"The namespace is not composed of valid identifiers", new RenameFix());
		}
	}
}
