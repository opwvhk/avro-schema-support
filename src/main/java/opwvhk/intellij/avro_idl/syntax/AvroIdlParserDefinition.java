package opwvhk.intellij.avro_idl.syntax;

import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class AvroIdlParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE = new IFileElementType(AvroIdlLanguage.INSTANCE);

    public static final TokenSet COMMENTS = TokenSet.create(AvroIdlTypes.LINE_COMMENT, AvroIdlTypes.BLOCK_COMMENT);
    public static final TokenSet WHITE_SPACE = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet STRING_LITERALS = TokenSet.create(AvroIdlTypes.STRING_LITERAL);

    @NotNull
    @Override
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

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACE;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return STRING_LITERALS;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return AvroIdlTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new AvroIdlFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
