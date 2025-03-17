package com.example.carpool;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterRequest extends RecyclerView.Adapter<AdapterRequest.MyViewHolder> {

    android.content.Context context;
    ArrayList<Route> list;
    String email;

    public AdapterRequest(android.content.Context context, ArrayList<Route> list,String email){
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
        holder.status.setText(route.Status);
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
            status.setVisibility(View.VISIBLE);
            statusHeader= itemView.findViewById(R.id.statusHeader);
            statusHeader.setVisibility(View.VISIBLE);
            add = itemView.findViewById(R.id.add);
            add.setVisibility(View.INVISIBLE);
        }
    }

}