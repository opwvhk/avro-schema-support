// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiListLikeElement;
import com.intellij.psi.PsiLiteralValue;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceOwner;

public class AvroIdlVisitor extends PsiElementVisitor {

  public void visitArrayType(@NotNull AvroIdlArrayType o) {
    visitType(o);
  }

  public void visitDecimalType(@NotNull AvroIdlDecimalType o) {
    visitPrimitiveType(o);
  }

  public void visitEnumBody(@NotNull AvroIdlEnumBody o) {
    visitPsiListLikeElement(o);
  }

  public void visitEnumConstant(@NotNull AvroIdlEnumConstant o) {
    visitNameIdentifierOwner(o);
  }

  public void visitEnumDeclaration(@NotNull AvroIdlEnumDeclaration o) {
    visitNamedSchemaDeclaration(o);
  }

  public void visitEnumDefault(@NotNull AvroIdlEnumDefault o) {
    visitPsiElement(o);
  }

  public void visitFieldDeclaration(@NotNull AvroIdlFieldDeclaration o) {
    visitPsiListLikeElement(o);
  }

  public void visitFixedDeclaration(@NotNull AvroIdlFixedDeclaration o) {
    visitNamedSchemaDeclaration(o);
  }

  public void visitFormalParameter(@NotNull AvroIdlFormalParameter o) {
    visitPsiElement(o);
  }

  public void visitIdentifier(@NotNull AvroIdlIdentifier o) {
    visitPsiElement(o);
  }

  public void visitImportDeclaration(@NotNull AvroIdlImportDeclaration o) {
    visitPsiElement(o);
  }

  public void visitImportType(@NotNull AvroIdlImportType o) {
    visitPsiElement(o);
  }

  public void visitJsonArray(@NotNull AvroIdlJsonArray o) {
    visitPsiListLikeElement(o);
  }

  public void visitJsonObject(@NotNull AvroIdlJsonObject o) {
    visitPsiListLikeElement(o);
  }

  public void visitJsonPair(@NotNull AvroIdlJsonPair o) {
    visitPsiElement(o);
  }

  public void visitJsonStringLiteral(@NotNull AvroIdlJsonStringLiteral o) {
    visitJsonValue(o);
    // visitPsiLiteralValue(o);
    // visitFileReferenceOwner(o);
  }

  public void visitJsonValue(@NotNull AvroIdlJsonValue o) {
    visitPsiElement(o);
  }

  public void visitMainSchemaDeclaration(@NotNull AvroIdlMainSchemaDeclaration o) {
    visitPsiElement(o);
  }

  public void visitMapType(@NotNull AvroIdlMapType o) {
    visitType(o);
  }

  public void visitMessageAttributeThrows(@NotNull AvroIdlMessageAttributeThrows o) {
    visitPsiElement(o);
  }

  public void visitMessageAttributes(@NotNull AvroIdlMessageAttributes o) {
    visitPsiElement(o);
  }

  public void visitMessageDeclaration(@NotNull AvroIdlMessageDeclaration o) {
    visitWithSchemaProperties(o);
    // visitNameIdentifierOwner(o);
    // visitPsiListLikeElement(o);
  }

  public void visitNamedSchemaDeclaration(@NotNull AvroIdlNamedSchemaDeclaration o) {
    visitWithSchemaProperties(o);
    // visitNamespacedNameIdentifierOwner(o);
  }

  public void visitNamespaceDeclaration(@NotNull AvroIdlNamespaceDeclaration o) {
    visitNamespaceIdentifierOwner(o);
  }

  public void visitNamespaceProperty(@NotNull AvroIdlNamespaceProperty o) {
    visitSchemaProperty(o);
    // visitNamespaceIdentifierOwner(o);
  }

  public void visitNullableType(@NotNull AvroIdlNullableType o) {
    visitType(o);
  }

  public void visitPrimitiveType(@NotNull AvroIdlPrimitiveType o) {
    visitNullableType(o);
  }

  public void visitProtocolBody(@NotNull AvroIdlProtocolBody o) {
    visitPsiElement(o);
  }

  public void visitProtocolDeclaration(@NotNull AvroIdlProtocolDeclaration o) {
    visitWithSchemaProperties(o);
    // visitNamespacedNameIdentifierOwner(o);
  }

  public void visitRecordBody(@NotNull AvroIdlRecordBody o) {
    visitPsiElement(o);
  }

  public void visitRecordDeclaration(@NotNull AvroIdlRecordDeclaration o) {
    visitNamedSchemaDeclaration(o);
  }

  public void visitReferenceType(@NotNull AvroIdlReferenceType o) {
    visitNullableType(o);
  }

  public void visitResultType(@NotNull AvroIdlResultType o) {
    visitType(o);
  }

  public void visitSchemaProperty(@NotNull AvroIdlSchemaProperty o) {
    visitNamedType(o);
  }

  public void visitType(@NotNull AvroIdlType o) {
    visitWithSchemaProperties(o);
  }

  public void visitUnionType(@NotNull AvroIdlUnionType o) {
    visitType(o);
    // visitPsiListLikeElement(o);
  }

  public void visitVariableDeclarator(@NotNull AvroIdlVariableDeclarator o) {
    visitWithSchemaProperties(o);
    // visitAnnotatedNameIdentifierOwner(o);
  }

  public void visitWithSchemaProperties(@NotNull AvroIdlWithSchemaProperties o) {
    visitPsiElement(o);
  }

  public void visitPsiListLikeElement(@NotNull PsiListLikeElement o) {
    visitElement(o);
  }

  public void visitNameIdentifierOwner(@NotNull AvroIdlNameIdentifierOwner o) {
    visitPsiElement(o);
  }

  public void visitNamedType(@NotNull AvroIdlNamedType o) {
    visitPsiElement(o);
  }

  public void visitNamespaceIdentifierOwner(@NotNull AvroIdlNamespaceIdentifierOwner o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
