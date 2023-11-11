package com.akstechies.logtheworkouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.akstechies.logtheworkouts.saveworkouts.Workout;
import com.akstechies.logtheworkouts.saveworkouts.WorkoutDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewWorkoutActivity extends AppCompatActivity {

    private Context context;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ArrayAdapter<String> adapter;
    private List<String> workoutNames = new ArrayList<>();
    List<Long> yourWorkoutIdsList = new ArrayList<>();
    private long selectedWorkoutId = -1; // Initialize it to an invalid value
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workout);

        context = this; // Store the context for later use

        // Retrieve data from the Intent
        Intent intent = getIntent();
        message = intent.getStringExtra("formattedDate");

        TextView textView = findViewById(R.id.viewWorkoutTextView);
        textView.setText(message);

        ListView listView = findViewById(R.id.viewWorkoutListView);

        // Initialize the ArrayAdapter for workout names
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.listItemTextView, workoutNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the ID associated with the clicked workout item
                selectedWorkoutId = yourWorkoutIdsList.get(position);

                Intent intent1 = new Intent(ViewWorkoutActivity.this, ViewWorkoutItemActivity.class);

                // Pass the workout ID as an extra in the Intent
                intent1.putExtra("workoutId", selectedWorkoutId);
                startActivity(intent1);

                // Perform database operations on a background thread using ExecutorService
                executorService.execute(new LoadWorkoutsTask(message));
            }
        });

        Button createWorkoutButton = findViewById(R.id.createWorkoutButton);
        createWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ViewWorkoutActivity.this, CreateWorkoutActivity.class);
                intent2.putExtra("workoutDate", message);
                startActivity(intent2);
            }
        });

        // Initial loading of workouts
        executorService.execute(new LoadWorkoutsTask(message));
    }

    private class LoadWorkoutsTask implements Runnable {

        private String message;

        public LoadWorkoutsTask(String message) {
            this.message = message;
        }


        @Override
        public void run() {
            // This method runs on a background thread
            WorkoutDatabase db = WorkoutDatabase.getInstance(context);
            List<Workout> workouts = db.workoutDao().getWorkoutsByDate(message);

            // Extract workout names and use the selectedWorkoutId as needed
            workoutNames.clear(); // Clear the previous names
            yourWorkoutIdsList.clear();
            for (Workout workout : workouts) {
                workoutNames.add(workout.workoutName);
                yourWorkoutIdsList.add(workout.id);
            }

            // Update the UI on the main thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the list of workouts when the activity is resumed
        executorService.execute(new LoadWorkoutsTask(message));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the executor service when the activity is destroyed
        executorService.shutdown();
    }
}