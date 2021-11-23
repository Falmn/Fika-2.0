package ie.ul.fika_20.Adapter;

import android.content.Context;
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

import java.util.List;

import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder> {

    private Context mContext;
    private List<Post> mPosts;

    private FirebaseUser firebaseUser;

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
        Post post = mPosts.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.postImage);
        holder.caption.setText(post.getCaption());

        // Get values from database for post
        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                //Set avatar and username from User class (otherwise launcher avatar)
                if (user.getAvatar().equals("default")) {
                    holder.avatarImage.setImageResource(R.mipmap.ic_launcher);
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

        // Like pictures
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checks if user already liked post, remove like if user press like again
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.
                            getPostid()).child(firebaseUser.getUid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.
                            getPostid()).child(firebaseUser.getUid()).removeValue();
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
    private void noOfLikes(String postId,TextView  text){
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
}
