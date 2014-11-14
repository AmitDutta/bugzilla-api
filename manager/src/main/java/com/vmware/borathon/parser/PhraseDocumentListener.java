package com.vmware.borathon.parser;

import java.io.IOException;
import java.io.OutputStream;

public class PhraseDocumentListener extends PhraseBaseListener {
	
	private OutputStream outputStream;

	public PhraseDocumentListener(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public static String normalizePhrase(String phrase) {
		return phrase.replaceAll("\n", " & ") + "\n";
	}
	
	private void handlePhrase(String phrase) {		
		phrase = normalizePhrase(phrase);
		try {
			if (phrase.length() > 50)
				System.out.println("   phrase " + phrase.substring(0, 50) + "...");
			else
				System.out.println("   phrase " + phrase);
				
			outputStream.write(phrase.getBytes());
		} catch (IOException e) {
		}
	}
	
	@Override 
	public void exitJavaException(PhraseParser.JavaExceptionContext ctx) {
		handlePhrase(ctx.getText());
	}
	
	@Override 
	public void exitBacktrace(PhraseParser.BacktraceContext ctx) {
		handlePhrase(ctx.getText());
	}
}
