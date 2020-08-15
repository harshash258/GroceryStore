package com.mycompany.grocerystore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mycompany.grocerystore.Models.Cart;
import com.mycompany.grocerystore.R;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    Context mContext;
    List<Cart> mList;

    public OrderDetailsAdapter(Context mContext, List<Cart> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart info = mList.get(position);
        holder.name.setText(info.getName());
        holder.price.setText(info.getPrice());
        holder.qty.setText(String.valueOf(info.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Orderproduct_name);
            price = itemView.findViewById(R.id.Orderproduct_price);
            qty = itemView.findViewById(R.id.orderProductqty);
        }
    }
}
