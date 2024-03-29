// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiListLikeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.util.IncorrectOperationException;

public interface AvroIdlMessageDeclaration extends AvroIdlWithSchemaProperties, AvroIdlNameIdentifierOwner, PsiListLikeElement {

  @NotNull
  List<AvroIdlFormalParameter> getFormalParameterList();

  @Nullable
  AvroIdlIdentifier getIdentifier();

  @Nullable
  AvroIdlMessageAttributes getMessageAttributes();

  @NotNull
  AvroIdlType getType();

  @Nullable PsiElement getNameIdentifier();

  int getTextOffset();

  @Nullable @NonNls String getName();

  PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException;

  @NotNull ItemPresentation getPresentation();

  @NotNull List<AvroIdlFormalParameter> getComponents();

}
