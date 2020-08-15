package com.mycompany.grocerystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mycompany.grocerystore.Activities.DisplayProductDetails;
import com.mycompany.grocerystore.Models.ProductInfo;
import com.mycompany.grocerystore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public static final String PRODUCT_ID = "productID";
    Context mContext;
    List<ProductInfo> mList;

    public ProductAdapter(Context mContext, List<ProductInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product_display, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final ProductInfo info = mList.get(position);
        holder.productName.setText(info.getProduct_name());
        holder.productPrice.setText("₹." + info.getProduct_price());
        Picasso.get()
                .load(info.getImage_url())
                .fit()
                .centerCrop()
                .into(holder.productImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DisplayProductDetails.class);
                intent.putExtra(PRODUCT_ID, info.getProductID());
                mContext.startActivity(intent);

            }
        });
}

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
