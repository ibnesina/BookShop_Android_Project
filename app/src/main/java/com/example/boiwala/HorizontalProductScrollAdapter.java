package com.example.boiwala;

import android.content.Context;
import android.content.Intent;
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

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    //String resource, title, name, price;

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder holder, int position) {
        String resource = horizontalProductScrollModelList.get(position).getProductImage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String name = horizontalProductScrollModelList.get(position).getProductWritter();
        String price = horizontalProductScrollModelList.get(position).getProductPrice();

        holder.setProductImage(resource);
        holder.setProductTitle(title);
        holder.setProductWritter(name);
        holder.setProductPrice(price);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.passIntentContent(resource, title, price);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalProductScrollModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private TextView productTitle;
        private TextView productWritter;
        private TextView productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.horizontalScrollBookImage);
            productTitle = itemView.findViewById(R.id.horizontalScrollBookName);
            productWritter = itemView.findViewById(R.id.horizontalScrollBookWriter);
            productPrice = itemView.findViewById(R.id.horizontalScrollBookPrice);
        }
        private void setProductImage(String resource) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions()).placeholder(R.drawable.book_cover_1).into(productImage);
        }
        private void setProductTitle(String title) {
            productTitle.setText(title);
        }
        private void setProductWritter(String name) {
            productWritter.setText(name);
        }
        private void setProductPrice(String price) {
            productPrice.setText(price);
        }

        private void passIntentContent(String resource, String title, String price) {
            Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
            productDetailsIntent.putExtra("Image", resource);
            productDetailsIntent.putExtra("Price", price);
            productDetailsIntent.putExtra("Title", title);
            itemView.getContext().startActivity(productDetailsIntent);
        }
    }

}
