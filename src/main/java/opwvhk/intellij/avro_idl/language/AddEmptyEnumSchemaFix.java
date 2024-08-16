package opwvhk.intellij.avro_idl.language;

import com.intellij.psi.PsiElement;
import opwvhk.intellij.avro_idl.TextBundle;

public class AddEmptyEnumSchemaFix extends AddDeclarationFix {

	public AddEmptyEnumSchemaFix(PsiElement element, String identifier) {
		super(element, TextBundle.message("syntax.unknown.schema.fixEnum"),
				String.format("\n\tenum %s {\n\t\t/*focus*/\n\t}\n", identifier));
	}
}
