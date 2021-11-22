
package ie.ul.fika_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.regex.Pattern;


public class SignUp extends AppCompatActivity {
    private TextInputLayout username;
    private TextInputLayout fullname;
    private TextInputLayout email;
    private TextInputLayout password;
    private Button register;
    private TextView loginUser;
    private ProgressBar progressBar;

    DatabaseReference fRDB;
    //FirebaseFirestore fStore;
    FirebaseAuth fAuth;

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
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        fRDB = FirebaseDatabase.getInstance().getReference();
        //fStore = FirebaseFirestore.getInstance();
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
                String txtEmail = email.getEditText().getText().toString();
                String txtPassword = password.getEditText().getText().toString();
                String txtFullName = fullname.getEditText().getText().toString();
                String txtUsername = username.getEditText().getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                registerUser(txtUsername, txtFullName, txtEmail, txtPassword);
            }

        });
    }


    // Register user to firebase database
    private void registerUser(final String username, final String name, final String email, String password) {

        fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("username", username);
                map.put("id", fAuth.getCurrentUser().getUid());

                fRDB.child("Users").child(fAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Welcome to Fika!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, "Error !" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    //Pattern for password validation
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("" +
                    "(?=.*[a-zA-Z])" +          // Any letter -short
                    "(?=.*[@#%$&+=])" +         // At least one special character -short
                    "(?=\\S+$)" +               // No white spaces
                    ".{4,}" +                   // At least four characters
                    "$");

    //Methods to validate all input
    private boolean validateUsername() {
        String usernameInput = username.getEditText().getText().toString();

        if (usernameInput.isEmpty()) {
            username.setError("Please enter username");
            return false;
        } else if (usernameInput.length() > 15) {
            username.setError("Username too long");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        String nameInput = fullname.getEditText().getText().toString();

        if (nameInput.isEmpty()) {
            fullname.setError("Please enter full name");
            return false;
        } else {
            fullname.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = email.getEditText().getText().toString();

        if (emailInput.isEmpty()) {
            email.setError("Please enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = password.getEditText().getText().toString();

        if (passwordInput.isEmpty()) {
            password.setError("Please enter password");
            return false;
        } else if (PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password too weak");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    //Confirm input when clicking button
    public void confirmInput(View view) {
        boolean validation = validateName() && validateEmail() && validateUsername() && validatePassword();
        if (validation) {
            //Start activity
        }
    }


  /*  // Example 2
    //Register user to firebase

private void registerUser(final String txtUsername, final String txtFullname, final String txtEmail, String txtPassword) {
        fAuth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    userID = fAuth.getCurrentUser().getUid();

                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("name", txtFullname);
                    hashMap.put("email", txtEmail);
                    hashMap.put("username", txtUsername);

                    fStore.collection("Users").document(userID).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    //Nisses example
    //Register user to firebase


fAuth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "User created", Toast.LENGTH_SHORT);
                            userID = fAuth.getCurrentUser().getUid();
                            //DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", txtFullName);
                            user.put("email", txtEmail);
                            user.put("username", txtUsername);

                            fStore.collection("Users").document(userID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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

*/



}

