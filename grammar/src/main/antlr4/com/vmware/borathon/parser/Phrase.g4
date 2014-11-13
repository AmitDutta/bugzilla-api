grammar Phrase;

phrases
   :  phrase*
   ;

phrase
   :  javaException
   |  unknown
   ;

javaException
	:	javaExceptionName (javaExceptionMessage|EOL)
      javaExceptionLocation
	;

javaExceptionName
   :  (IDENTIFIER DOT)*
      EXCEPTION
   ;

javaExceptionMessage
   :  COLON (~AT)*
   ;

javaExceptionLocation
   :  javaLocation+
   ;

javaLocation
   :  WS* AT WS+
      IDENTIFIER (javaLocationDelimiter IDENTIFIER)+
      OPEN_BRAKET javaFile CLOSE_BRAKET
      EOL
   ;

javaFile
   :  IDENTIFIER JAVA_EXTENSION COLON NUMBER
   |  UNKNOWN_SOURCE
   ;

javaLocationDelimiter
   :  DOT
   |  DOLLAR
   ;

unknown
   :  AT
   |  JAVA_EXTENSION
   |  UNKNOWN_SOURCE
   |  OPEN_BRAKET
   |  CLOSE_BRAKET
   |  COLON
   |  DOT
   |  DOLLAR
   |  EXCEPTION
   |  IDENTIFIER
   |  NUMBER
   |  WS
   |  EOL
   |  ANY
   ;

AT:                  'at';
JAVA_EXTENSION:      '.java';
UNKNOWN_SOURCE:      'Unknown Source';

OPEN_BRAKET:         '(';
CLOSE_BRAKET:        ')';
COLON:               ':';
DOT:                 '.';
DOLLAR:              '$';

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

ANY
   :  .
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