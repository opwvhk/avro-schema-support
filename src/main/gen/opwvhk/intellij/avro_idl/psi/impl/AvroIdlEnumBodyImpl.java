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

public class AvroIdlEnumBodyImpl extends ASTWrapperPsiElement implements AvroIdlEnumBody {

  public AvroIdlEnumBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AvroIdlVisitor visitor) {
    visitor.visitEnumBody(this);
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
  @NotNull
  public List<AvroIdlEnumConstant> getEnumConstantList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AvroIdlEnumConstant.class);
  }

}
