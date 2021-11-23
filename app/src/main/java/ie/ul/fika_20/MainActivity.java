
package ie.ul.fika_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ie.ul.fika_20.Fragments.FeedFragment;
import ie.ul.fika_20.Fragments.userProfile;


public class MainActivity extends AppCompatActivity {

    private ImageView navProfile;
    private ImageView navHome;
    private ImageView navNewPost;
    DatabaseReference fRDB;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navProfile = findViewById(R.id.nav_profile);
        navHome = findViewById(R.id.nav_home);
        navNewPost = findViewById(R.id.nav_add_post);
        fAuth = FirebaseAuth.getInstance();
        fRDB = FirebaseDatabase.getInstance().getReference();

        // Starts feed fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FeedFragment()).commit();

        // Go to profile fragment if user clicks on profile icon
        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new userProfile()).commit();
            }
        });

        // Go to new post if user clicks on add button
        navNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewPost.class));
            }
        });

    }

    // If user not logged in, sends them to Start activity
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, StartApp.class));
            finish();
        }
    }

}

