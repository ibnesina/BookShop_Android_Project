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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class    SignUpFragment extends Fragment {

    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email, fullName, password, conformPassword, phoneNo, fullAddress;
    private Button btnSignUp;
    private ImageButton btnClose;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private final String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        alreadyHaveAnAccount = view.findViewById(R.id.signIn);

        btnClose = view.findViewById(R.id.btnClose);

        email = view.findViewById(R.id.email);
        fullName = view.findViewById(R.id.fullName);
        password = view.findViewById(R.id.password);
        conformPassword = view.findViewById(R.id.conformPassword);
        phoneNo = view.findViewById(R.id.mobileNumber);
        fullAddress = view.findViewById(R.id.address);

        btnSignUp = view.findViewById(R.id.signUp);

        parentFrameLayout = getActivity().findViewById(R.id.registerFrameLayout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
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

        fullName.addTextChangedListener(new TextWatcher() {
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

        conformPassword.addTextChangedListener(new TextWatcher() {
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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo send data to firebase
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

    public void checkInputs() {
        if(!TextUtils.isEmpty(email.getText())) {
            if(!TextUtils.isEmpty(fullName.getText())) {
                if(!TextUtils.isEmpty(password.getText()) && password.length()>=8) {
                    if(!TextUtils.isEmpty(conformPassword.getText())) {
                        btnSignUp.setEnabled(true);
                    }
                    else {
                        btnSignUp.setEnabled(false);
                    }
                }
                else {
                    btnSignUp.setEnabled(false);
                }
            }
            else {
                btnSignUp.setEnabled(false);
            }
        }
        else {
            btnSignUp.setEnabled(false);
        }
    }

    private void checkEmailAndPassword() {
        if(email.getText().toString().matches(emailPattern)) {
            if(password.getText().toString().equals(conformPassword.getText().toString())) {

                btnSignUp.setEnabled(false);
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Map<Object, String> userData = new HashMap<>();
                                    userData.put("FullName", fullName.getText().toString());
                                    userData.put("Email", email.getText().toString());
                                    userData.put("PhoneNo", phoneNo.getText().toString());
                                    userData.put("Address", fullAddress.getText().toString());
                                    DocumentReference documentReference = firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getActivity(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(mainIntent);
                                            getActivity().finish();
                                        }
                                    });
                                }
                                else {
                                    btnSignUp.setEnabled(true);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else {
                conformPassword.setError("Password doesn't match");
            }
        }
        else {
            email.setError("Invalid email");
        }
    }
}