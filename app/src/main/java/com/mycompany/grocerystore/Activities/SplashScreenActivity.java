package com.mycompany.grocerystore.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mycompany.grocerystore.R;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference db;
    private long ms = 0;
    private long splashTime = 2000;
    private boolean splashActive = true;
    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference().child("UserInfo");

        Thread mythread = new Thread() {
            public void run() {
                try {
                    while (splashActive && ms < splashTime) {
                        if (!paused)
                            ms = ms + 100;
                        sleep(100);
                    }
                } catch (Exception e) {
                } finally {
                    if (user != null) {
                        Intent intent = new Intent(SplashScreenActivity.this, NavigationMenu.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(SplashScreenActivity.this, RegisterPhoneNumber.class);
                        startActivity(intent);
                    }
                }
            }
        };
        mythread.start();
    }
    private void checkInternetConnectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.check_internet, null);
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
