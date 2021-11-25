package ie.ul.fika_20.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
private final ArrayList<Post> postList;

    public RecyclerViewAdapter(Context mContext, ArrayList<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }



    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);


        return myViewHolder;
    }

    @Override
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

    @Override
    public int getItemCount() {
        return postList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.image_profile);


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
