package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.options.OptPane;
import com.intellij.json.codeinsight.JsonStandardComplianceInspection;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElementVisitor;
import opwvhk.intellij.avro_idl.AvroSchemaLanguage;
import org.jetbrains.annotations.NotNull;

public class AvroSchemaJsonStandardComplianceInspection extends JsonStandardComplianceInspection {
	private final Language languageToInspect;

	public AvroSchemaJsonStandardComplianceInspection() {
		this(AvroSchemaLanguage.INSTANCE);
	}

	protected AvroSchemaJsonStandardComplianceInspection(Language languageToInspect) {
		this.languageToInspect = languageToInspect;
	}

	@Override
	public @NotNull OptPane getOptionsPane() {
		// Disable the JSON options to (optionally) ignore JSON errors.
		return OptPane.EMPTY;
	}

	@Override
	public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
		// Our superclass does not generate a visitor for derived languages, but we cannot either:
		// the language filter in the definition does not work for all versions.
		if (holder.getFile().getLanguage() != languageToInspect) return PsiElementVisitor.EMPTY_VISITOR;
		return new StandardJsonValidatingElementVisitor(holder);
	}
}
