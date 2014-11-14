package com.vmware.borathon;

import java.io.File;

public final class Util {
   
   public static String getCorrectMountDir(int id, String mountDir, String mountDir1) {
      String mountPath = mountDir;
      if(!Util.isEmptyDir(id, mountPath)) {
         return mountPath;
      }
      
      mountPath = mountDir1;
      return mountPath;
   }
   
   public static String getAttachmentDir(int id, String sourceDirPrefix) {
      StringBuffer path = new StringBuffer(sourceDirPrefix);
      StringBuffer ret = new StringBuffer();
      int digit = 0;
      while (id > 0) {
         ret.append(id % 10);
         ret.append("/");
         digit++;
         id /= 10;
      }
      
      while (digit < 8) {
         ret.append("0/");
         digit++;
      }
      
      path.append(ret.reverse().toString());
      return path.toString();
   }
   
   public static boolean isEmptyDir(int id, String sourceDirPrefix) {
      boolean empty = true;
      String dirPath = getAttachmentDir(id, sourceDirPrefix + "files/");
      File dir = new File(dirPath);
      if (dir.exists() && dir.isDirectory() && dir.list().length > 0) empty = false;
      return empty;
   }
}
