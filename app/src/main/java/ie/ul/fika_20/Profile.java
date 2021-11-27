package ie.ul.fika_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ie.ul.fika_20.Adapter.RecyclerViewAdapter;
import ie.ul.fika_20.Fragments.NotificationFragment;
import ie.ul.fika_20.Fragments.SavedFragment;
import ie.ul.fika_20.Fragments.SearchFragment;
import ie.ul.fika_20.Fragments.profile2;
import ie.ul.fika_20.Model.Post;


public class Profile extends AppCompatActivity {


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
    private TextView userName_profile;
    private ImageView image_profile;
    private String userId, profileid;

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");


        // Fetching username
        image_profile = findViewById(R.id.image_avatar);
        userName_profile = findViewById(R.id.username_profile);


        // Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();
        userId = FirebaseAuth.getInstance().getUid();
        myRef = FirebaseDatabase.getInstance().getReference();
        // fDBS = FirebaseDatabase.getInstance();

        // Gridlayout for images
        recyclerView = findViewById(R.id.recycler_view_profile);
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        postList = new ArrayList<>();
        //  recyclerViewAdapter = new RecyclerViewAdapter(postList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);


        // Lists of methods


        /* userInfo();


         */
        // Get Data method

        myFotos();
        // userInfo();

        // Clear List
        ClearAll();
        // userInfo();
        //   GetDataFromFireBase();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_grid_profile:
                        selectorFragment = new profile2();
                        break;
                    case R.id.nav_search_user :
                        selectorFragment = new SearchFragment();
                        break;

                    case R.id.nav_notifications :
                        selectorFragment = new NotificationFragment();
                        break;
                    case R.id.nav_saved_posts:
                        selectorFragment = new SavedFragment();
                        break;
                    case R.id.nav_logout:
                        selectorFragment = new logout();
                        break;
                }

                if (selectorFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                }

                return  true;

            }
        });

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profile2()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }/* else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment()).commit();
        }*/

    }


    private void myFotos() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)) {
                        postList.add(post);
                    }
                }
                // mContext ist för getApplicationContext. la till arraylist<Post>.
                recyclerViewAdapter = new RecyclerViewAdapter(mContext, (ArrayList<Post>) postList);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
                /*Collections.reverse(postList);
                RecyclerViewAdapter.notifyDataSetChanged();*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }






    private void ClearAll() {
        if (postList != null) {
            postList.clear();

            if (recyclerViewAdapter != null) {
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }
        postList = new ArrayList<>();

    }
}

 /*private void GetDataFromFireBase () {

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
    }*/
/*
package ie.ul.fika_20;

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
import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.Model.User;
import ie.ul.fika_20.NewPost;
import ie.ul.fika_20.R;


// changed array from User.java to Post.java

public class Profile extends Fragment {

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
    private ArrayList<Post>  postList;
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
        recyclerViewAdapter = new RecyclerViewAdapter(postList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);


        // Lists of methods


        userInfo();
        myFotos();

        postList = new ArrayList<>();

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
                    Post post = new Post();
                    post.setImageurl(snapshot.child("imageurl").getValue().toString());
                    post.setPublisher(snapshot.child("username").getValue().toString());

                    postList.add(post);

                }
                // mContext ist för getApplicationContext.
                recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext, postList);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ClearAll(){
        if (postList =! null){
            postList.clear();

            if (recyclerViewAdapter != null){
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }
        postList = new ArrayList<>();

    }

    // Displaying user name in the textView. Need to fix imageurls.
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
                *//*fullname.setText(user.getFullname());
                bio.setText(user.getBio());*//*
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
// Fetching fotos and adding them to the recyclerView
// Needs more work.
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
        }*/
//}





