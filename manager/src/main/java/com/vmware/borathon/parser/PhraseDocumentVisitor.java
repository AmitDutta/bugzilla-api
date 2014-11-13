package com.vmware.borathon.parser;

import java.io.IOException;
import java.io.OutputStream;

public class PhraseDocumentVisitor extends PhraseBaseVisitor<Boolean> {
	
	private OutputStream outputStream;

	public PhraseDocumentVisitor(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	@Override 
	public Boolean visitJavaException(PhraseParser.JavaExceptionContext ctx) {
		String phrase = ctx.getText();
		try {
			outputStream.write(phrase.getBytes());
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
