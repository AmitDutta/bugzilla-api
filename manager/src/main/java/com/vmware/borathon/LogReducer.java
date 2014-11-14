package com.vmware.borathon;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import com.vmware.borathon.issue.AttachmentFetcher;
import com.vmware.borathon.issue.IssueLog;
import com.vmware.borathon.parser.LogParser;

public class LogReducer {
	
	private AttachmentFetcher fetcher;
	private OutputStream outputStream;
	
	public LogReducer(AttachmentFetcher fetcher, OutputStream outputStream) {
		this.fetcher = fetcher;
		this.outputStream = outputStream;
	}

	public void reduce() throws IOException {
		fetcher.processLogs();
        Iterator<IssueLog> iterator = fetcher.iterator();
        
        while (iterator.hasNext()) {
           IssueLog iLog = iterator.next();
           
           LogParser parser = new LogParser();
           parser.computePhrases(iLog, outputStream);
           iLog.getStream().close();
        }
	}
}
