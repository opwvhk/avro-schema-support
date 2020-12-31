package opwvhk.intellij.avro_idl.language;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import opwvhk.intellij.avro_idl.psi.*;
import opwvhk.intellij.avro_idl.syntax.AvroIdlLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvroIdlFindUsagesProvider implements com.intellij.lang.findUsages.FindUsagesProvider {

	@Override
	public @Nullable WordsScanner getWordsScanner() {
		final TokenSet identifiers = TokenSet.create(AvroIdlTypes.IDENTIFIER);
		final TokenSet comments = TokenSet.create(AvroIdlTypes.DOC_COMMENT, AvroIdlTypes.BLOCK_COMMENT, AvroIdlTypes.LINE_COMMENT);
		return new DefaultWordsScanner(new AvroIdlLexer(), identifiers, comments, TokenSet.EMPTY);
	}

	@Override
	public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
		return psiElement instanceof AvroIdlNamedSchemaDeclaration || psiElement instanceof AvroIdlEnumConstant;
	}

	@Override
	public @Nullable String getHelpId(@NotNull PsiElement psiElement) {
		return null;
	}

	@Override
	public @NotNull String getType(@NotNull PsiElement element) {
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
	public @NotNull String getDescriptiveName(@NotNull PsiElement element) {
		return getNodeText(element, false);
	}

	@Override
	public @NotNull String getNodeText(@NotNull PsiElement element, boolean useFullName) {
		String name;
		if (element instanceof AvroIdlNamespacedNameIdentifierOwner) {
			name = ((AvroIdlNamedSchemaDeclaration) element).getFullName();
		} else if (element instanceof PsiNamedElement) {
			name = ((PsiNamedElement) element).getName();
		} else {
			name = element.getText();
		}
		if (name == null) {
			return "";
		} else if (useFullName) {
			return name;
		} else
		return name.substring(name.lastIndexOf(".") + 1);
	}
}
