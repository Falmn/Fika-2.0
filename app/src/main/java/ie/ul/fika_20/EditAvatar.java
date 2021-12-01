package ie.ul.fika_20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class EditAvatar extends AppCompatActivity {
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage;
    ImageView backButtonNewPost;
    Button postBtn, libraryBtn;
    String currentPhotoPath;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    EditText Caption;
    ProgressBar progressBarPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_avatar);
        selectedImage = findViewById(R.id.displayImageView);
        //    cameraBtn = findViewById(R.id.cameraButton);
        postBtn = findViewById(R.id.post_button);
        libraryBtn = findViewById(R.id.library_button);
        backButtonNewPost = findViewById(R.id.nav_back);
        Caption = findViewById(R.id.write_caption);
        storageReference = FirebaseStorage.getInstance().getReference(); // to store in storage

        // is a backbutton to go back to mainpage
        backButtonNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditAvatar.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });
        //opens the phone photogallery
        libraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // the name of the picture

                String imageFileName = "JPEG_" + timeStamp + "_" + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                selectedImage.setImageURI(contentUri);

                postBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadImageToFirebase(imageFileName, contentUri);
                        Intent intent = new Intent(EditAvatar.this, Profile.class);
                        startActivity(intent);
                        finish();
                        //progressBarPost.setVisibility(View.VISIBLE);
                        //If we can move upload to firebase here
                    }
                });
            }
        }
    }

    //uploads the image to firebase and creates a folder for the picture
    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("avatarpicture/" + name); // puts the images in directory pictures
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) { // uri of image
                        Picasso.get().load(uri).into(selectedImage); // takes the picture from firebase and puts it in the image picture


                        DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("avatar");  //creats a post folder in realtime database

                         //firebaseAuth.getCurrentUser().getUid(),firebaseAuth.getCurrentUser().getUid()firebaseUser.getUid()); // Uploads user id
                        imagestore.setValue(String.valueOf(uri)); // puts the hashmap into realtime database "posts"


                        //String imageReference = uri.toString();
                        //databaseReference.child("specimens").child(specimenDTO.getKey()).child("imageUrl").setValue(imageReference);
                        //specimenDTO.setImageUrl(imageReference);
                    }
                });

                // progressBarPost.setVisibility(View.GONE);
                Toast.makeText(EditAvatar.this, "Avatar picture changed", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() { // if it fails
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditAvatar.this, "Avatar upload Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }
}
