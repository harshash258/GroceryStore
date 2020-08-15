package com.mycompany.grocerystore.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mycompany.grocerystore.Models.User;
import com.mycompany.grocerystore.R;

public class UploadUserDetails extends AppCompatActivity {

    EditText firstName, lastName;
    Button upload;

    FirebaseUser user;
    DatabaseReference databaseReference;

    String userId, phoneNumber, fName, lName, deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        firstName = findViewById(R.id.editText_firstName);
        lastName = findViewById(R.id.editText_lastName);
        upload = findViewById(R.id.upload);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        userId = user.getUid();
        phoneNumber = user.getPhoneNumber();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserInfo();
            }
        });

    }

    private void uploadUserInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(UploadUserDetails.this);
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
        else {
            deviceID = FirebaseInstanceId.getInstance().getToken();
            final ProgressDialog progress = new ProgressDialog(UploadUserDetails.this);
            progress.setTitle("Verifying");
            progress.setMessage("Please wait...");
            progress.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progress.cancel();
                    fName = firstName.getText().toString().trim();
                    lName = lastName.getText().toString().trim();
                    String fullName = fName + " " + lName;
                    User user = new User(fullName, userId, phoneNumber, "", "", deviceID);
                    databaseReference.child(userId).setValue(user);

                }
            };
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 3000);

            Intent intent = new Intent(UploadUserDetails.this, NavigationMenu.class);
            startActivity(intent);
        }
    }

}