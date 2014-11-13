package com.vmware.borathon;

public final class Util {
   
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
}
