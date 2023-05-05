package com.example.boiwala;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.boiwala.cart.Book;
import com.example.boiwala.cart.CartViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productTitle, productPrice, productDetails;
    private Button btnCart;
    private String description;
    private CartViewModel cartViewModel;
    private List<Book> bookList;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        productImage = findViewById(R.id.product_images_viewPager);
        productDetails = findViewById(R.id.product_details_viewPager);
        productTitle = findViewById(R.id.cartProductTitle);
        productPrice = findViewById(R.id.productPrice);
        btnCart = findViewById(R.id.btnCart);
        constraintLayout = findViewById(R.id.productDetailsLayout);

        String productImageURL = getIntent().getStringExtra("Image");
        String currentProductTitle = getIntent().getStringExtra("Title");
        String currentProductPrice = getIntent().getStringExtra("Price");
        getSupportActionBar().setTitle(currentProductTitle);

        Calendar calendar = Calendar.getInstance();
        String dateAndTime = DateFormat.getDateInstance(android.icu.text.DateFormat.FULL).format(calendar.getTime());


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        System.out.println("---------Title------" + currentProductTitle);

        firebaseFirestore.collection("Description").document(currentProductTitle).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isComplete()){
                            DocumentSnapshot ds = task.getResult();
                            description = ds.getString("description");
                            System.out.println(description);
                            productDetails.setText(description);
                        }
                    }
                });

        Glide.with(this).load(productImageURL).apply(new RequestOptions()).placeholder(R.drawable.book_cover_2).into(productImage);
        productTitle.setText(currentProductTitle);
        productPrice.setText(currentProductPrice);

        //////Cart
        bookList = new ArrayList<>();

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    Book bookCart = new Book();
                    bookCart.setBookName(currentProductTitle);
                    bookCart.setBookPrice(Double.parseDouble(currentProductPrice));
                    bookCart.setBookImage(productImageURL);

//                    bookCart.setPurchaseDate(dateAndTime);
//                  System.out.println(dateAndTime+ "----------");

                    final int[] quantity = {1};
                    final int[] id = new int[1];
                    if(!bookList.isEmpty()) {
                        for(int i = 0; i<bookList.size(); i++) {
                            if(bookCart.getBookName().equals(bookList.get(i).getBookName())){
                                quantity[0] = bookList.get(i).getQuantity();
                                quantity[0]++;
                                id[0] = bookList.get(i).getId();
                            }
                        }
                    }
                    if(quantity[0] == 1) {
                        bookCart.setQuantity(quantity[0]);
                        bookCart.setTotalPrice(quantity[0]*bookCart.getBookPrice());
                        cartViewModel.insertCartItem(bookCart);
                    }
                    else {
                        cartViewModel.updateQuantity(id[0], quantity[0]);
                        cartViewModel.updatePrice(id[0], quantity[0]*bookCart.getBookPrice());
                    }

                    makeSnackBar("Item Added to Cart");
                    //cartViewModel.insertCartItem(bookCart);
                }
                else {
                    Toast.makeText(ProductDetailsActivity.this, "Please LogIn First", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }

            private void makeSnackBar(String message) {
                Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT)
                        .setAction("Go to Cart", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(ProductDetailsActivity.this, CartActivityTest.class));
                            }
                        }).show();
            }
        });

        cartViewModel.getAllCartItems().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                bookList.addAll(books);
            }
        });

        //////Cart
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_cart_icon) {
            startActivity(new Intent(ProductDetailsActivity.this, CartActivityTest.class));
            return true;
        }
        else if(id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}