
package ie.ul.fika_20.Fragments;

import android.content.Context;
import android.content.Intent;
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

import ie.ul.fika_20.Adapter.RecyclerViewAdapter;
import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.NewPost;
import ie.ul.fika_20.R;
import ie.ul.fika_20.StartApp;


// changed array from User.java to Post.java

public class profile2 extends Fragment {

    // Widgets
    private RecyclerView recyclerView;
    //  private MyFotosAdapter myFotosAdapter;
    private List<Post> postList;
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
    TextView userName_profile;
    ImageView image_profile;
    String userId;
    ImageButton searchUser, notification, logout;

    //  int [] arr = {R.drawable.image1,R.drawable.image22, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8};

// MÅSTE FIXA onCREATEVIEW!!!! ÄR ngt med fragments.


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(ie.ul.fika_20.R.layout.fragment_profile2, container, false);
        //   View view = inflater.inflate(ie.ul.fika_20.R.layout.fragment_profile2, container, false);
        // Fetching username
        image_profile = view.findViewById(R.id.image_avatar);
        userName_profile = view.findViewById(R.id.username_profile);
        // Imagebuttons
        searchUser = view.findViewById(R.id.search_user);
        notification = view.findViewById(R.id.notifications);
        logout = view.findViewById(R.id.log_out);
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
        postList = new ArrayList<>();
        // recyclerViewAdapter = new RecyclerViewAdapter(postList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        // från den nya


/*
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(profile2.this, StartApp.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
*/
        //logout button returns to startpage
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), StartApp.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });


       /*
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        myFotosAdapter = new MyFotosAdapter(getContext(), postList);
        recyclerView.setAdapter(RecyclerViewAdapter);*/

        // Lists of methods


       /* userInfo();
        myFotos();

*/
        // Get Data method
        GetDataFromFireBase();
        // Clear List
        ClearAll();


        return view;

    }

    private void GetDataFromFireBase () {

        Query query = myRef.child("Posts");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = new Post();
                    post.setImageurl(snapshot.child("imageurl").getValue().toString());
                    post.setPublisher(snapshot.child("username").getValue().toString());

                    postList.add(post);

                }
                // mContext ist för getApplicationContext. la till arraylist<Post>.
                recyclerViewAdapter = new RecyclerViewAdapter(mContext, (ArrayList<Post>) postList);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


 /*   private void myFotos(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                myFotosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/



    private void ClearAll () {
        if (postList != null) {
            postList.clear();

            if (recyclerViewAdapter != null) {
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }
        postList = new ArrayList<>();

    }
    }


