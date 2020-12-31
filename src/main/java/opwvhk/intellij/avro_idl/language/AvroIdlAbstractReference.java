package opwvhk.intellij.avro_idl.language;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import opwvhk.intellij.avro_idl.psi.AvroIdlElementFactory;
import opwvhk.intellij.avro_idl.psi.AvroIdlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AvroIdlAbstractReference<T extends PsiElement> extends PsiReferenceBase<PsiElement>
	implements PsiPolyVariantReference {

	private final PsiElement identifier;

	protected AvroIdlAbstractReference(@NotNull PsiElement element, @NotNull PsiElement identifier) {
		super(element, identifier.getTextRangeInParent(), false);
		this.identifier = identifier;
	}

	private static TextRange relativeTextRange(@NotNull PsiElement element) {
		if (element instanceof PsiNameIdentifierOwner) {
			final PsiElement nameIdentifier = ((PsiNameIdentifierOwner) element).getNameIdentifier();
			if (nameIdentifier != null) {
				return nameIdentifier.getTextRangeInParent();
			}
		}
		return element.getTextRangeInParent();
	}

	@Nullable
	@Override
	public T resolve() {
		ResolveResult[] resolveResults = multiResolve(false);
		//noinspection unchecked
		return resolveResults.length > 0 ? (T) resolveResults[0].getElement() : null;
	}

	@Override
	public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
		final String oldName = identifier.getText();
		final int offset = oldName.lastIndexOf(".") + 1;
		final String prefix = oldName.substring(0, offset);

		final PsiElement newIdentifier = AvroIdlElementFactory.createIdentifier(myElement.getProject(), prefix + newElementName);
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
