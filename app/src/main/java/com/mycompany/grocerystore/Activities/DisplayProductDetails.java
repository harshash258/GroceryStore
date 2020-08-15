package com.mycompany.grocerystore.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystore.Adapter.ProductAdapter;
import com.mycompany.grocerystore.Models.Cart;
import com.mycompany.grocerystore.Models.ProductInfo;
import com.mycompany.grocerystore.R;

public class DisplayProductDetails extends AppCompatActivity {

    ImageView productImage;
    TextView productName, productDesc, productPrice;
    Button addtocart;
    ElegantNumberButton button;

    int quantity = 1;
    String userID;
    String name, desc, price, url, id, cartID;

    DatabaseReference reference, cartReference, checkreference;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product_details);

        productImage = findViewById(R.id.display_ProductImage);
        productName = findViewById(R.id.display_ProductName);
        productDesc = findViewById(R.id.display_ProductDesc);
        productPrice = findViewById(R.id.display_ProductPrice);
        addtocart = findViewById(R.id.display_AddtoCart);
        button = findViewById(R.id.enb);


        button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                quantity = newValue;
            }
        });

        Intent intent = getIntent();
        id = intent.getStringExtra(ProductAdapter.PRODUCT_ID);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();


        reference = FirebaseDatabase.getInstance().getReference().child("Products").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProductInfo info = dataSnapshot.getValue(ProductInfo.class);

                name = info.getProduct_name();
                desc = info.getProduct_description();
                price = info.getProduct_price();
                url = info.getImage_url();
                Glide.with(DisplayProductDetails.this)
                        .load(info.getImage_url())
                        .placeholder(R.drawable.loading)
                        .fitCenter()
                        .into(productImage);

                productName.setText(info.getProduct_name());
                productDesc.setText(info.getProduct_description());
                productPrice.setText("₹." + info.getProduct_price());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToCart();
            }
        });
    }

    private void addProductToCart() {
        cartReference = FirebaseDatabase.getInstance().getReference().child("Product In Cart");
        cartID = cartReference.push().getKey();
        final Cart cart = new Cart(name, quantity, price, url, id);
        checkreference = FirebaseDatabase.getInstance().getReference().child("Product In Cart")
                .child(userID).child("Products").child(id);
        checkreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(DisplayProductDetails.this, "Already in Cart", Toast.LENGTH_LONG).show();
                } else {
                    cartReference.child(userID).child("Products").child(id).setValue(cart);
                    Toast.makeText(DisplayProductDetails.this, "Added to Cart", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DisplayProductDetails.this, NavigationMenu.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}