package com.vmware.borathon.issue;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.Issue;

public class AttachmentFetcherTest {
   private static String mountDir;
   private static String unzipDir;
   
   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
      mountDir = System.getProperty("java.io.tmpdir") + "bugs/";
      unzipDir = System.getProperty("java.io.tmpdir") + "unzip/";
      String cmd = "mount -t nfs bugs.eng.vmware.com:/bugs " + mountDir;
      File destDir = new File(mountDir);
      if (!destDir.exists()) {
         destDir.mkdir();
      }
      System.out.println("Executing: " + cmd);
      try {
         Process p = Runtime.getRuntime().exec(cmd);
         p.waitFor();
      }catch(Exception ex) {
         ex.printStackTrace();
      }
   }
   
   @Test
   public void testBasicIssueLogIterator() {
      BugFetcher fetcher = new BugFetcher("amitd", "!05432Mn7891");
      Issue issue = fetcher.getBug("1355263"); //1350176
      if (issue != null) {
         AttachmentFetcher aFetcher = new AttachmentFetcher(issue, mountDir + "files/", unzipDir);
         aFetcher.processLogs();
         Iterator<IssueLog> iterator = aFetcher.iterator();
         while (iterator.hasNext()) {
            IssueLog iLog = iterator.next();
            assertNotNull(iLog.getPath());
            System.out.println(iLog.getPath());
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
   
   @Test
   public void testGetAttachmentDir() {
   }
   
   @AfterClass
   public static void tearDownAfterClass() throws Exception {
      try {
         String cmd = "umount " + mountDir;
         System.out.println("Executing: " + cmd);
         Process p = Runtime.getRuntime().exec(cmd);
         p.waitFor();
      }catch(Exception ex) {
         ex.printStackTrace();
      }
   }
}
