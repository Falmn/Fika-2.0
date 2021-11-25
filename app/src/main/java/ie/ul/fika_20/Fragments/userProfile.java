/*

package ie.ul.fika_20.Fragments;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ie.ul.fika_20.Adapter.RecyclerViewAdapter;
import ie.ul.fika_20.Adapter.UserAdapter;
import ie.ul.fika_20.Login;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.NewPost;
import ie.ul.fika_20.R;




public class userProfile<Private> extends Fragment {

    // Widgets
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    // Firebase
    private FirebaseUser firebaseUser;
    private FirebaseAuth fAuth;
  //  private FirebaseDatabase fDBS;
    private DatabaseReference myRef;
    // Variabels
    private ArrayList<User>  userList;
    private Context mContext;
    TextView userName_profile;
    ImageView image_profile;
    String userId;






  //  int [] arr = {R.drawable.image1,R.drawable.image22, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8};





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        // Fetching username
        userName_profile = findViewById(R.id.userName_profile);
        image_profile = findViewById(R.id.image_profile);
// Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        fAuth = FirebaseAuth.getInstance();

        userId = FirebaseAuth.getInstance().getUid();
        myRef = FirebaseDatabase.getInstance().getReference();

        // fDBS = FirebaseDatabase.getInstance();



        // Gridlayout for images
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(userList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);


        // Lists of methods


        userInfo();
        myFotos();

        userList = new ArrayList<>();

        // Get Data method

         GetDataFromFireBase();

         // Clear List
        ClearAll();

    }

    private void GetDataFromFireBase(){

        Query query = myRef.child("Posts");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ClearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = new User();
                    user.(snapshot.child("image").getValue().toString());
                    user.setUsername(snapshot.child("username").getValue().toString());

                    userList.add(user);

                }
                // mContext ist för getApplicationContext.
                recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext, userList);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ClearAll(){
        if (userList =! null){
            userList.clear();

            if (recyclerViewAdapter != null){
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }
        userList = new ArrayList<>();

    }

    // Displaying user name in the textView. Need to fix imageurls.
   */
/* private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }

                User user = dataSnapshot.getValue(User.class);

                Picasso.get().load(user.getAvatar()).placeholder(R.drawable.).into(image_profile);
                userName_profile.setText(user.getUsername());
                *//*
*/
/*fullname.setText(user.getFullname());
                bio.setText(user.getBio());*//*
*/
/*
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*//*

        // Fetching fotos and adding them to the recyclerView
        // Needs more work.
     */
/*   private void myFotos() {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        NewPost post = snapshot.getValue(NewPost.class);
                        if (post.getPublisher().equals(profileid)){
                            postList.add(post);
                        }
                    }

                    Collections.reverse(postList);
                    RecyclerViewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }*//*

    }*/
