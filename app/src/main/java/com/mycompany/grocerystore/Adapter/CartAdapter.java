package com.mycompany.grocerystore.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystore.Models.Cart;
import com.mycompany.grocerystore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context mContext;
    List<Cart> mList;
    int totalPrice;
    int qty=1;
    FirebaseUser user;
    String userID, name, price, imageURL, productId;

    public CartAdapter(Context mContext, List<Cart> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.display_cart_items, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        final Cart order = mList.get(position);
        holder.productName.setText(order.getName());
        holder.productPrice.setText("₹. " + order.getPrice());
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        holder.productQuantity.setText("Quantity: " + Integer.toString(order.getQuantity()));
        totalPrice = Integer.parseInt(order.getPrice()) * order.getQuantity();
        holder.totalPrice.setText("Total: " + String.valueOf(totalPrice));
        Picasso.get().load(order.getImageURL()).fit().centerCrop().into(holder.productImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Edit",
                        "Delete"
                };
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                            View view = LayoutInflater.from(mContext).inflate(R.layout.edit_quantity, null);
                            builder1.setView(view);
                            final AlertDialog dialog1 = builder1.create();
                            dialog1.show();
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Product In Cart").
                                    child(userID).child("Products").child(order.getProductId());
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Cart cart = dataSnapshot.getValue(Cart.class);
                                    name = cart.getName();
                                    imageURL = cart.getImageURL();
                                    price = cart.getPrice();
                                    productId = cart.getProductId();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            ElegantNumberButton numberButton = view.findViewById(R.id.enb2);
                            Button button = view.findViewById(R.id.ok);

                            numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                @Override
                                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                    qty = newValue;
                                }
                            });
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Cart cart = new Cart(name, qty, price, imageURL, productId);
                                    databaseReference.setValue(cart);
                                    Toast.makeText(mContext, "Quantity Updated", Toast.LENGTH_SHORT).show();
                                    dialog1.dismiss();
                                }
                            });

                        }
                        else if (which == 1){
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Product In Cart").
                                    child(userID).child("Products").child(order.getProductId());
                            databaseReference.removeValue();
                            Toast.makeText(mContext, "Product Deleted", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }

                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, productQuantity, productPrice, totalPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cartProductImage);
            productName = itemView.findViewById(R.id.cartProductName);
            productQuantity = itemView.findViewById(R.id.cart_Quantity);
            productPrice = itemView.findViewById(R.id.cartProductPrice);
            totalPrice = itemView.findViewById(R.id.cartTotalPrice);
        }
    }
}
