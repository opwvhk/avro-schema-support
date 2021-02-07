package opwvhk.intellij.avro_idl.syntax;

import com.intellij.json.JsonParserDefinition;
import com.intellij.json.psi.impl.JsonFileImpl;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import opwvhk.intellij.avro_idl.AvroSchemaLanguage;

public class AvroSchemaParserDefinition extends JsonParserDefinition {
	public static final IFileElementType FILE = new IFileElementType(AvroSchemaLanguage.INSTANCE);

	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}

	@Override
	public PsiFile createFile(FileViewProvider fileViewProvider) {
		return new JsonFileImpl(fileViewProvider, AvroSchemaLanguage.INSTANCE);
	}
}
