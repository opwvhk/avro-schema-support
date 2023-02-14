package opwvhk.intellij.avro_idl.language;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import opwvhk.intellij.avro_idl.psi.AvroIdlNamedSchemaDeclaration;
import opwvhk.intellij.avro_idl.psi.AvroIdlNamedType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvroIdlRefactoringSupportProvider extends RefactoringSupportProvider {

	@Override
	public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement elementToRename, @Nullable PsiElement context) {
		return (elementToRename instanceof AvroIdlNamedType);
	}

	@Override
	public boolean isSafeDeleteAvailable(@NotNull PsiElement element) {
		return element instanceof AvroIdlNamedSchemaDeclaration ||
				super.isSafeDeleteAvailable(element);
	}
}
