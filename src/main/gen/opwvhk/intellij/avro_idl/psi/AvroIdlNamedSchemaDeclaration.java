// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.util.IncorrectOperationException;

public interface AvroIdlNamedSchemaDeclaration extends AvroIdlWithSchemaProperties, AvroIdlNamespacedNameIdentifierOwner {

  @Nullable PsiElement getNameIdentifier();

  int getTextOffset();

  @Nullable @NonNls String getName();

  PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException;

  @Nullable @NonNls String getFullName();

  @NotNull ItemPresentation getPresentation();

  boolean isErrorType();

  void delete() throws IncorrectOperationException;

}
