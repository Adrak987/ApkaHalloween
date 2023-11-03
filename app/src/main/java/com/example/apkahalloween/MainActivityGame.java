package com.example.apkahalloween;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityGame extends AppCompatActivity {

    private TextView scoreView;
    private Button stopButton;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        scoreView = findViewById(R.id.score);
        stopButton = findViewById(R.id.stop);

        addScore();

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScore();
                stopGame();
            }
        });
    }

    public void addScore() {
        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                count++;
                scoreView.setText(Integer.toString(count));
                handler.postDelayed(this, delay);
            }
        }, delay);
    }
    public void stopGame() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String liczba = Integer.toString(count);
        editor.putString("score", liczba);
        editor.commit();




        Toast.makeText(this, liczba, Toast.LENGTH_SHORT).show();
    }

}