package opwvhk.intellij.avro_idl.language;

import com.intellij.psi.PsiElement;

public class AddFixedSchemaFix extends AddDeclarationFix {

	public AddFixedSchemaFix(PsiElement element, String identifier) {
		super(element, "Create fixed", String.format("\n\tfixed %s(/*focus*/);\n", identifier));
	}
}
