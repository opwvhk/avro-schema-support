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

public class AvroIdlJsonPairImpl extends ASTWrapperPsiElement implements AvroIdlJsonPair {

  public AvroIdlJsonPairImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitJsonPair(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public AvroIdlJsonValue getJsonValue() {
    return findChildByClass(AvroIdlJsonValue.class);
  }

  @Override
  @NotNull
  public PsiElement getStringLiteral() {
    return findNotNullChildByType(STRING_LITERAL);
  }

}
