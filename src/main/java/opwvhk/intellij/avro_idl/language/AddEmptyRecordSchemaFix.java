package opwvhk.intellij.avro_idl.language;

import com.intellij.psi.PsiElement;

public class AddEmptyRecordSchemaFix extends AddDeclarationFix {

	public AddEmptyRecordSchemaFix(PsiElement element, String identifier, boolean isError) {
		super(element, isError ? "Create error" : "Create record",
				String.format("\n\t%s %s {\n\t\t/*focus*/\n\t}\n", isError ? "error" : "record", identifier));
	}
}
