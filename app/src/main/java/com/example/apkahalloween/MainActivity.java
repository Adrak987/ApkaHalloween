package com.example.apkahalloween;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView highScoreView;
    private Button startButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        highScoreView = findViewById(R.id.highScore);
        startButton = findViewById(R.id.start);

        loadScore();



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });


    }

    public void startGame() {
        Intent intent = new Intent(this, MainActivityGame.class);
        startActivity(intent);
        finish();
    }

    public void loadScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String highScore = sharedPreferences.getString("score", "0");
        highScoreView.setText(highScore);
    }
}