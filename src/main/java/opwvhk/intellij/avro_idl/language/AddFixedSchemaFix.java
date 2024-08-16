package opwvhk.intellij.avro_idl.language;

import com.intellij.psi.PsiElement;
import opwvhk.intellij.avro_idl.TextBundle;

public class AddFixedSchemaFix extends AddDeclarationFix {

	public AddFixedSchemaFix(PsiElement element, String identifier) {
		super(element, TextBundle.message("syntax.unknown.schema.fixFixed"),
				String.format("\n\tfixed %s(/*focus*/);\n", identifier));
	}
}
