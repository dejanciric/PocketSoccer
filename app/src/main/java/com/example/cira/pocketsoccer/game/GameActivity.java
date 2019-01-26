package com.example.cira.pocketsoccer.game;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.cira.pocketsoccer.MainActivity;
import com.example.cira.pocketsoccer.R;
import com.google.gson.Gson;

public class GameActivity extends AppCompatActivity {

    private MyCustomView myCustomView;

    private int field, gameSpeed, timeProgress, goalsProgress, currState1, currState2, computer;
    private String rule, player1Name, player2Name;
    ResumeStorage resume;
    private int[] rates={0,18,15,12, 9, 6};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        myCustomView = findViewById(R.id.customView);
        myCustomView.setContext(this);
        SharedPreferences sharedPreferences = getSharedPreferences("resume", MODE_PRIVATE);
        String json = sharedPreferences.getString("ResumeStorage","");
        if (!json.equals("")){
            Gson gson = new Gson();
            resume = gson.fromJson(json, ResumeStorage.class);
            myCustomView.turn = resume.turn;

            myCustomView.setParams(resume.field, resume.rule, resume.time, resume.goals, resume.gameSpeed,resume.state1,resume.state2, resume.player1Name, resume.player2Name, this, resume.computer);
            myCustomView.restore  = true;
            return;
        }


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
        computer = mojIntent.getIntExtra("computer", 0);



        myCustomView.setParams(field, rule, getStringValue(timeProgress), getStringValue(goalsProgress), gameSpeed,currState1,currState2, player1Name, player2Name, this, computer);

        myCustomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (myCustomView.winner.equals("") && !myCustomView.myThread.goal){
                            if ((computer == 1 && myCustomView.turn.equals("left"))||(computer == 2 && myCustomView.turn.equals("right"))){

                            }else{
                                myCustomView.down(event.getX(), event.getY());
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        if ((computer == 1 && myCustomView.turn.equals("left"))||(computer == 2 && myCustomView.turn.equals("right"))){

                        }else {
                            myCustomView.up(event.getX(), event.getY());
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }
                return true;
            }
        });
    }

    public void restore() {
        field = resume.field;
        rule = resume.rule;
        currState1 = resume.state1;
        currState2 = resume.state2;
        player1Name = resume.player1Name;
        player2Name = resume.player2Name;
        gameSpeed = resume.gameSpeed;
        computer = resume.computer;
        myCustomView.myThread.flag = resume.goal;
        myCustomView.setParams(resume.field, resume.rule, resume.time, resume.goals, resume.gameSpeed,resume.state1,resume.state2, resume.player1Name, resume.player2Name, this, resume.computer);
       // myCustomView.decode();
        myCustomView.myThread.frameRate = rates[resume.gameSpeed];
        for (int i=0; i < 7; i++){
            myCustomView.figures[i].x = resume.x[i];
            myCustomView.figures[i].y = resume.y[i];
            myCustomView.figures[i].dx = resume.dx[i];
            myCustomView.figures[i].dy = resume.dy[i];
        }
        myCustomView.leftScore = resume.score1;
        myCustomView.rightScore = resume.score2;
        myCustomView.seconds = resume.timeLeftForMove;
        //myCustomView.turn = resume.turn;
        myCustomView.computer = resume.computer;

        myCustomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (myCustomView.winner.equals("") && !myCustomView.myThread.goal) {
                            if ((computer == 1 && myCustomView.turn.equals("left")) || (computer == 2 && myCustomView.turn.equals("right"))) {

                            } else {
                                myCustomView.down(event.getX(), event.getY());
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((computer == 1 && myCustomView.turn.equals("left"))||(computer == 2 && myCustomView.turn.equals("right"))){

                        }else {
                            myCustomView.up(event.getX(), event.getY());
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }
                return true;
            }
        });

        if (resume.goal){
            myCustomView.startAgain();
        }

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

    @Override
    public void onBackPressed() {

        if (!myCustomView.winner.equals("")){
            myCustomView.myThread.active = false;
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("endGame","yes");
            intent.putExtra("result",myCustomView.leftScore+":"+myCustomView.rightScore);
            intent.putExtra("players",player1Name+"  X  "+player2Name);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        myCustomView.myThread.active = false;
        float[] x = new float[7];
        float[] y = new float[7];
        float[] dx = new float[7];
        float[] dy = new float[7];

        for (int i=0; i < myCustomView.figures.length; i++){
            x[i] = myCustomView.figures[i].x;
            y[i] = myCustomView.figures[i].y;
            dx[i] = myCustomView.figures[i].dx;
            dy[i] = myCustomView.figures[i].dy;
        }


        ResumeStorage resumeStorage = new ResumeStorage(x,y,dx,dy,currState1, currState2,player1Name, player2Name, myCustomView.leftScore, myCustomView.rightScore, rule, myCustomView.goals,myCustomView.time, myCustomView.seconds, field, gameSpeed, myCustomView.turn, myCustomView.computer,myCustomView.myThread.goal);

        SharedPreferences sharedPreferences = getSharedPreferences("resume",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(resumeStorage);
        editor.putString("ResumeStorage", json);
        editor.commit();

        if (!myCustomView.winner.equals("")){
            SharedPreferences preferences = this.getSharedPreferences("resume", MODE_PRIVATE);
            preferences.edit().clear().commit();
        }

            finish();




    }
}
