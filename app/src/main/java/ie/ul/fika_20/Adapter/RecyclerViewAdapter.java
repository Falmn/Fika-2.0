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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


private static final String Tag = "recyclerView";
private final Context mContext;
private List<Post> postList;

    public RecyclerViewAdapter(Context mContext, List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.single_view, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view);
       /* MyViewHolder myViewHolder = new MyViewHolder(view);


        return myViewHolder;*/
    }

   /* @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        // Textview username??
        holder.textView.setText(postList.get(position).getPublisher());
        // Imageview with glidelibary.
        // KOLLA I User classen efter image url att den e med.
        Glide.with(mContext)
        .load(postList.get(position).getImageurl())
                .into(holder.imageView);



        //holder.imageView.setImageResource(arr[position]);
//Kanske textview
    }
*/

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Post post = postList.get(position);

        Glide.with(mContext).load(post.getImageurl()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();
/*
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostDetailFragment()).commit();*/
            }
        });

    }
    @Override
    public int getItemCount() {
        return postList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.image_singleview);


        }
    }


    /*@NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

// get data and put it in this template.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_view, parent, false);


        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.image_profile);
            textView = itemView.findViewById(R.id.userName_profile);


        }
    }*/

  //  int [] arr;
  /*  public RecyclerViewAdapter(int[] arr) {
        this.arr = arr;
    }*/


}
