// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AvroIdlFieldDeclaration extends PsiElement {

  @NotNull
  List<AvroIdlWithSchemaProperties> getWithSchemaPropertiesList();

  @NotNull AvroIdlType getType();

  @NotNull List<AvroIdlVariableDeclarator> getVariableDeclaratorList();

}
