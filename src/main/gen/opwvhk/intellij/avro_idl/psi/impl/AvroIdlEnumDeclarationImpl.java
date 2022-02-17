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

public class AvroIdlEnumDeclarationImpl extends AvroIdlNamedSchemaDeclarationImpl implements AvroIdlEnumDeclaration {

  public AvroIdlEnumDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitEnumDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public AvroIdlEnumBody getEnumBody() {
    return findChildByClass(AvroIdlEnumBody.class);
  }

  @Override
  @Nullable
  public AvroIdlEnumDefault getEnumDefault() {
    return findChildByClass(AvroIdlEnumDefault.class);
  }

  @Override
  @Nullable
  public AvroIdlIdentifier getIdentifier() {
    return findChildByClass(AvroIdlIdentifier.class);
  }

}
