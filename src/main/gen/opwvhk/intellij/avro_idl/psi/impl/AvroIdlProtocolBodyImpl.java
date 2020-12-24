// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import opwvhk.intellij.avro_idl.psi.*;

public class AvroIdlProtocolBodyImpl extends ASTWrapperPsiElement implements AvroIdlProtocolBody {

  public AvroIdlProtocolBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitProtocolBody(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AvroIdlImportDeclaration> getImportDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlImportDeclaration.class);
  }

  @Override
  @NotNull
  public List<AvroIdlMessageDeclaration> getMessageDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlMessageDeclaration.class);
  }

  @Override
  @NotNull
  public List<AvroIdlNamedSchemaDeclaration> getNamedSchemaDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlNamedSchemaDeclaration.class);
  }

}
