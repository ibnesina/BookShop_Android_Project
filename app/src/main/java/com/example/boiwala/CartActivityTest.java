package com.example.boiwala;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiwala.cart.Book;
import com.example.boiwala.cart.CartAdapter;
import com.example.boiwala.cart.CartViewModel;

import java.util.List;

public class CartActivityTest extends AppCompatActivity implements CartAdapter.CartClickedListeners {
    private RecyclerView recyclerView;
    private CartViewModel cartViewModel;
    private TextView totalCartPriceTv, textView;
    private AppCompatButton checkoutBtn;
    private CardView cardView;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_test);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("My Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeVariables();
        cartViewModel.getAllCartItems().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> bookCarts) {
                double price = 0;
                cartAdapter.setBookCartList(bookCarts);
                for (int i=0;i<bookCarts.size();i++){
                    price = price + bookCarts.get(i).getTotalPrice();
                }
                totalCartPriceTv.setText("Tk. "+ String.valueOf(price));
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cartViewModel.deleteAllCartItems();
//                textView.setVisibility(View.INVISIBLE);
//                checkoutBtn.setVisibility(View.INVISIBLE);
//                totalCartPriceTv.setVisibility(View.INVISIBLE);
                cartViewModel.getAllCartItems().observe(CartActivityTest.this, new Observer<List<Book>>() {
                    @Override
                    public void onChanged(List<Book> bookCarts) {
                        double price = 0;
                        cartAdapter.setBookCartList(bookCarts);
                        for (int i=0;i<bookCarts.size();i++){
                            price = price + bookCarts.get(i).getTotalPrice();
                        }
                        if(bookCarts.size()!=0) {
                            Intent intent = new Intent(CartActivityTest.this, PaymentActivity.class);
                            intent.putExtra("TotalPrice", String.valueOf(price));
                            intent.putExtra("Items", String.valueOf(bookCarts.size()));
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(CartActivityTest.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }


    private void initializeVariables() {

        cartAdapter = new CartAdapter(this);
        textView = findViewById(R.id.textView2);
        totalCartPriceTv = findViewById(R.id.cartActivityTotalPriceTv);
        checkoutBtn = findViewById(R.id.cartActivityCheckoutBtn);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        recyclerView = findViewById(R.id.cartRecyclerVieww);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(cartAdapter);

    }

    @Override
    public void onDeleteClicked(Book bookCart) {
        cartViewModel.deleteCartItem(bookCart);
    }

    @Override
    public void onPlusClicked(Book bookCart) {
        int quantity = bookCart.getQuantity() + 1;
        cartViewModel.updateQuantity(bookCart.getId() , quantity);
        cartViewModel.updatePrice(bookCart.getId() , quantity*bookCart.getBookPrice());
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusClicked(Book bookCart) {
        int quantity = bookCart.getQuantity() - 1;
        if (quantity != 0){
            cartViewModel.updateQuantity(bookCart.getId() , quantity);
            cartViewModel.updatePrice(bookCart.getId() , quantity*bookCart.getBookPrice());
            cartAdapter.notifyDataSetChanged();
        } else{
            cartViewModel.deleteCartItem(bookCart);
        }
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