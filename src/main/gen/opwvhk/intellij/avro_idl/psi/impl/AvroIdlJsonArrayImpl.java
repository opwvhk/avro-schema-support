// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.psi.AvroIdlJsonArray;
import opwvhk.intellij.avro_idl.psi.AvroIdlJsonValue;
import opwvhk.intellij.avro_idl.psi.AvroIdlPsiUtil;
import opwvhk.intellij.avro_idl.psi.AvroIdlVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AvroIdlJsonArrayImpl extends ASTWrapperPsiElement implements AvroIdlJsonArray {

  public AvroIdlJsonArrayImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitJsonArray(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AvroIdlJsonValue> getJsonValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlJsonValue.class);
  }

  @Override
  public @NotNull List<AvroIdlJsonValue> getComponents() {
    return AvroIdlPsiUtil.getComponents(this);
  }

}
