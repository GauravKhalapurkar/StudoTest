package com.gakdevelopers.studotest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
import com.gakdevelopers.studotest.internet.NetworkChangeListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import kotlinx.coroutines.channels.ProduceKt;

public class BuyTest extends AppCompatActivity implements PaymentResultListener {

    private EditText editCouponCode;

    private Button btnSubmit;

    private TextView txtNoCoupon, txtTestPrice;

    private String categoryName;

    private ProgressDialog loading;

    private String couponCode = null;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_test);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");

        editCouponCode = (EditText) findViewById(R.id.editCouponCode);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        txtNoCoupon = (TextView) findViewById(R.id.txtNoCoupon);
        txtTestPrice = (TextView) findViewById(R.id.txtTestPrice);

        txtTestPrice.setText("Test @ ₹" + DbQuery.g_price);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couponCode = editCouponCode.getText().toString();

                if (couponCode.equals("")) {
                    Toast.makeText(BuyTest.this, "Enter Coupon Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                loading =  ProgressDialog.show(BuyTest.this,"Loading","Please Wait",false,false);

                DbQuery.loadCouponCodes(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {

                        if (DbQuery.g_couponList.contains(couponCode)) {
                            payWithRazorpay((DbQuery.g_price * 5) / 100, "Discounted Payment for " + categoryName);
                            editCouponCode.setText("");

                        } else {
                            Toast.makeText(BuyTest.this, "Your Coupon Code is invalid. Please try again!", Toast.LENGTH_SHORT).show();
                        }

                        loading.dismiss();

                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(BuyTest.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });

                loading.dismiss();
            }
        });

        txtNoCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payWithRazorpay(DbQuery.g_price, "Payment for " + categoryName);
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {
        if (couponCode == null) {
            savePurchasedData(DbQuery.g_price);
        } else {
            savePurchasedData((DbQuery.g_price * 5) / 100);
            deleteAppliedCouponCode();
        }

        Toast.makeText(this, "Payment is successful: " + s, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(BuyTest.this, Tests.class);
        intent.putExtra("testType", "PAID_TESTS");
        intent.putExtra("categoryName", categoryName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //BuyTest.this.finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed due to error: " + s, Toast.LENGTH_SHORT).show();
    }

    private void payWithRazorpay(double priceToPay, String description) {
        int amount = Math.round(Float.parseFloat(String.valueOf(priceToPay)) * 100);

        Checkout checkout = new Checkout();
        checkout.setKeyID(getString(R.string.razorpay_api_key));
        checkout.setImage(R.drawable.app_icon);

        JSONObject object = new JSONObject();
        try {
            object.put("name", "Paying for " + categoryName);

            object.put("description", "Payment for " + categoryName);

            object.put("theme.color", "");

            object.put("currency", "INR");

            object.put("amount", amount);

            object.put("prefill.email", DbQuery.myProfile.getEmail());

            checkout.open(BuyTest.this, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void savePurchasedData(int amount) {
        loading =  ProgressDialog.show(BuyTest.this,"Loading","Please Wait",false,false);

        DbQuery.savePurchaseData(amount, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                loading.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(BuyTest.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }

    private void deleteAppliedCouponCode() {
        loading =  ProgressDialog.show(BuyTest.this,"Loading","Please Wait",false,false);

        DbQuery.deleteUsedCoupon(couponCode, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                loading.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(BuyTest.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
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