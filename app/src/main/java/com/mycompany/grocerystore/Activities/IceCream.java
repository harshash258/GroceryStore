package com.mycompany.grocerystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystore.Adapter.ProductAdapter;
import com.mycompany.grocerystore.Models.ProductInfo;
import com.mycompany.grocerystore.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IceCream extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ProductInfo> productsList, searchList;
    ProductAdapter adapter;
    EditText search;
    ImageButton btnsearch;
    String productName;
    DatabaseReference databaseReference;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ice_cream);
        checkInternetConnectivity();

        recyclerView = findViewById(R.id.cookingRecycler);
        search = findViewById(R.id.editText_search);
        btnsearch = findViewById(R.id.btn_search);
        bar = findViewById(R.id.progressBar);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsList = new ArrayList<>();
        searchList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        Query query = databaseReference.orderByChild("category").equalTo("Ice-Cream");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsList.clear();
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    ProductInfo info = post.getValue(ProductInfo.class);
                    productsList.add(info);
                    Collections.reverse(productsList);
                    bar.setVisibility(View.INVISIBLE);
                }
                adapter = new ProductAdapter(IceCream.this, productsList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                bar.setVisibility(View.INVISIBLE);
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = search.getText().toString().trim();
                searchProducts(productName);
            }
        });
    }

    private void searchProducts(String productName) {
        Query query = databaseReference.orderByChild("product_name").startAt(productName).endAt(productName + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchList.clear();
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    ProductInfo info = post.getValue(ProductInfo.class);
                    searchList.add(info);
                }
                adapter = new ProductAdapter(IceCream.this, searchList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void checkInternetConnectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.check_internet, null);
            builder.setView(view);
            Button button = view.findViewById(R.id.refreshButton);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
        }
    }

}