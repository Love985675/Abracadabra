package com.example.abracadabra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    TextView btn;
    Button login;
    private EditText inpEmail,  inpPassward;
    boolean yo = false;
    //to use Firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();//for authorisation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inpEmail = findViewById(R.id.inputUsername);
        inpPassward = findViewById(R.id.inputPassword);
        btn=findViewById(R.id.createAnAccount);
        login = findViewById(R.id.btnlogin);


        //ON clicking "Create an account"
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        //ON clicking login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });
    }

    private void checkCredentials() {

        String email = inpEmail.getText().toString();
        String pass = inpPassward.getText().toString();


        if (email.isEmpty() || email.length()<3){
            Toast.makeText(this, "Invalid Username!", Toast.LENGTH_SHORT).show();
        }

        else if (pass.isEmpty() || pass.length()<5){
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
        }

        else{
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity2.class));

                }
                else
                {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
}