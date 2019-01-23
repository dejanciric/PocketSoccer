package com.example.cira.pocketsoccer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.cira.pocketsoccer.game.SingleplayerActivity;

import static android.content.Context.MODE_PRIVATE;


public class StartGameFragment extends Fragment {

    private static Context context;
    private static String type;
    private TextView player1Name;
    private TextView player2Name;
    private ImageButton player1Left;
    private ImageButton player2Left;
    private ImageButton player1Right;
    private ImageButton player2Right;
    private ImageButton player1Flag;
    private ImageButton player2Flag;
    private TextView player1FlagName;
    private TextView player2FlagName;
    private RadioButton player1Radio;
    private RadioButton player2Radio;
    private TextView startGame;

    private String[] stateNames = {"Serbia", "Russia", "Brazil", "Greece"};
    private int currState1 = 0;
    private int currState2 = 0;
    private Drawable[] stateFlags = new Drawable[4];

    public StartGameFragment() {
        // Required empty public constructor
    }

    public static StartGameFragment newInstance(Context con, String t) {
        StartGameFragment fragment = new StartGameFragment();
        context = con;
        type = t;
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
        View view = inflater.inflate(R.layout.fragment_start_game, container, false);
        //((MainActivity)context).backSetInvisible();
        player1Name= view.findViewById(R.id.player1_name);
        player2Name= view.findViewById(R.id.player2_name);
        player1Left= view.findViewById(R.id.player1_left);
        player2Left= view.findViewById(R.id.player2_left);
        player1Right= view.findViewById(R.id.player1_right);
        player2Right= view.findViewById(R.id.player2_right);
        player1Flag= view.findViewById(R.id.player1_flag);
        player2Flag= view.findViewById(R.id.player2_flag);
        player1FlagName= view.findViewById(R.id.player1_flag_name);
        player2FlagName= view.findViewById(R.id.player2_flag_name);
        player1Radio= view.findViewById(R.id.player1_radio);
        player2Radio= view.findViewById(R.id.player2_radio);
        startGame= view.findViewById(R.id.start_game);
        stateFlags[0] = view.getResources().getDrawable(R.drawable.state1);
        stateFlags[1] = view.getResources().getDrawable(R.drawable.state2);
        stateFlags[2] = view.getResources().getDrawable(R.drawable.state3);
        stateFlags[3] = view.getResources().getDrawable(R.drawable.state4);
        if (type.equals("multiplayer")){
            player1Radio.setVisibility(View.GONE);
            player2Radio.setVisibility(View.GONE);
        }else{
            player2Name.setText("COMPUTER");
            player2Radio.setChecked(true);
        }

        setOnTouchListener(player1Left);
        setOnTouchListener(player2Left);
        setOnTouchListener(player1Right);
        setOnTouchListener(player2Right);
        setOnTouchListener(player1Radio);
        setOnTouchListener(player2Radio);
        setOnTouchListener(startGame);
        return view;

    }

    private void setOnTouchListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.click_sound);
                mp.start();

                switch(v.getId()){
                    case R.id.start_game:
                        ((TextView)v).setTextColor(getWhiteColor());
                        // pokupi podatke sve i nova aktivnost za igru
                        break;
                    case R.id.player1_radio:
                        ((RadioButton)v).setTextColor(getWhiteColor());
                        player2Radio.setChecked(false);
                        String tmp = player1Name.getText().toString();
                        player1Name.setText(player2Name.getText().toString());
                        player2Name.setText(tmp);
                        break;
                    case R.id.player2_radio:
                        ((RadioButton)v).setTextColor(getWhiteColor());
                        player1Radio.setChecked(false);
                        String tmp1 = player2Name.getText().toString();
                        player2Name.setText(player1Name.getText().toString());
                        player1Name.setText(tmp1);
                        break;
                    case R.id.player1_left:
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));
                        currState1--;
                        if (currState1 == -1)
                            currState1 = stateNames.length-1;
                        player1Flag.setBackground(stateFlags[currState1]);
                        player1FlagName.setText(stateNames[currState1]);
                        break;
                    case R.id.player2_left:
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));
                        currState2--;
                        if (currState2 == -1)
                            currState2 = stateNames.length-1;
                        player2Flag.setBackground(stateFlags[currState2]);
                        player2FlagName.setText(stateNames[currState2]);
                        break;
                    case R.id.player1_right:
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
                        currState1 = (currState1+1)%stateNames.length;
                        player1Flag.setBackground(stateFlags[currState1]);
                        player1FlagName.setText(stateNames[currState1]);
                        break;
                    case R.id.player2_right:
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
                        currState2 = (currState2+1)%stateNames.length;
                        player2Flag.setBackground(stateFlags[currState2]);
                        player2FlagName.setText(stateNames[currState2]);
                        break;
                }

            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        switch(v.getId()){
                            case R.id.start_game:
                                ((TextView)v).setTextColor(getGoldColor());
                                // nova aktivnost za igru
                                SharedPreferences sharedPreferences = context.getSharedPreferences("lastValues", MODE_PRIVATE);
                                ((MainActivity)context).field = sharedPreferences.getInt("field", -1);
                                if (((MainActivity)context).field == -1){
                                    ((MainActivity)context).field = 0;
                                }
                                ((MainActivity)context).gameSpeed =  sharedPreferences.getInt("speed", -1);
                                if (((MainActivity)context).gameSpeed == -1){
                                    ((MainActivity)context).gameSpeed = 3;
                                }
                                ((MainActivity)context).rule = sharedPreferences.getString("rule", "");
                                if (((MainActivity)context).rule.equals("")){
                                    ((MainActivity)context).rule="goals";
                                }
                                ((MainActivity)context).timeProgress = sharedPreferences.getInt("timeProgress", -1);
                                if(((MainActivity)context).timeProgress==-1){
                                    ((MainActivity)context).timeProgress = 10;
                                }
                                ((MainActivity)context).goalsProgress = sharedPreferences.getInt("goalsProgress", -1);
                                if (((MainActivity)context).goalsProgress == -1){
                                    ((MainActivity)context).goalsProgress= 20;
                                }
                                Intent eksplicitniIntent = new Intent(context, SingleplayerActivity.class);
                                eksplicitniIntent.putExtra("field", ((MainActivity)context).field);
                                eksplicitniIntent.putExtra("rule", ((MainActivity)context).rule);
                                eksplicitniIntent.putExtra("timeProgress", ((MainActivity)context).timeProgress);
                                eksplicitniIntent.putExtra("goalsProgress", ((MainActivity)context).goalsProgress);
                                eksplicitniIntent.putExtra("gameSpeed", ((MainActivity)context).gameSpeed);
                                eksplicitniIntent.putExtra("currState1", currState1);
                                eksplicitniIntent.putExtra("currState2", currState2);
                                eksplicitniIntent.putExtra("player1Name", player1Name.getText().toString());
                                eksplicitniIntent.putExtra("player2Name", player2Name.getText().toString());

                                ((MainActivity) context).startActivityForResult(eksplicitniIntent, 100);

                                break;
                            case R.id.player1_radio:
                                ((RadioButton)v).setTextColor(getGoldColor());
                                break;
                            case R.id.player2_radio:
                                ((RadioButton)v).setTextColor(getGoldColor());

                                break;
                            case R.id.player1_left:
                                ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_left_gold_24dp));

                                break;
                            case R.id.player2_left:
                                ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_left_gold_24dp));


                                break;
                            case R.id.player1_right:
                                ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_right_gold_24dp));


                                break;
                            case R.id.player2_right:
                                ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_right_gold_24dp));


                                break;
                        }

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
