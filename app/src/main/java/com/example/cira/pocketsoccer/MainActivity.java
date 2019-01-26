package com.example.cira.pocketsoccer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.cira.pocketsoccer.game.ResumeStorage;
import com.google.gson.Gson;

import java.util.Stack;

import model.MyDataHelper;

public class MainActivity extends AppCompatActivity {

    public int gameSpeed;
    public String rule;
    public int field, timeProgress, goalsProgress;;
    private String endGameValue;
    private Context context = this;
    private Stack<Fragment> stackTrace = new Stack<Fragment>();
    private TextView back;
    private MyDataHelper model;
    public boolean showResume;
    DefaultFragment defaultFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        defaultFragment = DefaultFragment.newInstance(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, defaultFragment);
        fragmentTransaction.commit();

        back = findViewById(R.id.back);
        setOnTouchListener(back);

        //setDefaultValues();
        model = new MyDataHelper(this);
       /* model.addScore("kurta", "mika", "1:3", 2);
        model.addScore("murta", "mika", "1:2", 2);
        model.addScore("mica", "mika", "3:0", 1);
        model.addScore("joca", "pera", "1:3", 2);
        model.addScore("jakov", "pera", "3:1", 1);
        model.addScore("ziza", "mika", "1:3", 2);
        model.addScore("luna", "pera2", "3:2", 1);
        model.addScore("pera", "mika", "1:3", 2);
        model.addScore("pera", "mika", "1:2", 2);
        model.addScore("mika", "pera", "3:0", 1);
        model.addScore("mika", "pera", "1:3", 2);
        model.addScore("djoka", "pera", "3:1", 1);
        model.addScore("pera", "mika", "1:3", 2);
        model.addScore("djko", "pera2", "3:2", 1);*/

        SharedPreferences sharedPreferences = context.getSharedPreferences("lastValues", MODE_PRIVATE);
        field = sharedPreferences.getInt("field", -1);
        if (field == -1){
            field = 0;
        }
        gameSpeed =  sharedPreferences.getInt("speed", -1);
        if (gameSpeed == -1){
            gameSpeed = 3;
        }
        rule = sharedPreferences.getString("rule", "");
        if (rule.equals("")){
            rule="goals";
        }
        timeProgress = sharedPreferences.getInt("timeProgress", -1);
        if(timeProgress==-1){
            timeProgress = 10;
        }
        goalsProgress = sharedPreferences.getInt("goalsProgress", -1);
        if (goalsProgress == -1){
            goalsProgress= 20;
        }

        SharedPreferences sp = getSharedPreferences("resume",MODE_PRIVATE);
        String json = sp.getString("ResumeStorage", "");
        if (json.equals("")){
            showResume = false;
        }else{

            showResume = true;
        }
    }

    private void setDefaultValues() {
        SharedPreferences sharedPreferences = getSharedPreferences("defaultValues", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("speed", 3);
        editor.putString("rule", "goals");
        editor.putInt("timeProgress", 10);
        editor.putInt("goalsProgress", 20);
        editor.commit();

    }

    public MyDataHelper getModel(){
        return model;
    }

    private void setOnTouchListener(TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.click_sound);
                mp.start();
                ((TextView)v).setTextColor(getWhiteColor());
                backFragment();
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((TextView)v).setTextColor(getGoldColor());
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void backFragment(){
        /*if (rule.equals("time")){
            Log.i("valll", getStringValue(getTimeProgress()));
        }else{
            Log.i("valll", getStringValue(getGoalsProgress()));
        }*/
        //Log.i("valll", field+"");
        if (stackTrace.size() == 0){
            super.onBackPressed();
        }else{
            Fragment fragment = stackTrace.pop();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        }

    }

    public void pushFragment(Fragment fragment){
        stackTrace.push(fragment);
    }

    public Fragment popFragment(){
        return stackTrace.pop();
    }

    public void backSetVisible(){
        back.setVisibility(View.VISIBLE);
    }

    public void backSetInvisible(){
        back.setVisibility(View.GONE);
    }

    public void setGameSpeed(int gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setTimeProgress(int timeProgress) {
        this.timeProgress = timeProgress;
    }

    public void setGoalsProgress(int goalsProgress) {
        this.goalsProgress = goalsProgress;
    }

    public void setField(int field) {
        this.field = field;
    }

    public void setEndGameValue(String time) {
        this.endGameValue = time;
    }

    public int getGameSpeed() {
        return gameSpeed;
    }

    public String getRule() {
        return rule;
    }

    public int getField() {
        return field;
    }

    public int getTimeProgress() {
        return timeProgress;
    }

    public int getGoalsProgress() {
        return goalsProgress;
    }

    private int getGoldColor() { return ContextCompat.getColor(context, R.color.gold); }
    private int getWhiteColor() { return ContextCompat.getColor(context, R.color.white); }


    @Override
    public void onBackPressed() {
        backFragment();
    }
    private String getStringValue(int progress) {
        String retVal="";
        if (rule.equals("time")){
            if (progress >= 0 && progress < 10)
                retVal = "0:30";
            else if (progress >=10 && progress < 20)
                retVal = "1:00";
            else if (progress >=20 && progress < 30)
                retVal = "1:30";
            else if (progress >=30 && progress < 40)
                retVal = "2:00";
            else if (progress >=40 && progress < 50)
                retVal = "2:30";
            else if (progress >=50 && progress < 60)
                retVal = "3:00";
            else if (progress >=60 && progress < 70)
                retVal = "3:30";
            else if (progress >=70 && progress < 80)
                retVal = "4:00";
            else if (progress >=80 && progress < 90)
                retVal = "4:30";
            else if (progress >=90 && progress <= 100)
                retVal = "5:00";
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

    public void endGame(String players, String result){
        String tmp[] = players.split("  ");
        String res[] = result.split(":");
        int result1;
        if (Integer.parseInt(res[0])>Integer.parseInt(res[1])){
            result1=1;
        }else if (Integer.parseInt(res[1])>Integer.parseInt(res[0])){
            result1 = 2;
        }else{
            result1=0;
        }
        model.addScore(tmp[0], tmp[2],result,result1);
        pushFragment(defaultFragment);
        FragmentManager fm  = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        backSetInvisible();
        ScoresFragment scoresFragment = ScoresFragment.newInstance(context, players);
        ft.replace(R.id.frame, scoresFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100){

            SharedPreferences sharedPreferences = getSharedPreferences("resume",MODE_PRIVATE);
            String json = sharedPreferences.getString("ResumeStorage", "");
            if (json.equals("")){
                SharedPreferences preferences = context.getSharedPreferences("resume", MODE_PRIVATE);
                preferences.edit().clear().commit();
                endGame(data.getStringExtra("players"),data.getStringExtra("result"));
                showResume = false;

            }else{

                showResume = true;
            }



        }
    }
}
