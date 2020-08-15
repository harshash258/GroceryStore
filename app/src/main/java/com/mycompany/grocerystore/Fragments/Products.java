package com.mycompany.grocerystore.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystore.Activities.BeautyandBeast;
import com.mycompany.grocerystore.Activities.CookingEssentials;
import com.mycompany.grocerystore.Activities.DairyProduct;
import com.mycompany.grocerystore.Activities.HousingNeeds;
import com.mycompany.grocerystore.Activities.IceCream;
import com.mycompany.grocerystore.Activities.PackagedFood;
import com.mycompany.grocerystore.Adapter.SliderAdapter;
import com.mycompany.grocerystore.Models.SliderItem;
import com.mycompany.grocerystore.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class Products extends Fragment {


    List<SliderItem> sliderItemList;

    SliderAdapter sliderAdapter;
    DatabaseReference databaseReference2;

    SliderView sliderView;


    RelativeLayout rl1, rl2, rl3, rl4, rl5, rl6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_products, container, false);

        checkInternetConnectivity();


        sliderView = v.findViewById(R.id.imageSlider);
        rl1 = v.findViewById(R.id.container1);
        rl2 = v.findViewById(R.id.container2);
        rl3 = v.findViewById(R.id.container3);
        rl4 = v.findViewById(R.id.container4);
        rl5 = v.findViewById(R.id.container5);
        rl6 = v.findViewById(R.id.container6);
        sliderItemList = new ArrayList<>();



        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CookingEssentials.class);
                startActivity(intent);
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BeautyandBeast.class);
                startActivity(intent);
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DairyProduct.class);
                startActivity(intent);
            }
        });
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), IceCream.class);
                startActivity(intent);
            }
        });
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HousingNeeds.class);
                startActivity(intent);
            }
        });
        rl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PackagedFood.class);
                startActivity(intent);
            }
        });





        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Image Slider");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sliderItemList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SliderItem sliderItem = child.getValue(SliderItem.class);
                    sliderItemList.add(sliderItem);
                }
                sliderAdapter = new SliderAdapter(getContext(), sliderItemList);
                sliderView.setSliderAdapter(sliderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }


    private void checkInternetConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .detach(Products.this)
                            .attach(Products.this);
                }
            });
        } else {

        }
    }


}
