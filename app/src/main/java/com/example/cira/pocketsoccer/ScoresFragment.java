package com.example.cira.pocketsoccer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import beans.Score;


public class ScoresFragment extends Fragment {
    private final static int N = 4;
    private static Context context;
    private TextView page;
    private ArrayList<Score> scores = new ArrayList<Score>();
    private TextView row1;
    private TextView row2;
    private  TextView row3;
    private ImageButton delete;
    private  TextView row4;
    private TextView date1;
    private TextView date2;
    private  TextView date3;
    private  TextView date4;
    private int currPage = 0;
    private int pageNum;
    private String player1, player2;
    private static String text;


    public ScoresFragment() {
        // Required empty public constructor
    }


    public static ScoresFragment newInstance(Context con, String txt) {
        ScoresFragment fragment = new ScoresFragment();
        context = con;
        text = txt;
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
        View view = inflater.inflate(R.layout.fragment_scores, container, false);
        row1 = view.findViewById(R.id.row1);
        row2 = view.findViewById(R.id.row2);
        row3 = view.findViewById(R.id.row3);
        row4 = view.findViewById(R.id.row4);
        date1 = view.findViewById(R.id.date1);
        date2 = view.findViewById(R.id.date2);
        date3 = view.findViewById(R.id.date3);
        date4 = view.findViewById(R.id.date4);
        delete = view.findViewById(R.id.delete);

        ImageButton left = view.findViewById(R.id.left);
        ImageButton right = view.findViewById(R.id.right);

        page = view.findViewById(R.id.page_num);
        TextView back = view.findViewById(R.id.back);

        String tmp[] = text.split("  ");
        player1 = tmp[0];
         player2 = tmp[2];
        scores = ((MainActivity)context).getModel().getAllScores(player1, player2);
        pageNum = scores.size()/N;
        if (scores.size()%N > 0)
            pageNum++;

        for (int i = 0; i < N; i++){
            if (i < scores.size()) {
                switch(i){
                    case 0:
                        if (scores.get(i).getHost() == 1){
                            row1.setText(scores.get(i).getName1()+" "+scores.get(i).getScore()+" "+scores.get(i).getName2());
                        }else{
                            row1.setText(scores.get(i).getName2()+" "+scores.get(i).getScore()+" "+scores.get(i).getName1());
                        }
                        date1.setText(scores.get(i).getDate());
                        break;
                    case 1:
                        if (scores.get(i).getHost() == 1){
                            row2.setText(scores.get(i).getName1()+" "+scores.get(i).getScore()+" "+scores.get(i).getName2());
                        }else{
                            row2.setText(scores.get(i).getName2()+" "+scores.get(i).getScore()+" "+scores.get(i).getName1());
                        }
                        date2.setText(scores.get(i).getDate());
                        break;
                    case 2:                     if (scores.get(i).getHost() == 1){
                        row3.setText(scores.get(i).getName1()+" "+scores.get(i).getScore()+" "+scores.get(i).getName2());
                    }else{
                        row3.setText(scores.get(i).getName2()+" "+scores.get(i).getScore()+" "+scores.get(i).getName1());
                    }
                        date3.setText(scores.get(i).getDate());
                    break;
                    case 3:                    if (scores.get(i).getHost() == 1){
                        row4.setText(scores.get(i).getName1()+" "+scores.get(i).getScore()+" "+scores.get(i).getName2());
                    }else{
                        row4.setText(scores.get(i).getName2()+" "+scores.get(i).getScore()+" "+scores.get(i).getName1());
                    }
                        date4.setText(scores.get(i).getDate());
                    break;
                }
            }


        }

        page.setText(1+"/"+pageNum);

        setOnTouchListener(row1);
        setOnTouchListener(row2);
        setOnTouchListener(row3);
        setOnTouchListener(row4);
        setOnTouchListener(back);
        setOnTouchListener(left);
        setOnTouchListener(right);
        setOnTouchListener(delete);
        TextView player1_win = view.findViewById(R.id.player1_win);
        TextView player2_win = view.findViewById(R.id.player2_win);

        int[] nums = ((MainActivity)context).getModel().getNumWinOfPlayers(player1, player2);
        player1_win.setText(player1+" won "+nums[0]+" times");
        player2_win.setText(player2+" won "+nums[1]+" times");

        return view;
    }


