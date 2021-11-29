package ie.ul.fika_20.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.R;

public class MyPhotosAdapter extends RecyclerView.Adapter<MyPhotosAdapter.MyViewHolder> {

    private final Context mContext;
    private List<Post> postList;

    public MyPhotosAdapter(Context mContext, List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyPhotosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_view, parent, false);
        return new MyPhotosAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPhotosAdapter.MyViewHolder holder, int position) {

        final Post post = postList.get(position);
        Picasso.get().load(post.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.postImage);

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postid", post.getPostid()).apply();
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
