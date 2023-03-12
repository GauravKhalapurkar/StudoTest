package com.gakdevelopers.studotest.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    EditText editEmail, editPassword;

    Button btnSignIn, btnSubmit;

    TextView txtSignUp, txtForgotPassword;

    private FirebaseAuth mAuth;

    ProgressDialog loading;

    EditText editForgotEmail;

    BottomSheetDialog dialogForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);

        dialogForgotPassword = new BottomSheetDialog(this);

        dialogForgotPassword.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        mAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

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
                                            Intent intent = new Intent(SignIn.this, Main.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            loading.dismiss();
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

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createForgotEmailDialog();

                dialogForgotPassword.show();
            }
        });
    }

    private void createForgotEmailDialog() {
        View view = getLayoutInflater().inflate(R.layout.forgot_password_dialog, null, false);

        editForgotEmail = view.findViewById(R.id.editForgotEmail);

        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String forgotEmail = editForgotEmail.getText().toString();

                if (forgotEmail.equals("")) {
                    Toast.makeText(SignIn.this, "Your Email is required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                loading =  ProgressDialog.show(SignIn.this,"Loading","Please Wait",false,false);

                mAuth.sendPasswordResetEmail(forgotEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignIn.this, "A Password Reset Link has been sent to your Email.", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                    dialogForgotPassword.dismiss();
                                } else {
                                    Toast.makeText(SignIn.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            }
                        });
            }
        });

        dialogForgotPassword.setContentView(view);
    }
}