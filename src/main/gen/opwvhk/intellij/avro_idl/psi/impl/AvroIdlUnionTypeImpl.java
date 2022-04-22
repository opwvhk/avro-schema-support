// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;
import opwvhk.intellij.avro_idl.psi.*;

public class AvroIdlUnionTypeImpl extends AvroIdlTypeImpl implements AvroIdlUnionType {

  public AvroIdlUnionTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitUnionType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AvroIdlType> getTypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlType.class);
  }

  @Override
  public @NotNull List<AvroIdlType> getComponents() {
    return AvroIdlPsiUtil.getComponents(this);
  }

}
