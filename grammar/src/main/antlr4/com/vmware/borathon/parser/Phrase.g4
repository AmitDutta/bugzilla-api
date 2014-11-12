grammar Phrase;

phrases
   : phrase*
   ;

phrase
   : javaException
   ;

javaException
	:	EXCEPTION
	;


EXCEPTION:				'Exception';
