package com.vmware.borathon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.Issue;

import com.vmware.borathon.issue.AttachmentFetcher;
import com.vmware.borathon.issue.BugFetcher;

public class LogReducerTest {
   private static String mountDir, mountDir1;
   private static String unzipDir;
   private static String userName;
   private static String password;
   private static File outputDir;
   
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
      mountDir1 = System.getProperty("java.io.tmpdir") + "bugs-archive/";
      unzipDir = System.getProperty("java.io.tmpdir") + "unzip/";
      outputDir = new File(System.getProperty("java.io.tmpdir") + "output/");
      
      if (!outputDir.exists()) {
    	  outputDir.mkdir();
      }
      
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
      
      cmd = "mount -t nfs bugs-archive.eng.vmware.com:/bugs-archive " + mountDir1;
      destDir = new File(mountDir1);
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
   
   public void testReduce(String number) throws IOException {
       System.out.println("Start: " + number);
	   
       String outputPath = outputDir + "/" + number + ".txt";
       if (new File(outputPath).exists()) return;
       
       BugFetcher fetcher = new BugFetcher(userName, password);
       Issue issue = fetcher.getBug(number);
       
       FileOutputStream output = new FileOutputStream(outputDir + "/" + number + ".txt");
       
       String mountpath = Util.getCorrectMountDir(Integer.parseInt(issue.getId()), mountDir, mountDir1);
       
       AttachmentFetcher aFetcher = new AttachmentFetcher(issue, mountpath + "files/", unzipDir);

       LogReducer logReducer = new LogReducer(aFetcher, output);
       
       logReducer.reduce();
       
       output.close();
       
       aFetcher.clean();
       
       System.out.println("End: " + number);
   }
/*   
   @Test
   public void testOneReduce() {
      try {
    	  testReduce("1016604");
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
*/
   @Test
   public void testBatchReduce() {
      try {
         String file = System.getProperty("user.dir") + "/data/filtered-bugs";
         Scanner scanner = new Scanner(new File(file));
         while (scanner.hasNext()) {
            testReduce(scanner.next());
         }
         System.out.println("END: " + file );
         scanner.close();
      }catch (Exception ex) {
         ex.printStackTrace();
      }
   }
   
   @AfterClass
   public static void tearDownAfterClass() throws Exception {
      try {
         String cmd = "umount " + mountDir;
         System.out.println("Executing: " + cmd);
         Process p = Runtime.getRuntime().exec(cmd);
         p.waitFor();
         
         cmd = "umount " + mountDir1;
         System.out.println("Executing: " + cmd);
         p = Runtime.getRuntime().exec(cmd);
         p.waitFor(); 
      }catch(Exception ex) {
         ex.printStackTrace();
      }
   }
}
