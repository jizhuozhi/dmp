grammar Dmp;

program : expr EOF;

mapping : IDENT COLON expr;

dot     : DOT;
number  : NUMBER (DOT NUMBER)?;
bool    : TRUE | FALSE;
string  : STRING;
field   : DOT IDENT QUEST?;
object  : LBACKET mapping (COMMA mapping)* RBACKET;

expr
    : dot
    | number
    | bool
    | string
    | object
    | projection;

projection          : (IDENT | IDENT (field)+ | (field)+) (objectProjection | arrayProjection)?;
arrow               : IDENT ARROW expr;
objectProjection    : LPAREN arrow RPAREN;
arrayProjection     : LBRACE arrow RBRACE;

DOT     : '.';
COLON   : ':';
COMMA   : ',';
QUEST   : '?';
LPAREN  : '(';
RPAREN  : ')';
LBRACE  : '[';
RBRACE  : ']';
LBACKET : '{';
RBACKET : '}';
ARROW   : '->';
QUOTE   : '\'';
DOUBLE_QUOTE : '"';

TRUE    : 'true';
FALSE   : 'false';
IDENT   : ALPHA (ALNUM)*;
NUMBER  : NUM+;

STRING
    : QUOTE (ESC_SINGLE|.)*? QUOTE
    | DOUBLE_QUOTE (ESC_DOUBLE|.)*? DOUBLE_QUOTE;

fragment ESC_SINGLE : '\\' QUOTE;
fragment ESC_DOUBLE : '\\' DOUBLE_QUOTE;

fragment ALNUM  : ALPHA | NUM;
fragment ALPHA  : [a-zA-Z_];
fragment NUM    : [0-9];

WHITESPACE: [ \t\r\n] -> skip;