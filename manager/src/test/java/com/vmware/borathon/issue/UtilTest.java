package com.vmware.borathon.issue;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vmware.borathon.Util;

public class UtilTest {
   @Test
   public void testGetAttachmentDir() {
      String path = Util.getAttachmentDir(786447, "/mts/bugs/files");
      assertEquals("/mts/bugs/files/0/0/7/8/6/4/4/7", path);
      
      path = Util.getAttachmentDir(3, "/mts/bugs/files");
      assertEquals("/mts/bugs/files/0/0/0/0/0/0/0/3", path);
      
      path = Util.getAttachmentDir(1350176, "/mts/bugs/files");
      assertEquals("/mts/bugs/files/0/1/3/5/0/1/7/6", path);
   } 
}
