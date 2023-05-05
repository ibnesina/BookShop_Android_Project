package com.example.boiwala;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {
    List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, null);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(parent.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("Image", horizontalProductScrollModelList.get(position).getProductImage());
                    productDetailsIntent.putExtra("Price", horizontalProductScrollModelList.get(position).getProductPrice());
                    productDetailsIntent.putExtra("Title", horizontalProductScrollModelList.get(position).getProductTitle());
                    parent.getContext().startActivity(productDetailsIntent);
                }
            });

            ImageView productImage = view.findViewById(R.id.horizontalScrollBookImage);
            TextView productTitle = view.findViewById(R.id.horizontalScrollBookName);
            TextView productWriter = view.findViewById(R.id.horizontalScrollBookWriter);
            TextView productPrice = view.findViewById(R.id.horizontalScrollBookPrice);

            Glide.with(parent.getContext()).load(horizontalProductScrollModelList.get(position).getProductImage()).apply(new RequestOptions()).placeholder(R.drawable.book_cover_1).into(productImage);
            //productImage.setImageResource(horizontalProductScrollModelList.get(position).getProductImage());
            productTitle.setText(horizontalProductScrollModelList.get(position).getProductTitle());
            productWriter.setText(horizontalProductScrollModelList.get(position).getProductWritter());
            productPrice.setText(horizontalProductScrollModelList.get(position).getProductPrice());
        }
        else{
            view = convertView;
        }
        return view;
    }
}
