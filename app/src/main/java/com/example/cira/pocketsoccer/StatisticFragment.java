package com.example.cira.pocketsoccer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
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
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import beans.Win;


public class StatisticFragment extends Fragment {
    private static final int N = 4;
    private static Context context;
    private TextView page;
    private ArrayList<Win> wins = new ArrayList<Win>();
    private TextView row1;
    private TextView row2;
    private  TextView row3;
    private ImageButton delete;
    private  TextView row4;
    private int currPage = 0;
    private int pageNum;
    private Fragment fragment = this;

    public StatisticFragment() {
        // Required empty public constructor
    }


    public static StatisticFragment newInstance(Context con) {
        StatisticFragment fragment = new StatisticFragment();
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
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        row1 = view.findViewById(R.id.row1);
         row2 = view.findViewById(R.id.row2);
         row3 = view.findViewById(R.id.row3);
         row4 = view.findViewById(R.id.row4);
        delete = view.findViewById(R.id.delete);
        ImageButton left = view.findViewById(R.id.left);
        ImageButton right = view.findViewById(R.id.right);

        page = view.findViewById(R.id.page_num);
        TextView back = view.findViewById(R.id.back);


        wins = ((MainActivity)context).getModel().getAllWins();
        pageNum = wins.size()/N;
        if (wins.size()%N > 0)
            pageNum++;

        if (pageNum == 0)
            pageNum = 1;
        placeWins(currPage);
       /* for (int i = 0; i < N; i++){
            if (i < wins.size()){
                switch(i){
                    case 0: row1.setText(wins.get(i).getName1()+"  "+wins.get(i).getWin1()+ ":" + wins.get(i).getWin2()+"  "+wins.get(i).getName2());
                        break;
                    case 1: row2.setText(wins.get(i).getName1()+"  "+wins.get(i).getWin1()+ ":" + wins.get(i).getWin2()+"  "+wins.get(i).getName2());
                        break;
                    case 2: row3.setText(wins.get(i).getName1()+"  "+wins.get(i).getWin1()+ ":" + wins.get(i).getWin2()+"  "+wins.get(i).getName2());
                        break;
                    case 3: row4.setText(wins.get(i).getName1()+"  "+wins.get(i).getWin1()+ ":" + wins.get(i).getWin2()+"  "+wins.get(i).getName2());
                        break;
                }
            }


        }*/

        page.setText((currPage+1)+"/"+pageNum);

        setOnTouchListener(row1);
        setOnTouchListener(row2);
        setOnTouchListener(row3);
        setOnTouchListener(row4);
        setOnTouchListener(back);
        setOnTouchListener(delete);
        setOnTouchListener(left);
        setOnTouchListener(right);

        return view;
    }

    private void setOnTouchListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.click_sound);
                mp.start();

                //((MainActivity)context).pushFragment(fragment);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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
                        if (!row1.getText().toString().equals("")){
                            ((MainActivity)context).pushFragment(fragment);
                            ((TextView)v).setTextColor(getWhiteColor());
                            ScoresFragment scoresFragment = ScoresFragment.newInstance(context, row1.getText().toString());
                            fragmentTransaction.replace(R.id.frame, scoresFragment);
                        }

                        break;
                    case R.id.row2:
                        if (!row2.getText().toString().equals("")){
                            ((MainActivity)context).pushFragment(fragment);
                            ((TextView)v).setTextColor(getWhiteColor());
                            ScoresFragment scoresFragment1 = ScoresFragment.newInstance(context, row2.getText().toString());
                            fragmentTransaction.replace(R.id.frame, scoresFragment1);
                        }

                        break;
                    case R.id.row3:
                        if (!row3.getText().toString().equals("")){
                            ((MainActivity)context).pushFragment(fragment);
                            ((TextView)v).setTextColor(getWhiteColor());
                            ScoresFragment scoresFragment2 = ScoresFragment.newInstance(context, row3.getText().toString());
                            fragmentTransaction.replace(R.id.frame, scoresFragment2);
                        }

                        break;
                    case R.id.row4:
                        if (!row4.getText().toString().equals("")){

                            ((MainActivity)context).pushFragment(fragment);
                            ((TextView)v).setTextColor(getWhiteColor());
                            ScoresFragment scoresFragment3 = ScoresFragment.newInstance(context, row4.getText().toString());
                            fragmentTransaction.replace(R.id.frame, scoresFragment3);
                        }

                        break;
                    case R.id.left:
                        if (currPage > 0){
                            currPage--;
                            placeWins(currPage);
                            page.setText((currPage+1)+"/"+pageNum);
                        }
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));

                        break;
                    case R.id.right:
                        if (currPage+1 < pageNum){
                            currPage++;
                            placeWins(currPage);
                            page.setText((currPage+1)+"/"+pageNum);
                        }
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));

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

    @Override
    public void onPause() {
        super.onPause();
        //currPage = 0;

    }

    private void placeWins(int currPage) {
        if(wins.size() == 0){
            row2.setText("Empty statistics");
            return;
        }
        for (int i = 0; i < N; i++) {
            int index = i + currPage * N;
            if (index < wins.size()) {
                switch (i) {
                    case 0:
                        row1.setText(wins.get(index).getName1() + "  " + wins.get(index).getWin1() + ":" + wins.get(index).getWin2() + "  " + wins.get(index).getName2());
                        break;
                    case 1:
                        row2.setText(wins.get(index).getName1() + "  " + wins.get(index).getWin1() + ":" + wins.get(index).getWin2() + "  " + wins.get(index).getName2());
                        break;
                    case 2:
                        row3.setText(wins.get(index).getName1() + "  " + wins.get(index).getWin1() + ":" + wins.get(index).getWin2() + "  " + wins.get(index).getName2());
                        break;
                    case 3:
                        row4.setText(wins.get(index).getName1() + "  " + wins.get(index).getWin1() + ":" + wins.get(index).getWin2() + "  " + wins.get(index).getName2());
                        break;
                }

            }else{
                switch (i){
                    case 0: row1.setText("");
                        break;
                    case 1: row2.setText("");
                        break;
                    case 2: row3.setText("");
                        break;
                    case 3: row4.setText("");
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
                        ((MainActivity)context).getModel().deleteAllWins();
                        ((MainActivity)context).getModel().deleteAllScoresForReal();
                        dialog.dismiss();
                        wins.clear();
                        currPage = 0;
                        StatisticFragment statisticFragment = StatisticFragment.newInstance(context);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame, statisticFragment);
                            fragmentTransaction.commit();

                        //((MainActivity)context).backFragment();
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
