package com.example.boiwala;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder> {
    private List<MyOrderItemModel> myOrderItemModelList;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        String resource = myOrderItemModelList.get(position).getProductImage();
        String title = myOrderItemModelList.get(position).getProductTitle();
        String orderDate = myOrderItemModelList.get(position).getDeliveryStatus();
        String priceAndQuantity = myOrderItemModelList.get(position).getTotalPriceAndQuantity();

        holder.setData(resource, title, orderDate, priceAndQuantity);
    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        ImageView productImage, orderIndicator;
        TextView productTitle, orderDate, productPriceAndQuantity;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.orderProductImage);
            productTitle = itemView.findViewById(R.id.orderProductTitle);
            orderIndicator = itemView.findViewById(R.id.orderIndicator);
            orderDate = itemView.findViewById(R.id.orderDate);
            productPriceAndQuantity = itemView.findViewById(R.id.priceAndQuantity);

        }

        private void setData(String resource, String title, String deliveryDate, String priceAndQuantity) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.book_icon)).into(productImage);
            productTitle.setText(title);
            orderDate.setText(deliveryDate);
            productPriceAndQuantity.setText(priceAndQuantity);
        }
    }
}
