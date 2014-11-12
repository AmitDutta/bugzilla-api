package com.vmware.borathon.parser;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.vmware.borathon.IssueLog;

public class LogParserTest {
	
	String parse(String sample) throws IOException {
		LogParser parser = new LogParser();
		
		InputStream input = new ByteArrayInputStream(sample.getBytes(StandardCharsets.UTF_8));

		IssueLog log = new IssueLog("sps", input);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		parser.ComputePhrases(log, output);
		
		return new String( output.toByteArray(), StandardCharsets.UTF_8);			
	}

	@Test
	public void test() throws IOException {
		
		String sample = "";
		String result = parse(sample);
		
		assertEquals("", result);
	}
}
