package com.akstechies.logtheworkouts.saveworkouts;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Workout.class}, version = 1)
public abstract class WorkoutDatabase extends RoomDatabase {
    public abstract WorkoutDao workoutDao();

    private static volatile WorkoutDatabase INSTANCE;

    public static WorkoutDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (WorkoutDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            WorkoutDatabase.class,
                            "workout_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}

