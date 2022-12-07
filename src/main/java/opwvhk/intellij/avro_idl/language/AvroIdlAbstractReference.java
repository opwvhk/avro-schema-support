package opwvhk.intellij.avro_idl.language;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import opwvhk.intellij.avro_idl.psi.AvroIdlElementFactory;
import org.jetbrains.annotations.NotNull;

public abstract class AvroIdlAbstractReference extends PsiReferenceBase<PsiElement> {

	private final PsiElement identifier;

	protected AvroIdlAbstractReference(@NotNull PsiElement element, @NotNull PsiElement identifier) {
		super(element, identifier.getTextRangeInParent(), false);
		this.identifier = identifier;
	}

	@Override
	public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
		final String oldName = identifier.getText();
		final int offset = oldName.lastIndexOf(".") + 1;
		final String prefix = oldName.substring(0, offset);

		final PsiElement newIdentifier = new AvroIdlElementFactory(myElement.getProject()).createIdentifier(prefix + newElementName);
		identifier.replace(newIdentifier);
		return myElement;
	}

	/**
	 * @return Range in {@link #getElement() element} that denotes the reference,
	 * e.g., range of `bar` in `foo.bar` qualified reference expression
	 */
    @Override
    @NotNull
    public TextRange getRangeInElement() {
		final TextRange rangeInElement = super.getRangeInElement();
		final String name = myElement.getText();
		final int offset = name.lastIndexOf(".") + 1;
		final int startOffset = rangeInElement.getStartOffset() + offset;
		final int endOffset = rangeInElement.getEndOffset();
		return startOffset < endOffset ? TextRange.create(startOffset, endOffset) : rangeInElement;
	}
}
