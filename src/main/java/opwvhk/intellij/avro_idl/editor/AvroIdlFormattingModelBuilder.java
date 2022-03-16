package opwvhk.intellij.avro_idl.editor;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.google.common.primitives.Ints.asList;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlFormattingModelBuilder implements FormattingModelBuilder {
	private SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
		final CommonCodeStyleSettings avroIdlSettings = settings.getCommonSettings(AvroIdlLanguage.INSTANCE.getID());

		// IntelliJ applies spacing rules in order: the first match wins

		final TokenSet allTypes = TokenSet.create(PRIMITIVE_TYPE, ARRAY_TYPE, MAP_TYPE, UNION_TYPE, REFERENCE_TYPE);
		SpacingBuilder spacingBuilder = new SpacingBuilder(settings, AvroIdlLanguage.INSTANCE)
			// Universal
			.beforeInside(SEMICOLON, TokenSet.create(
				NAMESPACE_DECLARATION, MAIN_SCHEMA_DECLARATION, IMPORT_DECLARATION, FIXED_DECLARATION
			)).spacing(0, 0, 0, false, 0) // Force 1-line import/fixed statement
			.before(TokenSet.create(COMMA, SEMICOLON)).spaces(0)
			.after(SEMICOLON).spaces(1)
			.around(EQUALS).spaceIf(avroIdlSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
			.after(AT).spacing(0, 0, 0, false, 0) // Force no space: let @ be part of the annotation name

			// Documentation
			.after(LINE_COMMENT).lineBreakInCode()
			.after(TokenSet.create(BLOCK_COMMENT, DOC_COMMENT)).lineBreakInCode()

			// Annotations
			.aroundInside(NAMESPACE_PROPERTY, TokenSet.create(
				PROTOCOL_DECLARATION, RECORD_DECLARATION, ENUM_DECLARATION, FIXED_DECLARATION, MESSAGE_DECLARATION)).lineBreakInCode()
			.around(NAMESPACE_PROPERTY).spaces(1)
			.aroundInside(SCHEMA_PROPERTY, TokenSet.create(
				PROTOCOL_DECLARATION, RECORD_DECLARATION, ENUM_DECLARATION, FIXED_DECLARATION, MESSAGE_DECLARATION)).lineBreakInCode()
			.around(SCHEMA_PROPERTY).spaces(1)

			// Namespace declaration
			.beforeInside(IDENTIFIER, NAMESPACE_DECLARATION).spaces(1)
			.afterInside(NAMESPACE, NAMESPACE_DECLARATION).spaces(1)

			// Main schema declaration
			.afterInside(SCHEMA, MAIN_SCHEMA_DECLARATION).spaces(1)

			// Protocol definition
			.beforeInside(LEFT_BRACE, PROTOCOL_DECLARATION).spaces(1)
			.withinPairInside(LEFT_BRACE, RIGHT_BRACE, PROTOCOL_DECLARATION).blankLines(0)
			.beforeInside(IDENTIFIER, PROTOCOL_DECLARATION).spaces(1)
			.between(IMPORT_DECLARATION, IMPORT_DECLARATION).blankLines(0)
			.aroundInside(IMPORT_TYPE, IMPORT_DECLARATION).spacing(1, 1, 0, false, 0); // Force 1-line import statement

		// Define blank lines in descending order (so the largest wins)
		SortedSet<Integer> blankLinesDescending = new TreeSet<>(Comparator.reverseOrder());
		blankLinesDescending.addAll(
			asList(avroIdlSettings.BLANK_LINES_AFTER_PACKAGE, avroIdlSettings.BLANK_LINES_AROUND_CLASS, avroIdlSettings.BLANK_LINES_AROUND_METHOD,
				avroIdlSettings.BLANK_LINES_BEFORE_IMPORTS, avroIdlSettings.BLANK_LINES_AFTER_IMPORTS));
		for (int blankLines : blankLinesDescending) {
			if (blankLines == avroIdlSettings.BLANK_LINES_AFTER_PACKAGE) {
				spacingBuilder.after(NAMESPACE_DECLARATION).blankLines(avroIdlSettings.BLANK_LINES_AFTER_PACKAGE);
			}
			if (blankLines == avroIdlSettings.BLANK_LINES_AROUND_CLASS) {
				spacingBuilder.around(TokenSet.create(MAIN_SCHEMA_DECLARATION, RECORD_DECLARATION, ENUM_DECLARATION, FIXED_DECLARATION))
					.blankLines(avroIdlSettings.BLANK_LINES_AROUND_CLASS);
			}
			if (blankLines == avroIdlSettings.BLANK_LINES_AROUND_METHOD) {
				spacingBuilder.around(MESSAGE_DECLARATION).blankLines(avroIdlSettings.BLANK_LINES_AROUND_METHOD);
			}
			if (blankLines == avroIdlSettings.BLANK_LINES_BEFORE_IMPORTS) {
				spacingBuilder.before(IMPORT_DECLARATION).blankLines(avroIdlSettings.BLANK_LINES_BEFORE_IMPORTS);
			}
			if (blankLines == avroIdlSettings.BLANK_LINES_AFTER_IMPORTS) {
				spacingBuilder.after(IMPORT_DECLARATION).blankLines(avroIdlSettings.BLANK_LINES_AFTER_IMPORTS);
			}
		}

		spacingBuilder
			// Documentation
			.before(TokenSet.create(BLOCK_COMMENT, DOC_COMMENT)).lineBreakInCode()
			// Record/error or enum declaration
			.beforeInside(IDENTIFIER, TokenSet.create(RECORD_DECLARATION, ENUM_DECLARATION, FIELD_DECLARATION)).spaces(1)
			.withinPairInside(LEFT_BRACE, RIGHT_BRACE, ENUM_DECLARATION).blankLines(0)
			.withinPairInside(LEFT_BRACE, RIGHT_BRACE, RECORD_DECLARATION).blankLines(0)
			.beforeInside(LEFT_BRACE, TokenSet.create(RECORD_DECLARATION, ENUM_DECLARATION)).spaces(1)
			.afterInside(COMMA, ENUM_BODY).spaceIf(avroIdlSettings.SPACE_AFTER_COMMA)
			.around(FIELD_DECLARATION).blankLines(avroIdlSettings.BLANK_LINES_AROUND_FIELD)

			// Fixed type or method declaration
			.beforeInside(IDENTIFIER, TokenSet.create(FIXED_DECLARATION, MESSAGE_DECLARATION, FORMAL_PARAMETER)).spaces(1)
			.before(LEFT_PAREN).spaces(0)
			.afterInside(RIGHT_PAREN, MESSAGE_DECLARATION).spaces(1)
			.afterInside(COMMA, MESSAGE_DECLARATION).spaceIf(avroIdlSettings.SPACE_AFTER_COMMA_IN_TYPE_ARGUMENTS)
			.withinPair(LEFT_PAREN, RIGHT_PAREN).spaces(0)
			.before(MESSAGE_ATTRIBUTE_THROWS).spaces(1)
			.after(THROWS).spaces(1)

			// Type definitions
			.aroundInside(TokenSet.create(LEFT_BRACE, RIGHT_BRACE), UNION_TYPE).spaces(0)
			.withinPairInside(LEFT_BRACE, RIGHT_BRACE, UNION_TYPE).spaces(0)
			.aroundInside(TokenSet.create(LEFT_ANGLE, RIGHT_ANGLE), TokenSet.create(ARRAY_TYPE, MAP_TYPE)).spaces(0)
			.withinPairInside(LEFT_ANGLE, RIGHT_ANGLE, ARRAY_TYPE).spaces(0)
			.withinPairInside(LEFT_ANGLE, RIGHT_ANGLE, MAP_TYPE).spaces(0)
			.afterInside(COMMA, UNION_TYPE).spaceIf(avroIdlSettings.SPACE_AFTER_COMMA)
			.aroundInside(allTypes, TokenSet.create(ARRAY_TYPE, MAP_TYPE, UNION_TYPE)).spaces(0)
			.around(allTypes).spaces(1)

			// JSON values
			.beforeInside(COLON, JSON_PAIR).spaces(0)
			.afterInside(COLON, JSON_PAIR).spaces(1)
			.withinPairInside(LEFT_BRACE, RIGHT_BRACE, JSON_OBJECT).spaces(0)
			.withinPairInside(LEFT_BRACKET, RIGHT_BRACKET, JSON_ARRAY).spaces(0)
			.afterInside(COMMA, TokenSet.create(JSON_ARRAY, JSON_OBJECT)).spaceIf(avroIdlSettings.SPACE_AFTER_COMMA)

			// Fallback (when nothing else matches)
			.around(TokenSet.ANY).spaces(0);

		return spacingBuilder;
	}

	@Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
		PsiElement psiElement = formattingContext.getPsiElement();
		CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
		Wrap normalWrap = Wrap.createWrap(WrapType.NORMAL, false);
		Alignment alignment = null;//Alignment.createAlignment();
		final Indent indent = Indent.getNoneIndent();
		SpacingBuilder spaceBuilder = createSpaceBuilder(codeStyleSettings);
		AvroIdlBlock block = new AvroIdlBlock(psiElement.getNode(), normalWrap, alignment, indent, spaceBuilder);
		return FormattingModelProvider.createFormattingModelForPsiFile(psiElement.getContainingFile(), block, codeStyleSettings);
	}

	@Override
    public @Nullable TextRange getRangeAffectingIndent(PsiFile psiFile, int i, ASTNode astNode) {
		return null;
	}

}
