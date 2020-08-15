package com.mycompany.grocerystore.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystore.Fragments.AboutTheShop;
import com.mycompany.grocerystore.Fragments.CurrentOrders;
import com.mycompany.grocerystore.Fragments.Products;
import com.mycompany.grocerystore.Fragments.Profile;
import com.mycompany.grocerystore.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationMenu extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    TextView username, userEmail, cartCount;
    CircleImageView userImage;
    DatabaseReference reference;
    FirebaseUser user;
    String userId;
    ImageButton cart;
    int size;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_side_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer);
        cartCount = findViewById(R.id.cartCount);
        NavigationView navigationView = findViewById(R.id.navigation);
        cart = findViewById(R.id.cart);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Product In Cart").
                child(userId).child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();
                cartCount.setText(String.valueOf(size));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.userName);
        userEmail = headerView.findViewById(R.id.userEmail);
        userImage = headerView.findViewById(R.id.profile_picture);

        updateHeaderInfo();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_open, R.string.navigation_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                    new Products()).commit();
            navigationView.setCheckedItem(R.id.nav_products);
        }

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationMenu.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_products:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                        new Products()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                        new Profile()).addToBackStack(null).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                        new AboutTheShop()).addToBackStack(null).commit();
                break;
            case R.id.nav_currentOrder:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                        new CurrentOrders()).addToBackStack(null).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateHeaderInfo() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("userName").getValue(String.class);
                    String Email = dataSnapshot.child("userEmail").getValue(String.class);
                    String URL = dataSnapshot.child("profilePhotoUrl").getValue(String.class);
                    username.setText(name);
                    userEmail.setText(Email);
                    if (URL.equals(""))
                        userImage.setImageResource(R.drawable.user);
                    else
                        Picasso.get().load(URL).
                                centerCrop().
                                fit().into(userImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}