package ie.ul.fika_20.Model;

public class User {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String avatar;


    public User(){
    }
    public User(String id, String email, String fullName, String username, String avatar) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.username = username;
        this.avatar = avatar;
    }

    public String getId(){return id;}

    public void setId(String id){this.id = id;}
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
