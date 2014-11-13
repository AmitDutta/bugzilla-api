grammar Phrase;

phrases
   :  phrase*
      UNKNUWN*
   ;

phrase
   :  javaException
   ;

javaException
	:	javaExceptionName EOL
      javaExceptionLocation
	;

javaExceptionName
   :  (IDENTIFIER DOT)*
      EXCEPTION
   ;

javaExceptionLocation
   :  javaLocation*
   ;

javaLocation
   :  WS* AT WS+
      IDENTIFIER (javaLocationDelimiter IDENTIFIER)+
      OPEN_BRAKET javaFile CLOSE_BRAKET
      EOL
   ;

javaFile
   :  IDENTIFIER '.java' COLON NUMBER
   |  UNKNOWN_SOURCE
   ;

javaLocationDelimiter
   :  DOT
   |  DOLLAR
   ;

OPEN_BRAKET:         '(';
CLOSE_BRAKET:        ')';
COLON:               ':';
DOT:                 '.';
DOLLAR:              '$';

AT:                  'at';
UNKNOWN_SOURCE:      'Unknown Source';

EXCEPTION
   :  LETTER (LETTER|ID_DIGIT)*
      'Exception'
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

UNKNOWN
	:	. -> skip
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