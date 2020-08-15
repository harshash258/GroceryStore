package com.mycompany.grocerystore.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystore.Models.About;
import com.mycompany.grocerystore.R;

public class AboutTheShop extends Fragment {

    TextView name, address, email, phone;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_about, container, false);
        name = v.findViewById(R.id.shopName);
        address = v.findViewById(R.id.shopAddress);
        email = v.findViewById(R.id.shopEmail);
        phone = v.findViewById(R.id.shopPhone);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("About Shop");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                About shop = dataSnapshot.getValue(About.class);

                name.setText(shop.getName());
                address.setText(shop.getAddress());
                email.setText(shop.getEmail());
                phone.setText(shop.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }
}
