import b4j.core.Issue;

public class AttachmentFetcher {
   private Issue issue;
   private final String prefix = "/mts/bugs/files/";
   
   public AttachmentFetcher(Issue issue) {
      if (issue == null) throw new IllegalArgumentException("Argument issue is NULL");
      this.issue = issue;
   }
   
   public String getAttachmentDir() {
      int id = Integer.parseInt(issue.getId());
      StringBuffer path = new StringBuffer(prefix);
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
}
