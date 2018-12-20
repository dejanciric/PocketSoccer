package com.example.cira.pocketsoccer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class RulesFragment extends Fragment {

    private Fragment fragment = this;
    private static Context context;
    private TextView value, time, goals;
    private SeekBar seekBar;
    private enum Type { TIME, GOALS};
    private Type currType;
    private String stringValue;
    private int currProgressTime= 10;
    private int currProgressGoals = 20;
    public RulesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param con Parameter 2.
     * @return A new instance of fragment RulesFragment.
     */
    public static RulesFragment newInstance(Context con) {
        RulesFragment fragment = new RulesFragment();
        context = con;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_rules, container, false);
        time = view.findViewById(R.id.time);
        goals = view.findViewById(R.id.goals);
        seekBar = view.findViewById(R.id.seekbar);
        value = view.findViewById(R.id.value);

        setOnTouchListener(time);
        setOnTouchListener(goals);

        // perform seek bar change listener event used for getting the progress value
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stringValue = getStringValue(progress);
                value.setText(stringValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                value.setText(stringValue);
            }
        });

        // ovo zameniti sa sharedpreference
        currType = Type.TIME;
        seekBar.setProgress(currProgressTime);
        time.setTextColor(getGoldColor());
        stringValue = getStringValue(currProgressTime);
        value.setText(stringValue);

        return view;
    }

    private String getStringValue(int progress) {
        String retVal="";
        if (currType == Type.TIME){
            currProgressTime = progress;
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
            currProgressGoals = progress;
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

    private void setOnTouchListener(TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView typedView = (TextView) v;
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.click_sound);
                mp.start();
                switch(v.getId()){
                    case R.id.time:
                        goals.setTextColor(getWhiteColor());
                        currType = Type.TIME;
                        seekBar.setProgress(currProgressTime);
                        break;
                    case R.id.goals:
                        time.setTextColor(getWhiteColor());
                        currType = Type.GOALS;
                        seekBar.setProgress(currProgressGoals);
                        break;
                }

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
                        ((TextView)v).setTextColor(getGoldColor());
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private int getGoldColor() { return ContextCompat.getColor(context, R.color.gold); }
    private int getWhiteColor() { return ContextCompat.getColor(context, R.color.white); }

}
