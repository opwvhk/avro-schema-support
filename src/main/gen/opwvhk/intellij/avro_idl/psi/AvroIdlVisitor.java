// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceOwner;

public class AvroIdlVisitor extends PsiElementVisitor {

  public void visitArrayType(@NotNull AvroIdlArrayType o) {
    visitType(o);
  }

  public void visitDecimalType(@NotNull AvroIdlDecimalType o) {
    visitPrimitiveType(o);
  }

  public void visitDocumentation(@NotNull AvroIdlDocumentation o) {
    visitPsiElement(o);
  }

  public void visitEnumBody(@NotNull AvroIdlEnumBody o) {
    visitPsiElement(o);
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
    visitPsiElement(o);
  }

  public void visitFixedDeclaration(@NotNull AvroIdlFixedDeclaration o) {
    visitNamedSchemaDeclaration(o);
  }

  public void visitFormalParameter(@NotNull AvroIdlFormalParameter o) {
    visitPsiElement(o);
  }

  public void visitImportDeclaration(@NotNull AvroIdlImportDeclaration o) {
    visitPsiElement(o);
  }

  public void visitImportType(@NotNull AvroIdlImportType o) {
    visitPsiElement(o);
  }

  public void visitJsonArray(@NotNull AvroIdlJsonArray o) {
    visitPsiElement(o);
  }

  public void visitJsonObject(@NotNull AvroIdlJsonObject o) {
    visitPsiElement(o);
  }

  public void visitJsonPair(@NotNull AvroIdlJsonPair o) {
    visitPsiElement(o);
  }

  public void visitJsonStringLiteral(@NotNull AvroIdlJsonStringLiteral o) {
    visitJsonValue(o);
    // visitFileReferenceOwner(o);
  }

  public void visitJsonValue(@NotNull AvroIdlJsonValue o) {
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
  }

  public void visitMisplacedDocumentation(@NotNull AvroIdlMisplacedDocumentation o) {
    visitDocumentation(o);
  }

  public void visitNamedSchemaDeclaration(@NotNull AvroIdlNamedSchemaDeclaration o) {
    visitWithSchemaProperties(o);
    // visitNamespacedNameIdentifierOwner(o);
  }

  public void visitNamespaceProperty(@NotNull AvroIdlNamespaceProperty o) {
    visitSchemaProperty(o);
    // visitNameIdentifierOwner(o);
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
  }

  public void visitVariableDeclarator(@NotNull AvroIdlVariableDeclarator o) {
    visitWithSchemaProperties(o);
    // visitAnnotatedNameIdentifierOwner(o);
  }

  public void visitWithSchemaProperties(@NotNull AvroIdlWithSchemaProperties o) {
    visitPsiElement(o);
  }

  public void visitNameIdentifierOwner(@NotNull AvroIdlNameIdentifierOwner o) {
    visitPsiElement(o);
  }

  public void visitNamedType(@NotNull AvroIdlNamedType o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
