package com.akstechies.logtheworkouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akstechies.logtheworkouts.saveworkouts.Workout;
import com.akstechies.logtheworkouts.saveworkouts.WorkoutDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateWorkoutActivity extends AppCompatActivity {

    private Context context;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        context = this; // Store the context for later use

        Intent intent = getIntent();
        String workoutDate = intent.getStringExtra("workoutDate");

        EditText edtExerciseName = findViewById(R.id.editTextExercise);
        EditText editTextReps = findViewById(R.id.editTextReps);
        EditText editTextWeight = findViewById(R.id.editTextWeight);
        EditText editTextDuration = findViewById(R.id.editTextDuration);
        EditText editTextSets = findViewById(R.id.editTextSets);

        Button saveWorkoutBtn = findViewById(R.id.saveWorkoutButton);
        saveWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String validateName = edtExerciseName.getText().toString().trim();
                if (validateName.isEmpty()) {
                    Toast.makeText(context, "Please enter an Exercise Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Pass parameters to the InsertWorkoutTask constructor
                InsertWorkoutTask insertWorkoutTask = new InsertWorkoutTask(context,
                        edtExerciseName.getText().toString(),
                        editTextReps.getText().toString(),
                        editTextWeight.getText().toString(),
                        editTextDuration.getText().toString(),
                        editTextSets.getText().toString(),
                        workoutDate
                        );
                // Execute the task
                executorService.execute(insertWorkoutTask);

                Toast.makeText(context, "Workout has been added!", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(CreateWorkoutActivity.this, ViewWorkoutActivity.class);
                intent.putExtra("formattedDate", workoutDate);
                startActivity(intent);*/
            }
        });
    }

    private class InsertWorkoutTask implements Runnable {
        private Context taskContext;
        private String workoutName, workoutReps, workoutWeight, workoutDuration, workoutSets, workoutDate;

        // Constructor to receive parameters
        public InsertWorkoutTask(Context context, String name, String workoutReps, String workoutWeight, String workoutDuration, String workoutSets, String workoutDate) {
            this.taskContext = context;
            this.workoutName = name;
            this.workoutReps = workoutReps;
            this.workoutWeight = workoutWeight;
            this.workoutDuration = workoutDuration;
            this.workoutSets = workoutSets;
            this.workoutDate = workoutDate;
        }

        @Override
        public void run() {
            // This method runs on a background thread
            Workout workout = new Workout();
            workout.workoutName = workoutName;
            workout.workoutReps = workoutReps;
            workout.workoutWeight = workoutWeight;
            workout.workoutDuration = workoutDuration;
            workout.workoutSets = workoutSets;
            workout.workoutDate = workoutDate;

            // Get an instance of your database and insert the workout
            WorkoutDatabase db = WorkoutDatabase.getInstance(taskContext);
            db.workoutDao().insert(workout);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the executor service when the activity is destroyed
        executorService.shutdown();
    }
}