package com.example.cira.pocketsoccer;

import android.app.ActionBar;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

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


    private int getGoldColor() { return ContextCompat.getColor(context, R.color.gold); }
    private int getWhiteColor() { return ContextCompat.getColor(context, R.color.white); }
}
