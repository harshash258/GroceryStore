package com.mycompany.grocerystore.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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
import com.mycompany.grocerystore.Adapter.CurrentOrderAdapter;
import com.mycompany.grocerystore.Adapter.OrderDetailsAdapter;
import com.mycompany.grocerystore.Models.Cart;
import com.mycompany.grocerystore.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayOrderDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Cart> list;
    OrderDetailsAdapter adapter;
    DatabaseReference databaseReference;
    TextView date, time, orderiD, totalPrice;
    String userId;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order_details);

        recyclerView = findViewById(R.id.recyclerView);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        orderiD = findViewById(R.id.orderId);
        totalPrice = findViewById(R.id.totalPrice);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        Intent intent = getIntent();
        String id = intent.getStringExtra(CurrentOrderAdapter.ORDER_ID);
        String dates = intent.getStringExtra("date");
        String times = intent.getStringExtra("time");
        String total = intent.getStringExtra("total");
        orderiD.setText(id);
        date.setText(dates);
        time.setText(times);
        totalPrice.setText("Total: " + total);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("User Order")
                .child("Current Order").child(userId).child(id).child("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Cart products = child.getValue(Cart.class);
                    list.add(products);
                }
                adapter = new OrderDetailsAdapter(DisplayOrderDetails.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}