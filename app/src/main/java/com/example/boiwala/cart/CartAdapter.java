package com.example.boiwala.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.boiwala.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private CartClickedListeners cartClickedListeners;
    private List<Book> bookCartList;

    public CartAdapter(CartClickedListeners cartClickedListeners) {
        this.cartClickedListeners = cartClickedListeners;
    }

    public void setBookCartList(List<Book> bookCartList) {
        this.bookCartList = bookCartList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Book bookCart = bookCartList.get(position);
        holder.setImage(bookCart.getBookImage());
        //holder.bookImage.setImageResource(bookCart.getBookImage());
        holder.bookName.setText(bookCart.getBookName());
        holder.bookQuantity.setText(bookCart.getQuantity() + "");
        holder.bookPrice.setText(bookCart.getTotalPrice() + "");

        holder.btnDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartClickedListeners.onDeleteClicked(bookCart);
            }
        });


        holder.addQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartClickedListeners.onPlusClicked(bookCart);
            }
        });

        holder.minusQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartClickedListeners.onMinusClicked(bookCart);
            }
        });


    }

    @Override
    public int getItemCount() {
        if(bookCartList == null) {
            return 0;
        }
        else {
            return bookCartList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName, bookPrice, bookQuantity;
        private ImageView btnDeleteBook;
        private ImageView bookImage;
        private ImageButton addQuantityBtn, minusQuantityBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.eachCartItemName);
            bookPrice = itemView.findViewById(R.id.eachCartItemPriceTv);
            btnDeleteBook = itemView.findViewById(R.id.eachCartItemDeleteBtn);
            bookImage = itemView.findViewById(R.id.eachCartItemIV);
            bookQuantity = itemView.findViewById(R.id.eachCartItemQuantityTV);
            addQuantityBtn = itemView.findViewById(R.id.eachCartItemAddQuantityBtn);
            minusQuantityBtn = itemView.findViewById(R.id.eachCartItemMinusQuantityBtn);
        }

        void setImage(String imageUrl) {
            Glide.with(itemView.getContext()).load(imageUrl).apply(new RequestOptions().placeholder(R.drawable.book_icon)).into(bookImage);
        }
    }

    public interface CartClickedListeners {
        void onDeleteClicked(Book bookCart);

        void onPlusClicked(Book bookCart);

        void onMinusClicked(Book bookCart);
    }
}
