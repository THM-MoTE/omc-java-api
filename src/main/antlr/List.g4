grammar List;

list : LBRACE (listElement ( COMMA listElement)* )? RBRACE
     | LCBRACE (listElement ( COMMA listElement)* )? RCBRACE
     ;

listElement : list
            | number
            | bool
            | path
            | string
            ;

path    : WORD (DOT WORD)*;

number  : NUMBER;

bool    : TRUE|FALSE;

string  : DQ_STRING
        | SQ_STRING
        ;

TRUE : 'true';
FALSE : 'false';

LBRACE : '(';
RBRACE : ')';
LCBRACE : '{';
RCBRACE : '}';
COMMA   : ',';
DOT : '.';

WORD : [A-Za-z_][0-9A-Za-z_]*;

NUMBER  : '-'?[0-9]+
        | '-'?[0-9]+'.'[0-9]+
        | '-'?[0-9]+'.'[0-9]+'e''-'?[0-9]+;

DQ_STRING          : '\"' DQ_CHAR* '\"';
SQ_STRING        : '\'' SQ_CHAR* '\'';

WHITESPACE  : [ \r\n\t] + -> channel (HIDDEN);

fragment DQ_CHAR : ~["\\]
                | ESCAPESEQUENCE
                ;

fragment SQ_CHAR : ~['\\]
                | ESCAPESEQUENCE
                ;

fragment ESCAPESEQUENCE :   '\\' [btnfr"'\\]
                        |   OCTALESCAPE
                        |   UNICODEESCAPE
                        ;

fragment OCTALESCAPE    :   '\\' OCTALDIGIT
                        |   '\\' OCTALDIGIT OCTALDIGIT
                        |   '\\' ZEROTOTHREE OCTALDIGIT OCTALDIGIT
                        ;

fragment UNICODEESCAPE  :   '\\' 'u' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT;

fragment ZEROTOTHREE    : [0-3];
fragment OCTALDIGIT     : [0-7];
fragment HEXDIGIT       : [0-9a-fA-F];