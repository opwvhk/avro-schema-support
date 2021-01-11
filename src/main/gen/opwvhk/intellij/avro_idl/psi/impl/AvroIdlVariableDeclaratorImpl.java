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
import com.intellij.util.IncorrectOperationException;

public class AvroIdlVariableDeclaratorImpl extends ASTWrapperPsiElement implements AvroIdlVariableDeclarator {

  public AvroIdlVariableDeclaratorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitVariableDeclarator(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AvroIdlDocumentation> getDocumentationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlDocumentation.class);
  }

  @Override
  @Nullable
  public AvroIdlJsonValue getJsonValue() {
    return findChildByClass(AvroIdlJsonValue.class);
  }

  @Override
  @NotNull
  public List<AvroIdlSchemaProperty> getSchemaPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlSchemaProperty.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
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
  @NonNls
  public @Nullable String getName() {
    return AvroIdlPsiUtil.getName(this);
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return AvroIdlPsiUtil.setName(this, name);
  }

}
