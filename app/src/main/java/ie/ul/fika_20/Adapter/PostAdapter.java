package ie.ul.fika_20.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import ie.ul.fika_20.CommentActivity;
import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder> {

    private final Context mContext;
    private final List<Post> mPosts;
    private final FirebaseUser firebaseUser;

    // Constructor for context and posts
    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    //Creates a viewholder for posts
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        // Get post image and set caption from user
        final Post post = mPosts.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.postImage);
        holder.caption.setText(post.getCaption());

        // Get values from database for post
        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                //Set avatar and username from User class (otherwise launcher avatar)
                if (user.getAvatar().equals("default")) {
                    holder.avatarImage.setImageResource(R.drawable.ic_account_circle);
                } else {
                    Picasso.get().load(user.getAvatar()).into(holder.avatarImage);
                }
                holder.username.setText(user.getUsername());
                holder.author.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Call on methods
        isLiked(post.getPostid(), holder.like);
        noOfLikes(post.getPostid(), holder.noOfLikes);
        getComments(post.getPostid(),holder.noOfComments);
        isSaved(post.getPostid(), holder.save);

        // Like pictures
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checks if user already liked post, remove like if user press like again
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.
                            getPostid()).child(firebaseUser.getUid()).setValue(true);

                    addNotification(post.getPostid(), post.getPublisher());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.
                            getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        // Comment on pictures
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        // Number of comments
        /*holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });*/


        // Save picture/remove save - and update database
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostid()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostid()).removeValue();
                }

            }
        });

    }

    // Returns number of posts
    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {


        // Declaring all variables from post_item
        public ImageView location;
        public ImageView avatarImage;
        public ImageView postImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;

        public TextView username;
        public TextView noOfLikes;
        public TextView noOfComments;
        public TextView author;
        public TextView caption;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            // Linking all variables to xml items
            location = itemView.findViewById(R.id.location);
            avatarImage = itemView.findViewById(R.id.avatar_image);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment_post);
            save = itemView.findViewById(R.id.save);

            username = itemView.findViewById(R.id.username);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
            author = itemView.findViewById(R.id.author);
            caption = itemView.findViewById(R.id.caption);
        }
    }

    // Check if post is saved and sets tags & changes icon accordingly
    private void isSaved(String postId, ImageView image){
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postId).exists()){
                    image.setImageResource(R.drawable.ic_bookmark_added);
                    image.setTag("saved");
                }else {
                    image.setImageResource(R.drawable.ic_bookmark_border);
                    image.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Check if post is liked and sets tags and like-icon accordingly
    private void isLiked(String postId, ImageView imageView) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                            imageView.setImageResource(R.drawable.ic_liked);
                            imageView.setTag("liked");
                        } else {
                            imageView.setImageResource(R.drawable.ic_heart_border);
                            imageView.setTag("like");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // Method to count number of likes
    private void noOfLikes(String postId, TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        text.setText(dataSnapshot.getChildrenCount() + " likes");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    // Method to get comments
    private void getComments (String postId, final TextView text){
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        text.setText(dataSnapshot.getChildrenCount() + " Comments");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
//when we get a like vi get a notification
    private void addNotification(String postId, String publisherId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", publisherId);
        map.put("text", "liked your post.");
        map.put("postid", postId);
        map.put("isPost", true);

        FirebaseDatabase.getInstance().getReference().child("Notification").child(firebaseUser.getUid()).push().setValue(map);
   }
}
