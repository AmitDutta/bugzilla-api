package com.vmware.borathon;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import b4j.core.Issue;

public class AttachmentFetcherTest {
   
   @Test
   public void testBasicIssueLogIterator() {
      BugFetcher fetcher = new BugFetcher("amitd", "!05432Mn7891");
      Issue issue = fetcher.getBug("1350176");
      if (issue != null) {
         AttachmentFetcher aFetcher = new AttachmentFetcher(issue);
         Iterator<IssueLog> iterator = aFetcher.iterator();
         while (iterator.hasNext()) {
            IssueLog iLog = iterator.next();
            assertNotNull(iLog.getPath());
            assertNotNull(iLog.getStream());
            try {
               iLog.getStream().close();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
   }
}
