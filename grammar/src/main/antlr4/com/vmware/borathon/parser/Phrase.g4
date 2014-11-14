grammar Phrase;

phrases
   :  phrase*
   ;

phrase
   :  javaException
   |  backtrace
   |  unknown
   ;

javaException
	:	( (IDENTIFIER DOT)* EXCEPTION) ( (COLON (~AT)*) | EOL)
      (  WS* AT WS+
         IDENTIFIER ( (DOT | DOLLAR) IDENTIFIER)+
         OPEN_BRAKET (IDENTIFIER JAVA_EXTENSION COLON NUMBER | UNKNOWN_SOURCE) CLOSE_BRAKET
         EOL)+
	;
   
backtrace
   :  ARROW WS+ OPEN_SQ_BRAKE BACKTRACE WS BEGIN CLOSE_SQ_BRAKE (~EOL)* EOL
      (WS* ARROW WS+ BACKTRACE (~EOL)* EOL)*
      WS* ARROW WS+ OPEN_SQ_BRAKE BACKTRACE WS END CLOSE_SQ_BRAKE EOL
   ;

unknown
   :  IDENTIFIER
   |  ARROW
   |  EXCEPTION

   |  (
         JAVA_EXTENSION

      |  AT
      |  BACKTRACE
      |  BEGIN
      |  END
      |  UNKNOWN_SOURCE

      |  OPEN_BRAKET
      |  CLOSE_BRAKET
      |  OPEN_SQ_BRAKE
      |  CLOSE_SQ_BRAKE
      |  COLON
      |  DOT
      |  DOLLAR

      |  NUMBER
      |  WS
      |  EOL)+
   ;

ARROW:               '-->';
JAVA_EXTENSION:      '.java';

AT:                  'at';
BACKTRACE:           'backtrace';
BEGIN:               'begin';
END:                 'end';
UNKNOWN_SOURCE:      'Unknown Source';

OPEN_BRAKET:         '(';
CLOSE_BRAKET:        ')';
OPEN_SQ_BRAKE:       '[';
CLOSE_SQ_BRAKE:      ']';
COLON:               ':';
DOT:                 '.';
DOLLAR:              '$';

EXCEPTION
   :  LETTER (LETTER|ID_DIGIT)*
      ('Exception'|'Error')
   ;

IDENTIFIER
	:	LETTER (LETTER|ID_DIGIT)*
	;

NUMBER
   :  ID_DIGIT+
   ;

WS
	:	' '
	|	'\t'
	|	'\u000C'
	;

EOL
	:	('\n' | '\r\n')
	;

ANY
   :  . -> skip
   ;

fragment
LETTER
	:	'_'
	|	'A'..'Z'
	|	'a'..'z'
	;

fragment
ID_DIGIT
	:	'0'..'9'
   ;