package com.example.studytimerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText TaskType;
    TextView LastTimerRecord;
    Chronometer Chronometer;
    SharedPreferences SharedPreferences;
    boolean ActiveTimer = false;
    long TimePause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LastTimerRecord = findViewById(R.id.LastTimerRecord);
        TaskType = findViewById(R.id.TaskType);
        Chronometer = findViewById(R.id.Chronometer);
        SharedPreferences = getSharedPreferences("com.example.PrefTimer", MODE_PRIVATE);
        LoadSharedPreferences();
        TimePause = SystemClock.elapsedRealtime() - Chronometer.getBase();

        if (savedInstanceState != null){
            LastTimerRecord.setText(savedInstanceState.getString("PASTIME"));
            Chronometer.setBase(SystemClock.elapsedRealtime() + savedInstanceState.getLong("TIMER"));
            TimePause = savedInstanceState.getLong("TIMER_PAUSED");

            if (savedInstanceState.getBoolean("TIMER_STATE")){
                Chronometer.start();
                ActiveTimer = true;
            }
            else
            {
                Chronometer.setBase(SystemClock.elapsedRealtime() - TimePause);
            }
        }
        else
        {
            ActiveTimer = false;
        }
    }
// Save the state to retrieve per-instance state from an activity
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("TIMER_STATE", ActiveTimer);
        outState.putLong("TIMER", Chronometer.getBase() - SystemClock.elapsedRealtime());
        outState.putLong("TIMER_PAUSED", TimePause);
        outState.putString("PASTIME", LastTimerRecord.getText().toString());
    }

    //Load preferences
    private void LoadSharedPreferences() {
        String text = SharedPreferences.getString("SUMMARY_MEM", "");
        LastTimerRecord.setText(text);
    }

    //Update Text View about recorded time and task name
    public void LastTimerUpdate(CharSequence time)
    {
        if (TaskType.getText().toString().equals(""))
        {
            LastTimerRecord.setText("You have recorded " + time.toString() + " on your last Timer!");
        }
        else
        {
            LastTimerRecord.setText("You have recorded " + time.toString() + " on " + TaskType.getText().toString() + "  last timer!");
        }
    }

    //Click activity
    public void onPlayClick(View view) {
        if (!ActiveTimer) {
            Chronometer.setBase(SystemClock.elapsedRealtime() - TimePause);
            Chronometer.start();
            ActiveTimer = true;
        }
    }

    public void onPauseClick(View view)
        {
            if (ActiveTimer)
            {
                TimePause = SystemClock.elapsedRealtime() - Chronometer.getBase();
                Chronometer.stop();
                ActiveTimer = false;
            }
        }

    public void onEndClick(View view){
        LastTimerUpdate(Chronometer.getText());
        Chronometer.stop();
        Chronometer.setBase(SystemClock.elapsedRealtime());
        TimePause = 0;
        ActiveTimer = false;
        SharedPreferences.Editor editor = SharedPreferences.edit();
        editor.putString("SUMMARY_MEM", LastTimerRecord.getText().toString());
        editor.apply();


    }


}