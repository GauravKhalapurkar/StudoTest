package com.gakdevelopers.studotest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;

public class BuyTest extends AppCompatActivity {

    EditText editCouponCode;

    Button btnSubmit;

    TextView txtNoCoupon, txtTestPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_test);

        editCouponCode = (EditText) findViewById(R.id.editCouponCode);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        txtNoCoupon = (TextView) findViewById(R.id.txtNoCoupon);
        txtTestPrice = (TextView) findViewById(R.id.txtTestPrice);

        txtTestPrice.setText("Test @ ₹" + DbQuery.g_price);

        txtNoCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BuyTest.this, "RazorPay Gateway", Toast.LENGTH_SHORT).show();
            }
        });

    }
}