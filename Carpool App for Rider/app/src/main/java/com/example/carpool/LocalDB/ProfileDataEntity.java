package com.example.carpool.LocalDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "profile_data_table")
public class ProfileDataEntity{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    int id;
    @NonNull
    @ColumnInfo(name = "user_email")
    private String email;

    @NonNull
    @ColumnInfo(name = "user_name")
    private String name;
    @NonNull
    @ColumnInfo(name = "user_phone_number")
    private String phoneNumber;
    //..other fields, getters, setters
    public ProfileDataEntity(@NonNull String name, @NonNull String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    //Getters

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    //Setters
    public void setUserName(String name){
        this.name = name;
    }
    public void setUserPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

}