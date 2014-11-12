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
	public void sanity() throws IOException {
		
		String sample = "";
		String result = parse(sample);
		
		assertEquals("", result);
	}

	@Test
	public void javaException() throws IOException {
		
		String sample = 
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
		
		String result = parse(sample);
		
		assertEquals(sample, result);
	}
}
