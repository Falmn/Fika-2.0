package ie.ul.fika_20.Model;

public class Comment {

    private String comment;
    private String publisher;

    public Comment() {
    }

    //Constructor for comment
    public Comment(String comment, String publisher) {
        this.comment = comment;
        this.publisher = publisher;
    }

    //Get & set methods for comment and publisher
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
