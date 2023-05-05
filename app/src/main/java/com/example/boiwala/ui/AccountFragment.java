package com.example.boiwala.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.boiwala.HorizontalProductScrollModel;
import com.example.boiwala.ImageModel;
import com.example.boiwala.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    private TextView userName, userEmail, userFullName, userPhone, userAddress;
    private CircleImageView profileImage;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userFullName = view.findViewById(R.id.userFullName);
        userPhone = view.findViewById(R.id.userPhoneNo);
        userAddress = view.findViewById(R.id.userAddress);
        profileImage = view.findViewById(R.id.profileImage);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ImageModel");

        if(currentUser!=null) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ImageModel imageModel = dataSnapshot.getValue(ImageModel.class);
                        String userId = imageModel.getUderId();
                        System.out.println(userId + "----------user");
                        if (userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            if (imageModel.getImageUrl() != null) {
                                Glide.with(getActivity()).load(imageModel.getImageUrl()).apply(new RequestOptions()).placeholder(R.drawable.profile_pic).into(profileImage);
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
                            if (task.isComplete()) {
                                DocumentSnapshot ds = task.getResult();
                                userName.setText(ds.getString("FullName"));
                                userFullName.setText(ds.getString("FullName"));
                                userEmail.setText(ds.getString("Email"));
                                userPhone.setText(ds.getString("PhoneNo"));
                                userAddress.setText(ds.getString("Address"));
                            }
                        }
                    });
        }
    }
}
