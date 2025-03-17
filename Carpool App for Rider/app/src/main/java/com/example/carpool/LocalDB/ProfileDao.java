package com.example.carpool.LocalDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface ProfileDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(ProfileDataEntity ProfileData);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    public void updateProfile(ProfileDataEntity NewProfileData);

    @Delete
    public void deleteProfile(ProfileDataEntity ProfileData);

    @Query("SELECT * FROM profile_data_table WHERE user_email==:userEmail LIMIT 1")
    public LiveData<ProfileDataEntity> findByEmail(String userEmail);

    @Query("DELETE FROM profile_data_table")
    public void deleteAll();
    @Query("SELECT * FROM profile_data_table ORDER BY user_name ASC")
    public LiveData<List<ProfileDataEntity>> getAlphabetizedUsers();
}
