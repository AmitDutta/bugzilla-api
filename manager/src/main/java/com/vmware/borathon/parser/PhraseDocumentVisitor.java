package com.vmware.borathon.parser;

import java.io.IOException;
import java.io.OutputStream;

public class PhraseDocumentVisitor extends PhraseBaseVisitor<Boolean> {
	
	private OutputStream outputStream;

	public PhraseDocumentVisitor(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public static String normalizePhrase(String phrase) {
		return phrase.replaceAll("\n", " & ") + "\n";
	}
	
	private Boolean handlePhrase(String phrase) {		
		phrase = normalizePhrase(phrase);
		try {
			if (phrase.length() > 50)
				System.out.println("   phrase " + phrase.substring(0, 50) + "...");
			else
				System.out.println("   phrase " + phrase);
				
			outputStream.write(phrase.getBytes());
		} catch (IOException e) {
			return false;
		}
		return true;		
	}
	
	@Override 
	public Boolean visitJavaException(PhraseParser.JavaExceptionContext ctx) {
		return handlePhrase(ctx.getText());
	}
	
	@Override 
	public Boolean visitBacktrace(PhraseParser.BacktraceContext ctx) {
		return handlePhrase(ctx.getText());
	}
}
