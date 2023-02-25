package com.gakdevelopers.studotest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gakdevelopers.studotest.R;

public class SignUp extends AppCompatActivity {

    EditText editFullName, editEmail, editPassword;

    Button btnCreateAccount;

    TextView txtSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editFullName = (EditText) findViewById(R.id.editFullName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);

        txtSignIn = (TextView) findViewById(R.id.txtSignIn);

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