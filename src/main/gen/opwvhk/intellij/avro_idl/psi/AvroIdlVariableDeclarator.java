// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;

public interface AvroIdlVariableDeclarator extends AvroIdlWithSchemaProperties, AvroIdlAnnotatedNameIdentifierOwner {

  @Nullable
  AvroIdlJsonValue getJsonValue();

  @Nullable
  PsiElement getIdentifier();

  @Nullable PsiElement getNameIdentifier();

  int getTextOffset();

  @Nullable @NonNls String getName();

  PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException;

}
