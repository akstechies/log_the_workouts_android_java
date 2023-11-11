package com.akstechies.logtheworkouts.saveworkouts;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workouts")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "workout_name")
    public String workoutName;

    @ColumnInfo(name = "reps")
    public String workoutReps;

    @ColumnInfo(name = "weight")
    public String workoutWeight;

    @ColumnInfo(name = "duration")
    public String workoutDuration;

    @ColumnInfo(name = "sets")
    public String workoutSets;

    @ColumnInfo(name = "date")
    public String workoutDate;

}