package com.example.cira.pocketsoccer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SpeedFragment extends Fragment {

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private Fragment fragment = this;
    private static Context context;
    private  LinearLayout linearLayout;
    private int currSpeed;
    private ArrayList<TextView> speeds = new ArrayList<TextView>();

    public SpeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param con Parameter 2.
     * @return A new instance of fragment RulesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpeedFragment newInstance(Context con) {
        SpeedFragment fragment = new SpeedFragment();
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
        View view =  inflater.inflate(R.layout.fragment_speed, container, false);
        TextView tv1 = view.findViewById(R.id.speed1);
        TextView tv2 = view.findViewById(R.id.speed2);
        TextView tv3 = view.findViewById(R.id.speed3);
        TextView tv4 = view.findViewById(R.id.speed4);
        TextView tv5 = view.findViewById(R.id.speed5);
        speeds.add(tv1); speeds.add(tv2); speeds.add(tv3); speeds.add(tv4); speeds.add(tv5);

        setOnTouchListener(tv1);
        setOnTouchListener(tv2);
        setOnTouchListener(tv3);
        setOnTouchListener(tv4);
        setOnTouchListener(tv5);

        // set last settings or default
        sharedPreferences = context.getSharedPreferences("lastValues", MODE_PRIVATE);
        int s = sharedPreferences.getInt("speed", -1);
        if (s != -1){
            ((MainActivity)context).setGameSpeed(s);
            speeds.get(s-1).setTextColor(getGoldColor());
        }else{
            SharedPreferences sp = context.getSharedPreferences("defaultValues", MODE_PRIVATE);
            currSpeed = sp.getInt("speed", -1);
            ((MainActivity)context).setGameSpeed(currSpeed);
            speeds.get(currSpeed-1).setTextColor(getGoldColor());
        }

        linearLayout = view.findViewById(R.id.linear_layout_speed);
        return view;
    }
    private void setOnTouchListener(TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView typedView = (TextView) v;
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.click_sound);
                mp.start();
                for (int i = 0; i < speeds.size(); i++){
                    if (typedView == speeds.get(i)){
                        editor = sharedPreferences.edit();
                        editor.putInt("speed", Integer.parseInt(typedView.getText().toString()));
                        editor.commit();
                        ((MainActivity)context).setGameSpeed(Integer.parseInt(typedView.getText().toString()));
                    }
                        currSpeed = i+1;

                    speeds.get(i).setTextColor(getWhiteColor());
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
