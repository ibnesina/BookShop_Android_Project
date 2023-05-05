package com.example.boiwala;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView categoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ////copy from homefragmentt


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("products").document(title).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                        if(task.isComplete()){
                            DocumentSnapshot ds = task.getResult();
                            long total = ds.getLong("no_of_products");
                            System.out.println("-------Total product------ "+total + title);
                            for (long x = 0; x < total; x++) {
                                    horizontalProductScrollModelList.add(new HorizontalProductScrollModel(ds.getString("product_id_" + x),
                                            ds.getString("product_image_" + x), ds.getString("product_title_" + x),
                                            ds.getString("product_subtitle_" + x), ds.getString("product_price_" + x)));
                                }
                            GridView gridView = findViewById(R.id.categoryGridView);
                            if(horizontalProductScrollModelList.size()>0) {
                                GridProductLayoutAdapter adapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
                                gridView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.main_search_icon) {
//            return true;
//        }
        if(id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}