package ie.ul.fika_20.Model;

public class Notification {

    private String userid;
    private String text;
    private String postid;
    private boolean isPost;

    public Notification() {
    }

    //Constructor for notification
    public Notification(String userid, String text, String postid, boolean isPost) {
        this.userid = userid;
        this.text = text;
        this.postid = postid;
        this.isPost = isPost;
    }

    //getters and setters for all variables
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setIsPost(boolean isPost) {
        this.isPost = isPost;
    }
}

