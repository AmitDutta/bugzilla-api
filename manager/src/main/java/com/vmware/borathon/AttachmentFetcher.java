package com.vmware.borathon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import b4j.core.Issue;

public class AttachmentFetcher {
   private Issue issue;
   private final String sourceDirPrefix = "/Users/amitd/Documents/mnt/bugs/files/";
   private final String tmpDirPrefix = "/Users/amitd/Documents/tmp/";
   private Set<String> items;
   private List<String> logs;
   
   public AttachmentFetcher(Issue issue) {
      if (issue == null) throw new IllegalArgumentException("Argument issue is NULL");
      this.issue = issue;
      processLogs();
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
   
   private void processLogs() {
      
      String tmpBugPath = tmpDirPrefix + issue.getId() + "/";
      
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
      
      // Traverse temp directory recursively to unzip and get log files
      items = new HashSet<String>();
      logs = new ArrayList<String>();
      items.add(tmpBugPath);
      unzipRecursive();
   }
   
   private boolean createDirectory(String tmpBugPath) {
      boolean created = false;
      File tmpDir = new File(tmpBugPath);
      if (!tmpDir.exists()) {
         tmpDir.mkdir();
         created = true;
      }
      return created;
   }
   
   private boolean copyDirectory(String tmpBugPath) {
      boolean copied = false;
      
      String sourceBugPath =  getAttachmentDir();
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
   
   /*private void traverse(File file, String space, final String tmpBugPath) {
      if (file.isFile()) {
         if (file.getName().endsWith(".tgz") || file.getName().endsWith(".tar.gz")) {
            String cmd = "tar -zxf " + file.getAbsolutePath() + " -C " + tmpBugPath;
            System.out.println("Execute: " + cmd);
            try {
               Process untar = Runtime.getRuntime().exec(cmd);
               untar.waitFor();
            } catch (Exception e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         return;
      }
      else if (file.isDirectory()) {
         System.out.println(space + file.getName());
         File[] listOfFiles = file.listFiles();
         for (File child : listOfFiles) {
            traverse(child, space + " ", tmpBugPath);
         }
      }
   }*/
   
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
               String cmd = "tar -zxf " + file.getAbsolutePath() + " -C " + file.getParent();
               try {
                  Process untar = Runtime.getRuntime().exec(cmd);
                  untar.waitFor();
                  items.add(file.getParent());
                  file.delete();
               }catch (Exception ex) {
                  ex.printStackTrace();
               }
            } else if (file.getName().endsWith(".gz")) {
               String cmd = "gzip -d " + file.getAbsolutePath();
               try {
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
		// TODO Auto-generated method stub
		
	}
   }
}
