package com.akstechies.logtheworkouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.akstechies.logtheworkouts.saveworkouts.Workout;
import com.akstechies.logtheworkouts.saveworkouts.WorkoutDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewWorkoutItemActivity extends AppCompatActivity {

    private WorkoutDatabase db; // Declare the WorkoutDatabase instance
    private ExecutorService executorService = Executors.newSingleThreadExecutor(); // Create an ExecutorService

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workout_item);

        db = WorkoutDatabase.getInstance(this); // Initialize the WorkoutDatabase instance

        Intent intent = getIntent();
        long workoutId = intent.getLongExtra("workoutId", -1); // Get the workoutId from Intent

        if (workoutId != -1) {
            // Use ExecutorService to perform the database query on a background thread
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Workout workout = db.workoutDao().getWorkoutById(workoutId);

                    // Update the UI on the main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (workout != null) {
                                // Now you have the Workout object, you can access its attributes.
                                TextView txtExerciseName = findViewById(R.id.txtExerciseName);
                                TextView txtReps = findViewById(R.id.txtReps);
                                TextView txtWeight = findViewById(R.id.txtWeight);
                                TextView txtDuration = findViewById(R.id.txtDuration);
                                TextView txtSet = findViewById(R.id.txtSet);
                                TextView txtDate = findViewById(R.id.txtDate);

                                txtExerciseName.setText(workout.workoutName); // Display the workout name or any other attribute you want
                                txtReps.setText(workout.workoutReps);
                                txtWeight.setText(workout.workoutWeight);
                                txtDuration.setText(workout.workoutDuration);
                                txtSet.setText(workout.workoutSets);
                                txtDate.setText(workout.workoutDate);

                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the executor service when the activity is destroyed
        executorService.shutdown();
    }
}