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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private static Context context;
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param con Parameter 1.
     * @return A new instance of fragment SettingsFragment.
     */
    private Fragment fragment = this;

    public static SettingsFragment newInstance(Context con) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ((MainActivity)context).backSetVisible();

        TextView field = view.findViewById(R.id.field);
        TextView rules = view.findViewById(R.id.rules);
        TextView speed = view.findViewById(R.id.speed);
        TextView reset = view.findViewById(R.id.reset);

        setOnTouchListener(field);
        setOnTouchListener(rules);
        setOnTouchListener(speed);
        setOnTouchListener(reset);

        return view;
    }

    private void setOnTouchListener(TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.click_sound);
                mp.start();
                ((TextView)v).setTextColor(getWhiteColor());
                ((MainActivity)context).pushFragment(fragment);


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch(v.getId()){
                    case R.id.field:
                        FieldFragment settingsFragment = FieldFragment.newInstance(context);
                        fragmentTransaction.replace(R.id.frame, settingsFragment);
                        break;
                    case R.id.rules:
                        RulesFragment rulesFragment = RulesFragment.newInstance(context);
                        fragmentTransaction.replace(R.id.frame, rulesFragment);

                        break;
                    case R.id.speed:
                        SpeedFragment speedFragment = SpeedFragment.newInstance(context);
                        fragmentTransaction.replace(R.id.frame, speedFragment);
                        break;
                    case R.id.reset:
                        ((MainActivity)context).popFragment();
                       /* SharedPreferences sharedPreferences = context.getSharedPreferences("lastValues", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("speed", 3);
                        editor.putString("rule", "goals");
                        editor.putInt("timeProgress", 10);
                        editor.putInt("goalsProgress", 20);
                        editor.putInt("field", 0);
                        editor.commit();*/
                        ((MainActivity)context).setField(0);
                        ((MainActivity)context).setGoalsProgress(20);
                        ((MainActivity)context).setTimeProgress(10);
                        ((MainActivity)context).rule = "goals";
                        ((MainActivity)context).gameSpeed = 3;

                        SharedPreferences preferences = context.getSharedPreferences("lastValues", MODE_PRIVATE);
                        preferences.edit().clear().commit();
                       // setAllParamsToDefault();
                        break;
                }
                fragmentTransaction.commit();
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


    private int getGoldColor() { return ContextCompat.getColor(context, R.color.gold); }
    private int getWhiteColor() { return ContextCompat.getColor(context, R.color.white); }

}
