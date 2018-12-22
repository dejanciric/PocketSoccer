package com.example.cira.pocketsoccer;

import android.content.Context;
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

import beans.Score;


public class DefaultFragment extends Fragment {

    private static Context context;
    private Fragment fragment = this;

    public DefaultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param con Parameter 1.
     * @return A new instance of fragment DefaultFragment.
     */
    public static DefaultFragment newInstance(Context con) {
        DefaultFragment fragment = new DefaultFragment();
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
        View view = inflater.inflate(R.layout.fragment_default, container, false);

        ((MainActivity)context).backSetInvisible();

        TextView resume = view.findViewById(R.id.resume);
        TextView newGame = view.findViewById(R.id.new_game);
        TextView statistics = view.findViewById(R.id.statistics);
        TextView settings = view.findViewById(R.id.settings);

        setOnTouchListener(resume);
        setOnTouchListener(newGame);
        setOnTouchListener(settings);
        setOnTouchListener(statistics);

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
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                switch(v.getId()){
                    case R.id.resume:
                        break;
                    case R.id.new_game:
                        break;
                    case R.id.statistics:
                        StatisticFragment statisticFragment = StatisticFragment.newInstance(context);
                        fragmentManager = getActivity().getSupportFragmentManager();fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, statisticFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.settings:
                        SettingsFragment settingsFragment = SettingsFragment.newInstance(context);
                        fragmentManager = getActivity().getSupportFragmentManager();fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, settingsFragment);
                        fragmentTransaction.commit();
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
