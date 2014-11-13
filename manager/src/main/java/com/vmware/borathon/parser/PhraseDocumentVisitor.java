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
	
	@Override 
	public Boolean visitJavaException(PhraseParser.JavaExceptionContext ctx) {
		String phrase = ctx.getText();
		
		phrase = normalizePhrase(phrase);
		try {
			System.out.println("   phrase " + phrase.substring(50));
			outputStream.write(phrase.getBytes());
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
