package ie.ul.fika_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ie.ul.fika_20.Adapter.RecyclerViewAdapter;
import ie.ul.fika_20.Fragments.NotificationFragment;
import ie.ul.fika_20.Fragments.SearchFragment;
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
    private ImageButton searchUser, notification, saved, logout;


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
        // Imagebuttons
        searchUser = findViewById(R.id.search_user);
        notification = findViewById(R.id.notifications);
        saved = findViewById(R.id.save);
        logout = findViewById(R.id.log_out);
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




        // från den nya
        //search users button
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchFragment.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        //notifications button
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NotificationFragment.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        // saved posts button
       /* saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SavedFragment.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });*/

        //logout button returns to startpage
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), StartApp.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });



     /*   recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), postList);
        recyclerView.setAdapter(recyclerViewAdapter);*/

        // Lists of methods


       /* userInfo();
        myFotos();

*/
        // Get Data method

        myFotos();
        // userInfo();
        userProfile();
        // Clear List
        ClearAll();
        // userInfo();
        //   GetDataFromFireBase();

     //   return view;

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


    private void myFotos(){
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

    /*private void userInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }
                User user = dataSnapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageUrl()).into(image_profile);
                userName_profile.setText(user.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//wtf
            }
        });
    }*/

    // Fetching userdata from firebase.
    public void userProfile(){

        if (firebaseUser != null) {
            // User is Login
            String displayName = firebaseUser.getDisplayName();
            for (UserInfo userInfo : firebaseUser.getProviderData()) {
                if (displayName == null && userInfo.getDisplayName() != null) {
                    displayName = userInfo.getDisplayName();
                }
            }

            userName_profile.setText(displayName);
        }
    }


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





