package com.vmware.borathon.parser;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.vmware.borathon.issue.IssueLog;

public class LogParserTest {
	
	String parse(InputStream input) throws IOException {
		LogParser parser = new LogParser();
		
		IssueLog log = new IssueLog("sps", input);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		parser.computePhrases(log, output);
		
		return new String( output.toByteArray(), StandardCharsets.UTF_8);			
	}

	String parse(String sample) throws IOException {
		InputStream input = new ByteArrayInputStream(sample.getBytes(StandardCharsets.UTF_8));
		return parse(input);
	}

	@Test
	public void sanity() throws IOException {
		
		String sample = "";
		String result = parse(sample);
		
		assertEquals("", result);
	}
	
	private String nullException = 
"java.lang.NullPointerException\n" +
"	at com.vmware.vim.sms.policy.PolicyAdapterImpl.copyVasaComplianceResult(PolicyAdapterImpl.java:69)\n" +
"	at com.vmware.vim.sms.policy.PolicyManagerImpl.queryComplianceResult(PolicyManagerImpl.java:150)\n" +
"	at com.vmware.sps.pbm.impl.LocalSMSServiceImpl.queryComplianceResult(Unknown Source)\n" +
"	at com.vmware.sps.pbm.compliance.ObjectStorageComplianceTask.run(Unknown Source)\n" +
"	at java.util.concurrent.Executors$RunnableAdapter.call(Unknown Source)\n" +
"	at java.util.concurrent.FutureTask.run(Unknown Source)\n" +
"	at java.util.concurrent.ThreadPoolExecutor.runWorker(Unknown Source)\n" +
"	at java.util.concurrent.ThreadPoolExecutor$Worker.run(Unknown Source)\n" +
"	at java.lang.Thread.run(Unknown Source)\n";

	@Test
	public void javaException() throws IOException {

		
		String result = parse(nullException);
		
		String expected = PhraseDocumentVisitor.normalizePhrase(nullException);
		assertEquals(expected, result);
	}

	@Test
	public void javaExceptionNoise() throws IOException {
		String sample = "noice " + nullException + "noise\nmore noice\n " + nullException;

		String result = parse(sample);
		
		String expected = PhraseDocumentVisitor.normalizePhrase(nullException) +
					      PhraseDocumentVisitor.normalizePhrase(nullException);
		
		assertEquals(expected, result);
	}

	/*
	@Test
	public void spsLog() throws IOException {
		FileInputStream stream = new FileInputStream("/Users/ggeorgiev/Downloads/vc-sof2-lab8-dhcp516-2014-11-07--14.59/var/log/vmware/vmware-sps/sps.log.1");
		
		String result = parse(stream);
		
		int count = StringUtils.countMatches(result, "\n");
		
		FileOutputStream output = new FileOutputStream("/Users/ggeorgiev/Downloads/vc-sof2-lab8-dhcp516-2014-11-07--14.59/var/log/vmware/vmware-sps/sps.log.1.txt");
		output.write(result.getBytes());
		output.close();

		assertEquals(39, count);		
	}
	*/
	
	
}
