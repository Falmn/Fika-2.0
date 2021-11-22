
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context mContext;
    private List<User> mUsers;
    private boolean isFragment;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<User> mUsers, boolean isFragment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);
        holder.btn_follow.setVisibility(View.VISIBLE);

        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getFullName());
        Picasso.get().load(user.getAvatar()).placeholder(R.drawable.ic_account_circle).into(holder.avatar);
        isFollowed(user.getId() , holder.btn_follow);

        if (user.getId().equals(firebaseUser.getUid())){
            holder.btn_follow.setVisibility(View.GONE);
        }
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFragment) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getId());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                } else {
                    Intent intent = new Intent(mContext , MainActivity.class);
                    intent.putExtra("publisherid" , user.getId());
                    mContext.startActivity(intent);
                }
            }
        });*/
 //Skicka vänförfrågan

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_follow.getText().toString().equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
// om vi inte följer finns en knapp för "follow" som ändras till "following" när vi trycker på den och tvärt om om det redan står following. Och personen läggs till i firebase som personer man följer
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });
    }
    //Notiser

/*
    private void addNotifications(String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("userid" , firebaseUser.getUid());
        hashMap.put("text" , "started following you");
        hashMap.put("postid" , "");
        hashMap.put("ispost" , false);

        reference.push().setValue(hashMap);
    }//notis slut

     */





    @Override
    public int getItemCount() { return mUsers.size(); }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView fullname;
        public CircleImageView avatar;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            avatar = itemView.findViewById(R.id.avatar);
            btn_follow = itemView.findViewById(R.id.btn_follow);
        }
    }
    private void isFollowed(final String id, Button button) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists())
                    button.setText("Following");
                else button.setText("Follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

