package com.example.boiwala;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolder> {
    private List<String> productDescriptions;

    public ProductDetailsAdapter(List<String> productDescription) {
        this.productDescriptions = productDescription;
    }

    @NonNull
    @Override
    public ProductDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_description_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailsAdapter.ViewHolder holder, int position) {
        holder.description.setText(productDescriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return productDescriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView description, btnCart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.product_details_viewPager);
            btnCart = itemView.findViewById(R.id.btnCart);
        }
    }

    public interface BookClickListener {
        void onAddToCartBtnClicked();
    }
}
