package com.example.boiwala.cart;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Book.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    private static CartDatabase instance;

    public static synchronized CartDatabase getInstance(Context context) {
        if(instance==null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CartDatabase.class, "BookDatabase")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
