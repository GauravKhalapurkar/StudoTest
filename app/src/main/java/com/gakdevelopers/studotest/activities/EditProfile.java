package com.gakdevelopers.studotest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
import com.gakdevelopers.studotest.internet.NetworkChangeListener;

public class EditProfile extends AppCompatActivity {

    private Toolbar toolbar;

    EditText editFullName, editEmail;

    Button btnUpdate;

    ProgressDialog loading;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        editFullName = (EditText) findViewById(R.id.editFullName);
        editEmail = (EditText) findViewById(R.id.editEmail);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editFullName.setText(DbQuery.myProfile.getName());
        editEmail.setText(DbQuery.myProfile.getEmail());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullName = editFullName.getText().toString();
                String email = editEmail.getText().toString();

                if (fullName.equals("") || email.equals("")) {
                    Toast.makeText(EditProfile.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                loading =  ProgressDialog.show(EditProfile.this,"Loading","Please Wait",false,false);

                try {
                    DbQuery.updateProfile(email, fullName, new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(EditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                            startActivity(new Intent(EditProfile.this, Main.class));
                            EditProfile.this.finish();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(EditProfile.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(EditProfile.this, "Error Code: 705. Please restart app and try again!", Toast.LENGTH_SHORT).show();
                    Log.d("ERROR_CODE", e.getMessage());
                }

                loading.dismiss();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            EditProfile.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}