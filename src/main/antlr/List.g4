grammar List;

list : LCBRACE (listElement ( COMMA listElement)* )? RCBRACE;

listElement : list
            | STRING
            | NUMBER
            | bool
            | WORD
            ;


bool    : TRUE|FALSE;

TRUE : 'true';
FALSE : 'false';

LCBRACE : '{';
RCBRACE : '}';
COMMA   : ',';

WHITESPACE  : [ \r\n\t] + -> channel (HIDDEN);

NUMBER  : '-'?[0-9]+
        | '-'?[0-9]+'.'[0-9]+
        | '-'?[0-9]+'.'[0-9]+'e''-'?[0-9]+;

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