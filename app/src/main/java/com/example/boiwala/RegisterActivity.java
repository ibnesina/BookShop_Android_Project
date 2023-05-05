package com.example.boiwala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

public class RegisterActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        frameLayout = findViewById(R.id.registerFrameLayout);
        setFragment(new SignInFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragementTransaction = getSupportFragmentManager().beginTransaction();
        fragementTransaction.replace(frameLayout.getId(), fragment);
        fragementTransaction.commit();
    }
}