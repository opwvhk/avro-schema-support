package opwvhk.intellij.avro_idl.language;

import com.intellij.psi.PsiElement;
import opwvhk.intellij.avro_idl.TextBundle;

public class AddEmptyRecordSchemaFix extends AddDeclarationFix {

	public AddEmptyRecordSchemaFix(PsiElement element, String identifier, boolean isError) {
		super(element, isError ?
						TextBundle.message("syntax.unknown.schema.fixError") :
						TextBundle.message("syntax.unknown.schema.fixRecord"),
				String.format("\n\t%s %s {\n\t\t/*focus*/\n\t}\n", isError ? "error" : "record", identifier));
	}
}
