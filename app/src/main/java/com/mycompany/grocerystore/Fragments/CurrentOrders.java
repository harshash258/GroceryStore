package com.mycompany.grocerystore.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.mycompany.grocerystore.Models.OrderUserDetail;
import com.mycompany.grocerystore.R;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrders extends Fragment {

    FirebaseUser user;
    DatabaseReference databaseReference;
    ProgressBar progressBar;

    List<OrderUserDetail> orderList;
    RecyclerView recyclerView;
    CurrentOrderAdapter currentOrderAdapter;
    String userID;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_current_order, container, false);

        progressBar = view.findViewById(R.id.order_ProgressBar);
        recyclerView = view.findViewById(R.id.order_RecyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User Order").
                child("Current Order").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    OrderUserDetail orderDetails = childSnapshot.child("Order Details").getValue(OrderUserDetail.class);
                    orderList.add(orderDetails);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                currentOrderAdapter = new CurrentOrderAdapter(getContext(), orderList);
                recyclerView.setAdapter(currentOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }


}
