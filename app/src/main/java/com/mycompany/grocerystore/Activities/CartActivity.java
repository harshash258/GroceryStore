package com.mycompany.grocerystore.Activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystore.Adapter.CartAdapter;
import com.mycompany.grocerystore.Models.Cart;
import com.mycompany.grocerystore.Models.OrderUserDetail;
import com.mycompany.grocerystore.Models.User;
import com.mycompany.grocerystore.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Cart> mList;
    CartAdapter cartAdapter;
    DatabaseReference databaseReference, adminOrder, adminOrderDetails, userOrder, profile,
            userDetail, notification;
    FirebaseUser firebaseUser;


    String userId;
    String orderId;
    String consumerName;
    String currentDate;
    String currentTime, phoneNumber;
    Button placeOrder;
    int size, Totalprice;
    TextView totalPrice;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intialisevariable();


        profile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                consumerName = user1.getUserName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mList.clear();
                size = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    Cart cart = post.getValue(Cart.class);
                    int price = Integer.parseInt(cart.getPrice()) * cart.getQuantity();
                    Totalprice += price;
                    mList.add(cart);
                    totalPrice.setText("Total Price: " + String.valueOf(Totalprice));
                }
                cartAdapter = new CartAdapter(CartActivity.this, mList);
                recyclerView.setAdapter(cartAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        currentDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a ");
        currentTime = timeFormat.format(calendar.getTime());

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (size <= 0) {
                    placeOrder.setEnabled(false);
                    placeOrder.setBackgroundColor(Color.parseColor("#C0C0C0"));
                    Toast.makeText(CartActivity.this, "No Item in Cart", Toast.LENGTH_SHORT).show();
                }
                else {
                    notify = true;
                    placeOrder.setEnabled(true);
                    placeOrder.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                    placeOrder(databaseReference, adminOrder);
                    placeOrder(databaseReference, userOrder);
                    databaseReference.removeValue();

                    OrderUserDetail details = new OrderUserDetail(orderId, consumerName, userId,
                            currentDate, currentTime, Totalprice, phoneNumber);
                    userDetail.setValue(details);
                    adminOrderDetails.setValue(details);
                    HashMap<String, String> notifications = new HashMap<>();
                    notifications.put("from", userId);
                    notifications.put("type", "Order");
                    notification.child("8fosEY4xsyOIfetCbFsyURKqunO2").push().setValue(notifications);

                }
            }
        });

    }

    private void Intialisevariable() {

        mList = new ArrayList<>();

        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placeOrder = findViewById(R.id.btn_PlaceOrder);
        totalPrice = findViewById(R.id.totalPrice);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product In Cart").
                child(userId).child("Products");

        orderId = databaseReference.push().getKey();

        adminOrder = FirebaseDatabase.getInstance().getReference().child("Admin Order")
                .child("Current Order").child(orderId).child("Products");

        adminOrderDetails = FirebaseDatabase.getInstance().getReference().child("Admin Order")
                .child("Current Order").child(orderId).child("Order Details");

        userOrder = FirebaseDatabase.getInstance().getReference().child("User Order")
                .child("Current Order").child(userId).child(orderId).child("Products");

        userDetail = FirebaseDatabase.getInstance().getReference().child("User Order").
                child("Current Order").child(userId).child(orderId).child("Order Details");

        profile = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        notification = FirebaseDatabase.getInstance().getReference().child("Notifications");
    }

    private void placeOrder(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            Toast.makeText(CartActivity.this, "Error in Placing Order", Toast.LENGTH_LONG).show();
                        } else {
                            final ProgressDialog progress = new ProgressDialog(CartActivity.this);
                            progress.setTitle("Placing Order");
                            progress.setMessage("Please wait...");
                            progress.show();
                            Runnable progressRunnable = new Runnable() {
                                @Override
                                public void run() {

                                    progress.cancel();
                                    Toast.makeText(CartActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                }
                            };
                            Handler pdCanceller = new Handler();
                            pdCanceller.postDelayed(progressRunnable, 2000);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}