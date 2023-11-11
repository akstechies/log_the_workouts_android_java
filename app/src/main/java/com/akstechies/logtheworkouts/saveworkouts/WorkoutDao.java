package com.akstechies.logtheworkouts.saveworkouts;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Insert
    void insert(Workout workout);

    @Query("SELECT * FROM workouts")
    List<Workout> getAllWorkouts();

    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    Workout getWorkoutById(long workoutId);

    @Query("SELECT * FROM workouts WHERE date = :date")
    List<Workout> getWorkoutsByDate(String date);
}
