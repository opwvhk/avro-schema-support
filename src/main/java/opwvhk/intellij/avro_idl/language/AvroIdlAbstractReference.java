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
	 * @return range in {@link #getElement() element} which is considered a reference,
	 * e.g. range of `bar` in `foo.bar` qualified reference expression
	 */
	@NotNull
	@Override
	public TextRange getRangeInElement() {
		final TextRange rangeInElement = super.getRangeInElement();
		final String name = myElement.getText();
		final int offset = name.lastIndexOf(".") + 1;
		return TextRange.create(rangeInElement.getStartOffset() + offset, rangeInElement.getEndOffset());
	}
}
