// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;

public interface AvroIdlSchemaProperty extends AvroIdlNamedType {

  @Nullable
  AvroIdlJsonValue getJsonValue();

  @Nullable
  PsiElement getIdentifier();

  @NonNls
  @Nullable String getName();

  PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException;

}
