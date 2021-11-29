package ie.ul.fika_20.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ie.ul.fika_20.Adapter.MyPhotosAdapter;
import ie.ul.fika_20.Adapter.RecyclerViewAdapter;
import ie.ul.fika_20.Model.Post;
import ie.ul.fika_20.R;

public class ProfileFragment extends Fragment {

    /**
     * This Fragment is index one in the switch menu and it the first one to display.
     * its main purpuse is to display a user's photos.
     */

    // Recyclerview adapters .
    private RecyclerView recyclerViewPhotos;
    private MyPhotosAdapter photoAdapter;
    private List<Post> myPhotosList;
    private FirebaseUser fUser;
    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Setting up the recycler view and connecting it. Also creates the grid.
        recyclerViewPhotos = view.findViewById(R.id.recycler_view_photos);
        recyclerViewPhotos.setHasFixedSize(true);
        recyclerViewPhotos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        myPhotosList = new ArrayList<>();
        photoAdapter = new MyPhotosAdapter(getContext(), myPhotosList);
        recyclerViewPhotos.setAdapter(photoAdapter);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");
        // Verifies user.
        if (data.equals("none")) {
            profileId = fUser.getUid();
        } else {
            profileId = data;
            getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }


        myPhotos();

        return view;

    }
    // Verfyfies the user and gets the users data from realtime database. It then displays it.
    private void myPhotos(){
        FirebaseDatabase.getInstance().getReference().child("Posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        myPhotosList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Post post = snapshot.getValue(Post.class);

                            if (post.getPublisher().equals(profileId)){
                                myPhotosList.add(post);
                            }
                        }

                        Collections.reverse(myPhotosList);
                        photoAdapter.notifyDataSetChanged();
                    }
                //
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}