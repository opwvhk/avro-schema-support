// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralValue;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceOwner;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference;

public interface AvroIdlJsonStringLiteral extends AvroIdlJsonValue, PsiLiteralValue, FileReferenceOwner {

  @NotNull
  PsiElement getStringLiteral();

  @Nullable Object getValue();

  @Nullable PsiFileReference getReference();

  @NotNull PsiReference[] getReferences();

  @Nullable PsiFileReference getLastFileReference();

}
