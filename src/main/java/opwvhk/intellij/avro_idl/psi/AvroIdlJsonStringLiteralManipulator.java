package opwvhk.intellij.avro_idl.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class AvroIdlJsonStringLiteralManipulator extends AbstractElementManipulator<AvroIdlJsonStringLiteral> {
	@Override
	public AvroIdlJsonStringLiteral handleContentChange(@NotNull AvroIdlJsonStringLiteral element,
	                                                    @NotNull TextRange range, String newContent)
			throws IncorrectOperationException {
		assert new TextRange(0, element.getTextLength()).contains(
				range); // Building the replacement works because of this assertion.

		final String originalString = element.getText();
		final TextRange contentRange = getRangeInElement(element);

		final String beginning = originalString.substring(contentRange.getStartOffset(), range.getStartOffset());
		final String ending = originalString.substring(range.getEndOffset(), contentRange.getEndOffset());
		final String replacement = beginning + newContent + ending;

		final AvroIdlElementFactory generator = new AvroIdlElementFactory(element.getProject());
		return (AvroIdlJsonStringLiteral) element.replace(generator.createJsonStringLiteral(replacement));
	}

	@Override
	@NotNull
	public TextRange getRangeInElement(@NotNull AvroIdlJsonStringLiteral element) {
		final String content = element.getText();
		return new TextRange(1, content.length() - 1);
	}
}
