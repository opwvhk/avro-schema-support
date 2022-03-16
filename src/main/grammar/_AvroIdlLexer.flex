package opwvhk.intellij.avro_idl.syntax;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

%%

%{
  public _AvroIdlLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _AvroIdlLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

NOT_USED=[ \t\n\x0B\f\r]+
INT_LITERAL=[+-]?(0|[1-9][0-9]*|0x[0-9a-fA-F]+|0[0-7]+)[lL]?
FLOAT_LITERAL=[+-]?(NaN|Infinity|([0-9]+\.[0-9]*|\.[0-9]+)([eE][+-]?[0-9]+)?[fFdD]?|[0-9]+([eE][+-]?[0-9]+[fFdD]?|[fFdD])|0[xX]([0-9a-fA-F]+\.?|[0-9a-fA-F]*\.[0-9a-fA-F]+)[pP][+-]?[0-9]+?[fFdD]?)
STRING_LITERAL=\"([^\"\\\n\r]|\\([ntbrf\\'\"]|[0-7][0-7]?|[0-3][0-7][0-7]))*\"
DOC_COMMENT="/"\*\*([^*]|\*+[^/*])*\*"/"
INCOMPLETE_DOC_COMMENT="/"\*\*([^*]|\*+[^/*])*
LINE_COMMENT="//".*
BLOCK_COMMENT="/"\*[^*]([^*]|\*+[^/*])*\*"/"
INCOMPLETE_BLOCK_COMMENT="/"\*[^*]([^*]|\*+[^/*])*
IDENTIFIER_TOKEN=(\`[:jletter:][:jletterdigit:]*\`|[:jletter:][:jletterdigit:]*)([.-](\`[:jletter:][:jletterdigit:]*\`|[:jletter:][:jletterdigit:]*))*

%%
<YYINITIAL> {
  {WHITE_SPACE}                   { return WHITE_SPACE; }

  "/*"                            { return BLOCK_COMMENT_START; }
  "{"                             { return LEFT_BRACE; }
  "}"                             { return RIGHT_BRACE; }
  "("                             { return LEFT_PAREN; }
  ")"                             { return RIGHT_PAREN; }
  "["                             { return LEFT_BRACKET; }
  "]"                             { return RIGHT_BRACKET; }
  "<"                             { return LEFT_ANGLE; }
  ">"                             { return RIGHT_ANGLE; }
  "@"                             { return AT; }
  ","                             { return COMMA; }
  ";"                             { return SEMICOLON; }
  ":"                             { return COLON; }
  "="                             { return EQUALS; }
  "?"                             { return QUESTION_MARK; }
  "array"                         { return ARRAY; }
  "boolean"                       { return BOOLEAN; }
  "bytes"                         { return BYTES; }
  "date"                          { return DATE; }
  "decimal"                       { return DECIMAL; }
  "double"                        { return DOUBLE; }
  "enum"                          { return ENUM; }
  "error"                         { return ERROR; }
  "false"                         { return FALSE; }
  "fixed"                         { return FIXED; }
  "float"                         { return FLOAT; }
  "idl"                           { return IDL; }
  "import"                        { return IMPORT; }
  "int"                           { return INT; }
  "local_timestamp_ms"            { return LOCAL_TIMESTAMP_MS; }
  "long"                          { return LONG; }
  "map"                           { return MAP; }
  "namespace"                     { return NAMESPACE; }
  "null"                          { return NULL; }
  "oneway"                        { return ONEWAY; }
  "protocol"                      { return PROTOCOL; }
  "record"                        { return RECORD; }
  "schema"                        { return SCHEMA; }
  "string"                        { return STRING; }
  "throws"                        { return THROWS; }
  "timestamp_ms"                  { return TIMESTAMP_MS; }
  "time_ms"                       { return TIME_MS; }
  "true"                          { return TRUE; }
  "union"                         { return UNION; }
  "uuid"                          { return UUID; }
  "void"                          { return VOID; }
  "@namespace"                    { return AT_NAMESPACE; }

  {NOT_USED}                      { return NOT_USED; }
  {INT_LITERAL}                   { return INT_LITERAL; }
  {FLOAT_LITERAL}                 { return FLOAT_LITERAL; }
  {STRING_LITERAL}                { return STRING_LITERAL; }
  {DOC_COMMENT}                   { return DOC_COMMENT; }
  {INCOMPLETE_DOC_COMMENT}        { return INCOMPLETE_DOC_COMMENT; }
  {LINE_COMMENT}                  { return LINE_COMMENT; }
  {BLOCK_COMMENT}                 { return BLOCK_COMMENT; }
  {INCOMPLETE_BLOCK_COMMENT}      { return INCOMPLETE_BLOCK_COMMENT; }
  {IDENTIFIER_TOKEN}              { return IDENTIFIER_TOKEN; }

}

[^] { return BAD_CHARACTER; }
