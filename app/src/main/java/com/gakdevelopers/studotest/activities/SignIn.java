package com.gakdevelopers.studotest.activities;

import android.app.ProgressDialog;
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
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    EditText editEmail, editPassword;

    Button btnSignIn;

    TextView txtSignUp;

    private FirebaseAuth mAuth;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        txtSignUp = (TextView) findViewById(R.id.txtSignUp);

        mAuth =FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(SignIn.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                loading =  ProgressDialog.show(SignIn.this,"Loading","Please Wait",false,false);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    DbQuery.loadData("FREE_TESTS", new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            //loading.dismiss();
                                            Intent intent = new Intent(SignIn.this, Main.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignIn.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            loading.dismiss();
                                        }
                                    });
                                } else {
                                    Toast.makeText(SignIn.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            }
                        });

            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });
    }
}