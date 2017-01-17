grammar List;

list : lbrace (listElement ( COMMA listElement)* )? rbrace;

lbrace  : LBRACE
        | LCBRACE
        ;

rbrace  : RBRACE
        | RCBRACE
        ;

listElement : list
            | NUMBER
            | bool
            | path
            | string
            ;

path    : WORD (DOT WORD)+;

bool    : TRUE|FALSE;

TRUE : 'true';
FALSE : 'false';

LBRACE : '(';
RBRACE : ')';
LCBRACE : '{';
RCBRACE : '}';
COMMA   : ',';
DOT : '.';

WHITESPACE  : [ \r\n\t] + -> channel (HIDDEN);

NUMBER  : '-'?[0-9]+
        | '-'?[0-9]+'.'[0-9]+
        | '-'?[0-9]+'.'[0-9]+'e''-'?[0-9]+;

string  : STRING
        | S_STRING
        | WORD
        ;

S_STRING        : '\'' S_CHAR* '\'';
STRING          : '\"' S_CHAR* '\"';

fragment S_CHAR : ~["\\]
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

WORD : [A-Za-z0-9]+;