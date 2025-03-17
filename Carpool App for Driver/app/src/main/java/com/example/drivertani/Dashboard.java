package com.example.drivertani;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {
    Button bookingBtn;
    Button myTripsBtn;
    Button MyProfileBtn;
    Intent bookingIntent;
    Intent TripsIntent;
    Intent  MyProfileIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bookingBtn = (Button)findViewById(R.id.Booking);
        myTripsBtn = (Button)findViewById(R.id.MyTrips);
        MyProfileBtn = (Button)findViewById(R.id.MyProfileButton);
        bookingIntent = new Intent(this, PreBooking.class);
        TripsIntent = new Intent(this, DriverTrips.class);
        MyProfileIntent = getIntent();
        String email = MyProfileIntent.getStringExtra("email");
        MyProfileIntent = new Intent(this, MyProfile.class);
        bookingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bookingIntent.putExtra("email",email);
                startActivity(bookingIntent);
            }
        });

        myTripsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TripsIntent.putExtra("email",email);
                startActivity(TripsIntent);
            }
        });

        MyProfileBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                MyProfileIntent.putExtra("email",email);
                startActivity(MyProfileIntent);
            }
        });
    }
}