// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import opwvhk.intellij.avro_idl.psi.impl.*;

public interface AvroIdlTypes {

  IElementType ARRAY_TYPE = new AvroIdlElementType("ARRAY_TYPE");
  IElementType DECIMAL_TYPE = new AvroIdlElementType("DECIMAL_TYPE");
  IElementType DOCUMENTATION = new AvroIdlElementType("DOCUMENTATION");
  IElementType ENUM_BODY = new AvroIdlElementType("ENUM_BODY");
  IElementType ENUM_CONSTANT = new AvroIdlElementType("ENUM_CONSTANT");
  IElementType ENUM_DECLARATION = new AvroIdlElementType("ENUM_DECLARATION");
  IElementType ENUM_DEFAULT = new AvroIdlElementType("ENUM_DEFAULT");
  IElementType FIELD_DECLARATION = new AvroIdlElementType("FIELD_DECLARATION");
  IElementType FIXED_DECLARATION = new AvroIdlElementType("FIXED_DECLARATION");
  IElementType FORMAL_PARAMETER = new AvroIdlElementType("FORMAL_PARAMETER");
  IElementType IMPORT_DECLARATION = new AvroIdlElementType("IMPORT_DECLARATION");
  IElementType IMPORT_TYPE = new AvroIdlElementType("IMPORT_TYPE");
  IElementType JSON_ARRAY = new AvroIdlElementType("JSON_ARRAY");
  IElementType JSON_OBJECT = new AvroIdlElementType("JSON_OBJECT");
  IElementType JSON_PAIR = new AvroIdlElementType("JSON_PAIR");
  IElementType JSON_VALUE = new AvroIdlElementType("JSON_VALUE");
  IElementType MAP_TYPE = new AvroIdlElementType("MAP_TYPE");
  IElementType MESSAGE_ATTRIBUTES = new AvroIdlElementType("MESSAGE_ATTRIBUTES");
  IElementType MESSAGE_ATTRIBUTE_THROWS = new AvroIdlElementType("MESSAGE_ATTRIBUTE_THROWS");
  IElementType MESSAGE_DECLARATION = new AvroIdlElementType("MESSAGE_DECLARATION");
  IElementType NAMED_SCHEMA_DECLARATION = new AvroIdlElementType("NAMED_SCHEMA_DECLARATION");
  IElementType PRIMITIVE_TYPE = new AvroIdlElementType("PRIMITIVE_TYPE");
  IElementType PROTOCOL_BODY = new AvroIdlElementType("PROTOCOL_BODY");
  IElementType PROTOCOL_DECLARATION = new AvroIdlElementType("PROTOCOL_DECLARATION");
  IElementType RECORD_BODY = new AvroIdlElementType("RECORD_BODY");
  IElementType RECORD_DECLARATION = new AvroIdlElementType("RECORD_DECLARATION");
  IElementType REFERENCE_TYPE = new AvroIdlElementType("REFERENCE_TYPE");
  IElementType RESULT_TYPE = new AvroIdlElementType("RESULT_TYPE");
  IElementType SCHEMA_PROPERTY = new AvroIdlElementType("SCHEMA_PROPERTY");
  IElementType TYPE = new AvroIdlElementType("TYPE");
  IElementType UNION_TYPE = new AvroIdlElementType("UNION_TYPE");
  IElementType VARIABLE_DECLARATOR = new AvroIdlElementType("VARIABLE_DECLARATOR");

