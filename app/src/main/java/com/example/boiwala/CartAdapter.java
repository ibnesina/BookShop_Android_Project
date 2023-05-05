package com.example.boiwala;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {
    private List<CartIemModel> cartIemModelList;

    public CartAdapter(List<CartIemModel> cartIemModelList) {
        this.cartIemModelList = cartIemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartIemModelList.get(position).getType()) {
            case 0:
                return CartIemModel.CART_ITEM;
            case 1:
                return CartIemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CartIemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartItemViewHolder(cartItemView);
            case CartIemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_ammount_layout, parent, false);
                return new CartTotalAmountViewHolder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         switch (cartIemModelList.get(position).getType()) {
             case CartIemModel.CART_ITEM:
                 int resource = cartIemModelList.get(position).getProductImage();
                 String title = cartIemModelList.get(position).getProductTitle();
                 String productPrice = cartIemModelList.get(position).getProductPrice();

                 ((CartItemViewHolder)holder).setItemDetails(resource, title, productPrice);
                 break;

             case CartIemModel.TOTAL_AMOUNT:
                 String totalItems = cartIemModelList.get(position).getTotalItems();
                 String totalItemPrice = cartIemModelList.get(position).getTotalItemPrice();
                 String deliveryPrice = cartIemModelList.get(position).getDeliveryPrice();
                 String totalAmount = cartIemModelList.get(position).getTotalAmount();

                 ((CartTotalAmountViewHolder)holder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount);

                 break;
             default:
                 return;
         }
    }

    @Override
    public int getItemCount() {
        return cartIemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private TextView productTitle, productPrice, productQuantity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.cartProductImage);
            productTitle = itemView.findViewById(R.id.cartProductTitle);
            productPrice = itemView.findViewById(R.id.cartProductPrice);
            productQuantity = itemView.findViewById(R.id.cartProductQuantity);
        }

        private void setItemDetails(int resource, String title, String productPriceText) {
            productImage.setImageResource(resource);
            productTitle.setText(title);
            productPrice.setText(productPriceText);
        }
    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView totalItems, totalItemPrice, deliveryPrice, totalAmount;

        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.totalItems);
            totalItemPrice = itemView.findViewById(R.id.totalItemsPrice);
            deliveryPrice = itemView.findViewById(R.id.deliveryPrice);
            totalAmount = itemView.findViewById(R.id.totalPrice);
        }

        private void setTotalAmount(String totalItemText, String totalItemPriceText, String deliveryPriceText, String totalAmountText) {

            totalItems.setText(totalItemText);
            totalItemPrice.setText(totalItemPriceText);
            deliveryPrice.setText(deliveryPriceText);
            totalAmount.setText(totalAmountText);
        }
    }
}
