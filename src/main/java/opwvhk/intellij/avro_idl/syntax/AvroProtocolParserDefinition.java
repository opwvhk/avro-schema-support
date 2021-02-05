package opwvhk.intellij.avro_idl.syntax;

import com.intellij.json.json5.Json5ParserDefinition;
import com.intellij.json.psi.impl.JsonFileImpl;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import opwvhk.intellij.avro_idl.AvroProtocolLanguage;

public class AvroProtocolParserDefinition extends Json5ParserDefinition {
	public static final IFileElementType FILE = new IFileElementType(AvroProtocolLanguage.INSTANCE);

	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}

	@Override
	public PsiFile createFile(FileViewProvider fileViewProvider) {
		return new JsonFileImpl(fileViewProvider, AvroProtocolLanguage.INSTANCE);
	}
}
