
package ie.ul.fika_20;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ComponentActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ie.ul.fika_20.Model.User;

public class NewPost extends AppCompatActivity {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
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
        setContentView(R.layout.activity_new_post);

        selectedImage = findViewById(R.id.displayImageView);
        //    cameraBtn = findViewById(R.id.cameraButton);
        postBtn = findViewById(R.id.post_button);
        libraryBtn = findViewById(R.id.library_button);
        backButtonNewPost = findViewById(R.id.nav_back);
        Caption = findViewById(R.id.write_caption);

        storageReference = FirebaseStorage.getInstance().getReference(); // to store in storage

        //  databaseReference = FirebaseDatabase.getInstance().getReference();

        // newpostdatabase = FirebaseDatabase.getInstance(); // to save in database


        /*        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });*/

        backButtonNewPost.setOnClickListener(new View.OnClickListener() { // is a backbutton to go back to mainpage
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewPost.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        libraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

       /* Caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtcaption = Caption.getEditText().getText().toString();
            }
        });*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }
        }*/
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // the name of the picture

                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                selectedImage.setImageURI(contentUri);

                postBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadImageToFirebase(imageFileName, contentUri);

                        //progressBarPost.setVisibility(View.VISIBLE);
                        //If we can move upload to firebase here
                    }
                });
            }
        }
    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("pictures/" + name); // puts the images in directory pictures
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) { // uri of image
                        Picasso.get().load(uri).into(selectedImage); // takes the picture from firebase and puts it in the image picture
                        //Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());

                        DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("Posts");  //creats a post folder in realtime database

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("caption", Caption.getText().toString());
                        //kommentar
                        hashMap.put("imageurl", String.valueOf(uri));
                        hashMap.put("postid", name);
                        hashMap.put("publisher", "firebaseAuth.getCurrentUser().getUid()"); //firebaseUser.getUid()); // Uploads user id
                        imagestore.push().setValue(hashMap); // puts the hashmap into realtime database "posts"

                        Toast.makeText(NewPost.this, "Perfect! Image Is Uploaded.", Toast.LENGTH_SHORT).show();
                        //String imageReference = uri.toString();
                        //databaseReference.child("specimens").child(specimenDTO.getKey()).child("imageUrl").setValue(imageReference);
                        //specimenDTO.setImageUrl(imageReference);
                    }
                });

                // progressBarPost.setVisibility(View.GONE);
                Toast.makeText(NewPost.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() { // if it fails
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewPost.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }
}


/*    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);// constant verible 101 -> CAMERA_REQUEST_CODE
        } else {
            dispatchTakePictureIntent();
        }
    }*/


/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    //   private void openCamera() {
    //     Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //     startActivityForResult(camera, CAMERA_REQUEST_CODE); // constant verible 102 -> CAMERA_REQUEST_CODE

    // }

    /*private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // creating timestamp
        String imageFileName = "JPEG_" + timeStamp + "_"; // creating file name
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); // sends pictures to phone gallery
        File image = File.createTempFile( // creating image file
                imageFileName, //prefix

                ".jpg", //suffix

                storageDir //directory

        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }*/



//https://stackoverflow.com/questions/64221188/write-external-storage-when-targeting-android-10
//https://developer.android.com/training/camera/photobasics#java