    private void setOnTouchListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.click_sound);
                mp.start();

                //((MainActivity)context).pushFragment(fragment);
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                switch(v.getId()){
                    case R.id.delete:
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_delete_black_24dp));

                        AlertDialog diaBox = AskOption();
                        diaBox.show();
                        break;
                    case R.id.back:
                        ((MainActivity)context).backFragment();
                        break;
                    case R.id.row1:
                        ((TextView)v).setTextColor(getWhiteColor());
                        break;
                    case R.id.row2:
                        ((TextView)v).setTextColor(getWhiteColor());
                        break;
                    case R.id.row3:
                        ((TextView)v).setTextColor(getWhiteColor());
                        break;
                    case R.id.row4:
                        ((TextView)v).setTextColor(getWhiteColor());
                        break;
                    case R.id.left:
                        if (currPage > 0){
                            currPage--;
                            placeScores(currPage);
                            page.setText((currPage+1)+"/"+pageNum);
                        }
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));

                        break;
                    case R.id.right:
                        if (currPage+1 < pageNum){
                            currPage++;
                            placeScores(currPage);
                            page.setText((currPage+1)+"/"+pageNum);
                        }
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));

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
                            case R.id.delete:
                                ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_delete_gold_24dp));
                                break;
                            case R.id.left:
                                ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_left_gold_24dp));
                                break;
                            case R.id.right:
                                ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_right_gold_24dp));
                                break;
                            default: ((TextView)v).setTextColor(getGoldColor());
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
    private void placeScores(int currPage) {
        for (int i = 0; i < N; i++){
            int index = i + currPage * N;
            if (index < scores.size()) {
                switch (i) {
                    case 0:
                        if (scores.get(index).getHost() == 1) {
                            row1.setText(scores.get(index).getName1() + " " + scores.get(index).getScore() + " " + scores.get(index).getName2());
                        } else {
                            row1.setText(scores.get(index).getName2() + " " + scores.get(index).getScore() + " " + scores.get(index).getName1());
                        }
                        date1.setText(scores.get(index).getDate());
                        break;
                    case 1:
                        if (scores.get(index).getHost() == 1) {
                            row2.setText(scores.get(index).getName1() + " " + scores.get(index).getScore() + " " + scores.get(index).getName2());
                        } else {
                            row2.setText(scores.get(index).getName2() + " " + scores.get(index).getScore() + " " + scores.get(index).getName1());
                        }
                        date2.setText(scores.get(index).getDate());
                        break;
                    case 2:
                        if (scores.get(index).getHost() == 1) {
                            row3.setText(scores.get(index).getName1() + " " + scores.get(index).getScore() + " " + scores.get(index).getName2());
                        } else {
                            row3.setText(scores.get(index).getName2() + " " + scores.get(index).getScore() + " " + scores.get(index).getName1());
                        }
                        date3.setText(scores.get(index).getDate());
                        break;
                    case 3:
                        if (scores.get(index).getHost() == 1) {
                            row4.setText(scores.get(index).getName1() + " " + scores.get(index).getScore() + " " + scores.get(index).getName2());
                        } else {
                            row4.setText(scores.get(index).getName2() + " " + scores.get(index).getScore() + " " + scores.get(index).getName1());
                        }
                        date4.setText(scores.get(index).getDate());
                        break;
                }
            }else{
                switch(i){
                    case 1:

                        row2.setText("");

                        date2.setText("");
                        break;
                    case 2:

                        row3.setText("");

                        date3.setText("");
                        break;
                    case 3:

                        row4.setText("");

                        date4.setText("");
                        break;
                }

            }
        }
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Delete permanently?")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((MainActivity)context).getModel().deleteAllScores(player1, player2);
                        ((MainActivity)context).getModel().deleteAllWinsForPlayers(player1, player2);

                        dialog.dismiss();
                        scores.clear();

                        ((MainActivity)context).backFragment();
                    }

                })



                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
    private int getGoldColor() { return ContextCompat.getColor(context, R.color.gold); }
    private int getWhiteColor() { return ContextCompat.getColor(context, R.color.white); }


}
