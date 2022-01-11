// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AvroIdlProtocolBody extends PsiElement {

  @NotNull
  List<AvroIdlImportDeclaration> getImportDeclarationList();

  @NotNull
  List<AvroIdlMisplacedDocumentation> getMisplacedDocumentationList();

  @NotNull
  List<AvroIdlWithSchemaProperties> getWithSchemaPropertiesList();

  @NotNull List<AvroIdlNamedSchemaDeclaration> getNamedSchemaDeclarationList();

  @NotNull List<AvroIdlMessageDeclaration> getMessageDeclarationList();

}
