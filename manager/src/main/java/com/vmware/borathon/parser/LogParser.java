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
	private Pattern vpxdRegex = Pattern.compile(".*/vpxd(-\\d+)?\\.log");
	
	PhraseParser getParser(IssueLog log) throws IOException {
		String path = log.getPath();
		
		Matcher spsMatcher = spsRegex.matcher(path);
		Matcher invservMatcher = invservRegex.matcher(path);
		Matcher vpxdMatcher = vpxdRegex.matcher(path);

		if (   spsMatcher.matches()
			|| invservMatcher.matches()
			|| vpxdMatcher.matches()) {
			
			ANTLRInputStream inputStream = new ANTLRInputStream(log.getStream());
			PhraseLexer lexer = new PhraseLexer(inputStream);
			CommonTokenStream tokenStream = new CommonTokenStream(lexer);

			return new PhraseParser(tokenStream);
		}
		
		return null;
	}

	public void computePhrases(IssueLog log, OutputStream outputStream) throws IOException {
		try {
			PhraseParser parser = getParser(log);
			if (parser == null)
				return;
	
			System.out.println("parse " + log.getPath() + "...");
	
			parser.setBuildParseTree(false);
			parser.addParseListener(new PhraseDocumentListener(outputStream));
			parser.phrases();
		}
		catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}
}
