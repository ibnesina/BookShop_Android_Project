package com.example.boiwala.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiwala.MyOrderAdapter;
import com.example.boiwala.MyOrderItemModel;
import com.example.boiwala.OrderedBooks;
import com.example.boiwala.R;
import com.example.boiwala.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private RecyclerView myOrderRecyclerView;
    private DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_order,container,false);
        myOrderRecyclerView = view.findViewById(R.id.myOrdersRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrderRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrderRecyclerView.setAdapter(myOrderAdapter);

        reference = FirebaseDatabase.getInstance().getReference().child("Orders");

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        OrderedBooks book = dataSnapshot.getValue(OrderedBooks.class);
                        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(book.getUserId())) {
                            myOrderItemModelList.add(new MyOrderItemModel(book.getBookImage(), book.getBookName(), book.getPurchaseDate(), "Tk. " + book.getBookPrice() + " x " + book.getQuantity()));
                        }
                        myOrderAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivity(intent);
            getActivity().finish();
            Toast.makeText(getActivity(), "Please SignIn First", Toast.LENGTH_SHORT).show();
        }





//        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.book_cover_1, "কবিতা সংগ্রহ", "Delivered on 14th december, 2022"));
//        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.book_cover_2, "কবিতা সংগ্রহ", "Canceled"));
//        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.book_cover_3, "কবিতা সংগ্রহ", "Delivered on 14th december, 2022"));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}