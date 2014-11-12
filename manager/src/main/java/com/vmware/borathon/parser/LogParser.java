package com.vmware.borathon.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vmware.borathon.IssueLog;

public class LogParser {
	
	private Pattern javaException = Pattern.compile("");
	
	private Pattern spsRegex = Pattern.compile("sps");
	private ArrayList<Pattern> spsList = new ArrayList<Pattern>() {
		private static final long serialVersionUID = 1L;
	{
	    add(javaException);
	}};
	
	private Pattern invservRegex = Pattern.compile("invserv");
	private ArrayList<Pattern> invservList = new ArrayList<Pattern>() {
		private static final long serialVersionUID = 2L;
	{
	    add(javaException);
	}};
	
	class PatternPair {
		public PatternPair(Pattern pathPattern, ArrayList<Pattern> patterns) {
			this.pathPattern = pathPattern;
			this.patterns = patterns;
		}
		
		public Pattern GetPathPattern() {
			return pathPattern;
		}
		
		public ArrayList<Pattern> GetPatterns() {
			return patterns;
		}
		
		private Pattern pathPattern;
		private ArrayList<Pattern> patterns;
	};
	
	private ArrayList<PatternPair> pairs = new ArrayList<PatternPair>() {
		private static final long serialVersionUID = 300L;
	{
		add(new PatternPair(spsRegex, spsList));
		add(new PatternPair(invservRegex, invservList));
	}};
	
	Iterator<Pattern> GetPatterns(String path) {
		for (PatternPair pair : pairs) {
			Pattern pathPattern = pair.GetPathPattern();
			
			Matcher pathMatcher = pathPattern.matcher(path);
			if (!pathMatcher.matches())
				continue;

			for (Pattern pattern : pair.GetPatterns()) {
				Matcher macher = pathPattern.matcher(path);
				
			}
		}
		return null;
	}

	Iterator<Phrase> ComputePhrases(IssueLog log) {
		ArrayList<Phrase> phrases = new ArrayList<Phrase>();
		
		Iterator<Pattern> patterns = GetPatterns(log.GetPath());
		
		if (patterns == null)
			return phrases.iterator();
		
		
		
		
		return phrases.iterator();
	}
}
