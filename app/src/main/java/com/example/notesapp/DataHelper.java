package com.example.notesapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

// Define the database version and entities here
@Database(entities = {database.class}, version = 1, exportSchema = false)
public abstract class DataHelper extends RoomDatabase {

    // Define the DAO abstract method
    public abstract dao noteDao();

    // Singleton instance
    private static volatile DataHelper INSTANCE;

    // Get the singleton instance of the database
    public static DataHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DataHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DataHelper.class, "notes_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public List<database> getAllNotes() {
        return noteDao().getAllNotes();
    }
}
