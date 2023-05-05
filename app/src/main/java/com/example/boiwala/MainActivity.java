package com.example.boiwala;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private static final int HOME_FRAGMENT = 0, CART_FRAGMENT = 1;
    private static int currentFragment;

    private FrameLayout frameLayout;
    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private NavController navController;
    private ImageView actionBarLogo;

    private CircleImageView profileView;
    private TextView fullName, email;
    private ImageView addProfileIcon;
    private final int GALLERY_REQ_CODE = 1000;
    private StorageReference storageReference;
    private Uri uri;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.app_bar_main);

        toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_myHome, R.id.nav_myOrders, R.id.nav_myCart, R.id.nav_myWishlist,R.id.nav_myAccount)
                .setOpenableLayout(drawerLayout)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        frameLayout = findViewById(R.id.mainFrameLayout);
        actionBarLogo = findViewById(R.id.actionBarLogo);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(navController == null) return false;

                if(item.getItemId() == R.id.nav_signOut){
                    showToast("Signed Out");
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                    return false;
                }

                int id = item.getItemId();

                if(id == R.id.nav_myHome || id == R.id.nav_myOrders || id == R.id.nav_myCart || id == R.id.nav_myWishlist || id == R.id.nav_myAccount){
                    navController.navigate(id);
                    navView.setCheckedItem(id);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });


        ////Profile Image

        View headerView = navView.getHeaderView(0);
        profileView = headerView.findViewById(R.id.mainProfileImage);
        fullName = headerView.findViewById(R.id.mainFullName);
        email = headerView.findViewById(R.id.mainEmail);
        addProfileIcon = headerView.findViewById(R.id.addProfileIcon);

        storageReference = FirebaseStorage.getInstance().getReference("Image");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ImageModel");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser!=null) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ImageModel imageModel = dataSnapshot.getValue(ImageModel.class);
                        String uderId = imageModel.getUderId();
                        if(uderId.equals(currentUser.getUid())) {
                            //String imageUrl = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).toString();
                            if(imageModel.getImageUrl()!=null) {
                                if(!MainActivity.this.isFinishing())
                                    Glide.with(MainActivity.this).load(imageModel.getImageUrl()).apply(new RequestOptions()).placeholder(R.drawable.profile_pic).into(profileView);
                                break;
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            firebaseFirestore.collection("USERS").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                            if(task.isComplete()){
                                DocumentSnapshot ds = task.getResult();
                                //System.out.println(ds.getString("FullName") + "-----1------");
                                fullName.setText(ds.getString("FullName"));
                                email.setText(ds.getString("Email"));
                            }
                        }
                    });

            addProfileIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent iGallery = new Intent(Intent.ACTION_PICK);
                    iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    iGallery.setType("image/*");
                    startActivityForResult(iGallery, GALLERY_REQ_CODE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQ_CODE && data!=null && data.getData()!=null) {
            //profileView.setImageURI(data.getData());
            uri = data.getData();

            StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ImageModel imageModel = new ImageModel(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), uri.toString());
                            String modelId = databaseReference.push().getKey();
                            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(imageModel);
                            Toast.makeText(MainActivity.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Uploading Failed!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(mUri));
    }

    ///Profile Image

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_cart_icon) {
            startActivity(new Intent(MainActivity.this, CartActivityTest.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void setFragment(Fragment fragment, int fragmentNo) {
        currentFragment = fragmentNo;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

}
