grammar Dmp;

program : expr;

mapping : IDENT COLON expr;

dot     : DOT;
number  : NUMBER (DOT NUMBER)?;
bool    : TRUE | FALSE;
string  : STRING;
field   : DOT IDENT;
object  : LBACKET mapping (COMMA mapping)* RBACKET;

expr    : dot | number | bool | string | (field)+ | object;

DOT     : '.';
COLON   : ':';
COMMA   : ',';
LBACKET : '{';
RBACKET : '}';
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