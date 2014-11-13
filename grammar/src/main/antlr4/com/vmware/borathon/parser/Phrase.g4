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

backtrace
   :  WS* ARROW WS+ OPEN_SQ_BRAKE BACKTRACE WS BEGIN CLOSE_SQ_BRAKE (~EOL)* EOL
      (WS* ARROW WS+ BACKTRACE (~EOL)* EOL)*
      WS* ARROW WS+ OPEN_SQ_BRAKE BACKTRACE WS END CLOSE_SQ_BRAKE EOL
   ;

unknown
   :  ARROW
   |  JAVA_EXTENSION

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

   |  EXCEPTION
   |  IDENTIFIER
   |  NUMBER
   |  WS
   |  EOL
   |  ANY
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