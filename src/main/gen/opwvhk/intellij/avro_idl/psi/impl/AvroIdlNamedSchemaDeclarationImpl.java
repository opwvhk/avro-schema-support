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
import com.intellij.navigation.ItemPresentation;
import com.intellij.util.IncorrectOperationException;

public abstract class AvroIdlNamedSchemaDeclarationImpl extends ASTWrapperPsiElement implements AvroIdlNamedSchemaDeclaration {

  public AvroIdlNamedSchemaDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitNamedSchemaDeclaration(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getNameIdentifier() {
    return AvroIdlPsiUtil.getNameIdentifier(this);
  }

  @Override
  public int getTextOffset() {
    return AvroIdlPsiUtil.getTextOffset(this);
  }

  @Override
  @Nullable
  @NonNls
  public String getName() {
    return AvroIdlPsiUtil.getName(this);
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return AvroIdlPsiUtil.setName(this, name);
  }

  @Override
  @Nullable
  @NonNls
  public String getFullName() {
    return AvroIdlPsiUtil.getFullName(this);
  }

  @Override
  @NotNull
  public ItemPresentation getPresentation() {
    return AvroIdlPsiUtil.getPresentation(this);
  }

  @Override
  public boolean isErrorType() {
    return AvroIdlPsiUtil.isErrorType(this);
  }

  @Override
  public void delete() throws IncorrectOperationException {
    AvroIdlPsiUtil.delete(this);
  }

}
