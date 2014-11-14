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
		
		IssueLog log = new IssueLog("/sps.log", input);
		
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
		
		String expected = PhraseDocumentListener.normalizePhrase(nullException);
		assertEquals(expected, result);
	}

	@Test
	public void javaExceptionNoise() throws IOException {
		String sample = "noice " + nullException + "noise\nmore noice\n " + nullException;

		String result = parse(sample);
		
		String expected = PhraseDocumentListener.normalizePhrase(nullException) +
				          PhraseDocumentListener.normalizePhrase(nullException);
		
		assertEquals(expected, result);
	}
	
	private String backtraceInvalidArgument = 
"	--> [backtrace begin] product: VMware VirtualCenter, version: 6.0.0, build: build-2029402, tag: vpxd\n" +
"	--> backtrace[00] libvmacore.so[0x003BE174]: Vmacore::System::Stacktrace::CaptureWork(unsigned int)\n" +
"	--> backtrace[01] libvmacore.so[0x001EC773]: Vmacore::System::SystemFactoryImpl::CreateQuickBacktrace(Vmacore::Ref<Vmacore::System::Backtrace>&)\n" +
"	--> backtrace[02] libvmacore.so[0x0019760D]: Vmacore::Throwable::Throwable(std::string const&)\n" +
"	--> backtrace[03] libvmomi.so[0x0027C9D9]\n" +
"	--> backtrace[04] libvmomi.so[0x00273BA1]: Vmomi::Fault::InvalidArgument::Throw()\n" +
"	--> backtrace[05] vpxd[0x00BA462F]\n" +
"	--> backtrace[06] vpxd[0x00BA51AA]\n" +
"	--> backtrace[07] vpxd[0x01C6F02D]\n" +
"	--> backtrace[08] vpxd[0x01C4B5E1]\n" +
"	--> backtrace[09] vpxd[0x01C31C04]\n" +
"	--> backtrace[10] vpxd[0x0175E977]\n" +
"	--> backtrace[11] vpxd[0x0175F07E]\n" +
"	--> backtrace[12] vpxd[0x0175F1CA]\n" +
"	--> backtrace[13] libvmodlTypes.so[0x01065C35]\n" +
"	--> backtrace[14] vpxd[0x00BBBD49]\n" +
"	--> backtrace[15] vpxd[0x00B976AB]\n" +
"	--> backtrace[16] vpxd[0x00B8EBEB]\n" +
"	--> backtrace[17] vpxd[0x00B9458C]\n" +
"	--> backtrace[18] vpxd[0x00B8EB15]\n" +
"	--> backtrace[19] vpxd[0x00BA1C3B]\n" +
"	--> backtrace[20] libvmacore.so[0x002F41D2]\n" +
"	--> backtrace[21] libvmacore.so[0x002F7C32]\n" +
"	--> backtrace[22] libvmacore.so[0x002F7E50]\n" +
"	--> backtrace[23] libvmacore.so[0x002FF23E]\n" +
"	--> backtrace[24] libvmacore.so[0x002F396B]\n" +
"	--> backtrace[25] libvmacore.so[0x003C8032]\n" +
"	--> backtrace[26] libpthread.so.0[0x00007806]\n" +
"	--> backtrace[27] libc.so.6[0x000DBB1D]\n" +
"	--> [backtrace end]\n";

	@Test
	public void backtrace() throws IOException {
		String result = parse(backtraceInvalidArgument);
		
		String expected = PhraseDocumentListener.normalizePhrase(backtraceInvalidArgument);
		assertEquals(expected, result);
	}

	@Test
	public void backtraceNoise() throws IOException {
		String sample = "noice" + backtraceInvalidArgument + "noise\nmore noice\n" + backtraceInvalidArgument;

		String result = parse(sample);
		
		String expected = PhraseDocumentListener.normalizePhrase(backtraceInvalidArgument) +
				          PhraseDocumentListener.normalizePhrase(backtraceInvalidArgument);
		
		assertEquals(expected, result);
	}

/*
	@Test
	public void spsLog() throws IOException {
		FileInputStream stream = new FileInputStream("/var/folders/pr/vdtn7bd905n5n62bhxql87t0002lsj/T/unzip/854760/Mar-21-2012/vcsupport-3-16-2012-16-48/Profile-Driven Storage/logs/sps.log.1");
		
		String result = parse(stream);
		
		int count = StringUtils.countMatches(result, "\n");
		
		FileOutputStream output = new FileOutputStream("/var/folders/pr/vdtn7bd905n5n62bhxql87t0002lsj/T/unzip/854760/Mar-21-2012/vcsupport-3-16-2012-16-48/Profile-Driven Storage/logs/sps.log.1.txt");
		output.write(result.getBytes());
		output.close();

		assertEquals(39, count);		
	}
	
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
	
	/var/folders/pr/vdtn7bd905n5n62bhxql87t0002lsj/T/unzip/854760/Mar-21-2012/vcsupport-3-16-2012-16-48/Profile-Driven Storage/logs/sps.log.1

	@Test
	public void vpxdLog() throws IOException {
		FileInputStream stream = new FileInputStream("/Users/ggeorgiev/Downloads/vc-pa-rdinfra3-vm3-dhcp1529-2014-08-14--01.43/var/log/vmware/vpx/vpxd-6.log");
		
		String result = parse(stream);
		
		int count = StringUtils.countMatches(result, "\n");
		
		FileOutputStream output = new FileOutputStream("/Users/ggeorgiev/Downloads/vc-pa-rdinfra3-vm3-dhcp1529-2014-08-14--01.43/var/log/vmware/vpx/vpxd-6.log.txt");
		output.write(result.getBytes());
		output.close();

		assertEquals(11, count);		
	}
*/
}
 