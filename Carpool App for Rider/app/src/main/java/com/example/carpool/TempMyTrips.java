package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TempMyTrips extends AppCompatActivity {
    DatabaseReference requestsDatabaseReference;
    ArrayList<String> requests;
    Intent getRiderrMail;
    String email;
    Intent toRealMyTrips;
    Button UpcomingTripsBtn;
    Button InProgressTripsBtn;
    Button DoneTripsBtn;
    Button PendingTripsBtn;
    Button CancelledTripsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_my_trips);
        getRiderrMail = getIntent();
        email = getRiderrMail.getStringExtra("email");
        String sanitizedPath = email.replace("@eng.asu.edu.eg", "");
        requestsDatabaseReference = FirebaseDatabase.getInstance().getReference("Requests").child(sanitizedPath);

        requests = new ArrayList<>();
        requestsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Handle requests data
                for (DataSnapshot requestsSnapshot : snapshot.getChildren()) {
                    String rideID = requestsSnapshot.getValue().toString();
                    requests.add(rideID);
                    Log.d("MyTrips", "1Requests data: " + requests.toString());
                }
                // Update the adapter
                //updateAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


        toRealMyTrips = new Intent(this,myTrips.class);
        toRealMyTrips.putExtra("email",email);
        toRealMyTrips.putExtra("requestList",requests);
        UpcomingTripsBtn=findViewById(R.id.UpcomingTripsBtn);
        PendingTripsBtn = findViewById(R.id.PendingTripsBtn);
        DoneTripsBtn = findViewById(R.id.DoneTripsBtn);
        InProgressTripsBtn = findViewById(R.id.InProgressTripsBtn);
        CancelledTripsBtn = findViewById(R.id.CancelledTripsBtn);
        UpcomingTripsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRealMyTrips.putExtra("TripType", "Upcoming");
                startActivity(toRealMyTrips);
            }
        });
        PendingTripsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRealMyTrips.putExtra("TripType", "Pending");
                startActivity(toRealMyTrips);
            }
        });
        //
        DoneTripsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRealMyTrips.putExtra("TripType", "Done");
                startActivity(toRealMyTrips);
            }
        });
        //
        InProgressTripsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRealMyTrips.putExtra("TripType", "InProgress");
                startActivity(toRealMyTrips);
            }
        });
        //
        CancelledTripsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRealMyTrips.putExtra("TripType", "Cancelled");
                startActivity(toRealMyTrips);
            }
        });


    }
}