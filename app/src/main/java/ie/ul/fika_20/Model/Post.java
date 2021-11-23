package ie.ul.fika_20.Model;

public class Post {

    private String caption;
    private String imageurl;
    private String postid;
    private String publisher;

    public Post(){
    }

    //Constructor for post
    public Post(String caption, String imageurl, String postid, String publisher){
        this.caption = caption;
        this.imageurl  = imageurl;
        this.postid = postid;
        this.publisher = publisher;
    }

    //Get & set methods for all variables

    public String getCaption(){
        return caption;
    }

    public void setCaption(){
        this.caption = caption;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

}
