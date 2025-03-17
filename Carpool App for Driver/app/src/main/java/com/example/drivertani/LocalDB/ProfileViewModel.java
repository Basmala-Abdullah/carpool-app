package com.example.drivertani.LocalDB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository mRepository;

    private final LiveData<List<ProfileDataEntity>> mAllUsersProfileData;

    public ProfileViewModel (Application application) {
        super(application);
        mRepository = new ProfileRepository(application);
        mAllUsersProfileData = mRepository.getAllUsersProfileData();
    }

    public LiveData<List<ProfileDataEntity>> getAllUsersProfileData() { return mAllUsersProfileData; }

    public void insert(ProfileDataEntity profileData) { mRepository.insert(profileData); }
    public void deleteProfile(ProfileDataEntity profileData) { mRepository.deleteProfile(profileData); }
}

