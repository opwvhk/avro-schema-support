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
import com.intellij.psi.tree.IElementType;

public class AvroIdlDocumentationImpl extends ASTWrapperPsiElement implements AvroIdlDocumentation {

  public AvroIdlDocumentationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitDocumentation(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getDocComment() {
    return findNotNullChildByType(DOC_COMMENT);
  }

  @Override
  public @Nullable PsiElement getOwner() {
    return AvroIdlPsiUtil.getOwner(this);
  }

  @Override
  public @NotNull IElementType getTokenType() {
    return AvroIdlPsiUtil.getTokenType(this);
  }

}
