package com.example.cira.pocketsoccer;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private int gameSpeed;
    private String rule;
    private int field, timeProgress, goalsProgress;;
    private String endGameValue;
    private Context context = this;
    private Stack<Fragment> stackTrace = new Stack<Fragment>();
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        DefaultFragment defaultFragment = DefaultFragment.newInstance(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, defaultFragment);
        fragmentTransaction.commit();

        back = findViewById(R.id.back);
        setOnTouchListener(back);

        //setDefaultValues();
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
        Log.i("valll", field+"");
        Fragment fragment = stackTrace.pop();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

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
}
