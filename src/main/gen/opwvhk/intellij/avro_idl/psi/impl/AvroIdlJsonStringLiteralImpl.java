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
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference;

public class AvroIdlJsonStringLiteralImpl extends AvroIdlJsonValueImpl implements AvroIdlJsonStringLiteral {

  public AvroIdlJsonStringLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitJsonStringLiteral(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getStringLiteral() {
    return findNotNullChildByType(STRING_LITERAL);
  }

  @Override
  public @Nullable PsiFileReference getReference() {
    return AvroIdlPsiUtil.getReference(this);
  }

  @Override
  public @Nullable PsiFileReference getLastFileReference() {
    return AvroIdlPsiUtil.getLastFileReference(this);
  }

}
