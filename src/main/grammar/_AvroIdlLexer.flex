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
INT_LITERAL=-?(0|[1-9][0-9]*)
FLOAT_LITERAL=-?(NaN|Infinity|(0|[1-9][0-9]*)(\.[0-9]+)?([eE][+-]?[0-9]+)?)
STRING_LITERAL=\"([^\"\\\n\r]|\\([ntbrf\\'\"]|[0-7][0-7]?|[0-3][0-7][0-7]))*\"
DOC_COMMENT="/"\*\*([^*]|\*+[^/*])*\*+"/"
LINE_COMMENT="//".*
BLOCK_COMMENT="/"\*([^*]|\*+[^/*])*\*+"/"
IDENTIFIER=(`[:jletter:][:jletterdigit:]*`)|([:jletter:][:jletterdigit:]*)([.-][:jletter:][:jletterdigit:]*)*

%%
<YYINITIAL> {
  {WHITE_SPACE}             { return WHITE_SPACE; }

  "{"                       { return LEFT_BRACE; }
  "}"                       { return RIGHT_BRACE; }
  "("                       { return LEFT_PAREN; }
  ")"                       { return RIGHT_PAREN; }
  "["                       { return LEFT_BRACKET; }
  "]"                       { return RIGHT_BRACKET; }
  "<"                       { return LEFT_ANGLE; }
  ">"                       { return RIGHT_ANGLE; }
  "@"                       { return AT; }
  ","                       { return COMMA; }
  ";"                       { return SEMICOLON; }
  ":"                       { return COLON; }
  "="                       { return EQUALS; }
  "array"                   { return ARRAY; }
  "boolean"                 { return BOOLEAN; }
  "double"                  { return DOUBLE; }
  "enum"                    { return ENUM; }
  "error"                   { return ERROR; }
  "false"                   { return FALSE; }
  "fixed"                   { return FIXED; }
  "float"                   { return FLOAT; }
  "idl"                     { return IDL; }
  "import"                  { return IMPORT; }
  "int"                     { return INT; }
  "long"                    { return LONG; }
  "map"                     { return MAP; }
  "oneway"                  { return ONEWAY; }
  "bytes"                   { return BYTES; }
  "schema"                  { return SCHEMA; }
  "string"                  { return STRING; }
  "null"                    { return NULL; }
  "protocol"                { return PROTOCOL; }
  "record"                  { return RECORD; }
  "throws"                  { return THROWS; }
  "true"                    { return TRUE; }
  "union"                   { return UNION; }
  "void"                    { return VOID; }
  "date"                    { return DATE; }
  "time_ms"                 { return TIME_MS; }
  "timestamp_ms"            { return TIMESTAMP_MS; }
  "local_timestamp_ms"      { return LOCAL_TIMESTAMP_MS; }
  "decimal"                 { return DECIMAL; }
  "uuid"                    { return UUID; }

  {NOT_USED}                { return NOT_USED; }
  {INT_LITERAL}             { return INT_LITERAL; }
  {FLOAT_LITERAL}           { return FLOAT_LITERAL; }
  {STRING_LITERAL}          { return STRING_LITERAL; }
  {DOC_COMMENT}             { return DOC_COMMENT; }
  {LINE_COMMENT}            { return LINE_COMMENT; }
  {BLOCK_COMMENT}           { return BLOCK_COMMENT; }
  {IDENTIFIER}              { return IDENTIFIER; }

}

[^] { return BAD_CHARACTER; }
