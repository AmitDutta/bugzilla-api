package com.vmware.borathon.issue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.lingala.zip4j.core.ZipFile;

import org.apache.commons.io.FileUtils;

import com.vmware.borathon.Util;

import b4j.core.Issue;

public class AttachmentFetcher {
   private Issue issue;
   private Set<String> items;
   private List<String> logs;
   private String sourceDirPrefix;
   private String tmpDirPrefix;
   private String tmpBugPath;
   
   public AttachmentFetcher(Issue issue, String sourceDirPrefix, String tmpDirPrefix) {
      if (issue == null) throw new IllegalArgumentException("Argument issue is NULL");
      this.issue = issue;
      this.sourceDirPrefix = sourceDirPrefix;
      this.tmpDirPrefix = tmpDirPrefix;
      items = new HashSet<String>();
      logs = new ArrayList<String>();
   }
   
   public String getAttachmentDir() {
      int id = Integer.parseInt(issue.getId());
      StringBuffer path = new StringBuffer(sourceDirPrefix);
      path.append("0");
      StringBuffer ret = new StringBuffer();
      while (id > 0) {
         ret.append(id % 10);
         ret.append("/");
         id /= 10;
      }
      path.append(ret.reverse().toString());
      return path.toString();
   }
   
   public Iterator<IssueLog> GetLogIterator(Issue issue) {
      List<IssueLog> logs = new ArrayList<IssueLog>();
	   return logs.iterator();
   }
   
   public void processLogs() {
      
      tmpBugPath = tmpDirPrefix + issue.getId() + "/";
      
      // Create a directory to process in tmp directory
      if (!createDirectory(tmpBugPath)) {
         System.out.println("Could not create tmp directory");
         return;
      }
      
      // Copy all log files to temporary directory
      if (!copyDirectory(tmpBugPath)) {
         System.out.println("Could not copy logs");
         return;
      }
      
      // Traverse temp directory recursively to unzip and get log file
      items.add(tmpBugPath);
      unzipRecursive();
      
      // Remove the tmp folder
      /*System.out.println("Cleaning tmp directory: " + tmpBugPath);
      try {
         Process p = Runtime.getRuntime().exec("rm -rf " + tmpBugPath);
         p.waitFor();
      } catch (Exception ex) {
         ex.printStackTrace();
      }*/
   }
   
   private boolean createDirectory(String tmpBugPath) {
      File tmpDir = new File(tmpBugPath);
      if (!tmpDir.exists()) {
         tmpDir.mkdirs();
      }
      return true;
   }
   
   private boolean copyDirectory(String tmpBugPath) {
      boolean copied = false;
      String sourceBugPath = Util.getAttachmentDir(Integer.parseInt(issue.getId()), sourceDirPrefix);
      System.out.println("source bug path: " + sourceBugPath);
      // Copy all log files to tmp directory
      File source = new File (sourceBugPath);
      File dest = new File (tmpBugPath);
      try {
         FileUtils.copyDirectory(source, dest);
         copied =true;
      } catch (IOException e) {
         e.printStackTrace();
      }         
      
      return copied;
   }
   
   private void unzipRecursive() {
      while (items.size() > 0) {
         String currentPath = items.iterator().next();
         items.remove(currentPath);
         File file = new File(currentPath);
         if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (File child : children) {
               items.add(child.getAbsolutePath());
            }
         } else if (file.isFile()) {
            if (file.getName().endsWith(".tgz") || file.getName().endsWith(".tar.gz")) {
          	   System.out.println("UNTAR " + file.getAbsolutePath() + "...");
               String cmd = "tar -zxf " + file.getAbsolutePath() + " -C " + file.getParent();
               try {
                  Process untar = Runtime.getRuntime().exec(cmd);
                  untar.waitFor();
                  items.add(file.getParent());
                  file.delete();
               }catch (Exception ex) {
                  ex.printStackTrace();
               }
            } else if (file.getName().endsWith(".zip")) {
               System.out.println("UNZIP " + file.getAbsolutePath() + "...");
               try {
                  ZipFile zipFile = new ZipFile(file.getAbsolutePath());
                  zipFile.extractAll(file.getParent());
                  items.add(file.getParent());
                  file.delete();
               }catch (Exception ex) {
                  ex.printStackTrace();
               }
               
            } else if (file.getName().endsWith(".gz")) {
               String cmd = "gzip -d " + file.getAbsolutePath();
               try {
            	  System.out.println("uncompress " + file.getAbsolutePath() + "...");
                  Process untar = Runtime.getRuntime().exec(cmd);
                  untar.waitFor();
                  String unzippedFile = getFileNameFromGz(file.getAbsolutePath());
                  if (new File(unzippedFile).exists()) {
                     logs.add(unzippedFile);
                  }
                  file.delete();
               }catch(Exception ex) {
                  ex.printStackTrace();
               }
            } else if (file.getName().endsWith(".log")
                       || file.getName().startsWith("vmkernel")
                       || file.getName().startsWith("hostd")
                       || file.getName().startsWith("vpxd")
                       || file.getName().startsWith("sps")) {
               logs.add(file.getAbsolutePath());
            }
         }
      }
   }
   
   public void clean() {
      try {
         // Remove the tmp folder
         System.out.println("Cleaning tmp directory: " + tmpBugPath);
         try {
            Process p = Runtime.getRuntime().exec("rm -rf " + tmpBugPath);
            p.waitFor();
         } catch (Exception ex) {
            ex.printStackTrace();
         }         
      }catch (Exception ex) {
         ex.printStackTrace();
      }
   }
   
   public Iterator<IssueLog> iterator() { return new IssueLogIterator(); }
   
   public static String getFileNameFromGz(String gzName) {
      return gzName.substring(0, gzName.indexOf(".gz"));
   }
   
   private class IssueLogIterator implements Iterator<IssueLog> {
      private int i = 0;
      @Override
      public boolean hasNext() {
        return i == logs.size() ? false : true;
      }

      @Override
      public IssueLog next() {
         String path = logs.get(i);
         IssueLog issueLog = null;
         try {
            issueLog = new IssueLog(path, new FileInputStream(new File(path)));
            i++;
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         }
         return issueLog;
      }

   	@Override
   	public void remove() {
   	}
   }
}
