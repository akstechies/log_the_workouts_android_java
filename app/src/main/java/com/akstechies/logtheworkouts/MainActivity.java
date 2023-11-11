package com.akstechies.logtheworkouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button selectDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        selectDateButton = findViewById(R.id.selectDateButton);

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        outputFormat.setTimeZone(TimeZone.getDefault());
        final String[] formattedDate = {outputFormat.format(new Date(calendarView.getDate()))};

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // This will be called when a date is selected in the CalendarView.
                // You can capture the selected date here and store it in a variable.
                // In this example, we'll store it in a String.
                String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                // You can also display the selected date or perform other actions here.
                formattedDate[0] = selectedDate;
            }
        });

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You can use the selectedDate for further processing, e.g., display it in a Toast or TextView.
                Toast.makeText(MainActivity.this, "Selected Date: " + formattedDate[0], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ViewWorkoutActivity.class);
                intent.putExtra("formattedDate", formattedDate[0]);
                startActivity(intent);
            }
        });

    }
}