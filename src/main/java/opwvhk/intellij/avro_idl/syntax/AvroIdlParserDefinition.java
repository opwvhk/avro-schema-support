package opwvhk.intellij.avro_idl.syntax;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageUtil;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlTypes;
import org.jetbrains.annotations.NotNull;

public class AvroIdlParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE = new IFileElementType(AvroIdlLanguage.INSTANCE);

    @Override
    @NotNull
    public Lexer createLexer(Project project) {
        return new AvroIdlLexer();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new AvroIdlParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
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
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new AvroIdlFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
	    if (left.getElementType() == AvroIdlTypes.LINE_COMMENT) {
		    return SpaceRequirements.MUST_LINE_BREAK;
	    }

		// Return SpaceRequirements.MUST or SpaceRequirements.MAY, depending on whether omitting the space would change tokens.
	    Lexer lexer = createLexer(null);
	    return LanguageUtil.canStickTokensTogetherByLexer(left, right, lexer);
    }
}
