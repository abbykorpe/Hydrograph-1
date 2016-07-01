grammar QueryParser;


eval : leftBrace? expression (andOr leftBrace* expression rightBrace*)* rightBrace?
   | eval ';';


expression : fieldname (condition javaiden)? (specialexpr)?;

leftBrace: '(';
rightBrace: ')';

specialexpr :  'in' leftBrace javaiden rightBrace
                          |'not in' javaiden rightBrace
                          |'between'  javaiden andOr javaiden
                           | 'like' leftBrace? javaiden rightBrace?
                           | 'not like' leftBrace? javaiden rightBrace?
                           |'IN' leftBrace javaiden rightBrace
                          |'NOT IN'leftBrace javaiden rightBrace
                          |'BETWEEN'  javaiden andOr javaiden
                           | 'LIKE' javaiden
                           | 'NOT LIKE' javaiden;

andOr : 'or' | 'and';

condition : '='
           | '<'
            |'<='
            |'>'
            |'>='
            |'<>'
           ;


javaiden : Identifier+ ;

fieldname:  Identifier+;
Identifier
         :   JavaLetterOrDigit+
         ;

     fragment
     JavaLetterOrDigit
         :   [a-zA-Z0-9-.$_,%'];

WS  :  [\t\r\n\u000C]+ -> skip;