package opwvhk.intellij.avro_idl.syntax;

import com.intellij.json.json5.Json5ParserDefinition;
import com.intellij.json.psi.impl.JsonFileImpl;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import opwvhk.intellij.avro_idl.AvroProtocolLanguage;
import org.jetbrains.annotations.NotNull;

public class AvroProtocolParserDefinition extends Json5ParserDefinition {
	private static final Logger LOG = Logger.getInstance(AvroIdlParserDefinition.class);
	public static final IFileElementType FILE;

	static {
		try {
			FILE = new IFileElementType(AvroProtocolLanguage.INSTANCE);
		} catch (Throwable t) {
			LOG.error("Failure during static initialization", t);
			throw t;
		}
	}

	@Override
	public @NotNull IFileElementType getFileNodeType() {
		return FILE;
	}

	@Override
	public @NotNull PsiFile createFile(@NotNull FileViewProvider fileViewProvider) {
		return new JsonFileImpl(fileViewProvider, AvroProtocolLanguage.INSTANCE);
	}
}
