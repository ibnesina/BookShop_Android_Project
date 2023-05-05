package com.example.boiwala;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.boiwala.cart.Book;
import com.example.boiwala.cart.CartAdapter;
import com.example.boiwala.cart.CartViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    TextView totalItemPrice, totalPrice, totalItems;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btnContinue;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private FirebaseAuth mAuth;
    static int id = 0;
    private DatabaseReference reference;
    private long maxId = 0;
    private List<OrderedBooks> orderedBooksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totalItemPrice = findViewById(R.id.totalItemsPrice);
        totalPrice = findViewById(R.id.totalPrice);
        radioGroup = findViewById(R.id.paymentMethodRadioGroup);
        btnContinue = findViewById(R.id.paymentContinueBtn);
        totalItems = findViewById(R.id.totalItems);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderedBooksList = new ArrayList<>();

        String totalItemsPrice = getIntent().getStringExtra("TotalPrice");
        String totalItem = getIntent().getStringExtra("Items");

        totalItemPrice.setText("Tk. " + totalItemsPrice);
        totalPrice.setText("Tk. " + totalItemsPrice);
        totalItems.setText("Price (" + totalItem + " items)");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxId = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();

                if(radioId==R.id.cashOnDelivery) {
                    Toast.makeText(PaymentActivity.this, "Cash On Delivery", Toast.LENGTH_SHORT).show();

                    cartViewModel.getAllCartItems().observe(PaymentActivity.this, new Observer<List<Book>>() {
                        @Override
                        public void onChanged(List<Book> bookCarts) {
                            for (int i=0;i<bookCarts.size();i++){
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                String purchaseDate = DateFormat.getDateInstance(android.icu.text.DateFormat.FULL).format(Calendar.getInstance().getTime());
                                String bookName = bookCarts.get(i).getBookName();
                                String productImage = bookCarts.get(i).getBookImage();
                                Double productPrice = bookCarts.get(i).getTotalPrice();
                                int productQuantity = bookCarts.get(i).getQuantity();
                                Double totalProductPrice = bookCarts.get(i).getTotalPrice();

                                orderedBooksList.add(new OrderedBooks(userId, purchaseDate, bookName, productImage, productPrice, productQuantity, totalProductPrice));
                                reference.child(String.valueOf(maxId+1)).setValue(orderedBooksList.get(i));
                                maxId++;
                            }
                        }
                    });
                    cartViewModel.deleteAllCartItems();
                    Intent intent = new Intent(PaymentActivity.this, OrderSuccessfull.class);
                    intent.putExtra("paymentStatus", "successful");
                    startActivity(intent);
                    finish();
                }
                else if(radioId==R.id.bkashPayment) {
                    Toast.makeText(PaymentActivity.this, "Bkash Payment", Toast.LENGTH_SHORT).show();

                    cartViewModel.getAllCartItems().observe(PaymentActivity.this, new Observer<List<Book>>() {
                        @Override
                        public void onChanged(List<Book> bookCarts) {
                            for (int i=0;i<bookCarts.size();i++){
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                String purchaseDate = DateFormat.getDateInstance(android.icu.text.DateFormat.FULL).format(Calendar.getInstance().getTime());
                                String bookName = bookCarts.get(i).getBookName();
                                String productImage = bookCarts.get(i).getBookImage();
                                Double productPrice = bookCarts.get(i).getTotalPrice();
                                int productQuantity = bookCarts.get(i).getQuantity();
                                Double totalProductPrice = bookCarts.get(i).getTotalPrice();

                                orderedBooksList.add(new OrderedBooks(userId, purchaseDate, bookName, productImage, productPrice, productQuantity, totalProductPrice));
                                reference.child(String.valueOf(maxId+1)).setValue(orderedBooksList.get(i));
                                maxId++;
                            }
                        }
                    });
                    cartViewModel.deleteAllCartItems();
                    Intent intent = new Intent(PaymentActivity.this, BkashPaymentActivity.class);
                    intent.putExtra("paymentStatus", "successful");
                    startActivity(intent);
                    finish();
                }
                else if(radioId == R.id.cancelOrder) {
                    Toast.makeText(PaymentActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                    cartViewModel.deleteAllCartItems();
                    Intent intent = new Intent(PaymentActivity.this, OrderSuccessfull.class);
                    intent.putExtra("paymentStatus", "canceled");
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(PaymentActivity.this, "Please select a Payment Method", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}