package com.example.drivertani;
import java.util.Calendar;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.drivertani.LocalDB.ProfileDataEntity;
import com.example.drivertani.LocalDB.ProfileViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.text.ParseException;
public class BookingActivity extends AppCompatActivity {
    Spinner spinnerFrom;
    Spinner spinnerTO;
    String selectedFrom;
    String selectedTo;
    TextView textAt;
    TextView price;
    EditText editTextFrom;
    EditText editTextTo;
    Button submitRideButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private ProfileViewModel mProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
        firebaseDatabase = FirebaseDatabase.getInstance("https://carpool-8a3a7-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference();
        Intent getEmailIntent = getIntent();
        String email = getEmailIntent.getStringExtra("email");
        String driverPhone = getEmailIntent.getStringExtra("phone");
        String driverName = getEmailIntent.getStringExtra("name");
        spinnerFrom = findViewById(R.id.FromOptions);
        spinnerTO = findViewById(R.id.ToOptions);
        textAt = findViewById(R.id.Atime);
        price = findViewById(R.id.price);
        editTextFrom = findViewById(R.id.editTextFrom);
        editTextTo = findViewById(R.id.editTextTo);
        submitRideButton = findViewById(R.id.submitRideButton);
        String[] FromOptions = {"Gate 3", "Gate 4", "Other"};
        ArrayAdapter<String> adapterFrom = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FromOptions);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapterFrom);
        String[] ToOptions = {"Gate 3", "Gate 4"};
        ArrayAdapter<String> adapterTo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ToOptions);
        // Set up the OnItemSelectedListener
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item
                selectedFrom = parentView.getItemAtPosition(position).toString();

                // Display a toast with the selected item
                Toast.makeText(getApplicationContext(), "Selected: " + selectedFrom, Toast.LENGTH_SHORT).show();
                if(selectedFrom.equals("Gate 3")||selectedFrom.equals("Gate 4")){
                    textAt.setText("5:30 PM");
                    editTextFrom.setVisibility(View.INVISIBLE);
                    editTextTo.setVisibility(View.VISIBLE);
                    spinnerTO.setVisibility(View.INVISIBLE);
                }
                else{
                    textAt.setText("7:30 AM");
                    editTextFrom.setVisibility(View.VISIBLE);
                    spinnerTO.setVisibility(View.VISIBLE);
                    editTextTo.setVisibility(View.INVISIBLE);
                    adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTO.setAdapter(adapterTo);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here, or add logic for nothing selected if needed
            }
        });

        //CommonFuncs commonFuncs = new CommonFuncs(mProfileViewModel);

        submitRideButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String enteredDate = dateButton.getText().toString();
                String currentDate = getTodaysDate();
                String[] arrOfEnteredDate = enteredDate.split("/", 4);
                String[] arrOfCurrentDate = currentDate.split("/", 4);
                int enteredMonth = Integer.parseInt(arrOfEnteredDate[0]);
                int enteredDay = Integer.parseInt(arrOfEnteredDate[1]);
                int enteredYear = Integer.parseInt(arrOfEnteredDate[2]);
                Log.d("b-test el date",enteredMonth+"/"+enteredDay+"/"+enteredYear);
                int currentMonth = Integer.parseInt(arrOfCurrentDate[0]);
                int currentDay = Integer.parseInt(arrOfCurrentDate[1]);
                int currentYear = Integer.parseInt(arrOfCurrentDate[2]);
                if((enteredYear<currentYear) || (enteredYear==currentYear && enteredMonth<currentMonth) || (enteredYear==currentYear && enteredMonth==currentMonth && enteredDay<currentDay)){
                    Toast.makeText(getApplicationContext(), "Enter suitable date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(enteredYear==currentYear && enteredMonth==currentMonth){
                    if((textAt.getText().toString()).equals("7:30 AM")){
                        if(enteredDay==currentDay){
                            Toast.makeText(getApplicationContext(), "Enter suitable date", Toast.LENGTH_SHORT).show();
                            return;
                        }else if((enteredDay-1)==currentDay){
                            Date PM1130 = fixTime(23,30).getTime();
                            Date currentTime = new Date();
                            if(PM1130.before(currentTime)){
                                Toast.makeText(getApplicationContext(), "Enter suitable date", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }else if((textAt.getText().toString()).equals("5:30 PM")){
                        if(enteredDay==currentDay){
                            Date PM430 = fixTime(16,30).getTime();
                            Date currentTime = new Date();
                            if(PM430.before(currentTime)){
                                Toast.makeText(getApplicationContext(), "Enter suitable date", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }


                HashMap<String,Object> hashMap = new HashMap<>();
//                mProfileViewModel = new ViewModelProvider(BookingActivity.this).get(ProfileViewModel.class);
//                mProfileViewModel.getAllUsersProfileData().observe(BookingActivity.this, users -> {
//                    // Update the cached copy of the words in the adapter.
//                    for(ProfileDataEntity p : users){
//                        String pEmail = p.getEmail();
//                        if(pEmail.equals(email)){
//                            hashMap.put("Driver_Phone",p.getPhoneNumber());
//                            hashMap.put("Driver_Name",p.getName());
//                        }
//                    }
//                });
//                String sanitizedPath = email.replace("@eng.asu.edu.eg", "");
//                databaseReference.child("Drivers").child(sanitizedPath).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            if (snapshot.hasChild("Driver_Phone") && snapshot.hasChild("Driver_Name")) {
//                                String driverPhone = snapshot.child("Driver_Phone").getValue(String.class);
//                                String driverName = snapshot.child("Driver_Name").getValue(String.class);
//
//                                // Create a HashMap to store the retrieved data
//                                HashMap<String, Object> hashMap = new HashMap<>();
//                                hashMap.put("Driver_Phone", driverPhone);
//                                hashMap.put("Driver_Name", driverName);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                hashMap.put("Driver_Phone", driverPhone);
                hashMap.put("Driver_Name", driverName);
                if (spinnerTO.getSelectedItem() != null) {
                    selectedTo = spinnerTO.getSelectedItem().toString();
                }
                if(selectedFrom.equals("Other")){
                    hashMap.put("From",editTextFrom.getText().toString());
                    hashMap.put("To",selectedTo);
                }
                else{
                    hashMap.put("From",selectedFrom);
                    hashMap.put("To",editTextTo.getText().toString());
                }
                hashMap.put("Time", textAt.getText().toString());
                hashMap.put("Price",price.getText().toString());
                hashMap.put("Date",dateButton.getText());

                String rideKey = databaseReference.child("Rides").push().getKey();
                hashMap.put("Ride_ID",rideKey);
                hashMap.put("Driver_Mail",email);
                hashMap.put("Status","Not Confirmed");
                hashMap.put("Driver_Phone",driverPhone);
                hashMap.put("Driver_Name",driverName);

                databaseReference.child("Rides").child(rideKey).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Ride is added successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseError", "Error writing to Firebase", e);
                        Toast.makeText(getApplicationContext(), "Failed to add ride. Check logs for details.", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);


    }
    private String makeDateString(int day, int month, int year){
        return month + "/" + day + "/" + year;
    }
    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
    String value= null;
    private String returnDriverData(String email, String dataType){
        mProfileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mProfileViewModel.getAllUsersProfileData().observe(this, users -> {
            // Update the cached copy of the words in the adapter.

            for(ProfileDataEntity p : users){
                String pEmail = p.getEmail();
                if(pEmail.equals(email)){
                    if(dataType.equals("phone")){
                        value = p.getPhoneNumber();
                        Log.d("Driverphone",value);


                    }else if(dataType.equals("name")){
                        value = p.getName();
                        Log.d("DriverName",value);

                    }
                }
                }
        });

        return value;

    }

    Calendar fixTime(int hour, int minutes){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        // Combine the current date with the specified time
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        return calendar;

    }
}

