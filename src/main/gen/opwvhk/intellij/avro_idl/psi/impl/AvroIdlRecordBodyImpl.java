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

public class AvroIdlRecordBodyImpl extends ASTWrapperPsiElement implements AvroIdlRecordBody {

  public AvroIdlRecordBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitRecordBody(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AvroIdlVisitor) accept((AvroIdlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AvroIdlFieldDeclaration> getFieldDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlFieldDeclaration.class);
  }

}
