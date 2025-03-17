package com.example.drivertani;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdapterRoute extends RecyclerView.Adapter<AdapterRoute.MyViewHolder> {

    android.content.Context context;
    ArrayList<Route> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public AdapterRoute(android.content.Context context, ArrayList<Route> list) {
        this.context = context;
        this.list = list;
        firebaseDatabase = FirebaseDatabase.getInstance("https://carpool-8a3a7-default-rtdb.firebaseio.com/");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.route,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Route route = list.get(position);
        holder.from.setText(route.From);
        holder.price.setText(route.Price);
        holder.time.setText(route.Time);
        holder.to.setText(route.To);
        holder.date.setText(route.Date);
        String routeStatus = getRouteNewStatus(route.Status,route.Date,route.Time,holder.getAdapterPosition());
        holder.status.setText(routeStatus);
        route.Status= routeStatus;
        databaseReference = firebaseDatabase.getReference("Rides").child(route.Ride_ID);
        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((route.Status).equals("Not Confirmed")){
                    updateStatusInDB("Confirmed",holder.getAdapterPosition());
                    holder.status.setText("Confirmed");
                    route.Status="Confirmed";
                }else if((route.Status).equals("Cancelled")){
                    Toast.makeText(context,"Sorry!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context,"Too late to confirm this ride", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView from, to, time, price, date,status;
        ImageView confirm;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            time = itemView.findViewById(R.id.rideTime);
            price = itemView.findViewById(R.id.ridePrice);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            confirm = itemView.findViewById(R.id.confirm);
        }
    }

    void updateStatusInDB(String newStatus, int position){
        Route route = list.get(position);
        Map<String, Object> updates = new HashMap<>();
        updates.put("Status", newStatus);
        firebaseDatabase.getReference("Rides").child(route.Ride_ID).updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Done", "updated");

            } else {
                Log.e("Error", "update failed", task.getException());
            }
        });
    }

    String getRouteNewStatus(String currentStatus, String routeDate, String routeTime,int position){
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
                if( AM9_30.before(currentTime)){
                    updateStatusInDB("Done",position);
                    return "Done";
                }else if(currentStatus.equals("Confirmed") && AM7_30.before(currentTime)){
                    updateStatusInDB("In Progress",position);
                    return "In Progress";
                }
            }else if(routeTime.equals("5:30 PM")){
                java.util.Date PM5_30 = fixTime(17,30).getTime();
                java.util.Date PM7_30 = fixTime(19,30).getTime();
                if(PM7_30.before(currentTime)){
                    updateStatusInDB("Done",position);
                    return "Done";
                }else if(currentStatus.equals("Confirmed") && PM5_30.before(currentTime)){
                    updateStatusInDB("In Progress",position);
                    return "In Progress";
                }
            }

        }else if(((routeYear<currentYear) || (routeYear==currentYear && routeMonth<currentMonth) || (routeYear==currentYear && routeMonth==currentMonth && routeDay<currentDay)) && (currentStatus.equals("Confirmed")||currentStatus.equals("In Progress")||currentStatus.equals("Done"))){
            updateStatusInDB("Done",position);
            return "Done";
        }else{
            int StatusValidity = checkStatusValidity(routeDate,routeTime);
            Log.d("StatusValidity",StatusValidity+"");
            if(StatusValidity == 0){
                updateStatusInDB("Cancelled",position);
                return "Cancelled";
            }
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

                    java.util.Date PM1130 = fixTime(23,30).getTime();
                    Date currentTime = new Date();
                    Log.d("btest","PM1130:"+PM1130+"--currentTime:"+currentTime+"-PM1130.before(currentTime)"+PM1130.before(currentTime));
                    if(PM1130.before(currentTime)){
                        return 0;
                    }
                }
                return 1;
            }else if(Time.equals("5:30 PM")){
                if(enteredDay==currentDay){
                    Date PM430 = fixTime(16,30).getTime();
                    Date currentTime = new Date();
                    Log.d("btest","PM430:"+PM430+"--currentTime:"+currentTime+"-PM430.before(currentTime)"+PM430.before(currentTime));
                    if(PM430.before(currentTime)){
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

}
