package com.example.apkahalloween;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivityGame extends AppCompatActivity {

    private TextView scoreView, testView;
    private Button stopButton;
    private View viewTop, viewCenter, viewBot;
    private int count = 0;
    private ImageView ghost;
    private ConstraintLayout constraintLayout;

    private int speed = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        scoreView = findViewById(R.id.score);
        testView = findViewById(R.id.test);
        stopButton = findViewById(R.id.stop);
        viewTop = findViewById(R.id.viewTop);
        viewCenter = findViewById(R.id.viewCenter);
        viewBot = findViewById(R.id.viewBot);


        ghost = findViewById(R.id.ghost);
        constraintLayout = findViewById(R.id.constraintLayout);

        gameSpeed();

        //spawnPumpkin();

        pumpkinSpawner();

        addScore();


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

        if (random == 1) {
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
                screenWidth + 150,
                -pumpkin.getWidth() - 400
        );

        animation.setDuration(1000 + speed);

        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                constraintLayout.removeView(pumpkin);
            }


        });
        animation.start();

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                //float currentX = pumpkin.getX();
                int[] location1 = new int[2];
                int[] location2 = new int[2];
                pumpkin.getLocationOnScreen(location1);
                ghost.getLocationOnScreen(location2);

                int movingViewX = location1[0];
                int movingViewY = location1[1];
                int movingViewWidth = pumpkin.getWidth();
                int movingViewHeight = pumpkin.getHeight();

                int targetViewX = location2[0];
                int targetViewY = location2[1];
                int targetViewWidth = ghost.getWidth();
                int targetViewHeight = ghost.getHeight();


                if (movingViewX + movingViewWidth > targetViewX &&
                        movingViewX < targetViewX + targetViewWidth &&
                        movingViewY + movingViewHeight > targetViewY &&
                        movingViewY < targetViewY + targetViewHeight) {
                    constraintLayout.removeView(pumpkin);
                    count+=50;
                    scoreView.setText(Integer.toString(count));
                }

            }
        });

    }


    public void pumpkinSpawner() {
        final Handler handler = new Handler();
        final int delay = 400 + speed;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                spawnPumpkin();
                int delay = 400 + speed;
                handler.postDelayed(this, delay);
            }
        }, delay);
    }


    public void gameSpeed() {
        final Handler handler = new Handler();
        final int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(speed-4 >= 0) {
                    speed -= 4;
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void addScore() {
        final Handler handler = new Handler();
        final int delay = 50 + speed;

        handler.postDelayed(new Runnable() {
            public void run() {
                count++;
                int delay = 50 + speed;
                scoreView.setText(Integer.toString(count));
                testView.setText(Integer.toString(delay));
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