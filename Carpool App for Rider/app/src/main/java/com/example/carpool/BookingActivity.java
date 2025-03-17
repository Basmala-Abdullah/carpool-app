package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class BookingActivity extends AppCompatActivity {
    Intent intent;
    TextView from,to,time,price,date,driverPhone,driverName;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        intent = getIntent();

        firebaseDatabase = FirebaseDatabase.getInstance("https://carpool-8a3a7-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference();

        from = findViewById(R.id.FromText);
        to  = findViewById(R.id.ToText);
        time = findViewById(R.id.AtText);
        price = findViewById(R.id.priceText);
        date= findViewById(R.id.dateText);
        driverName = findViewById(R.id.driverNameText);
        driverPhone= findViewById(R.id.driverPhoneText);
        submitBtn= findViewById(R.id.submitBtn);

        if(intent!=null){
            String receivedFrom = intent.getStringExtra("From");
            String receivedTo = intent.getStringExtra("To");
            String receivedTime = intent.getStringExtra("Time");
            String receivedPrice = intent.getStringExtra("Price");

            String receivedDate = intent.getStringExtra("Date");
            String receivedDriverName = intent.getStringExtra("Name");
            String receivedDriverPhone = intent.getStringExtra("Phone");
            String email = intent.getStringExtra("email");
            String rideID = intent.getStringExtra("rideID");

            from.setText(receivedFrom);
            to.setText(receivedTo);
            time.setText(receivedTime);
            price.setText(receivedPrice);

            date.setText(receivedDate);
            driverName.setText(receivedDriverName);
            driverPhone.setText(receivedDriverPhone);

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String reqKey = databaseReference.child("Requests").push().getKey();
                    //HashMap<String,Object> hashMap = new HashMap<>();
                    //hashMap.put(reqKey,rideID);
                    String sanitizedPath = email.replace("@eng.asu.edu.eg", "");
                    if (sanitizedPath != null) {
                        Log.d("SanitizedPath", "SanitizedPath: " + sanitizedPath);
                        databaseReference.child("Requests").child(sanitizedPath).child(rideID).setValue(rideID).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Request is done successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FirebaseError", "Error writing to Firebase", e);
                                Toast.makeText(getApplicationContext(), "Failed to request ride. Check logs for details.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });


        }


    }

}