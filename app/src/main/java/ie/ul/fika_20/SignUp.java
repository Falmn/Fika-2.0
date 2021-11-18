package ie.ul.fika_20;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends AppCompatActivity {
    private TextInputLayout username;
    private TextInputLayout fullname;
    private TextInputLayout email;
    private TextInputLayout password;
    private Button register;
    private TextView loginUser;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    //Pattern for password validation
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("" +
                    "(?=.*[a-zA-Z])" +          // Any letter -short
                    "(?=.*[@#%$&+=])" +         // At least one special character -short
                    "(?=\\S+$)" +               // No white spaces
                    ".{4,}" +                   // At least four characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        loginUser = findViewById(R.id.login_user);
        ProgressBar progressBar=(ProgressBar) findViewById(R.id.progressBar);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        //If user already a member, sends them to loginActivity
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });


        // Checks if all fields are filled in and start register method when user clicks on register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = email.getEditText().getText().toString().trim();
                String txtPassword = password.getEditText().getText().toString();
                String txtFullName = fullname.getEditText().getText().toString().trim();
                String txtUsername = username.getEditText().getText().toString().trim();

                progressBar.setVisibility(View.VISIBLE);

                //Register user to firebase
                fAuth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "User created", Toast.LENGTH_SHORT);
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("newUsers").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", txtFullName);
                            user.put("email", txtEmail);
                            user.put("username", txtUsername);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: User profile created for" + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            Toast.makeText(SignUp.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                });
            }

        });
    }

    //Methods to validate all input
    private boolean validateUsername(){
        String usernameInput = username.getEditText().getText().toString();

        if (usernameInput.isEmpty()){
            username.setError("Please enter username");
            return false;
        }else if (usernameInput.length()>15){
            username.setError("Username too long");
            return false;
        }else {
            username.setError(null);
            return true;
        }
    }

    private boolean validateName(){
        String nameInput = fullname.getEditText().getText().toString();

        if (nameInput.isEmpty()){
            fullname.setError("Please enter full name");
            return false;
        }else {
            fullname.setError(null);
            return true;
        }
    }

    private boolean validateEmail(){
        String emailInput = email.getEditText().getText().toString();

        if (emailInput.isEmpty()){
            email.setError("Please enter email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            email.setError("Please enter a valid email address");
            return false;
        }else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput = password.getEditText().getText().toString();

        if (passwordInput.isEmpty()){
            password.setError("Please enter password");
            return false;
        }else if (PASSWORD_PATTERN.matcher(passwordInput).matches()){
            password.setError("Password too weak");
            return false;
        }else {
            password.setError(null);
            return true;
        }
    }

    //Confirm input when clicking button
    public void confirmInput(View view){
        boolean validation = validateName() && validateEmail() && validateUsername() && validatePassword();
        if (validation){
            //Start activity
        }
    }


}