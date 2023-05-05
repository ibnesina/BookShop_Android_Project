package com.example.boiwala;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BkashPaymentActivity extends AppCompatActivity {
    private Button btnContinue;
    private String paymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_payment);

        btnContinue = findViewById(R.id.btnContinue);
        paymentStatus = getIntent().getStringExtra("paymentStatus");

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BkashPaymentActivity.this, OrderSuccessfull.class);
                intent.putExtra("paymentStatus", paymentStatus);
                startActivity(intent);
                finish();
            }
        });
    }
}