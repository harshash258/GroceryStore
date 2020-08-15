package com.mycompany.grocerystore.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.hbb20.CountryCodePicker;
import com.mycompany.grocerystore.R;

public class RegisterPhoneNumber extends AppCompatActivity {

    public static final String PHONE_NUMBER = "phoneNumber";
    CountryCodePicker ccp;
    EditText phoneNumber;
    Button submit;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ccp = findViewById(R.id.ccp);
        phoneNumber = findViewById(R.id.editText_PhoneNumber);
        submit = findViewById(R.id.submit);

        ccp.registerCarrierNumberEditText(phoneNumber);
        builder = new AlertDialog.Builder(RegisterPhoneNumber.this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPhoneNumber.this);
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
                } else {
                    sendPhoneNumber();
                }
            }
        });
    }

    private void sendPhoneNumber() {
        String number = phoneNumber.getText().toString().trim();

        if (number.isEmpty() || number.length() < 10) {

            builder.setTitle("Invalid Phone Number");
            builder.setMessage("Please Enter a valid Phone Number");
            builder.setCancelable(false);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            final Intent intent = new Intent(RegisterPhoneNumber.this, VerifyPhoneNumber.class);
            intent.putExtra(PHONE_NUMBER, ccp.getFullNumberWithPlus());
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Connecting");
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progress.cancel();
                    startActivity(intent);
                }
            };
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 3000);
        }

    }

}

