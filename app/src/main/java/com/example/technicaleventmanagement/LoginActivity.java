package com.example.technicaleventmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.technicaleventmanagement.helper.Dotenv;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginActivity extends AppCompatActivity {

    private Dotenv dotenv = new Dotenv(this);
    String COLLECTION_USER = dotenv.get("COLLECTION_USER");
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private EditText
    userpassword,
    useremail;
    private Spinner user_typeSpinner;
    private Button loginButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection(COLLECTION_USER);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        useremail = findViewById(R.id.emailEditText_login);
        userpassword = findViewById(R.id.passwordEditText_login);
        user_typeSpinner = findViewById(R.id.user_typeSpinner_login);
        loginButton = findViewById(R.id.loginAccountButton_login);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(view -> {

            // String name = username.getText().toString().trim();
            String email = useremail.getText().toString().trim();
            String password = userpassword.getText().toString().trim();
            String user_type = user_typeSpinner.getSelectedItem().toString().trim();


            if (!TextUtils.isEmpty(email) &&
                    !TextUtils.isEmpty(password)) {

                loginAccountUsingEmailPassword(email, password);

            }
            else {

                if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please Enter Details", Toast.LENGTH_SHORT).show();
                }
                else if (email.isEmpty()) {
                    useremail.setError("Please Enter Your Email");
                    //Toast.makeText(LoginActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    useremail.requestFocus();
                }
                else{
                    userpassword.setError("Please Enter Your Password");
                    //Toast.makeText(LoginActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    userpassword.requestFocus();
                }

            }

        });

    }

    private void loginAccountUsingEmailPassword(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                currentUser = authResult.getUser();

                String currentUserId = currentUser.getUid();

                collectionReference
                        .whereEqualTo("Users", currentUserId)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                if (error != null) {
                                    Toast.makeText(LoginActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                                    Log.d("login", "onEvent: There is an error: " + error);
                                    return;
                                }

                                if (value != null && !value.isEmpty()) {

                                    Toast.makeText(LoginActivity.this, "LogIn Successful", Toast.LENGTH_SHORT).show();


                                   // create a class to store all the stuff



                                } else {
                                    Toast.makeText(LoginActivity.this, "No Account Found!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(LoginActivity.this, "Failed To Sign In", Toast.LENGTH_SHORT).show();

            }
        });


    }




}