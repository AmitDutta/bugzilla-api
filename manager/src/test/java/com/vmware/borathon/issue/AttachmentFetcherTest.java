package com.vmware.borathon.issue;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
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
         String configPath = System.getProperty("user.dir") + "/../../config.txt";
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
   
   @Ignore @Test
   public void testBugDirectoryEmpty() {
      assertFalse(Util.isEmptyDir(786447, mountDir));
      assertTrue(Util.isEmptyDir(290807, mountDir));
      assertFalse(Util.isEmptyDir(717116, mountDir));
      assertTrue(Util.isEmptyDir(1306239, mountDir));
   }
   
   @Ignore @Test
   public void testDirectorySize() {
      String allbugs = System.getProperty("user.dir") + "/data/mahendra-bug-list";
      try {
         Scanner sc = new Scanner(new File (allbugs));
         while (sc.hasNext()) {
            String bugId = sc.next();
            if (!Util.isEmptyDir(Integer.parseInt(bugId), mountDir)) {
               System.out.print(bugId + ",");
               try {
                  File file = new File(Util.getAttachmentDir(Integer.parseInt(bugId), mountDir + "files/"));
                  System.out.println(FileUtils.sizeOfDirectory(file));
               }catch (Exception ex) {
                  System.out.println("failing: " + ex.getMessage());
               }
            }
         }
         sc.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }    
   }
   
   @Test
   public void testBasicIssueLogIterator() {
      BugFetcher fetcher = new BugFetcher(userName, password);
      Map<String, Boolean> map = new HashMap<String, Boolean>();
      Issue issue = fetcher.getBug("1350176"); //1350176 //1355263 //854760 //1016604
      if (issue != null) {
         AttachmentFetcher aFetcher = new AttachmentFetcher(issue, mountDir + "files/", unzipDir);
         aFetcher.processLogs();
         Iterator<IssueLog> iterator = aFetcher.iterator();
         while (iterator.hasNext()) {
            IssueLog iLog = iterator.next();
            assertNotNull(iLog.getPath());
            assertFalse(map.containsKey(iLog.getPath()));
            map.put(iLog.getPath(), true);
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
   
   @Ignore @Test
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
   
   public static String humanReadableByteCount(long bytes, boolean si) {
      int unit = si ? 1000 : 1024;
      if (bytes < unit) return bytes + " B";
      int exp = (int) (Math.log(bytes) / Math.log(unit));
      String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
      return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
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
