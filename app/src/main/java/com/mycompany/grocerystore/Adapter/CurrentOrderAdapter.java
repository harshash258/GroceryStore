package com.mycompany.grocerystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mycompany.grocerystore.Activities.DisplayOrderDetails;
import com.mycompany.grocerystore.Models.OrderUserDetail;
import com.mycompany.grocerystore.R;

import java.util.List;

public class CurrentOrderAdapter extends RecyclerView.Adapter<CurrentOrderAdapter.Viewholder> {
    Context mContext;
    List<OrderUserDetail> mList;
    public static final String ORDER_ID = "orderid";

    public CurrentOrderAdapter(Context mContext, List<OrderUserDetail> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.previous_order, parent, false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        final OrderUserDetail details = mList.get(position);
        holder.orderID.setText("OrderId: " + details.getOrderId());
        holder.username.setText("Name:" + details.getConsumer());
        holder.total.setText("Total:" + details.getTotal());
        final String orderID = details.getOrderId();
        holder.orderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DisplayOrderDetails.class);
                intent.putExtra(ORDER_ID, orderID);
                intent.putExtra("date", details.getDate());
                intent.putExtra("time", details.getTime());
                intent.putExtra("total",String.valueOf(details.getTotal()));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class Viewholder extends RecyclerView.ViewHolder {
        TextView orderID, username, total;
        Button orderDetails;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderId);
            username = itemView.findViewById(R.id.order_UserName);
            total = itemView.findViewById(R.id.order_totalPrice);
            orderDetails = itemView.findViewById(R.id.orderDetails);
        }
    }
}
