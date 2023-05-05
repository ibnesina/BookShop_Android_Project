package com.example.boiwala.cart;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CartDAO {

    @Insert
    void insertCartItem(Book book);

    @Query("SELECT * FROM bookTable")
    LiveData<List<Book>> getAllCartItems();

    @Delete
    void deleteCartItem(Book book);

    @Query("UPDATE bookTable SET quantity=:quantity WHERE id=:id")
    void updateQuantity(int id , int quantity);

    @Query("UPDATE bookTable SET totalPrice=:totalItemPrice WHERE id=:id")
    void updatePrice(int id , double totalItemPrice);

    @Query("DELETE FROM bookTable")
    void deleteAllItems();

}
