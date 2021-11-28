
package ie.ul.fika_20.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

import ie.ul.fika_20.Adapter.PostAdapter;
import ie.ul.fika_20.Adapter.RecyclerViewAdapter;
import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.NewPost;
import ie.ul.fika_20.R;
import ie.ul.fika_20.StartApp;


// changed array from User.java to Post.java

public class profile2 extends Fragment {

    // Widgets
    private RecyclerView recyclerView;

    //  private MyFotosAdapter myFotosAdapter;
    private List<Post> myPostList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    // Firebase
    private FirebaseUser firebaseUser;
    private FirebaseAuth fAuth;
    //  private FirebaseDatabase fDBS;
    private DatabaseReference myRef;
    // Variabels
    // private ArrayList<Post>  postList;
    private Context mContext;
    private TextView userName_profile;
    private ImageView image_profile;
    private String userId, profileid;
    private ImageButton searchUser, notification, saved, logout;
    private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(ie.ul.fika_20.R.layout.fragment_profile2, container, false);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");


        //   View view = inflater.inflate(ie.ul.fika_20.R.layout.fragment_profile2, container, false);
        // Fetching username
        image_profile = view.findViewById(R.id.image_avatar);
        userName_profile = view.findViewById(R.id.username_profile);
        // Imagebuttons
       /* searchUser = view.findViewById(R.id.search_user);
        notification = view.findViewById(R.id.notifications);
        logout = view.findViewById(R.id.log_out);*/
        saved = view.findViewById(R.id.save);
        // Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();
        userId = FirebaseAuth.getInstance().getUid();
        myRef = FirebaseDatabase.getInstance().getReference();
        // fDBS = FirebaseDatabase.getInstance();

        // Gridlayout for images
        recyclerView = view.findViewById(R.id.recycler_view_profile);
        layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        myPostList = new ArrayList<>();
        // recyclerViewAdapter = new RecyclerViewAdapter(postList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get Data method
        myFotos();


        return view;

    }


    private void myFotos(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myPostList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)){
                        myPostList.add(post);
                    }
                }

                Collections.reverse(myPostList);
                recyclerViewAdapter.notifyDataSetChanged();
                /*// mContext ist för getApplicationContext. la till arraylist<Post>.
                recyclerViewAdapter = new RecyclerViewAdapter(mContext, (ArrayList<Post>) myPostList);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
                *//*Collections.reverse(postList);
                RecyclerViewAdapter.notifyDataSetChanged();*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }








    }


