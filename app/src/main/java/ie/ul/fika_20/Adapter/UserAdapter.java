
package ie.ul.fika_20.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final Context mContext;
    private final List<User> mUsers;

    private FirebaseUser firebaseUser;

    // Constructor for context and users
    public UserAdapter(Context mContext, List<User> mUsers, boolean isFragment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    //Creates a viewholder for users
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //get username, full name and profile picture for user and the button for follow
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = mUsers.get(position);
        holder.btn_follow.setVisibility(View.VISIBLE);

        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getFullName());
        Picasso.get().load(user.getAvatar()).placeholder(R.drawable.ic_account_circle).into(holder.avatar);
        isFollowed(user.getId(), holder.btn_follow);

        //if it is your own username in the list, the button should not be visible
        if (user.getId().equals(firebaseUser.getUid())) {
            holder.btn_follow.setVisibility(View.GONE);
        }

        //Send friend request to people in the list

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_follow.getText().toString().equals("follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child((firebaseUser.getUid())).child("following").child(user.getId()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

                    addNotification(user.getId());
                    // if we do not follow there is a button for "follow" which changes to "following" when we press it and vice versa if it already says following. And the person is added to the firebase as people you follow
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child((firebaseUser.getUid())).child("following").child(user.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });
    }

    //checking if the person is followed by the current user
    private void isFollowed(final String id, final Button button) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            //changing the text on the button if the user is following or not
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(id).exists()) {
                    button.setText("following");
                } else {
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // Returns number of users
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Declaring all variables from user_item
        public TextView username;
        public TextView fullname;
        public CircleImageView avatar;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Linking the variables to xml items
            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            avatar = itemView.findViewById(R.id.avatar);
            btn_follow = itemView.findViewById(R.id.btn_follow);
        }
    }
    //Notification to the person you start follow

    private void addNotification(String userid) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("text", "started following you.");
        map.put("postid", "");
        map.put("isPost", false);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(map);

    }

}

