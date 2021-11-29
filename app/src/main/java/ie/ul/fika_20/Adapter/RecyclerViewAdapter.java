package ie.ul.fika_20.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

import ie.ul.fika_20.Fragments.ProfileFragment;
import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    /**
     * This class is an adapter for the recyclerview.
     * It holds and loads images to ProfileFragment and SavedFragment.
     */

    // varibels
    private final Context mContext;
    private List<Post> postList;

    public RecyclerViewAdapter(Context mContext, List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    // Created the viewholder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_view, parent, false);
        return  new RecyclerViewAdapter.MyViewHolder(view);
    }
    // Gets position from database (latest post). Loads the image url into the placeholder.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Post post = postList.get(position);
        Picasso.get().load(post.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.postImage);
        // PostImage onclicklistner, finds the right postid and replaces the fragment_container_profile with ProfileFragment.
        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postid", post.getPostid()).apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_profile, new ProfileFragment()).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView postImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.image_singleview);
        }
    }


}
