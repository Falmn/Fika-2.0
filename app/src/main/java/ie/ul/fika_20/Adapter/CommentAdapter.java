package ie.ul.fika_20.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import de.hdodenhof.circleimageview.CircleImageView;
import ie.ul.fika_20.Model.Comment;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Comment> mComments;

    private FirebaseUser fUser;

    // Constructor for context and comments
    public CommentAdapter(Context mContext, List<Comment> mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
    }

    //Creates a viewholder for comments
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //get comment
        final Comment comment = mComments.get(position);
        holder.comment.setText(comment.getComment());

        // Get values from database for comment
        FirebaseDatabase.getInstance().getReference().child("Users").child(comment.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                //Set username from User class and avatar, otherwise launcher avatar
                holder.username.setText(user.getUsername());
                if (user.getAvatar().equals("default")) {
                    holder.avatar.setImageResource(R.drawable.ic_account_circle);

                } else {
                    Picasso.get().load(user.getAvatar()).into(holder.avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    // Returns number of comments

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Declaring all variables from comment_item
        public CircleImageView avatar;
        public TextView username;
        public TextView comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Linking the variables to xml items
            avatar = itemView.findViewById(R.id.avatar);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);

        }
    }
}
