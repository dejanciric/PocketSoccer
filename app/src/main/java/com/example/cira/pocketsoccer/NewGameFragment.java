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


public class NewGameFragment extends Fragment {

    private static Context context;
    private Fragment fragment = this;

    public static NewGameFragment newInstance(Context con) {
        NewGameFragment fragment = new NewGameFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_game, container, false);
        TextView singleplayer = view.findViewById(R.id.singleplayer);
        TextView multiplayer = view.findViewById(R.id.multiplayer);
        ((MainActivity)context).backSetVisible();
        setOnTouchListener(singleplayer);
        setOnTouchListener(multiplayer);

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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();;
                switch(v.getId()){
                    case R.id.singleplayer:
                        StartGameFragment startGameFragment = StartGameFragment.newInstance(context, "singleplayer");
                        fragmentTransaction.replace(R.id.frame, startGameFragment);
                        break;
                    case R.id.multiplayer:
                        StartGameFragment startGameFragment2 = StartGameFragment.newInstance(context, "multiplayer");
                        fragmentTransaction.replace(R.id.frame, startGameFragment2);
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
