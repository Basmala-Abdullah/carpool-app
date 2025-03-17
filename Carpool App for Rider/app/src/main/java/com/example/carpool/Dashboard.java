package com.example.carpool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {
    Button bookingBtn;
    Button myTripsBtn;
    Button MyProfileBtn;
    Intent bookingIntent;
    Intent myTripsIntent;
    Intent  AvailableRoutesIntent;
    Intent  MyProfileIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bookingBtn = (Button)findViewById(R.id.Booking);
        myTripsBtn = (Button)findViewById(R.id.MyTrips);
        MyProfileBtn = (Button)findViewById(R.id.MyProfileButton);
        bookingIntent = new Intent(this, BookingActivity.class);
        myTripsIntent = new Intent(this, TempMyTrips.class);
        AvailableRoutesIntent = new Intent(this, AvailableRouts.class);
        MyProfileIntent = getIntent();
        String email = MyProfileIntent.getStringExtra("email");
        MyProfileIntent = new Intent(this, MyProfile.class);
        bookingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AvailableRoutesIntent.putExtra("email",email);
                startActivity(AvailableRoutesIntent);
            }
        });

        myTripsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myTripsIntent.putExtra("email",email);
                startActivity(myTripsIntent);
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