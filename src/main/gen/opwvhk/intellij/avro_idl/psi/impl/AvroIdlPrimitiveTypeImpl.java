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

public class AvroIdlPrimitiveTypeImpl extends AvroIdlNullableTypeImpl implements AvroIdlPrimitiveType {

  public AvroIdlPrimitiveTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitPrimitiveType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public AvroIdlDecimalType getDecimalType() {
    return findChildByClass(AvroIdlDecimalType.class);
  }

  @Override
  @NotNull
  public List<AvroIdlMisplacedDocumentation> getMisplacedDocumentationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlMisplacedDocumentation.class);
  }

}
