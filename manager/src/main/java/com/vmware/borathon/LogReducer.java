package com.vmware.borathon;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import com.vmware.borathon.issue.AttachmentFetcher;
import com.vmware.borathon.issue.IssueLog;
import com.vmware.borathon.parser.LogParser;
import com.vmware.borathon.parser.PhraseDocumentListener;

public class LogReducer {
	
	private AttachmentFetcher fetcher;
	private OutputStream outputStream;
	
	public LogReducer(AttachmentFetcher fetcher, OutputStream outputStream) {
		this.fetcher = fetcher;
		this.outputStream = outputStream;
		try {
		   if (fetcher.getIssue() != null) {
		      this.outputStream.write(PhraseDocumentListener.normalizePhrase(fetcher.getIssue().getSummary()).getBytes());
		   }
      } catch (IOException ex) {
         System.out.println(ex.getMessage());
      }
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
