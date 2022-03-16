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

public class AvroIdlFormalParameterImpl extends ASTWrapperPsiElement implements AvroIdlFormalParameter {

  public AvroIdlFormalParameterImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitFormalParameter(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AvroIdlWithSchemaProperties> getWithSchemaPropertiesList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlWithSchemaProperties.class);
  }

  @Override
  public @NotNull AvroIdlType getType() {
    return AvroIdlPsiUtil.getType(this);
  }

  @Override
  public @Nullable AvroIdlVariableDeclarator getVariableDeclarator() {
    return AvroIdlPsiUtil.getVariableDeclarator(this);
  }

}
