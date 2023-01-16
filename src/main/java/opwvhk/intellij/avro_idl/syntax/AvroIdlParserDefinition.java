package opwvhk.intellij.avro_idl.syntax;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageUtil;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlTypes;
import org.jetbrains.annotations.NotNull;

public class AvroIdlParserDefinition implements ParserDefinition {
	private static final Logger LOG = Logger.getInstance(AvroIdlParserDefinition.class);

	private static final IFileElementType FILE;
	static {
		try {
			FILE = new IFileElementType(AvroIdlLanguage.INSTANCE);
		} catch (Throwable t) {
			LOG.error("Failure during static initialization", t);
			throw t;
		}
	}

    @Override
    @NotNull
    public Lexer createLexer(Project project) {
        return new AvroIdlLexer();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new AvroIdlParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    @NotNull
    public TokenSet getWhitespaceTokens() {
        return AvroIdlTokenSets.WHITE_SPACE;
    }

    @Override
    @NotNull
    public TokenSet getCommentTokens() {
        return AvroIdlTokenSets.COMMENTS;
    }

    @Override
    @NotNull
    public TokenSet getStringLiteralElements() {
        return AvroIdlTokenSets.STRING_LITERALS;
    }

    @Override
    @NotNull
    public PsiElement createElement(ASTNode node) {
        return AvroIdlTypes.Factory.createElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new AvroIdlFile(viewProvider);
    }

    @Override
    public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
	    if (left.getElementType() == AvroIdlTypes.LINE_COMMENT) {
		    return SpaceRequirements.MUST_LINE_BREAK;
	    }

		// Return SpaceRequirements.MUST or SpaceRequirements.MAY, depending on whether omitting the space would change tokens.
	    Lexer lexer = createLexer(null);
	    return LanguageUtil.canStickTokensTogetherByLexer(left, right, lexer);
    }
}
