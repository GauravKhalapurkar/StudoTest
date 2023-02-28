package com.gakdevelopers.studotest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gakdevelopers.studotest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    EditText editFullName, editEmail, editPassword;

    Button btnCreateAccount;

    TextView txtSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editFullName = (EditText) findViewById(R.id.editFullName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);

        txtSignIn = (TextView) findViewById(R.id.txtSignIn);

        mAuth = FirebaseAuth.getInstance();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = editFullName.getText().toString();
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (fullName.equals("") || email.equals("") || password.equals("")) {
                    Toast.makeText(SignUp.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignUp.this, Main.class);
                                    intent.putExtra("fullName", fullName);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignUp.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, SignIn.class));
            }
        });

    }
}