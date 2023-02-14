package opwvhk.intellij.avro_idl.language;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import opwvhk.intellij.avro_idl.psi.*;
import opwvhk.intellij.avro_idl.syntax.AvroIdlLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvroIdlFindUsagesProvider implements FindUsagesProvider {

	private static final TokenSet IDENTIFIER_TOKENS = TokenSet.create(AvroIdlTypes.IDENTIFIER);
	private static final TokenSet COMMENT_TOKENS = TokenSet.create(AvroIdlTypes.DOC_COMMENT, AvroIdlTypes.BLOCK_COMMENT,
			AvroIdlTypes.LINE_COMMENT,
			AvroIdlTypes.INCOMPLETE_DOC_COMMENT, AvroIdlTypes.INCOMPLETE_BLOCK_COMMENT);

	@Override
	@Nullable
	public WordsScanner getWordsScanner() {
		final DefaultWordsScanner wordsScanner = new DefaultWordsScanner(new AvroIdlLexer(), IDENTIFIER_TOKENS,
				COMMENT_TOKENS, TokenSet.EMPTY);
		wordsScanner.setMayHaveFileRefsInLiterals(true);
		return wordsScanner;
	}

	@Override
	public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
		return psiElement instanceof AvroIdlNamedSchemaDeclaration || psiElement instanceof AvroIdlEnumConstant;
	}

	@Override
	@Nullable
	public String getHelpId(@NotNull PsiElement psiElement) {
		return null;
	}

	@Override
	@NotNull
	public String getType(@NotNull PsiElement element) {
		if (element instanceof AvroIdlRecordDeclaration) {
			if (((AvroIdlRecordDeclaration) element).isErrorType()) {
				return "error schema";
			} else {
				return "record schema";
			}
		} else if (element instanceof AvroIdlEnumDeclaration) {
			return "enum schema";
		} else if (element instanceof AvroIdlFixedDeclaration) {
			return "fixed schema";
		} else if (element instanceof AvroIdlEnumConstant) {
			return "enum schema";
		} else if (element instanceof AvroIdlNamedSchemaReference) {
			final PsiElement resolvedReference = ((AvroIdlNamedSchemaReference) element).resolve();
			if (resolvedReference != null) {
				return getType(resolvedReference);
			} else {
				return "unknown named schema";
			}
		} else {
			return "unknown";
		}
	}

	@Override
	@NotNull
	public String getDescriptiveName(@NotNull PsiElement element) {
		return getNodeText(element, false);
	}

	@Override
	@NotNull
	public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
		String name;
		if (element instanceof AvroIdlNamespacedNameIdentifierOwner) {
			name = ((AvroIdlNamespacedNameIdentifierOwner) element).getFullName();
		} else if (element instanceof PsiNamedElement) {
			name = ((PsiNamedElement) element).getName();
		} else {
			name = element.getText();
		}
		if (name == null) {
			return "";
		} else if (useFullName) {
			return name;
		} else {
			return name.substring(name.lastIndexOf(".") + 1);
		}
	}
}
