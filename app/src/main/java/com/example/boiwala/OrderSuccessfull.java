package com.example.boiwala;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class OrderSuccessfull extends AppCompatActivity {
    private Button homeBtn;
    private String orderStatus;
    private CardView cardViewSuccessful, cardViewFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_successfull);

        homeBtn = findViewById(R.id.homeBtn);
        cardViewSuccessful = findViewById(R.id.orderSuccessful);
        cardViewFailed = findViewById(R.id.orderFailed);

        orderStatus = getIntent().getStringExtra("paymentStatus");

        if(orderStatus.equals("successful")) {
            cardViewSuccessful.setVisibility(View.VISIBLE);
        }
        else if(orderStatus.equals("canceled")) {
            cardViewSuccessful.setVisibility(View.INVISIBLE);
            cardViewFailed.setVisibility(View.VISIBLE);
        }

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessfull.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}