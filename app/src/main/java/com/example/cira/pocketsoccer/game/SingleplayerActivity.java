package com.example.cira.pocketsoccer.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.cira.pocketsoccer.R;

public class SingleplayerActivity extends AppCompatActivity {

    private MyCustomView myCustomView;
    private int field, gameSpeed, timeProgress, goalsProgress, currState1, currState2;
    private String rule, player1Name, player2Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent mojIntent = getIntent();
        field = mojIntent.getIntExtra("field", 0);
        rule = mojIntent.getStringExtra("rule");
        gameSpeed = mojIntent.getIntExtra("gameSpeed",3);
        timeProgress = mojIntent.getIntExtra("timeProgress", 0);
        goalsProgress = mojIntent.getIntExtra("goalsProgress", 0);
        currState1 = mojIntent.getIntExtra("currState1", 0);
        currState2 = mojIntent.getIntExtra("currState2", 0);
        player1Name = mojIntent.getStringExtra("player1Name");
        player2Name=mojIntent.getStringExtra("player2Name");

        myCustomView = findViewById(R.id.customView);
        myCustomView.setParams(field, rule, getStringValue(timeProgress), getStringValue(goalsProgress), gameSpeed,currState1,currState2, player1Name, player2Name, this);

        myCustomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        myCustomView.down(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        myCustomView.up(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }
                return true;
            }
        });
    }

    private String getStringValue(int progress) {
        String retVal="";
        if (rule.equals("time")){
            if (progress >= 0 && progress < 10)
                retVal = "00:30";
            else if (progress >=10 && progress < 20)
                retVal = "01:00";
            else if (progress >=20 && progress < 30)
                retVal = "01:30";
            else if (progress >=30 && progress < 40)
                retVal = "02:00";
            else if (progress >=40 && progress < 50)
                retVal = "02:30";
            else if (progress >=50 && progress < 60)
                retVal = "03:00";
            else if (progress >=60 && progress < 70)
                retVal = "03:30";
            else if (progress >=70 && progress < 80)
                retVal = "04:00";
            else if (progress >=80 && progress < 90)
                retVal = "04:30";
            else if (progress >=90 && progress <= 100)
                retVal = "05:00";
        }else{
            if (progress >= 0 && progress < 10)
                retVal = "1";
            else if (progress >=10 && progress < 20)
                retVal = "2";
            else if (progress >=20 && progress < 30)
                retVal = "3";
            else if (progress >=30 && progress < 40)
                retVal = "4";
            else if (progress >=40 && progress < 50)
                retVal = "5";
            else if (progress >=50 && progress < 60)
                retVal = "6";
            else if (progress >=60 && progress < 70)
                retVal = "7";
            else if (progress >=70 && progress < 80)
                retVal = "8";
            else if (progress >=80 && progress < 90)
                retVal = "9";
            else if (progress >=90 && progress <= 100)
                retVal = "10";
        }
        return retVal;
    }
}
