package opwvhk.intellij.avro_idl.language;

import com.intellij.psi.PsiElement;

public class AddEmptyEnumSchemaFix extends AddDeclarationFix {

	public AddEmptyEnumSchemaFix(PsiElement element, String identifier) {
		super(element, "Create enum", String.format("\n\tenum %s {\n\t\t/*focus*/\n\t}\n", identifier));
	}
}
