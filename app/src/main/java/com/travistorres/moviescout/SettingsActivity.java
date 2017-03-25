package com.travistorres.moviescout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textView = (TextView) findViewById(R.id.settings_activity_text);
        textView.setText("Are you ready to ROCK!!!");
    }
}
