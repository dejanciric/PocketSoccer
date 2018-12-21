package com.example.cira.pocketsoccer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class FieldFragment extends Fragment {
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private static Context context;
    private ImageView field;
    private Fragment fragment = this;
    private int curr;

    private ArrayList<Integer> fields = new ArrayList<Integer>();
    public FieldFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param con Parameter 2.
     * @return A new instance of fragment FieldFragment.
     */
    public static FieldFragment newInstance(Context con) {
        FieldFragment fragment = new FieldFragment();
        context = con;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fields.add(R.drawable.field1);
        fields.add(R.drawable.field2);
        curr = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_field, container, false);
        ImageButton left = view.findViewById(R.id.left);
        ImageButton right = view.findViewById(R.id.right);
        field = view.findViewById(R.id.field);

        setOnTouchListener(left);
        setOnTouchListener(right);

        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.click_sound);
                mp.start();
                ((MainActivity)context).backFragment();
            }
        });
        // set last settings or default
         sharedPreferences = context.getSharedPreferences("lastValues", MODE_PRIVATE);
        int s = sharedPreferences.getInt("field", -1);
        if (s != -1){
            ((MainActivity)context).setField(s);
            field.setImageResource(fields.get(s));
            curr = s;
        }else{
            ((MainActivity)context).setField(0);
            field.setImageResource(fields.get(0));
            curr=  0;
        }

        return view;
    }

    private void setOnTouchListener(ImageButton view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.click_sound);
                mp.start();

                switch(v.getId()){
                    case R.id.left:
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));
                        curr--;
                        if (curr < 0)
                            curr = fields.size()-1;
                        field.setImageResource(fields.get(curr));
                        ((MainActivity)context).setField(curr);

                        editor = sharedPreferences.edit();
                        editor.putInt("field", curr);
                        editor.commit();
                        break;
                    case R.id.right:
                        ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
                        curr = (curr+1) % fields.size();
                        ((MainActivity)context).setField(curr);

                        field.setImageResource(fields.get(curr));
                        editor = sharedPreferences.edit();
                        editor.putInt("field",curr);
                        editor.commit();
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
                            case R.id.left:
                                ((ImageButton)v).setBackground(getResources().getDrawable(R.drawable.ic_chevron_left_gold_24dp));
                                break;
                            case R.id.right:
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
