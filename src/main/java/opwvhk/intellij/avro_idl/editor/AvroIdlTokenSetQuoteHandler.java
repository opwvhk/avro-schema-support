package opwvhk.intellij.avro_idl.editor;

import com.intellij.codeInsight.editorActions.MultiCharQuoteHandler;
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import opwvhk.intellij.avro_idl.psi.AvroIdlTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class AvroIdlTokenSetQuoteHandler extends SimpleTokenSetQuoteHandler implements MultiCharQuoteHandler {
	public AvroIdlTokenSetQuoteHandler() {
		super(AvroIdlTypes.STRING_LITERAL);
	}

	@Override
	public boolean isClosingQuote(HighlighterIterator iterator, int offset) {
		return super.isClosingQuote(iterator, offset);
	}

	@Override
	public boolean isOpeningQuote(HighlighterIterator iterator, int offset) {
		return super.isOpeningQuote(iterator, offset);
	}

	@Override
	public @Nullable CharSequence getClosingQuote(@NotNull HighlighterIterator iterator, int offset) {
		final IElementType tokenType = iterator.getTokenType();
		if (tokenType == TokenType.WHITE_SPACE || myLiteralTokenSet.contains(tokenType)) {
			final int index = iterator.getStart() - (tokenType == TokenType.WHITE_SPACE ? 1 : 0);
			if (index >= 0) {
				return String.valueOf(iterator.getDocument().getCharsSequence().charAt(index));
			}
		}
		return null;
	}
}
