package ie.ul.fika_20.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import ie.ul.fika_20.Model.Notification;
import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> mNotification;

    // Constructor for context and notifications
    public NotificationAdapter(Context mContext, List<Notification> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    //Creates a viewholder for notifications
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Notification notification = mNotification.get(position);
        //get username and profile picture and sets the comment
        getUser(holder.avatar, holder.username, notification.getUserid());
        holder.comment.setText(notification.getText());


//if it is an image that is posted, we want it to be displayed
        if (notification.isPost()) {
            holder.postImage.setVisibility(View.VISIBLE);
            getPostImage(holder.postImage, notification.getPostid());
        } else {//otherwise it is nothing, using GONE so that the space is not "saved" for an image
            holder.postImage.setVisibility(View.GONE);
        }

    }

    // Returns number of notifications
    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Declaring all variables from notification_item
        public ImageView avatar;
        public ImageView postImage;
        public TextView username;
        public TextView comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Linking the variables to xml items
            avatar = itemView.findViewById(R.id.avatar);
            postImage = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
        }
    }

    ///Method to get PostImage from firebase
    private void getPostImage(ImageView imageView, String postId) {
        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get().load(post.getImageurl()).placeholder(R.mipmap.ic_launcher).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Method to get username and profile picture from firebase
    private void getUser(final ImageView imageView, final TextView textView, String userId) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                //if we dont have a profile picture our default picture shows
                if (user.getAvatar().equals("default")) {
                    imageView.setImageResource(R.drawable.ic_account_circle);
                } else {
                    //but if we have one that is the one showing up
                    Picasso.get().load(user.getAvatar()).into(imageView);
                }
                textView.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
