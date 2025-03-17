package com.example.drivertani.LocalDB;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;


class ProfileRepository {

    private ProfileDao mProfileDao;
    private LiveData<List<ProfileDataEntity>> mAllUsersProfileData;


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    ProfileRepository(Application application) {
        ProfileRoomDatabase db = ProfileRoomDatabase.getDatabase(application);
        mProfileDao = db.profileDao();
        mAllUsersProfileData = mProfileDao.getAlphabetizedUsers();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<ProfileDataEntity>> getAllUsersProfileData() {
        return mAllUsersProfileData;
    }



    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(ProfileDataEntity profileData) {
        ProfileRoomDatabase.databaseWriteExecutor.execute(() -> {
            mProfileDao.insert(profileData);
        });
    }

    void deleteProfile(ProfileDataEntity profileData){
        ProfileRoomDatabase.databaseWriteExecutor.execute(() -> {
            mProfileDao.deleteProfile(profileData);
        });
    }

//    ProfileDataEntity findByEmail(String email){
//        ProfileDataEntity profileData;
//       ProfileRoomDatabase.databaseWriteExecutor.execute(() -> {
//           profileData= mProfileDao.findByEmail(email);
//        });
//    }
}
