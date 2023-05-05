package com.example.boiwala;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email, password;
    private ImageButton btnClose;
    private Button btnSignIn;

    private FirebaseAuth firebaseAuth;

    private final String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        dontHaveAnAccount = view.findViewById(R.id.signUp);
        parentFrameLayout = getActivity().findViewById(R.id.registerFrameLayout);

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        btnClose = view.findViewById(R.id.btnClose);
        btnSignIn = view.findViewById(R.id.signIn);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if(!TextUtils.isEmpty(email.getText())) {
            if(!TextUtils.isEmpty(password.getText())){
                btnSignIn.setEnabled(true);
            }
            else {
                btnSignIn.setEnabled(false);
            }
        }
        else {
            btnSignIn.setEnabled(false);
        }
    }

    private void checkEmailAndPassword() {
        if(email.getText().toString().matches(emailPattern)) {
            if(password.length()>=8) {
                //btnSignIn.setEnabled(false);
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                                else {
                                    btnSignIn.setEnabled(true);
//                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                                    email.setError("");
                                    email.requestFocus();
                                    password.setError("");
                                    password.requestFocus();
                                    return;
                                }
                            }
                        });
            }
            else {
                //Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                password.setError("Minimum Password length 8 chars");
                password.requestFocus();
                return;
            }
        }
        else {
            Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
            email.setError("");
            email.requestFocus();
            password.setError("");
            password.requestFocus();
            return;
        }
    }
}