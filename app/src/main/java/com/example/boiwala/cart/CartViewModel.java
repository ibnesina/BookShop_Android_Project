package com.example.boiwala.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private CartRepo cartRepo;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepo = new CartRepo(application);
    }

    public LiveData<List<Book>> getAllCartItems() {
        return cartRepo.getAllCartItemsLiveData();
    }

    public void insertCartItem(Book book) {
        cartRepo.insertCartItem(book);
    }

    public void updateQuantity(int id, int quantity) {
        cartRepo.updateQuantity(id, quantity);
    }

    public void updatePrice(int id, double price) {
        cartRepo.updatePrice(id, price);
    }

    public void deleteCartItem(Book book) {
        cartRepo.deleteCartItem(book);
    }

    public void deleteAllCartItems() {
        cartRepo.deleteAllCartItems();
    }
}
