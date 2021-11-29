package ie.ul.fika_20;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
import com.squareup.picasso.Picasso;

import ie.ul.fika_20.Fragments.NotificationFragment;
import ie.ul.fika_20.Fragments.ProfileFragment;
import ie.ul.fika_20.Fragments.SavedFragment;
import ie.ul.fika_20.Fragments.SearchFragment;
import ie.ul.fika_20.Model.User;


public class Profile extends AppCompatActivity {
    // Firebase
    private FirebaseUser firebaseUser;
    private FirebaseAuth fAuth;
    //  private FirebaseDatabase fDBS;
    private DatabaseReference myRef;
    // Variabels
    private TextView userName_profile;
    private ImageView image_profile;
    private String userId, profileid;
    private ImageButton logout, nav_back_profile;
    private ImageView imageAvatar;

    // Connected to a fragment container.
    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Verify user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");


        // Fetching username

        userName_profile = findViewById(R.id.username_profile);

        // Image button and Image avatar
        logout = findViewById(R.id.nav_logout);
        nav_back_profile = findViewById(R.id.nav_back_profile);
        imageAvatar = findViewById(R.id.image_avatar);

        // Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();
        userId = FirebaseAuth.getInstance().getUid();
        myRef = FirebaseDatabase.getInstance().getReference();


        userInfo();
// Shows Profile fragment first
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profile,
                new ProfileFragment()).commit();
        // Steps through the menu and the next fragments loads.
        bottomNavigationView = findViewById(R.id.menu_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_grid_profile:
                        selectorFragment = new ProfileFragment();
                        break;
                    case R.id.nav_search_user:
                        selectorFragment = new SearchFragment();
                        break;

                    case R.id.nav_notifications:
                        selectorFragment = new NotificationFragment();
                        break;
                    case R.id.nav_saved_posts:
                        selectorFragment = new SavedFragment();
                        break;

                }

                if (selectorFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profile, selectorFragment).commit();
                }

                return true;

            }
        });

        // Logout button

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), StartApp.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        // Back button
        nav_back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
            }
        });
    // If the image is pressed, the user can change the image from the libary.
        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, EditAvatar.class));
                finish();
            }
        });
    }



// Displays username and loads default profile unless a user has uploaded one.
    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user =dataSnapshot.getValue(User.class);

                    userName_profile.setText(snapshot.getValue().toString());
                   if (user.getAvatar().equals("default")){
                       imageAvatar.setImageResource(R.drawable.ic_launcher_background);
                   } else {
                    Picasso.get().load(user.getAvatar()).into(imageAvatar);
                }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

