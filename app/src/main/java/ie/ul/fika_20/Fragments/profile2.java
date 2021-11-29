
package ie.ul.fika_20.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ie.ul.fika_20.Adapter.RecyclerViewAdapter;
import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.R;


public class profile2 extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter photoAdapter;
    private List<Post> postList;

    private FirebaseUser fUser;

    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(ie.ul.fika_20.R.layout.fragment_profile2, container, false);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");

        if (data.equals("none")) {
            profileId = fUser.getUid();
        } else {
            profileId = data;
            getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }


        // Gridlayout for images
        recyclerView = view.findViewById(R.id.recycler_view_grid_profile);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        photoAdapter = new RecyclerViewAdapter(getContext(), postList);
        postList = new ArrayList<>();
        recyclerView.setAdapter(photoAdapter);
        // new array

        // Get Data method
        myPhotos();


        return view;

    }

    private void myPhotos(){
        FirebaseDatabase.getInstance().getReference().child("Posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Post post = snapshot.getValue(Post.class);

                            if (post.getPublisher().equals(profileId)){
                                postList.add(post);
                            }
                        }

                        Collections.reverse(postList);
                        photoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



    }


