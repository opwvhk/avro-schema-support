// This is a generated file. Not intended for manual editing.
package opwvhk.intellij.avro_idl.syntax;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

public class AvroIdlParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return CompilationUnit(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(NAMESPACE_PROPERTY, SCHEMA_PROPERTY),
    create_token_set_(JSON_STRING_LITERAL, JSON_VALUE),
    create_token_set_(ARRAY_TYPE, DECIMAL_TYPE, ENUM_DECLARATION, FIXED_DECLARATION,
      MAP_TYPE, MESSAGE_DECLARATION, NAMED_SCHEMA_DECLARATION, NULLABLE_TYPE,
      PRIMITIVE_TYPE, PROTOCOL_DECLARATION, RECORD_DECLARATION, REFERENCE_TYPE,
      RESULT_TYPE, TYPE, UNION_TYPE, VARIABLE_DECLARATOR),
  };

  /* ********************************************************** */
  // LEFT_ANGLE Type RIGHT_ANGLE
  static boolean AngleType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AngleType")) return false;
    if (!nextTokenIs(b, LEFT_ANGLE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LEFT_ANGLE);
    p = r; // pin = 1
    r = r && report_error_(b, Type(b, l + 1));
    r = p && consumeToken(b, RIGHT_ANGLE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ArrayType1 | ArrayType2
  public static boolean ArrayType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_TYPE, "<array type>");
    r = ArrayType1(b, l + 1);
    if (!r) r = ArrayType2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ ArrayType2
  static boolean ArrayType1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayType1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ArrayType1_0(b, l + 1);
    r = r && ArrayType2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SchemaProperty+
  private static boolean ArrayType1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayType1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ArrayType1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ARRAY AngleType
  static boolean ArrayType2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayType2")) return false;
    if (!nextTokenIs(b, ARRAY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, ARRAY);
    p = r; // pin = 1
    r = r && AngleType(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // SchemaFile1 | SchemaFile2 | SchemaFile3 | ProtocolDeclaration
  static boolean CompilationUnit(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompilationUnit")) return false;
    boolean r;
    r = SchemaFile1(b, l + 1);
    if (!r) r = SchemaFile2(b, l + 1);
    if (!r) r = SchemaFile3(b, l + 1);
    if (!r) r = ProtocolDeclaration(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // DecimalType1 | DecimalType2
  public static boolean DecimalType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DecimalType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DECIMAL_TYPE, "<decimal type>");
    r = DecimalType1(b, l + 1);
    if (!r) r = DecimalType2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ DecimalType2
  static boolean DecimalType1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DecimalType1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = DecimalType1_0(b, l + 1);
    r = r && DecimalType2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SchemaProperty+
  private static boolean DecimalType1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DecimalType1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "DecimalType1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DECIMAL LEFT_PAREN INT_LITERAL COMMA INT_LITERAL RIGHT_PAREN
  static boolean DecimalType2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DecimalType2")) return false;
    if (!nextTokenIs(b, DECIMAL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, DECIMAL, LEFT_PAREN, INT_LITERAL, COMMA, INT_LITERAL, RIGHT_PAREN);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // [ EQUALS JsonValue ]
  static boolean DefaultValueAssignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DefaultValueAssignment")) return false;
    DefaultValueAssignment_0(b, l + 1);
    return true;
  }

  // EQUALS JsonValue
  private static boolean DefaultValueAssignment_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DefaultValueAssignment_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQUALS);
    r = r && JsonValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [EnumConstant (COMMA EnumConstant)*]
  public static boolean EnumBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody")) return false;
    Marker m = enter_section_(b, l, _NONE_, ENUM_BODY, "<enum body>");
    EnumBody_0(b, l + 1);
    exit_section_(b, l, m, true, false, AvroIdlParser::recoverEnumBody);
    return true;
  }

  // EnumConstant (COMMA EnumConstant)*
  private static boolean EnumBody_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = EnumConstant(b, l + 1);
    r = r && EnumBody_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA EnumConstant)*
  private static boolean EnumBody_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!EnumBody_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumBody_0_1", c)) break;
    }
    return true;
  }

  // COMMA EnumConstant
  private static boolean EnumBody_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && EnumConstant(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Identifier
  public static boolean EnumConstant(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumConstant")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ENUM_CONSTANT, "<enum constant>");
    r = Identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EnumDeclaration1 | EnumDeclaration2
  public static boolean EnumDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ENUM_DECLARATION, "<enum declaration>");
    r = EnumDeclaration1(b, l + 1);
    if (!r) r = EnumDeclaration2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ EnumInnerDeclaration
  static boolean EnumDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = EnumDeclaration1_0(b, l + 1);
    r = r && EnumInnerDeclaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SchemaProperty+
  private static boolean EnumDeclaration1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumDeclaration1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EnumInnerDeclaration
  static boolean EnumDeclaration2(PsiBuilder b, int l) {
    return EnumInnerDeclaration(b, l + 1);
  }

  /* ********************************************************** */
  // Identifier
  public static boolean EnumDefault(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDefault")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ENUM_DEFAULT, "<enum default>");
    r = Identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EQUALS EnumDefault SEMICOLON
  static boolean EnumDefaultValueAssignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDefaultValueAssignment")) return false;
    if (!nextTokenIs(b, EQUALS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, EQUALS);
    p = r; // pin = 1
    r = r && report_error_(b, EnumDefault(b, l + 1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ENUM Identifier LEFT_BRACE EnumBody RIGHT_BRACE [ EnumDefaultValueAssignment ]
  static boolean EnumInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumInnerDeclaration")) return false;
    if (!nextTokenIs(b, ENUM)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, ENUM);
    p = r; // pin = 1
    r = r && report_error_(b, Identifier(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LEFT_BRACE)) && r;
    r = p && report_error_(b, EnumBody(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, RIGHT_BRACE)) && r;
    r = p && EnumInnerDeclaration_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ EnumDefaultValueAssignment ]
  private static boolean EnumInnerDeclaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumInnerDeclaration_5")) return false;
    EnumDefaultValueAssignment(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // Type VariableDeclarator ( COMMA VariableDeclarator )* SEMICOLON
  public static boolean FieldDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FIELD_DECLARATION, "<field declaration>");
    r = Type(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, VariableDeclarator(b, l + 1));
    r = p && report_error_(b, FieldDeclaration_2(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ( COMMA VariableDeclarator )*
  private static boolean FieldDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FieldDeclaration_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FieldDeclaration_2", c)) break;
    }
    return true;
  }

  // COMMA VariableDeclarator
  private static boolean FieldDeclaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && VariableDeclarator(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // FixedDeclaration1 | FixedDeclaration2
  public static boolean FixedDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIXED_DECLARATION, "<fixed declaration>");
    r = FixedDeclaration1(b, l + 1);
    if (!r) r = FixedDeclaration2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ FixedInnerDeclaration SEMICOLON
  static boolean FixedDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = FixedDeclaration1_0(b, l + 1);
    r = r && FixedInnerDeclaration(b, l + 1);
    p = r; // pin = 2
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // SchemaProperty+
  private static boolean FixedDeclaration1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FixedDeclaration1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // FixedInnerDeclaration SEMICOLON
  static boolean FixedDeclaration2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration2")) return false;
    if (!nextTokenIs(b, FIXED)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = FixedInnerDeclaration(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // FIXED Identifier LEFT_PAREN INT_LITERAL RIGHT_PAREN
  static boolean FixedInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedInnerDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, FIXED);
    p = r; // pin = 1
    r = r && report_error_(b, Identifier(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, LEFT_PAREN, INT_LITERAL, RIGHT_PAREN)) && r;
    exit_section_(b, l, m, r, p, AvroIdlParser::recoverFixedInnerDeclaration);
    return r || p;
  }

  /* ********************************************************** */
  // Type VariableDeclarator
  public static boolean FormalParameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormalParameter")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FORMAL_PARAMETER, "<formal parameter>");
    r = Type(b, l + 1);
    p = r; // pin = 1
    r = r && VariableDeclarator(b, l + 1);
    exit_section_(b, l, m, r, p, AvroIdlParser::recoverFormalParameter);
    return r || p;
  }

  /* ********************************************************** */
  // [ FormalParameter (COMMA FormalParameter)* ]
  static boolean FormalParameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormalParameters")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    FormalParameters_0(b, l + 1);
    exit_section_(b, l, m, true, false, AvroIdlParser::recoverFormalParameters);
    return true;
  }

  // FormalParameter (COMMA FormalParameter)*
  private static boolean FormalParameters_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormalParameters_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FormalParameter(b, l + 1);
    r = r && FormalParameters_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA FormalParameter)*
  private static boolean FormalParameters_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormalParameters_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FormalParameters_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FormalParameters_0_1", c)) break;
    }
    return true;
  }

  // COMMA FormalParameter
  private static boolean FormalParameters_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormalParameters_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && FormalParameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER_TOKEN
  //  | ARRAY
  //  | BOOLEAN
  //  | BYTES
  //  | DATE
  //  | DECIMAL
  //  | DOUBLE
  //  | ENUM
  //  | ERROR
  //  | FALSE
  //  | FIXED
  //  | FLOAT
  //  | IDL
  //  | IMPORT
  //  | INT
  //  | LOCAL_TIMESTAMP_MS
  //  | LONG
  //  | MAP
  //  | NAMESPACE
  //  | NULL
  //  | ONEWAY
  //  | PROTOCOL
  //  | RECORD
  //  | SCHEMA
  //  | STRING
  //  | THROWS
  //  | TIMESTAMP_MS
  //  | TIME_MS
  //  | TRUE
  //  | UNION
  //  | UUID
  //  | VOID
  public static boolean Identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Identifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IDENTIFIER, "<identifier>");
    r = consumeToken(b, IDENTIFIER_TOKEN);
    if (!r) r = consumeToken(b, ARRAY);
    if (!r) r = consumeToken(b, BOOLEAN);
    if (!r) r = consumeToken(b, BYTES);
    if (!r) r = consumeToken(b, DATE);
    if (!r) r = consumeToken(b, DECIMAL);
    if (!r) r = consumeToken(b, DOUBLE);
    if (!r) r = consumeToken(b, ENUM);
    if (!r) r = consumeToken(b, ERROR);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, FIXED);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, IDL);
    if (!r) r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, LOCAL_TIMESTAMP_MS);
    if (!r) r = consumeToken(b, LONG);
    if (!r) r = consumeToken(b, MAP);
    if (!r) r = consumeToken(b, NAMESPACE);
    if (!r) r = consumeToken(b, NULL);
    if (!r) r = consumeToken(b, ONEWAY);
    if (!r) r = consumeToken(b, PROTOCOL);
    if (!r) r = consumeToken(b, RECORD);
    if (!r) r = consumeToken(b, SCHEMA);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, THROWS);
    if (!r) r = consumeToken(b, TIMESTAMP_MS);
    if (!r) r = consumeToken(b, TIME_MS);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, UNION);
    if (!r) r = consumeToken(b, UUID);
    if (!r) r = consumeToken(b, VOID);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // IMPORT ImportInnerDeclaration SEMICOLON
  public static boolean ImportDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportDeclaration")) return false;
    if (!nextTokenIs(b, IMPORT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_DECLARATION, null);
    r = consumeToken(b, IMPORT);
    p = r; // pin = 1
    r = r && report_error_(b, ImportInnerDeclaration(b, l + 1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ImportType JsonStringLiteral
  static boolean ImportInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportInnerDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = ImportType(b, l + 1);
    r = r && JsonStringLiteral(b, l + 1);
    exit_section_(b, l, m, r, false, AvroIdlParser::recoverImportInnerDeclaration);
    return r;
  }

  /* ********************************************************** */
  // IDL | PROTOCOL | SCHEMA
  public static boolean ImportType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_TYPE, "<import type>");
    r = consumeToken(b, IDL);
    if (!r) r = consumeToken(b, PROTOCOL);
    if (!r) r = consumeToken(b, SCHEMA);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LEFT_BRACKET JsonElements RIGHT_BRACKET
  public static boolean JsonArray(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonArray")) return false;
    if (!nextTokenIs(b, LEFT_BRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, JSON_ARRAY, null);
    r = consumeToken(b, LEFT_BRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, JsonElements(b, l + 1));
    r = p && consumeToken(b, RIGHT_BRACKET) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // [ JsonValue (COMMA JsonValue)* ]
  static boolean JsonElements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonElements")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    JsonElements_0(b, l + 1);
    exit_section_(b, l, m, true, false, AvroIdlParser::recoverJsonElements);
    return true;
  }

  // JsonValue (COMMA JsonValue)*
  private static boolean JsonElements_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonElements_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = JsonValue(b, l + 1);
    r = r && JsonElements_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA JsonValue)*
  private static boolean JsonElements_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonElements_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!JsonElements_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "JsonElements_0_1", c)) break;
    }
    return true;
  }

  // COMMA JsonValue
  private static boolean JsonElements_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonElements_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && JsonValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LEFT_BRACE [ JsonPair (COMMA JsonPair)* ] RIGHT_BRACE
  public static boolean JsonObject(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonObject")) return false;
    if (!nextTokenIs(b, LEFT_BRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, JSON_OBJECT, null);
    r = consumeToken(b, LEFT_BRACE);
    p = r; // pin = 1
    r = r && report_error_(b, JsonObject_1(b, l + 1));
    r = p && consumeToken(b, RIGHT_BRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ JsonPair (COMMA JsonPair)* ]
  private static boolean JsonObject_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonObject_1")) return false;
    JsonObject_1_0(b, l + 1);
    return true;
  }

  // JsonPair (COMMA JsonPair)*
  private static boolean JsonObject_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonObject_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = JsonPair(b, l + 1);
    r = r && JsonObject_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA JsonPair)*
  private static boolean JsonObject_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonObject_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!JsonObject_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "JsonObject_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA JsonPair
  private static boolean JsonObject_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonObject_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && JsonPair(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // STRING_LITERAL COLON JsonValue
  public static boolean JsonPair(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonPair")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, JSON_PAIR, "<json pair>");
    r = consumeTokens(b, 1, STRING_LITERAL, COLON);
    p = r; // pin = 1
    r = r && JsonValue(b, l + 1);
    exit_section_(b, l, m, r, p, AvroIdlParser::recoverJsonPair);
    return r || p;
  }

  /* ********************************************************** */
  // STRING_LITERAL
  public static boolean JsonStringLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonStringLiteral")) return false;
    if (!nextTokenIs(b, STRING_LITERAL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING_LITERAL);
    exit_section_(b, m, JSON_STRING_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // JsonStringLiteral | INT_LITERAL | FLOAT_LITERAL | TRUE | FALSE | NULL | JsonObject | JsonArray
  public static boolean JsonValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, JSON_VALUE, "<json value>");
    r = JsonStringLiteral(b, l + 1);
    if (!r) r = consumeToken(b, INT_LITERAL);
    if (!r) r = consumeToken(b, FLOAT_LITERAL);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NULL);
    if (!r) r = JsonObject(b, l + 1);
    if (!r) r = JsonArray(b, l + 1);
    exit_section_(b, l, m, r, false, AvroIdlParser::recoverJsonValue);
    return r;
  }

  /* ********************************************************** */
  // SCHEMA Type SEMICOLON
  public static boolean MainSchemaDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MainSchemaDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MAIN_SCHEMA_DECLARATION, "<main schema declaration>");
    r = consumeToken(b, SCHEMA);
    p = r; // pin = 1
    r = r && report_error_(b, Type(b, l + 1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, AvroIdlParser::recoverMainSchemaDeclaration);
    return r || p;
  }

  /* ********************************************************** */
  // MapType1 | MapType2
  public static boolean MapType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MAP_TYPE, "<map type>");
    r = MapType1(b, l + 1);
    if (!r) r = MapType2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ MapType2
  static boolean MapType1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapType1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = MapType1_0(b, l + 1);
    r = r && MapType2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SchemaProperty+
  private static boolean MapType1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapType1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MapType1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // MAP AngleType
  static boolean MapType2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapType2")) return false;
    if (!nextTokenIs(b, MAP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, MAP);
    p = r; // pin = 1
    r = r && AngleType(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Identifier
  public static boolean MessageAttributeThrows(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrows")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MESSAGE_ATTRIBUTE_THROWS, "<message attribute throws>");
    r = Identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // THROWS MessageAttributeThrows (COMMA MessageAttributeThrows)*
  static boolean MessageAttributeThrowsList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList")) return false;
    if (!nextTokenIs(b, THROWS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, THROWS);
    p = r; // pin = 1
    r = r && report_error_(b, MessageAttributeThrows(b, l + 1));
    r = p && MessageAttributeThrowsList_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA MessageAttributeThrows)*
  private static boolean MessageAttributeThrowsList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MessageAttributeThrowsList_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageAttributeThrowsList_2", c)) break;
    }
    return true;
  }

  // COMMA MessageAttributeThrows
  private static boolean MessageAttributeThrowsList_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && MessageAttributeThrows(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ONEWAY | MessageAttributeThrowsList
  public static boolean MessageAttributes(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributes")) return false;
    if (!nextTokenIs(b, "<message attributes>", ONEWAY, THROWS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MESSAGE_ATTRIBUTES, "<message attributes>");
    r = consumeToken(b, ONEWAY);
    if (!r) r = MessageAttributeThrowsList(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // MessageDeclaration1 | MessageDeclaration2
  public static boolean MessageDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, MESSAGE_DECLARATION, "<message declaration>");
    r = MessageDeclaration1(b, l + 1);
    if (!r) r = MessageDeclaration2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ ResultType MessageSignature SEMICOLON
  static boolean MessageDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = MessageDeclaration1_0(b, l + 1);
    r = r && ResultType(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, MessageSignature(b, l + 1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // SchemaProperty+
  private static boolean MessageDeclaration1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageDeclaration1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ResultType MessageSignature SEMICOLON
  static boolean MessageDeclaration2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration2")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ResultType(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, MessageSignature(b, l + 1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Identifier LEFT_PAREN FormalParameters RIGHT_PAREN [ MessageAttributes ]
  static boolean MessageSignature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageSignature")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = Identifier(b, l + 1);
    r = r && consumeToken(b, LEFT_PAREN);
    r = r && FormalParameters(b, l + 1);
    r = r && consumeToken(b, RIGHT_PAREN);
    r = r && MessageSignature_4(b, l + 1);
    exit_section_(b, l, m, r, false, AvroIdlParser::recoverMessageSignature);
    return r;
  }

  // [ MessageAttributes ]
  private static boolean MessageSignature_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageSignature_4")) return false;
    MessageAttributes(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // FixedDeclaration | EnumDeclaration | RecordDeclaration
  public static boolean NamedSchemaDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NamedSchemaDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, NAMED_SCHEMA_DECLARATION, "<named schema declaration>");
    r = FixedDeclaration(b, l + 1);
    if (!r) r = EnumDeclaration(b, l + 1);
    if (!r) r = RecordDeclaration(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // NAMESPACE Identifier SEMICOLON
  public static boolean NamespaceDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NamespaceDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NAMESPACE_DECLARATION, "<namespace declaration>");
    r = consumeToken(b, NAMESPACE);
    p = r; // pin = 1
    r = r && report_error_(b, Identifier(b, l + 1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, AvroIdlParser::recoverNamespaceDeclaration);
    return r || p;
  }

  /* ********************************************************** */
  // AT_NAMESPACE LEFT_PAREN JsonValue RIGHT_PAREN
  public static boolean NamespaceProperty(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NamespaceProperty")) return false;
    if (!nextTokenIs(b, AT_NAMESPACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NAMESPACE_PROPERTY, null);
    r = consumeTokens(b, 1, AT_NAMESPACE, LEFT_PAREN);
    p = r; // pin = 1
    r = r && report_error_(b, JsonValue(b, l + 1));
    r = p && consumeToken(b, RIGHT_PAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // (PrimitiveType | ReferenceType) [ QUESTION_MARK ]
  public static boolean NullableType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NullableType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, NULLABLE_TYPE, "<nullable type>");
    r = NullableType_0(b, l + 1);
    r = r && NullableType_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PrimitiveType | ReferenceType
  private static boolean NullableType_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NullableType_0")) return false;
    boolean r;
    r = PrimitiveType(b, l + 1);
    if (!r) r = ReferenceType(b, l + 1);
    return r;
  }

  // [ QUESTION_MARK ]
  private static boolean NullableType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NullableType_1")) return false;
    consumeToken(b, QUESTION_MARK);
    return true;
  }

  /* ********************************************************** */
  // PrimitiveType1 | PrimitiveType2
  public static boolean PrimitiveType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_ | _UPPER_, PRIMITIVE_TYPE, "<primitive type>");
    r = PrimitiveType1(b, l + 1);
    if (!r) r = PrimitiveType2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ PrimitiveType2
  static boolean PrimitiveType1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = PrimitiveType1_0(b, l + 1);
    r = r && PrimitiveType2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SchemaProperty+
  private static boolean PrimitiveType1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "PrimitiveType1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // BOOLEAN | BYTES | INT | STRING | FLOAT | DOUBLE | LONG | NULL | DATE | TIME_MS | TIMESTAMP_MS| LOCAL_TIMESTAMP_MS | DecimalType | UUID
  static boolean PrimitiveType2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType2")) return false;
    boolean r;
    r = consumeToken(b, BOOLEAN);
    if (!r) r = consumeToken(b, BYTES);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, DOUBLE);
    if (!r) r = consumeToken(b, LONG);
    if (!r) r = consumeToken(b, NULL);
    if (!r) r = consumeToken(b, DATE);
    if (!r) r = consumeToken(b, TIME_MS);
    if (!r) r = consumeToken(b, TIMESTAMP_MS);
    if (!r) r = consumeToken(b, LOCAL_TIMESTAMP_MS);
    if (!r) r = DecimalType(b, l + 1);
    if (!r) r = consumeToken(b, UUID);
    return r;
  }

  /* ********************************************************** */
  // (ImportDeclaration | NamedSchemaDeclaration | MessageDeclaration)*
  public static boolean ProtocolBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolBody")) return false;
    Marker m = enter_section_(b, l, _NONE_, PROTOCOL_BODY, "<protocol body>");
    while (true) {
      int c = current_position_(b);
      if (!ProtocolBody_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ProtocolBody", c)) break;
    }
    exit_section_(b, l, m, true, false, AvroIdlParser::recoverProtocolBody);
    return true;
  }

  // ImportDeclaration | NamedSchemaDeclaration | MessageDeclaration
  private static boolean ProtocolBody_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolBody_0")) return false;
    boolean r;
    r = ImportDeclaration(b, l + 1);
    if (!r) r = NamedSchemaDeclaration(b, l + 1);
    if (!r) r = MessageDeclaration(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // ProtocolDeclaration1 | ProtocolDeclaration2
  public static boolean ProtocolDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROTOCOL_DECLARATION, "<protocol declaration>");
    r = ProtocolDeclaration1(b, l + 1);
    if (!r) r = ProtocolDeclaration2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ ProtocolDeclaration2
  static boolean ProtocolDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ProtocolDeclaration1_0(b, l + 1);
    p = r; // pin = 1
    r = r && ProtocolDeclaration2(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // SchemaProperty+
  private static boolean ProtocolDeclaration1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ProtocolDeclaration1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PROTOCOL Identifier LEFT_BRACE ProtocolBody RIGHT_BRACE
  static boolean ProtocolDeclaration2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration2")) return false;
    if (!nextTokenIs(b, PROTOCOL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, PROTOCOL);
    p = r; // pin = 1
    r = r && report_error_(b, Identifier(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LEFT_BRACE)) && r;
    r = p && report_error_(b, ProtocolBody(b, l + 1)) && r;
    r = p && consumeToken(b, RIGHT_BRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // FieldDeclaration*
  public static boolean RecordBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordBody")) return false;
    Marker m = enter_section_(b, l, _NONE_, RECORD_BODY, "<record body>");
    while (true) {
      int c = current_position_(b);
      if (!FieldDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "RecordBody", c)) break;
    }
    exit_section_(b, l, m, true, false, AvroIdlParser::recoverRecordBody);
    return true;
  }

  /* ********************************************************** */
  // RecordDeclaration1 | RecordDeclaration2
  public static boolean RecordDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RECORD_DECLARATION, "<record declaration>");
    r = RecordDeclaration1(b, l + 1);
    if (!r) r = RecordDeclaration2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ RecordInnerDeclaration
  static boolean RecordDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordDeclaration1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = RecordDeclaration1_0(b, l + 1);
    r = r && RecordInnerDeclaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SchemaProperty+
  private static boolean RecordDeclaration1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordDeclaration1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "RecordDeclaration1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // RecordInnerDeclaration
  static boolean RecordDeclaration2(PsiBuilder b, int l) {
    return RecordInnerDeclaration(b, l + 1);
  }

  /* ********************************************************** */
  // RecordType Identifier LEFT_BRACE RecordBody RIGHT_BRACE
  static boolean RecordInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordInnerDeclaration")) return false;
    if (!nextTokenIs(b, "", ERROR, RECORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = RecordType(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, Identifier(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LEFT_BRACE)) && r;
    r = p && report_error_(b, RecordBody(b, l + 1)) && r;
    r = p && consumeToken(b, RIGHT_BRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // RECORD | ERROR
  static boolean RecordType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordType")) return false;
    if (!nextTokenIs(b, "", ERROR, RECORD)) return false;
    boolean r;
    r = consumeToken(b, RECORD);
    if (!r) r = consumeToken(b, ERROR);
    return r;
  }

  /* ********************************************************** */
  // ReferenceType1 | ReferenceType2
  public static boolean ReferenceType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _UPPER_, REFERENCE_TYPE, "<reference type>");
    r = ReferenceType1(b, l + 1);
    if (!r) r = ReferenceType2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ Identifier
  static boolean ReferenceType1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceType1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ReferenceType1_0(b, l + 1);
    p = r; // pin = 1
    r = r && Identifier(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // SchemaProperty+
  private static boolean ReferenceType1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceType1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ReferenceType1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Identifier
  static boolean ReferenceType2(PsiBuilder b, int l) {
    return Identifier(b, l + 1);
  }

  /* ********************************************************** */
  // VOID | Type
  public static boolean ResultType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ResultType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, RESULT_TYPE, "<result type>");
    r = consumeToken(b, VOID);
    if (!r) r = Type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // NamespaceDeclaration [MainSchemaDeclaration] (NamedSchemaDeclaration | ImportDeclaration)*
  static boolean SchemaFile1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaFile1")) return false;
    if (!nextTokenIs(b, NAMESPACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = NamespaceDeclaration(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, SchemaFile1_1(b, l + 1));
    r = p && SchemaFile1_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [MainSchemaDeclaration]
  private static boolean SchemaFile1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaFile1_1")) return false;
    MainSchemaDeclaration(b, l + 1);
    return true;
  }

  // (NamedSchemaDeclaration | ImportDeclaration)*
  private static boolean SchemaFile1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaFile1_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!SchemaFile1_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SchemaFile1_2", c)) break;
    }
    return true;
  }

  // NamedSchemaDeclaration | ImportDeclaration
  private static boolean SchemaFile1_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaFile1_2_0")) return false;
    boolean r;
    r = NamedSchemaDeclaration(b, l + 1);
    if (!r) r = ImportDeclaration(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // MainSchemaDeclaration (NamedSchemaDeclaration | ImportDeclaration)*
  static boolean SchemaFile2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaFile2")) return false;
    if (!nextTokenIs(b, SCHEMA)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = MainSchemaDeclaration(b, l + 1);
    p = r; // pin = 1
    r = r && SchemaFile2_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (NamedSchemaDeclaration | ImportDeclaration)*
  private static boolean SchemaFile2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaFile2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!SchemaFile2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SchemaFile2_1", c)) break;
    }
    return true;
  }

  // NamedSchemaDeclaration | ImportDeclaration
  private static boolean SchemaFile2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaFile2_1_0")) return false;
    boolean r;
    r = NamedSchemaDeclaration(b, l + 1);
    if (!r) r = ImportDeclaration(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (NamedSchemaDeclaration | ImportDeclaration)+
  static boolean SchemaFile3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaFile3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaFile3_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaFile3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SchemaFile3", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // NamedSchemaDeclaration | ImportDeclaration
  private static boolean SchemaFile3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaFile3_0")) return false;
    boolean r;
    r = NamedSchemaDeclaration(b, l + 1);
    if (!r) r = ImportDeclaration(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // NamespaceProperty | SimpleSchemaProperty
  public static boolean SchemaProperty(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaProperty")) return false;
    if (!nextTokenIs(b, "<schema property>", AT, AT_NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, SCHEMA_PROPERTY, "<schema property>");
    r = NamespaceProperty(b, l + 1);
    if (!r) r = SimpleSchemaProperty(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // AT Identifier LEFT_PAREN JsonValue RIGHT_PAREN
  static boolean SimpleSchemaProperty(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleSchemaProperty")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, AT);
    p = r; // pin = 1
    r = r && report_error_(b, Identifier(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LEFT_PAREN)) && r;
    r = p && report_error_(b, JsonValue(b, l + 1)) && r;
    r = p && consumeToken(b, RIGHT_PAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ArrayType | MapType | UnionType | NullableType
  public static boolean Type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, TYPE, "<type>");
    r = ArrayType(b, l + 1);
    if (!r) r = MapType(b, l + 1);
    if (!r) r = UnionType(b, l + 1);
    if (!r) r = NullableType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [ Type (COMMA Type)* ]
  static boolean UnionContents(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionContents")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    UnionContents_0(b, l + 1);
    exit_section_(b, l, m, true, false, AvroIdlParser::recoverUnionContents);
    return true;
  }

  // Type (COMMA Type)*
  private static boolean UnionContents_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionContents_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Type(b, l + 1);
    r = r && UnionContents_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA Type)*
  private static boolean UnionContents_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionContents_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!UnionContents_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "UnionContents_0_1", c)) break;
    }
    return true;
  }

  // COMMA Type
  private static boolean UnionContents_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionContents_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // UNION LEFT_BRACE UnionContents RIGHT_BRACE
  public static boolean UnionType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionType")) return false;
    if (!nextTokenIs(b, UNION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UNION_TYPE, null);
    r = consumeTokens(b, 1, UNION, LEFT_BRACE);
    p = r; // pin = 1
    r = r && report_error_(b, UnionContents(b, l + 1));
    r = p && consumeToken(b, RIGHT_BRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // VariableDeclarator1 | VariableDeclarator2
  public static boolean VariableDeclarator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VARIABLE_DECLARATOR, "<variable declarator>");
    r = VariableDeclarator1(b, l + 1);
    if (!r) r = VariableDeclarator2(b, l + 1);
    exit_section_(b, l, m, r, false, AvroIdlParser::recoverVariableDeclarator);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty+ Identifier DefaultValueAssignment
  static boolean VariableDeclarator1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator1")) return false;
    if (!nextTokenIs(b, "", AT, AT_NAMESPACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = VariableDeclarator1_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, Identifier(b, l + 1));
    r = p && DefaultValueAssignment(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // SchemaProperty+
  private static boolean VariableDeclarator1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!SchemaProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Identifier DefaultValueAssignment
  static boolean VariableDeclarator2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator2")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Identifier(b, l + 1);
    p = r; // pin = 1
    r = r && DefaultValueAssignment(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // !(RIGHT_BRACE)
  static boolean recoverEnumBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverEnumBody")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverEnumBody_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (RIGHT_BRACE)
  private static boolean recoverEnumBody_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverEnumBody_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, RIGHT_BRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(SEMICOLON|IMPORT|FIXED|ENUM|RECORD|ERROR|AT|AT_NAMESPACE)
  static boolean recoverFixedInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverFixedInnerDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverFixedInnerDeclaration_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON|IMPORT|FIXED|ENUM|RECORD|ERROR|AT|AT_NAMESPACE
  private static boolean recoverFixedInnerDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverFixedInnerDeclaration_0")) return false;
    boolean r;
    r = consumeTokenFast(b, SEMICOLON);
    if (!r) r = consumeTokenFast(b, IMPORT);
    if (!r) r = consumeTokenFast(b, FIXED);
    if (!r) r = consumeTokenFast(b, ENUM);
    if (!r) r = consumeTokenFast(b, RECORD);
    if (!r) r = consumeTokenFast(b, ERROR);
    if (!r) r = consumeTokenFast(b, AT);
    if (!r) r = consumeTokenFast(b, AT_NAMESPACE);
    return r;
  }

  /* ********************************************************** */
  // !(RIGHT_PAREN|COMMA)
  static boolean recoverFormalParameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverFormalParameter")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverFormalParameter_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RIGHT_PAREN|COMMA
  private static boolean recoverFormalParameter_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverFormalParameter_0")) return false;
    boolean r;
    r = consumeTokenFast(b, RIGHT_PAREN);
    if (!r) r = consumeTokenFast(b, COMMA);
    return r;
  }

  /* ********************************************************** */
  // !(RIGHT_PAREN)
  static boolean recoverFormalParameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverFormalParameters")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverFormalParameters_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (RIGHT_PAREN)
  private static boolean recoverFormalParameters_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverFormalParameters_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, RIGHT_PAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(SEMICOLON|IMPORT|FIXED|ENUM|RECORD|ERROR|AT|AT_NAMESPACE)
  static boolean recoverImportInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverImportInnerDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverImportInnerDeclaration_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON|IMPORT|FIXED|ENUM|RECORD|ERROR|AT|AT_NAMESPACE
  private static boolean recoverImportInnerDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverImportInnerDeclaration_0")) return false;
    boolean r;
    r = consumeTokenFast(b, SEMICOLON);
    if (!r) r = consumeTokenFast(b, IMPORT);
    if (!r) r = consumeTokenFast(b, FIXED);
    if (!r) r = consumeTokenFast(b, ENUM);
    if (!r) r = consumeTokenFast(b, RECORD);
    if (!r) r = consumeTokenFast(b, ERROR);
    if (!r) r = consumeTokenFast(b, AT);
    if (!r) r = consumeTokenFast(b, AT_NAMESPACE);
    return r;
  }

  /* ********************************************************** */
  // !(RIGHT_BRACKET)
  static boolean recoverJsonElements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverJsonElements")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverJsonElements_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (RIGHT_BRACKET)
  private static boolean recoverJsonElements_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverJsonElements_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, RIGHT_BRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(RIGHT_BRACE|COMMA)
  static boolean recoverJsonPair(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverJsonPair")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverJsonPair_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RIGHT_BRACE|COMMA
  private static boolean recoverJsonPair_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverJsonPair_0")) return false;
    boolean r;
    r = consumeTokenFast(b, RIGHT_BRACE);
    if (!r) r = consumeTokenFast(b, COMMA);
    return r;
  }

  /* ********************************************************** */
  // !(RIGHT_PAREN|COMMA|SEMICOLON|IMPORT|FIXED|ENUM|RECORD|ERROR|AT|AT_NAMESPACE|RIGHT_BRACE|RIGHT_BRACKET)
  static boolean recoverJsonValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverJsonValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverJsonValue_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RIGHT_PAREN|COMMA|SEMICOLON|IMPORT|FIXED|ENUM|RECORD|ERROR|AT|AT_NAMESPACE|RIGHT_BRACE|RIGHT_BRACKET
  private static boolean recoverJsonValue_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverJsonValue_0")) return false;
    boolean r;
    r = consumeTokenFast(b, RIGHT_PAREN);
    if (!r) r = consumeTokenFast(b, COMMA);
    if (!r) r = consumeTokenFast(b, SEMICOLON);
    if (!r) r = consumeTokenFast(b, IMPORT);
    if (!r) r = consumeTokenFast(b, FIXED);
    if (!r) r = consumeTokenFast(b, ENUM);
    if (!r) r = consumeTokenFast(b, RECORD);
    if (!r) r = consumeTokenFast(b, ERROR);
    if (!r) r = consumeTokenFast(b, AT);
    if (!r) r = consumeTokenFast(b, AT_NAMESPACE);
    if (!r) r = consumeTokenFast(b, RIGHT_BRACE);
    if (!r) r = consumeTokenFast(b, RIGHT_BRACKET);
    return r;
  }

  /* ********************************************************** */
  // !(IMPORT|FIXED|ENUM|RECORD|ERROR|AT|AT_NAMESPACE)
  static boolean recoverMainSchemaDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverMainSchemaDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverMainSchemaDeclaration_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // IMPORT|FIXED|ENUM|RECORD|ERROR|AT|AT_NAMESPACE
  private static boolean recoverMainSchemaDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverMainSchemaDeclaration_0")) return false;
    boolean r;
    r = consumeTokenFast(b, IMPORT);
    if (!r) r = consumeTokenFast(b, FIXED);
    if (!r) r = consumeTokenFast(b, ENUM);
    if (!r) r = consumeTokenFast(b, RECORD);
    if (!r) r = consumeTokenFast(b, ERROR);
    if (!r) r = consumeTokenFast(b, AT);
    if (!r) r = consumeTokenFast(b, AT_NAMESPACE);
    return r;
  }

  /* ********************************************************** */
  // !(SEMICOLON)
  static boolean recoverMessageSignature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverMessageSignature")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverMessageSignature_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (SEMICOLON)
  private static boolean recoverMessageSignature_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverMessageSignature_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(SCHEMA|IMPORT|FIXED|ENUM|RECORD|ERROR|AT|AT_NAMESPACE)
  static boolean recoverNamespaceDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverNamespaceDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverNamespaceDeclaration_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SCHEMA|IMPORT|FIXED|ENUM|RECORD|ERROR|AT|AT_NAMESPACE
  private static boolean recoverNamespaceDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverNamespaceDeclaration_0")) return false;
    boolean r;
    r = consumeTokenFast(b, SCHEMA);
    if (!r) r = consumeTokenFast(b, IMPORT);
    if (!r) r = consumeTokenFast(b, FIXED);
    if (!r) r = consumeTokenFast(b, ENUM);
    if (!r) r = consumeTokenFast(b, RECORD);
    if (!r) r = consumeTokenFast(b, ERROR);
    if (!r) r = consumeTokenFast(b, AT);
    if (!r) r = consumeTokenFast(b, AT_NAMESPACE);
    return r;
  }

  /* ********************************************************** */
  // !(RIGHT_BRACE)
  static boolean recoverProtocolBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverProtocolBody")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverProtocolBody_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (RIGHT_BRACE)
  private static boolean recoverProtocolBody_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverProtocolBody_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, RIGHT_BRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(RIGHT_BRACE)
  static boolean recoverRecordBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverRecordBody")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverRecordBody_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (RIGHT_BRACE)
  private static boolean recoverRecordBody_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverRecordBody_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, RIGHT_BRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(RIGHT_BRACE)
  static boolean recoverUnionContents(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverUnionContents")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverUnionContents_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (RIGHT_BRACE)
  private static boolean recoverUnionContents_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverUnionContents_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, RIGHT_BRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(SEMICOLON|RIGHT_BRACE|RIGHT_PAREN|COMMA)
  static boolean recoverVariableDeclarator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverVariableDeclarator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverVariableDeclarator_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON|RIGHT_BRACE|RIGHT_PAREN|COMMA
  private static boolean recoverVariableDeclarator_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverVariableDeclarator_0")) return false;
    boolean r;
    r = consumeTokenFast(b, SEMICOLON);
    if (!r) r = consumeTokenFast(b, RIGHT_BRACE);
    if (!r) r = consumeTokenFast(b, RIGHT_PAREN);
    if (!r) r = consumeTokenFast(b, COMMA);
    return r;
  }

}
