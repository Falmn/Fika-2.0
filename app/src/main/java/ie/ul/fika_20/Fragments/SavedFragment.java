package ie.ul.fika_20.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ie.ul.fika_20.Adapter.PostAdapter;
import ie.ul.fika_20.Adapter.RecyclerViewAdapter;
import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.R;

public class SavedFragment extends Fragment {
    private RecyclerView recyclerViewSaves;
    private RecyclerViewAdapter postAdapterSaves;
    private List<Post> mySavedPosts;
    private FirebaseUser fUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_,  container, false);

        recyclerViewSaves = view.findViewById(R.id.recycler_view_saved);
        recyclerViewSaves.setHasFixedSize(true);
        recyclerViewSaves.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mySavedPosts = new ArrayList<>();
        postAdapterSaves = new RecyclerViewAdapter(getContext(), mySavedPosts);
        recyclerViewSaves.setAdapter(postAdapterSaves);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        getSavedPosts();

        return view;
    }

    private void getSavedPosts(){

        final List<String> savedIds = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Saves").child(fUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            savedIds.add(snapshot.getKey());
                        }

                        FirebaseDatabase.getInstance().getReference().child("Posts")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                        mySavedPosts.clear();

                                        for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()){
                                            Post post = snapshot1.getValue(Post.class);

                                            for (String id : savedIds){
                                                if (post.getPostid().equals(id)){
                                                    mySavedPosts.add(post);
                                                }
                                            }
                                        }

                                        postAdapterSaves.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}