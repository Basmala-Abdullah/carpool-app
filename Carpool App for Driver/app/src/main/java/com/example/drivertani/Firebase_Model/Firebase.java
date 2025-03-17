package com.example.drivertani.Firebase_Model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivertani.AdapterRoute;
import com.example.drivertani.Route;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Firebase {
    DatabaseReference databaseReference;
    DatabaseReference requestsDatabaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    AdapterRoute adapterRoute;

    RecyclerView recyclerView;

    List<Route> AvailableRoutsList(){
        List list = new ArrayList<Route>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Rides");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Route route = dataSnapshot.getValue(Route.class);
                    list.add(route);
                }
                adapterRoute.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list;
    }
    Context context = null;
    void makeRideBooking(String sanitizedPath, String rideID){

        databaseReference.child("Requests").child(sanitizedPath).child(rideID).setValue(rideID).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Request is done successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseError", "Error writing to Firebase", e);
                Toast.makeText(context, "Failed to request ride. Check logs for details.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void updateStatusInDB(String newStatus, Route route){
        Map<String, Object> updates = new HashMap<>();
        updates.put("Status", newStatus);

    }

    String getRouteNewStatus(String currentStatus, String routeDate, String routeTime,Route route){
        String newStatus = currentStatus;
        String currentDate = new SimpleDateFormat( "MM/dd/yyyy", Locale.getDefault()).format(new Date());
        String[] arrOfRouteDate = routeDate.split("/", 4);
        String[] arrOfCurrentDate = currentDate.split("/", 4);
        int routeMonth = Integer.parseInt(arrOfRouteDate[0]);
        int routeDay = Integer.parseInt(arrOfRouteDate[1]);
        int routeYear = Integer.parseInt(arrOfRouteDate[2]);
        int currentMonth = Integer.parseInt(arrOfCurrentDate[0]);
        int currentDay = Integer.parseInt(arrOfCurrentDate[1]);
        int currentYear = Integer.parseInt(arrOfCurrentDate[2]);
        if(currentYear == routeYear && routeDay ==  currentDay && routeMonth == currentMonth && (currentStatus.equals("Confirmed")||currentStatus.equals("In Progress"))){

            Date currentTime = new Date();
            if(routeTime.equals("7:30 AM")){
                java.util.Date AM7_30 = fixTime(7,30).getTime();
                java.util.Date AM9_30 = fixTime(9,30).getTime();
                if(AM9_30.before(currentTime)){
                    Log.d("btest","AM9_30:"+AM9_30+"--currentTime:"+currentTime+"-AM9_30.before(currentTime)"+AM9_30.before(currentTime));
                    updateStatusInDB("Done",route);
                    return "Done";
                }else if(currentStatus.equals("Confirmed") && AM7_30.before(currentTime)){
                    updateStatusInDB("In Progress",route);
                    return "In Progress";
                }
            }else if(routeTime.equals("5:30 PM")){
                java.util.Date PM5_30 = fixTime(17,30).getTime();
                java.util.Date PM7_30 = fixTime(19,30).getTime();
                if( PM7_30.before(currentTime)){
                    updateStatusInDB("Done",route);
                    //Log.d("btest","line 182");
                    return "Done";
                }else if(currentStatus.equals("Confirmed") && PM5_30.before(currentTime)){
                    updateStatusInDB("In Progress",route);
                    return "In Progress";
                }
            }
        }else if(((routeYear<currentYear) || (routeYear==currentYear && routeMonth<currentMonth) || (routeYear==currentYear && routeMonth==currentMonth && routeDay<currentDay)) && (currentStatus.equals("Confirmed")||currentStatus.equals("In Progress"))){
            updateStatusInDB("Done",route);
            //Log.d("btest","line 194");
            return "Done";
        }else{
            int StatusValidity = checkStatusValidity(routeDate,routeTime);
            Log.d("StatusValidity",StatusValidity+"");

        }
        return newStatus;
    }

    int checkStatusValidity(String enteredDate, String Time){

        String currentDate = new SimpleDateFormat( "MM/dd/yyyy", Locale.getDefault()).format(new Date());
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
            return 0;
        }
        if(enteredYear==currentYear && enteredMonth==currentMonth){
            if(Time.equals("7:30 AM")){
                if(enteredDay==currentDay){
                    return 0;
                }else if((enteredDay-1)==currentDay){

                    java.util.Date PM11_30 = fixTime(23,30).getTime();
                    Date currentTime = new Date();

                    if(PM11_30.before(currentTime)){
                        return 0;
                    }
                }
                return 1;
            }else if(Time.equals("5:30 PM")){
                if(enteredDay==currentDay){
                    Date PM4_30 = fixTime(16,30).getTime();
                    Date currentTime = new Date();
                    if(PM4_30.before(currentTime)){
                        return 0;
                    }
                }
                return 1;
            }
        }
        return -1;
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


    List<Route> returnUserReq(String sanitizedPath){
        requestsDatabaseReference = FirebaseDatabase.getInstance().getReference("Requests").child(sanitizedPath);

        List requests = new ArrayList<>();
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
        return requests;
    }



}
