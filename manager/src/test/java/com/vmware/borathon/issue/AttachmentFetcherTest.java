package com.vmware.borathon.issue;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.vmware.borathon.Util;

import b4j.core.Issue;

public class AttachmentFetcherTest {
   private static String mountDir;
   private static String unzipDir;
   private static String userName;
   private static String password;
   
   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
      try {
         String configPath = System.getProperty("user.dir") + "/data/config.txt";
         File file = new File(configPath);
         Scanner scanner = new Scanner(file);
         userName = scanner.next();
         password = scanner.next();
         scanner.close();
      }catch (Exception ex) {
         ex.printStackTrace();
      }
      
      mountDir = System.getProperty("java.io.tmpdir") + "bugs/";
      unzipDir = System.getProperty("java.io.tmpdir") + "unzip/";
      String cmd = "mount -t nfs bugs.eng.vmware.com:/bugs " + mountDir;
      File destDir = new File(mountDir);
      if (!destDir.exists()) {
         destDir.mkdirs();
      }
      System.out.println("Executing: " + cmd);
      try {
         Process p = Runtime.getRuntime().exec(cmd);
         p.waitFor();
      }catch(Exception ex) {
         ex.printStackTrace();
      }
   }
   
   //@Test
   public void testBugDirectoryEmpty() {
      assertFalse(Util.isEmptyDir(786447, mountDir));
      assertTrue(Util.isEmptyDir(290807, mountDir));
      assertFalse(Util.isEmptyDir(717116, mountDir));
      assertTrue(Util.isEmptyDir(1306239, mountDir));
   }
   
   @Test
   public void testBasicIssueLogIterator() {
      BugFetcher fetcher = new BugFetcher(userName, password);
      Issue issue = fetcher.getBug("854760"); //1350176 //1355263
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
               e.printStackTrace();
            }
         }
         aFetcher.clean();
      }
   }
   
   @Test
   public void IssueIteratorWithZipFile() {
      BugFetcher fetcher = new BugFetcher(userName, password);
      Issue issue = fetcher.getBug("786447"); //1350176
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
               e.printStackTrace();
            }
         }
         aFetcher.clean();
      }
   }
   
   //@Test
   public void FindLogWithAttachment() {
      String allbugs = System.getProperty("user.dir") + "/data/All-bugs.txt";
      try {
         Scanner sc = new Scanner(new File (allbugs));
         while (sc.hasNext()) {
            String bugId = sc.next();
            String mountPath = mountDir + "files/";
            if (!Util.isEmptyDir(Integer.parseInt(bugId), mountDir)) {
               System.out.println(bugId);
            }
         }
         sc.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
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
