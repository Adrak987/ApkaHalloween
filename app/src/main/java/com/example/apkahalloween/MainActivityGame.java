package com.example.apkahalloween;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivityGame extends AppCompatActivity {

    private TextView scoreView;
    private Button stopButton;
    private View viewTop, viewCenter, viewBot;
    private int count = 0;
    private ImageView ghost;
    private ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        scoreView = findViewById(R.id.score);
        stopButton = findViewById(R.id.stop);
        viewTop = findViewById(R.id.viewTop);
        viewCenter = findViewById(R.id.viewCenter);
        viewBot = findViewById(R.id.viewBot);

        ghost = findViewById(R.id.ghost);
        constraintLayout = findViewById(R.id.constraintLayout);


        spawnPumpkin();

        //pumpkinSpawner();

        //addScore();


        viewTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                constraintSet.connect(ghost.getId(), ConstraintSet.START, viewTop.getId(), ConstraintSet.START);
                constraintSet.connect(ghost.getId(), ConstraintSet.TOP, viewTop.getId(), ConstraintSet.TOP);

                constraintSet.applyTo(constraintLayout);
            }
        });

        viewCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                constraintSet.connect(ghost.getId(), ConstraintSet.START, viewCenter.getId(), ConstraintSet.START);
                constraintSet.connect(ghost.getId(), ConstraintSet.TOP, viewCenter.getId(), ConstraintSet.TOP);

                constraintSet.applyTo(constraintLayout);
            }
        });

        viewBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                constraintSet.connect(ghost.getId(), ConstraintSet.START, viewBot.getId(), ConstraintSet.START);
                constraintSet.connect(ghost.getId(), ConstraintSet.TOP, viewBot.getId(), ConstraintSet.TOP);

                constraintSet.applyTo(constraintLayout);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScore();
                stopGame();
            }
        });
    }

    public void spawnPumpkin() {
        ImageView pumpkin = new ImageView(this);
        pumpkin.setImageResource(R.drawable.pumpkin);
        pumpkin.setId(View.generateViewId());
        ConstraintLayout.LayoutParams newSize = new ConstraintLayout.LayoutParams(130, 130);
        pumpkin.setLayoutParams(newSize);
        constraintLayout.addView(pumpkin);


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        Random rand = new Random();
        int random = rand.nextInt(3) + 1;
        int topMargin = 90;

        if(random == 1) {
            constraintSet.connect(pumpkin.getId(), ConstraintSet.START, viewTop.getId(), ConstraintSet.START);
            constraintSet.connect(pumpkin.getId(), ConstraintSet.TOP, viewTop.getId(), ConstraintSet.TOP, topMargin);
        } else if (random == 2) {
            constraintSet.connect(pumpkin.getId(), ConstraintSet.START, viewCenter.getId(), ConstraintSet.START);
            constraintSet.connect(pumpkin.getId(), ConstraintSet.TOP, viewCenter.getId(), ConstraintSet.TOP, topMargin);
        } else if (random == 3) {
            constraintSet.connect(pumpkin.getId(), ConstraintSet.START, viewBot.getId(), ConstraintSet.START);
            constraintSet.connect(pumpkin.getId(), ConstraintSet.TOP, viewBot.getId(), ConstraintSet.TOP, topMargin);
        }

        constraintSet.applyTo(constraintLayout);


        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        ObjectAnimator animation = ObjectAnimator.ofFloat(
                pumpkin,
                "translationX",
                screenWidth+150,
                -pumpkin.getWidth()-400
        );

        animation.setDuration(3000);

        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                constraintLayout.removeView(pumpkin);
            }

        });

        animation.start();
    }

    public void pumpkinSpawner() {
        final Handler handler = new Handler();
        final int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                spawnPumpkin();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void addScore() {
        final Handler handler = new Handler();
        final int delay = 100;

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
        String highScore = sharedPreferences.getString("score", "0");
        String liczba = Integer.toString(count);

        if(Integer.parseInt(highScore) < Integer.parseInt(liczba)) {
            editor.putString("score", liczba);
            editor.commit();
        }
    }

}