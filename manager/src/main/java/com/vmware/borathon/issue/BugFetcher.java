package com.vmware.borathon.issue;

import java.net.URL;

import rs.baselib.security.AuthorizationCallback;
import rs.baselib.security.SimpleAuthorizationCallback;
import b4j.core.DefaultIssue;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.session.BugzillaHttpSession;

public class BugFetcher {
   private AuthorizationCallback authCallback;
   public BugFetcher(String username, String password) {
      try {         
         authCallback = new SimpleAuthorizationCallback(username, password);         
      } catch (Exception ex) {
         System.out.println("Error in initializing BugFetcher");
         ex.printStackTrace();
      }      
   }
   
   public Issue getBug(String bugId) {
      Issue issue = null;
      try {
         BugzillaHttpSession session = new BugzillaHttpSession();
         session.setBaseUrl(new URL("https://bugzilla.eng.vmware.com"));
         session.setBugzillaBugClass(DefaultIssue.class);
         session.getHttpSessionParams().setAuthorizationCallback(authCallback);
         if (session.open()) {
            DefaultSearchData searchData = new DefaultSearchData();
            searchData.add("bug_id", bugId);
            Iterable<Issue> issues = session.searchBugs(searchData, null);
            for (Issue item : issues) issue = item;
            session.close();
         }
      } catch (Exception ex) {
         ex.printStackTrace();
      }      
      return issue;
   }
}
