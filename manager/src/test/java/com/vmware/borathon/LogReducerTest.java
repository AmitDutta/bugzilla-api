package com.vmware.borathon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.Issue;

import com.vmware.borathon.issue.AttachmentFetcher;
import com.vmware.borathon.issue.BugFetcher;

public class LogReducerTest {
   private static String mountDir;
   private static String unzipDir;

   private static File outputDir;
   
   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
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
   
   //@Test
   public void testReduce() throws IOException {
	   String number = "1355263"; 
	   
       BugFetcher fetcher = new BugFetcher("ggeorgiev", "");
       Issue issue = fetcher.getBug(number);
       
       FileOutputStream output = new FileOutputStream(outputDir + "/" + number + ".txt");
       
       AttachmentFetcher aFetcher = new AttachmentFetcher(issue, mountDir + "files/", unzipDir);

       LogReducer logReducer = new LogReducer(aFetcher, output);
       
       logReducer.reduce();
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
