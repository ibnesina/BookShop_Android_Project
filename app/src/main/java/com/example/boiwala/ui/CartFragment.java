package com.example.boiwala.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiwala.CartAdapter;
import com.example.boiwala.CartIemModel;
import com.example.boiwala.R;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    private RecyclerView cartItemRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartItemRecyclerView = view.findViewById(R.id.cartItemsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemRecyclerView.setLayoutManager(layoutManager);

        List<CartIemModel> cartIemModelList = new ArrayList<>();
        cartIemModelList.add(new CartIemModel(0, R.drawable.book_cover_1, 1, "কবিতা সংগ্রহ", "Tk.499"));
        cartIemModelList.add(new CartIemModel(0, R.drawable.book_cover_2, 1, "কবিতা সংগ্রহ", "Tk.499"));
        cartIemModelList.add(new CartIemModel(0, R.drawable.book_cover_3, 1, "কবিতা সংগ্রহ", "Tk.499"));

        cartIemModelList.add(new CartIemModel(1, "Price (3 items)", "Tk.1497", "Tk.1497", "Free"));

        CartAdapter cartAdapter = new CartAdapter(cartIemModelList);
        cartItemRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}