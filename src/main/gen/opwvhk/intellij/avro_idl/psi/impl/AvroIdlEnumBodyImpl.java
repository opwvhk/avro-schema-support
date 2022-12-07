// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumBody;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumConstant;
import opwvhk.intellij.avro_idl.psi.AvroIdlPsiUtil;
import opwvhk.intellij.avro_idl.psi.AvroIdlVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AvroIdlEnumBodyImpl extends ASTWrapperPsiElement implements AvroIdlEnumBody {

  public AvroIdlEnumBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitEnumBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AvroIdlEnumConstant> getEnumConstantList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlEnumConstant.class);
  }

  @Override
  @NotNull
  public List<AvroIdlEnumConstant> getComponents() {
    return AvroIdlPsiUtil.getComponents(this);
  }

}
