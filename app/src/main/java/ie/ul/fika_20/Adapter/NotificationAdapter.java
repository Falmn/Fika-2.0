package ie.ul.fika_20.Adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import ie.ul.fika_20.Fragments.profile2;
import ie.ul.fika_20.Model.Notification;
import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> mNotification;

    public NotificationAdapter(Context mContext, List<Notification> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       Notification notification = mNotification.get(position);

       getUser(holder.avatar, holder.username, notification.getUserid());
//om bilden är postad
       if (notification.isPost()){
           holder.postImage.setVisibility(View.VISIBLE);
           getPostImage(holder.postImage, notification.getUserid());
       }else{//annars är det inget, använder gone för att inte platsen ska vara "sparad"
           holder.postImage.setVisibility(View.GONE);
       }
//Är ej klar, förstår ej funktionen
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(notification.isPost()){
                   mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postId", notification.getPostId()).apply();

               } else{
                   mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().putString("profileId", notification.getUserid()).apply();

                   ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profile2()).commit();
               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView avatar;
        public ImageView postImage;
        public TextView username;
        public TextView text;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            postImage = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.comment);
        }
    }

    private void getPostImage(ImageView imageView, String postId){
        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get().load(post.getImageurl()).placeholder(R.drawable.ic_account_circle).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//Hämta användarnamn och profilbild från firebase
    private void getUser(ImageView imageView, TextView textView, String userId){
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user =snapshot.getValue(User.class);
                //om ingen profilbild är uppladdad visas vår bruna bild
                if (user.getAvatar().equals("default")){
                   imageView.setImageResource(R.drawable.ic_account_circle);
                }else {
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
