package com.example.boiwala.cart;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartRepo {
    private CartDAO cartDAO;
    private LiveData<List<Book>> allCartItemsLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<List<Book>> getAllCartItemsLiveData() {
        return allCartItemsLiveData;
    }

    public CartRepo(Application application){
        cartDAO = CartDatabase.getInstance(application).cartDAO();
        allCartItemsLiveData = cartDAO.getAllCartItems();
    }

    public void insertCartItem(Book book){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.insertCartItem(book);
            }
        });
    }

    public void deleteCartItem(Book book){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.deleteCartItem(book);
            }
        });
    }

    public void updateQuantity(int id , int quantity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.updateQuantity(id, quantity);
            }
        });
    }

    public void updatePrice(int id , double price){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.updatePrice(id , price);
            }
        });
    }

    public void deleteAllCartItems(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.deleteAllItems();
            }
        });
    }

}
