package com.vmware.borathon;

import b4j.core.Attachment;
import b4j.core.Issue;

public class TestClient {
   public static void main(String[] args) {
      try {
         BugFetcher fetcher = new BugFetcher(args[0], args[1]);
         Issue issue = fetcher.getBug("1320034");
         if (issue != null) {
            System.out.println(issue.getSummary());
         }
         issue = fetcher.getBug("1350176");
         if (issue != null) {
            System.out.println(issue.getSummary());
            for (Attachment log : issue.getAttachments()) {
               System.out.println(log.getFilename());
            }
         }
         
         System.out.println("Path: " + new AttachmentFetcher(issue).getAttachmentDir());
//         BugzillaHttpSession session = new BugzillaHttpSession();
//         session.setBaseUrl(new URL("https://bugzilla.eng.vmware.com"));
//         session.setBugzillaBugClass(DefaultIssue.class);
//         AuthorizationCallback authCallback = new SimpleAuthorizationCallback(args[0], args[1]);
//         session.getHttpSessionParams().setAuthorizationCallback(authCallback);
//         if (session.open()) {
//            DefaultSearchData searchData = new DefaultSearchData();
//            /* searchData.add("priority", "P0");
//            searchData.add("limit", "5");
//            /*searchData.add("bug_id", "1320034"); */
//            /*searchData.add("category", "VPX");
//            searchData.add("limit", "1-5");*/
//            /*searchData.add("chfieldfrom", "2014-11-01");
//            searchData.add("chfieldto", "2014-11-10"); */
//
//            searchData.add("foundin", new String[] {"vSphere", "2015", "RTM"});
//            searchData.add("limit", "5");
//            Iterable<Issue> issues = session.searchBugs(searchData, null);            
//            for (Issue issue : issues) {
//               System.out.println("Bug found: "+ issue.getId()+" - "+ 
//                     issue.getSummary() + " " + issue.getAssignee() + " " + issue.getUpdateTimestamp());
//            }
//            session.close();
//         }
      }catch (Exception ex) {
         ex.printStackTrace();
      }
   }
}
