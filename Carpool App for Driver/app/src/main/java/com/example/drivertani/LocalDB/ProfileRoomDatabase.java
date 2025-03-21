package com.example.drivertani.LocalDB;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.annotation.NonNull;


@Database(entities = {ProfileDataEntity.class}, version = 1, exportSchema = false)
public abstract class ProfileRoomDatabase extends RoomDatabase {
    public abstract ProfileDao profileDao();
    private static volatile ProfileRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ProfileRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProfileRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ProfileRoomDatabase.class, "user_profile_database")
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                ProfileDao dao = INSTANCE.profileDao();
                dao.deleteAll();

                //ProfileDataEntity userProfileData = new ProfileDataEntity("dummyName","dummyPhone", "dummyEmail");
                //dao.insert(userProfileData);

            });
        }
    };

}