  IElementType ARRAY = new AvroIdlTokenType("array");
  IElementType AT = new AvroIdlTokenType("@");
  IElementType BLOCK_COMMENT = new AvroIdlTokenType("BLOCK_COMMENT");
  IElementType BOOLEAN = new AvroIdlTokenType("boolean");
  IElementType BYTES = new AvroIdlTokenType("bytes");
  IElementType COLON = new AvroIdlTokenType(":");
  IElementType COMMA = new AvroIdlTokenType(",");
  IElementType DATE = new AvroIdlTokenType("date");
  IElementType DECIMAL = new AvroIdlTokenType("decimal");
  IElementType DOC_COMMENT = new AvroIdlTokenType("DOC_COMMENT");
  IElementType DOUBLE = new AvroIdlTokenType("double");
  IElementType ENUM = new AvroIdlTokenType("enum");
  IElementType EQUALS = new AvroIdlTokenType("=");
  IElementType ERROR = new AvroIdlTokenType("error");
  IElementType FALSE = new AvroIdlTokenType("false");
  IElementType FIXED = new AvroIdlTokenType("fixed");
  IElementType FLOAT = new AvroIdlTokenType("float");
  IElementType FLOAT_LITERAL = new AvroIdlTokenType("FLOAT_LITERAL");
  IElementType IDENTIFIER = new AvroIdlTokenType("IDENTIFIER");
  IElementType IDL = new AvroIdlTokenType("idl");
  IElementType IMPORT = new AvroIdlTokenType("import");
  IElementType INT = new AvroIdlTokenType("int");
  IElementType INT_LITERAL = new AvroIdlTokenType("INT_LITERAL");
  IElementType LEFT_ANGLE = new AvroIdlTokenType("<");
  IElementType LEFT_BRACE = new AvroIdlTokenType("{");
  IElementType LEFT_BRACKET = new AvroIdlTokenType("[");
  IElementType LEFT_PAREN = new AvroIdlTokenType("(");
  IElementType LINE_COMMENT = new AvroIdlTokenType("LINE_COMMENT");
  IElementType LOCAL_TIMESTAMP_MS = new AvroIdlTokenType("local_timestamp_ms");
  IElementType LONG = new AvroIdlTokenType("long");
  IElementType MAP = new AvroIdlTokenType("map");
  IElementType NULL = new AvroIdlTokenType("null");
  IElementType ONEWAY = new AvroIdlTokenType("oneway");
  IElementType PROTOCOL = new AvroIdlTokenType("protocol");
  IElementType RECORD = new AvroIdlTokenType("record");
  IElementType RIGHT_ANGLE = new AvroIdlTokenType(">");
  IElementType RIGHT_BRACE = new AvroIdlTokenType("}");
  IElementType RIGHT_BRACKET = new AvroIdlTokenType("]");
  IElementType RIGHT_PAREN = new AvroIdlTokenType(")");
  IElementType SCHEMA = new AvroIdlTokenType("schema");
  IElementType SEMICOLON = new AvroIdlTokenType(";");
  IElementType STRING = new AvroIdlTokenType("string");
  IElementType STRING_LITERAL = new AvroIdlTokenType("STRING_LITERAL");
  IElementType THROWS = new AvroIdlTokenType("throws");
  IElementType TIMESTAMP_MS = new AvroIdlTokenType("timestamp_ms");
  IElementType TIME_MS = new AvroIdlTokenType("time_ms");
  IElementType TRUE = new AvroIdlTokenType("true");
  IElementType UNION = new AvroIdlTokenType("union");
  IElementType UUID = new AvroIdlTokenType("uuid");
  IElementType VOID = new AvroIdlTokenType("void");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARRAY_TYPE) {
        return new AvroIdlArrayTypeImpl(node);
      }
      else if (type == DECIMAL_TYPE) {
        return new AvroIdlDecimalTypeImpl(node);
      }
      else if (type == DOCUMENTATION) {
        return new AvroIdlDocumentationImpl(node);
      }
      else if (type == ENUM_BODY) {
        return new AvroIdlEnumBodyImpl(node);
      }
      else if (type == ENUM_CONSTANT) {
        return new AvroIdlEnumConstantImpl(node);
      }
      else if (type == ENUM_DECLARATION) {
        return new AvroIdlEnumDeclarationImpl(node);
      }
      else if (type == ENUM_DEFAULT) {
        return new AvroIdlEnumDefaultImpl(node);
      }
      else if (type == FIELD_DECLARATION) {
        return new AvroIdlFieldDeclarationImpl(node);
      }
      else if (type == FIXED_DECLARATION) {
        return new AvroIdlFixedDeclarationImpl(node);
      }
      else if (type == FORMAL_PARAMETER) {
        return new AvroIdlFormalParameterImpl(node);
      }
      else if (type == IMPORT_DECLARATION) {
        return new AvroIdlImportDeclarationImpl(node);
      }
      else if (type == IMPORT_TYPE) {
        return new AvroIdlImportTypeImpl(node);
      }
      else if (type == JSON_ARRAY) {
        return new AvroIdlJsonArrayImpl(node);
      }
      else if (type == JSON_OBJECT) {
        return new AvroIdlJsonObjectImpl(node);
      }
      else if (type == JSON_PAIR) {
        return new AvroIdlJsonPairImpl(node);
      }
      else if (type == JSON_VALUE) {
        return new AvroIdlJsonValueImpl(node);
      }
      else if (type == MAP_TYPE) {
        return new AvroIdlMapTypeImpl(node);
      }
      else if (type == MESSAGE_ATTRIBUTES) {
        return new AvroIdlMessageAttributesImpl(node);
      }
      else if (type == MESSAGE_ATTRIBUTE_THROWS) {
        return new AvroIdlMessageAttributeThrowsImpl(node);
      }
      else if (type == MESSAGE_DECLARATION) {
        return new AvroIdlMessageDeclarationImpl(node);
      }
      else if (type == PRIMITIVE_TYPE) {
        return new AvroIdlPrimitiveTypeImpl(node);
      }
      else if (type == PROTOCOL_BODY) {
        return new AvroIdlProtocolBodyImpl(node);
      }
      else if (type == PROTOCOL_DECLARATION) {
        return new AvroIdlProtocolDeclarationImpl(node);
      }
      else if (type == RECORD_BODY) {
        return new AvroIdlRecordBodyImpl(node);
      }
      else if (type == RECORD_DECLARATION) {
        return new AvroIdlRecordDeclarationImpl(node);
      }
      else if (type == REFERENCE_TYPE) {
        return new AvroIdlReferenceTypeImpl(node);
      }
      else if (type == RESULT_TYPE) {
        return new AvroIdlResultTypeImpl(node);
      }
      else if (type == SCHEMA_PROPERTY) {
        return new AvroIdlSchemaPropertyImpl(node);
      }
      else if (type == UNION_TYPE) {
        return new AvroIdlUnionTypeImpl(node);
      }
      else if (type == VARIABLE_DECLARATOR) {
        return new AvroIdlVariableDeclaratorImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
