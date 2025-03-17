package com.example.carpool;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
public class AdapterRoute extends RecyclerView.Adapter<AdapterRoute.MyViewHolder> {

    android.content.Context context;
    ArrayList<Route> list;
    String selectedFrom,selectedTo, selectedTime, selectedPrice, selectedDate, selectedDriverName,selectedDriverPhone,selectedRide_ID;

    String email;

    public AdapterRoute(android.content.Context context, ArrayList<Route> list,String email) {
        this.context = context;
        this.list = list;
        this.email=email;
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
        holder.driverName.setText(route.Driver_Name);
        holder.driverPhone.setText(route.Driver_Phone);
        Intent intent = new Intent(context,BookingActivity.class);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkChoiceValidity(route.getDate(),route.getTime())==1){
                    selectedFrom = route.getFrom();
                    selectedTo = route.getTo();
                    selectedTime = route.getTime();
                    selectedPrice = route.getPrice();
                    selectedDate = route.getDate();
                    selectedDriverName=route.getDriver_Name();
                    selectedDriverPhone = route.getDriver_Phone();
                    selectedRide_ID = route.getRide_ID();

                    intent.putExtra("From", selectedFrom);
                    intent.putExtra("To", selectedTo);
                    intent.putExtra("Time", selectedTime);
                    intent.putExtra("Price", selectedPrice);
                    intent.putExtra("Date", selectedDate);
                    intent.putExtra("Name", selectedDriverName);
                    intent.putExtra("Phone", selectedDriverPhone);
                    intent.putExtra("email",email);
                    intent.putExtra("rideID",selectedRide_ID);
                    context.startActivity(intent);
                }else{

                    Toast.makeText(context,"Sorry!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context,"Too late to book this ride", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView from, to, time, price, date,driverName,driverPhone,status,statusHeader;
        ImageView add;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            time = itemView.findViewById(R.id.rideTime);
            price = itemView.findViewById(R.id.ridePrice);
            date = itemView.findViewById(R.id.date);
            driverName = itemView.findViewById(R.id.driverName);
            driverPhone = itemView.findViewById(R.id.driverPhone);
            status = itemView.findViewById(R.id.status);
            status.setVisibility(View.INVISIBLE);
            statusHeader= itemView.findViewById(R.id.statusHeader);
            statusHeader.setVisibility(View.INVISIBLE);
            add = itemView.findViewById(R.id.add);
        }
    }
    int checkChoiceValidity(String enteredDate, String Time){

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
                int nextDay = enteredDay-1;
                Log.d("btest","enteredDay:"+enteredDay+"--currentDay"+currentDay);
                if(enteredDay==currentDay){
                    return 0;
                }else if(nextDay==currentDay){
                    java.util.Date PM10_00 = fixTime(22,0).getTime();
                    Date currentTime = new Date();
                   if(PM10_00.before(currentTime)){
                       Log.d("btest","PM1030:"+PM10_00+"--currentTime:"+currentTime+"-PM1030.before(currentTime)"+PM10_00.before(currentTime));
                       return 0;
                   }
                }
                return 1;
            }else if(Time.equals("5:30 PM")){
                if(enteredDay==currentDay){
                    Date PM1_00 = fixTime(13,0).getTime();
                    Date currentTime = new Date();
                    Log.d("btest","PM1_00:"+PM1_00+"--currentTime:"+currentTime+"-PM1_00.before(currentTime)"+PM1_00.before(currentTime));
                    if(PM1_00.before(currentTime)){
                        return 0;
                    }
                }
                return 1;
            }
        }else if((enteredYear>currentYear) || (enteredYear==currentYear && enteredMonth>currentMonth)){
            return 1;
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
