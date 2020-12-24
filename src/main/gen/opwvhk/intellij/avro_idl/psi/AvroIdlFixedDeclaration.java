// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AvroIdlFixedDeclaration extends AvroIdlNamedSchemaDeclaration {

  @NotNull
  List<AvroIdlDocumentation> getDocumentationList();

  @NotNull
  List<AvroIdlSchemaProperty> getSchemaPropertyList();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  PsiElement getIntLiteral();

}
