package com.vmware.borathon.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import com.vmware.borathon.issue.IssueLog;

public class LogParser {
	
	private Pattern spsRegex = Pattern.compile(".*/sps\\.log.*");	
	private Pattern invservRegex = Pattern.compile(".*/inv-svc\\.log.*");
	
	PhraseParser getParser(String path, CommonTokenStream tokenStream) {
		Matcher spsMatcher = spsRegex.matcher(path);
		if (spsMatcher.matches()) {
			return new PhraseParser(tokenStream);
		}
		Matcher invservMatcher = invservRegex.matcher(path);
		if (invservMatcher.matches()) {
			return new PhraseParser(tokenStream);
		}
		return null;
	}

	public void computePhrases(IssueLog log, OutputStream outputStream) throws IOException {
		ANTLRInputStream inputStream = new ANTLRInputStream(log.getStream());
		PhraseLexer lexer = new PhraseLexer(inputStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		
		PhraseParser parser = getParser(log.getPath(), tokenStream);
		
		if (parser == null)
			return;

		System.out.println("parse " + log.getPath() + "...");

		parser.setBuildParseTree(false);
		parser.addParseListener(new PhraseDocumentListener(outputStream));
		parser.phrases();
	}
}
