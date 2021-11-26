package ie.ul.fika_20.Model;

public class Notification {

        private String userId;
        private String text;
        private String postId;
        private boolean isPost;

        public Notification() {
        }

        public Notification(String userId, String text, String postId, boolean isPost) {
            this.userId = userId;
            this.text = text;
            this.postId = postId;
            this.isPost = isPost;
        }

        public String getUserid() {
            return userId;
        }

        public void setUserid(String userid) {
            this.userId = userid;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public boolean isPost() {
            return isPost;
        }

        public void setIsPost(boolean isPost) {
            this.isPost = isPost;
        }
    }

