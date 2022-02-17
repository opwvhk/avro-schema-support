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
import com.intellij.util.IncorrectOperationException;

public class AvroIdlProtocolDeclarationImpl extends AvroIdlWithSchemaPropertiesImpl implements AvroIdlProtocolDeclaration {

  public AvroIdlProtocolDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitProtocolDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public AvroIdlIdentifier getIdentifier() {
    return findChildByClass(AvroIdlIdentifier.class);
  }

  @Override
  @Nullable
  public AvroIdlProtocolBody getProtocolBody() {
    return findChildByClass(AvroIdlProtocolBody.class);
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return AvroIdlPsiUtil.getNameIdentifier(this);
  }

  @Override
  public int getTextOffset() {
    return AvroIdlPsiUtil.getTextOffset(this);
  }

  @Override
  public @Nullable @NonNls String getName() {
    return AvroIdlPsiUtil.getName(this);
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return AvroIdlPsiUtil.setName(this, name);
  }

  @Override
  public @Nullable @NonNls String getFullName() {
    return AvroIdlPsiUtil.getFullName(this);
  }

}
