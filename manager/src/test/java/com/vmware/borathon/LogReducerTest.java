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
   private static String mountDir;
   private static String unzipDir;
   private static String userName;
   private static String password;
   private static File outputDir;
   
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
   }
   
   public void testReduce(String number) throws IOException {
      
      System.out.println("Start: " + number);
	   
	    BugFetcher fetcher = new BugFetcher(userName, password);
       Issue issue = fetcher.getBug(number);
       
       String outputPath = outputDir + "/" + number + ".txt";
       
       if (new File(outputPath).exists()) return;
       
       FileOutputStream output = new FileOutputStream(outputDir + "/" + number + ".txt");
       
       AttachmentFetcher aFetcher = new AttachmentFetcher(issue, mountDir + "files/", unzipDir);

       LogReducer logReducer = new LogReducer(aFetcher, output);
       
       logReducer.reduce();
       
       output.close();
       
       aFetcher.clean();
       
       System.out.println("End: " + number);
   }
   
   @Test
   public void testBatchReduce() {
      try {
         String file = System.getProperty("user.dir") + "/data/Bug-With-Attachment.txt";
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
      }catch(Exception ex) {
         ex.printStackTrace();
      }
   }
}
