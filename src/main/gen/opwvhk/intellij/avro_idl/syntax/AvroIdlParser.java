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

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
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
    create_token_set_(JSON_STRING_LITERAL, JSON_VALUE),
    create_token_set_(ENUM_DECLARATION, FIXED_DECLARATION, NAMED_SCHEMA_DECLARATION, RECORD_DECLARATION),
    create_token_set_(ARRAY_TYPE, DECIMAL_TYPE, MAP_TYPE, NULLABLE_TYPE,
      PRIMITIVE_TYPE, REFERENCE_TYPE, RESULT_TYPE, TYPE,
      UNION_TYPE),
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
    if (!nextTokenIs(b, "<array type>", ARRAY, AT)) return false;
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
    if (!nextTokenIs(b, AT)) return false;
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
  // ProtocolDeclaration
  static boolean CompilationUnit(PsiBuilder b, int l) {
    return ProtocolDeclaration(b, l + 1);
  }

  /* ********************************************************** */
  // DecimalType1 | DecimalType2
  public static boolean DecimalType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DecimalType")) return false;
    if (!nextTokenIs(b, "<decimal type>", AT, DECIMAL)) return false;
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
    if (!nextTokenIs(b, AT)) return false;
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
  // DOC_COMMENT
  public static boolean Documentation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Documentation")) return false;
    if (!nextTokenIs(b, DOC_COMMENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOC_COMMENT);
    exit_section_(b, m, DOCUMENTATION, r);
    return r;
  }

  /* ********************************************************** */
  // Documentation* EnumConstant Documentation* (COMMA Documentation* EnumConstant Documentation*)*
  public static boolean EnumBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ENUM_BODY, "<enum body>");
    r = EnumBody_0(b, l + 1);
    r = r && EnumConstant(b, l + 1);
    r = r && EnumBody_2(b, l + 1);
    r = r && EnumBody_3(b, l + 1);
    exit_section_(b, l, m, r, false, AvroIdlParser::recoverEnumBody);
    return r;
  }

  // Documentation*
  private static boolean EnumBody_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumBody_0", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean EnumBody_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumBody_2", c)) break;
    }
    return true;
  }

  // (COMMA Documentation* EnumConstant Documentation*)*
  private static boolean EnumBody_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!EnumBody_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumBody_3", c)) break;
    }
    return true;
  }

  // COMMA Documentation* EnumConstant Documentation*
  private static boolean EnumBody_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && EnumBody_3_0_1(b, l + 1);
    r = r && EnumConstant(b, l + 1);
    r = r && EnumBody_3_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Documentation*
  private static boolean EnumBody_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody_3_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumBody_3_0_1", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean EnumBody_3_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody_3_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumBody_3_0_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean EnumConstant(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumConstant")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, ENUM_CONSTANT, r);
    return r;
  }

  /* ********************************************************** */
  // EnumDeclaration1 | EnumDeclaration2 | EnumDeclaration3
  public static boolean EnumDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ENUM_DECLARATION, "<enum declaration>");
    r = EnumDeclaration1(b, l + 1);
    if (!r) r = EnumDeclaration2(b, l + 1);
    if (!r) r = EnumDeclaration3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Documentation (SchemaProperty|Documentation)* EnumInnerDeclaration
  static boolean EnumDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration1")) return false;
    if (!nextTokenIs(b, DOC_COMMENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Documentation(b, l + 1);
    r = r && EnumDeclaration1_1(b, l + 1);
    r = r && EnumInnerDeclaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SchemaProperty|Documentation)*
  private static boolean EnumDeclaration1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!EnumDeclaration1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumDeclaration1_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean EnumDeclaration1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration1_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty (SchemaProperty|Documentation)* EnumInnerDeclaration
  static boolean EnumDeclaration2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration2")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    r = r && EnumDeclaration2_1(b, l + 1);
    r = r && EnumInnerDeclaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SchemaProperty|Documentation)*
  private static boolean EnumDeclaration2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!EnumDeclaration2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumDeclaration2_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean EnumDeclaration2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration2_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // EnumInnerDeclaration
  static boolean EnumDeclaration3(PsiBuilder b, int l) {
    return EnumInnerDeclaration(b, l + 1);
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean EnumDefault(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDefault")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, ENUM_DEFAULT, r);
    return r;
  }

  /* ********************************************************** */
  // Documentation* EQUALS Documentation* EnumDefault Documentation* SEMICOLON
  static boolean EnumDefaultValueAssignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDefaultValueAssignment")) return false;
    if (!nextTokenIs(b, "", DOC_COMMENT, EQUALS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = EnumDefaultValueAssignment_0(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    p = r; // pin = 2
    r = r && report_error_(b, EnumDefaultValueAssignment_2(b, l + 1));
    r = p && report_error_(b, EnumDefault(b, l + 1)) && r;
    r = p && report_error_(b, EnumDefaultValueAssignment_4(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Documentation*
  private static boolean EnumDefaultValueAssignment_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDefaultValueAssignment_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumDefaultValueAssignment_0", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean EnumDefaultValueAssignment_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDefaultValueAssignment_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumDefaultValueAssignment_2", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean EnumDefaultValueAssignment_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDefaultValueAssignment_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumDefaultValueAssignment_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ENUM Documentation* IDENTIFIER Documentation* LEFT_BRACE [ EnumBody] RIGHT_BRACE [ EnumDefaultValueAssignment ]
  static boolean EnumInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumInnerDeclaration")) return false;
    if (!nextTokenIs(b, ENUM)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, ENUM);
    p = r; // pin = 1
    r = r && report_error_(b, EnumInnerDeclaration_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, IDENTIFIER)) && r;
    r = p && report_error_(b, EnumInnerDeclaration_3(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, LEFT_BRACE)) && r;
    r = p && report_error_(b, EnumInnerDeclaration_5(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, RIGHT_BRACE)) && r;
    r = p && EnumInnerDeclaration_7(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Documentation*
  private static boolean EnumInnerDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumInnerDeclaration_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumInnerDeclaration_1", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean EnumInnerDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumInnerDeclaration_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumInnerDeclaration_3", c)) break;
    }
    return true;
  }

  // [ EnumBody]
  private static boolean EnumInnerDeclaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumInnerDeclaration_5")) return false;
    EnumBody(b, l + 1);
    return true;
  }

  // [ EnumDefaultValueAssignment ]
  private static boolean EnumInnerDeclaration_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumInnerDeclaration_7")) return false;
    EnumDefaultValueAssignment(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // FieldDeclaration1 | FieldDeclaration2 { //recoverWhile="recoverFieldDeclaration"
  // //	implements="opwvhk.intellij.avro_idl.psi.AvroIdlNameIdentifierOwner"
  // //	methods=[getNameIdentifier getTextOffset getName setName]
  // }
  public static boolean FieldDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD_DECLARATION, "<field declaration>");
    r = FieldDeclaration1(b, l + 1);
    if (!r) r = FieldDeclaration_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // FieldDeclaration2 { //recoverWhile="recoverFieldDeclaration"
  // //	implements="opwvhk.intellij.avro_idl.psi.AvroIdlNameIdentifierOwner"
  // //	methods=[getNameIdentifier getTextOffset getName setName]
  // }
  private static boolean FieldDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FieldDeclaration2(b, l + 1);
    r = r && FieldDeclaration_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { //recoverWhile="recoverFieldDeclaration"
  // //	implements="opwvhk.intellij.avro_idl.psi.AvroIdlNameIdentifierOwner"
  // //	methods=[getNameIdentifier getTextOffset getName setName]
  // }
  private static boolean FieldDeclaration_1_1(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // Documentation+ Type VariableDeclarator ( COMMA VariableDeclarator )* SEMICOLON
  static boolean FieldDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration1")) return false;
    if (!nextTokenIs(b, DOC_COMMENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = FieldDeclaration1_0(b, l + 1);
    r = r && Type(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, VariableDeclarator(b, l + 1));
    r = p && report_error_(b, FieldDeclaration1_3(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Documentation+
  private static boolean FieldDeclaration1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Documentation(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FieldDeclaration1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // ( COMMA VariableDeclarator )*
  private static boolean FieldDeclaration1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration1_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FieldDeclaration1_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FieldDeclaration1_3", c)) break;
    }
    return true;
  }

  // COMMA VariableDeclarator
  private static boolean FieldDeclaration1_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration1_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && VariableDeclarator(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Type VariableDeclarator ( COMMA VariableDeclarator )* SEMICOLON
  static boolean FieldDeclaration2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration2")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Type(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, VariableDeclarator(b, l + 1));
    r = p && report_error_(b, FieldDeclaration2_2(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ( COMMA VariableDeclarator )*
  private static boolean FieldDeclaration2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration2_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FieldDeclaration2_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FieldDeclaration2_2", c)) break;
    }
    return true;
  }

  // COMMA VariableDeclarator
  private static boolean FieldDeclaration2_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration2_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && VariableDeclarator(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // FixedDeclaration1 | FixedDeclaration2 | FixedDeclaration3
  public static boolean FixedDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIXED_DECLARATION, "<fixed declaration>");
    r = FixedDeclaration1(b, l + 1);
    if (!r) r = FixedDeclaration2(b, l + 1);
    if (!r) r = FixedDeclaration3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Documentation (SchemaProperty|Documentation)* FixedInnerDeclaration SEMICOLON
  static boolean FixedDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration1")) return false;
    if (!nextTokenIs(b, DOC_COMMENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Documentation(b, l + 1);
    r = r && FixedDeclaration1_1(b, l + 1);
    r = r && FixedInnerDeclaration(b, l + 1);
    p = r; // pin = 3
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (SchemaProperty|Documentation)*
  private static boolean FixedDeclaration1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FixedDeclaration1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FixedDeclaration1_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean FixedDeclaration1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration1_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty (SchemaProperty|Documentation)* FixedInnerDeclaration SEMICOLON
  static boolean FixedDeclaration2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration2")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = SchemaProperty(b, l + 1);
    r = r && FixedDeclaration2_1(b, l + 1);
    r = r && FixedInnerDeclaration(b, l + 1);
    p = r; // pin = 3
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (SchemaProperty|Documentation)*
  private static boolean FixedDeclaration2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FixedDeclaration2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FixedDeclaration2_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean FixedDeclaration2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration2_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // FixedInnerDeclaration SEMICOLON
  static boolean FixedDeclaration3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedDeclaration3")) return false;
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
  // FIXED Documentation* IDENTIFIER Documentation* LEFT_PAREN Documentation* INT_LITERAL Documentation* RIGHT_PAREN Documentation*
  static boolean FixedInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedInnerDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, FIXED);
    p = r; // pin = 1
    r = r && report_error_(b, FixedInnerDeclaration_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, IDENTIFIER)) && r;
    r = p && report_error_(b, FixedInnerDeclaration_3(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, LEFT_PAREN)) && r;
    r = p && report_error_(b, FixedInnerDeclaration_5(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, INT_LITERAL)) && r;
    r = p && report_error_(b, FixedInnerDeclaration_7(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, RIGHT_PAREN)) && r;
    r = p && FixedInnerDeclaration_9(b, l + 1) && r;
    exit_section_(b, l, m, r, p, AvroIdlParser::recoverFixedInnerDeclaration);
    return r || p;
  }

  // Documentation*
  private static boolean FixedInnerDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedInnerDeclaration_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FixedInnerDeclaration_1", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean FixedInnerDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedInnerDeclaration_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FixedInnerDeclaration_3", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean FixedInnerDeclaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedInnerDeclaration_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FixedInnerDeclaration_5", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean FixedInnerDeclaration_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedInnerDeclaration_7")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FixedInnerDeclaration_7", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean FixedInnerDeclaration_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedInnerDeclaration_9")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FixedInnerDeclaration_9", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // FormalParameter1 | FormalParameter2
  public static boolean FormalParameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormalParameter")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FORMAL_PARAMETER, "<formal parameter>");
    r = FormalParameter1(b, l + 1);
    if (!r) r = FormalParameter2(b, l + 1);
    exit_section_(b, l, m, r, false, AvroIdlParser::recoverFormalParameter);
    return r;
  }

  /* ********************************************************** */
  // Type VariableDeclarator
  static boolean FormalParameter1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormalParameter1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Type(b, l + 1);
    p = r; // pin = 1
    r = r && VariableDeclarator(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Documentation+ Type VariableDeclarator
  static boolean FormalParameter2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormalParameter2")) return false;
    if (!nextTokenIs(b, DOC_COMMENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = FormalParameter2_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, Type(b, l + 1));
    r = p && VariableDeclarator(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Documentation+
  private static boolean FormalParameter2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormalParameter2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Documentation(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FormalParameter2_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
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
    r = consumeTokens(b, 2, STRING_LITERAL, COLON);
    p = r; // pin = 2
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
  // MapType1 | MapType2
  public static boolean MapType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapType")) return false;
    if (!nextTokenIs(b, "<map type>", AT, MAP)) return false;
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
    if (!nextTokenIs(b, AT)) return false;
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
  // IDENTIFIER
  public static boolean MessageAttributeThrows(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrows")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, MESSAGE_ATTRIBUTE_THROWS, r);
    return r;
  }

  /* ********************************************************** */
  // Documentation* THROWS Documentation* MessageAttributeThrows Documentation* (COMMA Documentation* MessageAttributeThrows Documentation* )*
  static boolean MessageAttributeThrowsList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList")) return false;
    if (!nextTokenIs(b, "", DOC_COMMENT, THROWS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = MessageAttributeThrowsList_0(b, l + 1);
    r = r && consumeToken(b, THROWS);
    p = r; // pin = 2
    r = r && report_error_(b, MessageAttributeThrowsList_2(b, l + 1));
    r = p && report_error_(b, MessageAttributeThrows(b, l + 1)) && r;
    r = p && report_error_(b, MessageAttributeThrowsList_4(b, l + 1)) && r;
    r = p && MessageAttributeThrowsList_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Documentation*
  private static boolean MessageAttributeThrowsList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageAttributeThrowsList_0", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean MessageAttributeThrowsList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageAttributeThrowsList_2", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean MessageAttributeThrowsList_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageAttributeThrowsList_4", c)) break;
    }
    return true;
  }

  // (COMMA Documentation* MessageAttributeThrows Documentation* )*
  private static boolean MessageAttributeThrowsList_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MessageAttributeThrowsList_5_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageAttributeThrowsList_5", c)) break;
    }
    return true;
  }

  // COMMA Documentation* MessageAttributeThrows Documentation*
  private static boolean MessageAttributeThrowsList_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && MessageAttributeThrowsList_5_0_1(b, l + 1);
    r = r && MessageAttributeThrows(b, l + 1);
    r = r && MessageAttributeThrowsList_5_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Documentation*
  private static boolean MessageAttributeThrowsList_5_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList_5_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageAttributeThrowsList_5_0_1", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean MessageAttributeThrowsList_5_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributeThrowsList_5_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageAttributeThrowsList_5_0_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // Documentation* ONEWAY Documentation* | MessageAttributeThrowsList
  public static boolean MessageAttributes(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributes")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MESSAGE_ATTRIBUTES, "<message attributes>");
    r = MessageAttributes_0(b, l + 1);
    if (!r) r = MessageAttributeThrowsList(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // Documentation* ONEWAY Documentation*
  private static boolean MessageAttributes_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributes_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = MessageAttributes_0_0(b, l + 1);
    r = r && consumeToken(b, ONEWAY);
    r = r && MessageAttributes_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Documentation*
  private static boolean MessageAttributes_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributes_0_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageAttributes_0_0", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean MessageAttributes_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageAttributes_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageAttributes_0_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // MessageDeclaration1 | MessageDeclaration2 | MessageDeclaration3
  public static boolean MessageDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MESSAGE_DECLARATION, "<message declaration>");
    r = MessageDeclaration1(b, l + 1);
    if (!r) r = MessageDeclaration2(b, l + 1);
    if (!r) r = MessageDeclaration3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Documentation (SchemaProperty|Documentation)* ResultType Documentation* MessageSignature SEMICOLON
  static boolean MessageDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration1")) return false;
    if (!nextTokenIs(b, DOC_COMMENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Documentation(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, MessageDeclaration1_1(b, l + 1));
    r = p && report_error_(b, ResultType(b, l + 1)) && r;
    r = p && report_error_(b, MessageDeclaration1_3(b, l + 1)) && r;
    r = p && report_error_(b, MessageSignature(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (SchemaProperty|Documentation)*
  private static boolean MessageDeclaration1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MessageDeclaration1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageDeclaration1_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean MessageDeclaration1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration1_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  // Documentation*
  private static boolean MessageDeclaration1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration1_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageDeclaration1_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SchemaProperty (SchemaProperty|Documentation)* ResultType Documentation* MessageSignature SEMICOLON
  static boolean MessageDeclaration2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration2")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = SchemaProperty(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, MessageDeclaration2_1(b, l + 1));
    r = p && report_error_(b, ResultType(b, l + 1)) && r;
    r = p && report_error_(b, MessageDeclaration2_3(b, l + 1)) && r;
    r = p && report_error_(b, MessageSignature(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (SchemaProperty|Documentation)*
  private static boolean MessageDeclaration2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MessageDeclaration2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageDeclaration2_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean MessageDeclaration2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration2_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  // Documentation*
  private static boolean MessageDeclaration2_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration2_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageDeclaration2_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ResultType Documentation* MessageSignature SEMICOLON
  static boolean MessageDeclaration3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration3")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ResultType(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, MessageDeclaration3_1(b, l + 1));
    r = p && report_error_(b, MessageSignature(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Documentation*
  private static boolean MessageDeclaration3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration3_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageDeclaration3_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER Documentation* LEFT_PAREN FormalParameters RIGHT_PAREN [ MessageAttributes ]
  static boolean MessageSignature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageSignature")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, IDENTIFIER);
    r = r && MessageSignature_1(b, l + 1);
    r = r && consumeToken(b, LEFT_PAREN);
    r = r && FormalParameters(b, l + 1);
    r = r && consumeToken(b, RIGHT_PAREN);
    r = r && MessageSignature_5(b, l + 1);
    exit_section_(b, l, m, r, false, AvroIdlParser::recoverMessageSignature);
    return r;
  }

  // Documentation*
  private static boolean MessageSignature_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageSignature_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageSignature_1", c)) break;
    }
    return true;
  }

  // [ MessageAttributes ]
  private static boolean MessageSignature_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageSignature_5")) return false;
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
  // PrimitiveType1 | PrimitiveType2 | PrimitiveType3
  public static boolean PrimitiveType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_ | _UPPER_, PRIMITIVE_TYPE, "<primitive type>");
    r = PrimitiveType1(b, l + 1);
    if (!r) r = PrimitiveType2(b, l + 1);
    if (!r) r = PrimitiveType3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty (Documentation | SchemaProperty)* PrimitiveType3
  static boolean PrimitiveType1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType1")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    r = r && PrimitiveType1_1(b, l + 1);
    r = r && PrimitiveType3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (Documentation | SchemaProperty)*
  private static boolean PrimitiveType1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!PrimitiveType1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "PrimitiveType1_1", c)) break;
    }
    return true;
  }

  // Documentation | SchemaProperty
  private static boolean PrimitiveType1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType1_1_0")) return false;
    boolean r;
    r = Documentation(b, l + 1);
    if (!r) r = SchemaProperty(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // Documentation (Documentation | SchemaProperty)* PrimitiveType3
  static boolean PrimitiveType2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType2")) return false;
    if (!nextTokenIs(b, DOC_COMMENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Documentation(b, l + 1);
    r = r && PrimitiveType2_1(b, l + 1);
    r = r && PrimitiveType3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (Documentation | SchemaProperty)*
  private static boolean PrimitiveType2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!PrimitiveType2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "PrimitiveType2_1", c)) break;
    }
    return true;
  }

  // Documentation | SchemaProperty
  private static boolean PrimitiveType2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType2_1_0")) return false;
    boolean r;
    r = Documentation(b, l + 1);
    if (!r) r = SchemaProperty(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // BOOLEAN | BYTES | INT | STRING | FLOAT | DOUBLE | LONG | NULL | DATE | TIME_MS | TIMESTAMP_MS| LOCAL_TIMESTAMP_MS | DecimalType | UUID
  static boolean PrimitiveType3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType3")) return false;
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
  // ProtocolDeclaration1 | ProtocolDeclaration2 | ProtocolDeclaration3
  public static boolean ProtocolDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROTOCOL_DECLARATION, "<protocol declaration>");
    r = ProtocolDeclaration1(b, l + 1);
    if (!r) r = ProtocolDeclaration2(b, l + 1);
    if (!r) r = ProtocolDeclaration3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Documentation (SchemaProperty|Documentation)* ProtocolDeclaration3
  static boolean ProtocolDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration1")) return false;
    if (!nextTokenIs(b, DOC_COMMENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Documentation(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, ProtocolDeclaration1_1(b, l + 1));
    r = p && ProtocolDeclaration3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (SchemaProperty|Documentation)*
  private static boolean ProtocolDeclaration1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ProtocolDeclaration1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ProtocolDeclaration1_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean ProtocolDeclaration1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration1_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty (SchemaProperty|Documentation)* ProtocolDeclaration3
  static boolean ProtocolDeclaration2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration2")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = SchemaProperty(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, ProtocolDeclaration2_1(b, l + 1));
    r = p && ProtocolDeclaration3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (SchemaProperty|Documentation)*
  private static boolean ProtocolDeclaration2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ProtocolDeclaration2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ProtocolDeclaration2_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean ProtocolDeclaration2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration2_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // PROTOCOL Documentation* IDENTIFIER LEFT_BRACE ProtocolBody RIGHT_BRACE
  static boolean ProtocolDeclaration3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration3")) return false;
    if (!nextTokenIs(b, PROTOCOL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, PROTOCOL);
    p = r; // pin = 1
    r = r && report_error_(b, ProtocolDeclaration3_1(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, IDENTIFIER, LEFT_BRACE)) && r;
    r = p && report_error_(b, ProtocolBody(b, l + 1)) && r;
    r = p && consumeToken(b, RIGHT_BRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Documentation*
  private static boolean ProtocolDeclaration3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ProtocolDeclaration3_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ProtocolDeclaration3_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // FieldDeclaration* Documentation*
  public static boolean RecordBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordBody")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RECORD_BODY, "<record body>");
    r = RecordBody_0(b, l + 1);
    r = r && RecordBody_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // FieldDeclaration*
  private static boolean RecordBody_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordBody_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FieldDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "RecordBody_0", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean RecordBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordBody_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "RecordBody_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // RecordDeclaration1 | RecordDeclaration2 | RecordDeclaration3
  public static boolean RecordDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RECORD_DECLARATION, "<record declaration>");
    r = RecordDeclaration1(b, l + 1);
    if (!r) r = RecordDeclaration2(b, l + 1);
    if (!r) r = RecordDeclaration3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Documentation (SchemaProperty|Documentation)* RecordInnerDeclaration
  static boolean RecordDeclaration1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordDeclaration1")) return false;
    if (!nextTokenIs(b, DOC_COMMENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Documentation(b, l + 1);
    r = r && RecordDeclaration1_1(b, l + 1);
    r = r && RecordInnerDeclaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SchemaProperty|Documentation)*
  private static boolean RecordDeclaration1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordDeclaration1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!RecordDeclaration1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "RecordDeclaration1_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean RecordDeclaration1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordDeclaration1_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // SchemaProperty (SchemaProperty|Documentation)* RecordInnerDeclaration
  static boolean RecordDeclaration2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordDeclaration2")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SchemaProperty(b, l + 1);
    r = r && RecordDeclaration2_1(b, l + 1);
    r = r && RecordInnerDeclaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SchemaProperty|Documentation)*
  private static boolean RecordDeclaration2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordDeclaration2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!RecordDeclaration2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "RecordDeclaration2_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean RecordDeclaration2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordDeclaration2_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // RecordInnerDeclaration
  static boolean RecordDeclaration3(PsiBuilder b, int l) {
    return RecordInnerDeclaration(b, l + 1);
  }

  /* ********************************************************** */
  // RecordType Documentation* IDENTIFIER Documentation* LEFT_BRACE RecordBody RIGHT_BRACE
  static boolean RecordInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordInnerDeclaration")) return false;
    if (!nextTokenIs(b, "", ERROR, RECORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = RecordType(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, RecordInnerDeclaration_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, IDENTIFIER)) && r;
    r = p && report_error_(b, RecordInnerDeclaration_3(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, LEFT_BRACE)) && r;
    r = p && report_error_(b, RecordBody(b, l + 1)) && r;
    r = p && consumeToken(b, RIGHT_BRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Documentation*
  private static boolean RecordInnerDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordInnerDeclaration_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "RecordInnerDeclaration_1", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean RecordInnerDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RecordInnerDeclaration_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "RecordInnerDeclaration_3", c)) break;
    }
    return true;
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
  // Documentation* SchemaProperty (Documentation | SchemaProperty)* IDENTIFIER
  static boolean ReferenceType1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceType1")) return false;
    if (!nextTokenIs(b, "", AT, DOC_COMMENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ReferenceType1_0(b, l + 1);
    r = r && SchemaProperty(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, ReferenceType1_2(b, l + 1));
    r = p && consumeToken(b, IDENTIFIER) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Documentation*
  private static boolean ReferenceType1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceType1_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ReferenceType1_0", c)) break;
    }
    return true;
  }

  // (Documentation | SchemaProperty)*
  private static boolean ReferenceType1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceType1_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ReferenceType1_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ReferenceType1_2", c)) break;
    }
    return true;
  }

  // Documentation | SchemaProperty
  private static boolean ReferenceType1_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceType1_2_0")) return false;
    boolean r;
    r = Documentation(b, l + 1);
    if (!r) r = SchemaProperty(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (Documentation | SchemaProperty)* IDENTIFIER
  static boolean ReferenceType2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceType2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ReferenceType2_0(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  // (Documentation | SchemaProperty)*
  private static boolean ReferenceType2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceType2_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ReferenceType2_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ReferenceType2_0", c)) break;
    }
    return true;
  }

  // Documentation | SchemaProperty
  private static boolean ReferenceType2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceType2_0_0")) return false;
    boolean r;
    r = Documentation(b, l + 1);
    if (!r) r = SchemaProperty(b, l + 1);
    return r;
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
  // AT IDENTIFIER LEFT_PAREN JsonValue RIGHT_PAREN
  public static boolean SchemaProperty(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SchemaProperty")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SCHEMA_PROPERTY, null);
    r = consumeTokens(b, 1, AT, IDENTIFIER, LEFT_PAREN);
    p = r; // pin = 1
    r = r && report_error_(b, JsonValue(b, l + 1));
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
  // VariableDeclarator1 | VariableDeclarator2 | VariableDeclarator3
  public static boolean VariableDeclarator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VARIABLE_DECLARATOR, "<variable declarator>");
    r = VariableDeclarator1(b, l + 1);
    if (!r) r = VariableDeclarator2(b, l + 1);
    if (!r) r = VariableDeclarator3(b, l + 1);
    exit_section_(b, l, m, r, false, AvroIdlParser::recoverVariableDeclarator);
    return r;
  }

  /* ********************************************************** */
  // Documentation (SchemaProperty|Documentation)* IDENTIFIER Documentation* [ EQUALS Documentation* JsonValue ] Documentation*
  static boolean VariableDeclarator1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator1")) return false;
    if (!nextTokenIs(b, DOC_COMMENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Documentation(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, VariableDeclarator1_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, IDENTIFIER)) && r;
    r = p && report_error_(b, VariableDeclarator1_3(b, l + 1)) && r;
    r = p && report_error_(b, VariableDeclarator1_4(b, l + 1)) && r;
    r = p && VariableDeclarator1_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (SchemaProperty|Documentation)*
  private static boolean VariableDeclarator1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!VariableDeclarator1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator1_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean VariableDeclarator1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator1_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  // Documentation*
  private static boolean VariableDeclarator1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator1_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator1_3", c)) break;
    }
    return true;
  }

  // [ EQUALS Documentation* JsonValue ]
  private static boolean VariableDeclarator1_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator1_4")) return false;
    VariableDeclarator1_4_0(b, l + 1);
    return true;
  }

  // EQUALS Documentation* JsonValue
  private static boolean VariableDeclarator1_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator1_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQUALS);
    r = r && VariableDeclarator1_4_0_1(b, l + 1);
    r = r && JsonValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Documentation*
  private static boolean VariableDeclarator1_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator1_4_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator1_4_0_1", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean VariableDeclarator1_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator1_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator1_5", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SchemaProperty (SchemaProperty|Documentation)* IDENTIFIER Documentation* [ EQUALS Documentation* JsonValue ] Documentation*
  static boolean VariableDeclarator2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator2")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = SchemaProperty(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, VariableDeclarator2_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, IDENTIFIER)) && r;
    r = p && report_error_(b, VariableDeclarator2_3(b, l + 1)) && r;
    r = p && report_error_(b, VariableDeclarator2_4(b, l + 1)) && r;
    r = p && VariableDeclarator2_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (SchemaProperty|Documentation)*
  private static boolean VariableDeclarator2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!VariableDeclarator2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator2_1", c)) break;
    }
    return true;
  }

  // SchemaProperty|Documentation
  private static boolean VariableDeclarator2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator2_1_0")) return false;
    boolean r;
    r = SchemaProperty(b, l + 1);
    if (!r) r = Documentation(b, l + 1);
    return r;
  }

  // Documentation*
  private static boolean VariableDeclarator2_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator2_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator2_3", c)) break;
    }
    return true;
  }

  // [ EQUALS Documentation* JsonValue ]
  private static boolean VariableDeclarator2_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator2_4")) return false;
    VariableDeclarator2_4_0(b, l + 1);
    return true;
  }

  // EQUALS Documentation* JsonValue
  private static boolean VariableDeclarator2_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator2_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQUALS);
    r = r && VariableDeclarator2_4_0_1(b, l + 1);
    r = r && JsonValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Documentation*
  private static boolean VariableDeclarator2_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator2_4_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator2_4_0_1", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean VariableDeclarator2_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator2_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator2_5", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER Documentation* [ EQUALS Documentation* JsonValue ] Documentation*
  static boolean VariableDeclarator3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator3")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, IDENTIFIER);
    p = r; // pin = 1
    r = r && report_error_(b, VariableDeclarator3_1(b, l + 1));
    r = p && report_error_(b, VariableDeclarator3_2(b, l + 1)) && r;
    r = p && VariableDeclarator3_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Documentation*
  private static boolean VariableDeclarator3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator3_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator3_1", c)) break;
    }
    return true;
  }

  // [ EQUALS Documentation* JsonValue ]
  private static boolean VariableDeclarator3_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator3_2")) return false;
    VariableDeclarator3_2_0(b, l + 1);
    return true;
  }

  // EQUALS Documentation* JsonValue
  private static boolean VariableDeclarator3_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator3_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQUALS);
    r = r && VariableDeclarator3_2_0_1(b, l + 1);
    r = r && JsonValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Documentation*
  private static boolean VariableDeclarator3_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator3_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator3_2_0_1", c)) break;
    }
    return true;
  }

  // Documentation*
  private static boolean VariableDeclarator3_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VariableDeclarator3_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Documentation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VariableDeclarator3_3", c)) break;
    }
    return true;
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
  // !(SEMICOLON|FIXED|ENUM|RECORD|ERROR|AT)
  static boolean recoverFixedInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverFixedInnerDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverFixedInnerDeclaration_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON|FIXED|ENUM|RECORD|ERROR|AT
  private static boolean recoverFixedInnerDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverFixedInnerDeclaration_0")) return false;
    boolean r;
    r = consumeTokenFast(b, SEMICOLON);
    if (!r) r = consumeTokenFast(b, FIXED);
    if (!r) r = consumeTokenFast(b, ENUM);
    if (!r) r = consumeTokenFast(b, RECORD);
    if (!r) r = consumeTokenFast(b, ERROR);
    if (!r) r = consumeTokenFast(b, AT);
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
  // !(SEMICOLON|FIXED|ENUM|RECORD|ERROR|AT)
  static boolean recoverImportInnerDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverImportInnerDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverImportInnerDeclaration_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON|FIXED|ENUM|RECORD|ERROR|AT
  private static boolean recoverImportInnerDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverImportInnerDeclaration_0")) return false;
    boolean r;
    r = consumeTokenFast(b, SEMICOLON);
    if (!r) r = consumeTokenFast(b, FIXED);
    if (!r) r = consumeTokenFast(b, ENUM);
    if (!r) r = consumeTokenFast(b, RECORD);
    if (!r) r = consumeTokenFast(b, ERROR);
    if (!r) r = consumeTokenFast(b, AT);
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
  // !(RIGHT_PAREN|COMMA|SEMICOLON|FIXED|ENUM|RECORD|ERROR|AT|RIGHT_BRACE|RIGHT_BRACKET)
  static boolean recoverJsonValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverJsonValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recoverJsonValue_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RIGHT_PAREN|COMMA|SEMICOLON|FIXED|ENUM|RECORD|ERROR|AT|RIGHT_BRACE|RIGHT_BRACKET
  private static boolean recoverJsonValue_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recoverJsonValue_0")) return false;
    boolean r;
    r = consumeTokenFast(b, RIGHT_PAREN);
    if (!r) r = consumeTokenFast(b, COMMA);
    if (!r) r = consumeTokenFast(b, SEMICOLON);
    if (!r) r = consumeTokenFast(b, FIXED);
    if (!r) r = consumeTokenFast(b, ENUM);
    if (!r) r = consumeTokenFast(b, RECORD);
    if (!r) r = consumeTokenFast(b, ERROR);
    if (!r) r = consumeTokenFast(b, AT);
    if (!r) r = consumeTokenFast(b, RIGHT_BRACE);
    if (!r) r = consumeTokenFast(b, RIGHT_BRACKET);
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
