// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;

public interface AvroIdlNamespaceDeclaration extends AvroIdlNamespaceIdentifierOwner {

  @Nullable
  AvroIdlIdentifier getIdentifier();

  @Nullable PsiElement getNameIdentifier();

  int getTextOffset();

  @Nullable @NonNls String getName();

  PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException;

}
