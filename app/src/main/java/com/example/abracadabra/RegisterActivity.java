package com.example.abracadabra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity
{
    //to use form components
    TextView btn;
    Button registerButton;
    private EditText inpUsername, inpEmail , inpPassword, inpConfPassword;
    Boolean yo = false;

    //to use Firebase
     FirebaseAuth mAuth = FirebaseAuth.getInstance();//for authorisation
    private ProgressDialog loadingBar;

    //Firebase
    FirebaseFirestore fstore = FirebaseFirestore.getInstance(); //for database in Firestore of firebase

    //userid in database
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Connecting variables with form components
        inpUsername = findViewById(R.id.inputUsername);
        inpEmail = findViewById(R.id.inputEmail);
        inpPassword = findViewById(R.id.inputPassword);
        inpConfPassword = findViewById(R.id.inputConfirmPassword);
        btn=findViewById(R.id.alreadyhaveAccount);
        registerButton = findViewById(R.id.btnRegister);


        //loading bar when the credentials will be processed
        loadingBar = new ProgressDialog(RegisterActivity.this);

        //when user clicks on 'Already have an account'
        btn.setOnClickListener(v -> startActivity(new Intent (RegisterActivity.this,LoginActivity.class)));

        //when user clicks on register button
        registerButton.setOnClickListener(v -> {
            checkCredentials();//on clicking the credentials shell be checked

        });
    }

    private void checkCredentials() {//method to check credentials
        String username = inpUsername.getText().toString();
        String email = inpEmail.getText().toString();
        String pass = inpPassword.getText().toString();
        String confpass = inpConfPassword.getText().toString();

        if (username.isEmpty() || username.length()<3){
            Toast.makeText(this, "Try a different Username!", Toast.LENGTH_SHORT).show();
        }
        else if (!email.contains("@") || !email.contains(".")){
            Toast.makeText(this, "Invalid Email!", Toast.LENGTH_SHORT).show();
        }
        else if (pass.isEmpty() || pass.length()<5){
            Toast.makeText(this, "Try a different Password", Toast.LENGTH_SHORT).show();
        }
        else if (confpass.isEmpty() || !confpass.contentEquals(pass)){
            Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show();
        }
        else{

            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                 if(task.isSuccessful()){
                     userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                     DocumentReference documentReference = fstore.collection("user").document(userID);

                     Map<String,Object> user = new HashMap<>() ;
                     user.put("Username",username);
                     user.put("Email",email);
                     user.put("Password",pass);
                     documentReference.set(user).addOnSuccessListener(unused -> {
                         yo=true;
                         loadingBar.setTitle("Registering");
                         loadingBar.setMessage("Please wait while we process your credentials");
                         loadingBar.show();
                         Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent (RegisterActivity.this,LoginActivity.class));
                     });

                     documentReference.set(user).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Failure", Toast.LENGTH_SHORT).show());


                 }
                 else{
                     Toast.makeText(RegisterActivity.this, ""+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                 }
            });

        }

    }
}