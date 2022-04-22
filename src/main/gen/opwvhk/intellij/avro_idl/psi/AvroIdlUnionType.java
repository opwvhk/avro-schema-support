// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiListLikeElement;

public interface AvroIdlUnionType extends AvroIdlType, PsiListLikeElement {

  @NotNull
  List<AvroIdlType> getTypeList();

  @NotNull List<AvroIdlType> getComponents();

}
