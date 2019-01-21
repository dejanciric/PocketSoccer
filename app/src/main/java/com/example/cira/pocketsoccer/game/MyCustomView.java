package com.example.cira.pocketsoccer.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import com.example.cira.pocketsoccer.R;

public class MyCustomView extends AppCompatImageView {

    private static final int PLAYER_SIZE = 180;
    private static final int BALL_SIZE = 180;
    private static final int MAX = 250;

    public Figure[] figures = new Figure[7];
    private int[] stateFlags = {R.drawable.state1,R.drawable.state2,R.drawable.state3,R.drawable.state4};


    private int width, height;
    private Bitmap background;
    private int field, gameSpeed, currState1, currState2;
    private String rule,time, goals, player1Name,player2Name;
    private Paint paint;
    private Figure selectedFigure;


    public MyCustomView(Context context) {
        super(context);
        init();

    }

    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        height = getHeight();
        width = getWidth();

        paint = new Paint();
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawBitmap(background, 0, 0, paint);

        for(int i =0; i < figures.length; i++){
            canvas.drawBitmap(figures[i].bitmap, figures[i].getX(),  figures[i].getY(), paint);

            if (figures[i].selected){
                paint.setColor(Color.GRAY);
                paint.setAlpha(150);
                canvas.drawCircle(selectedFigure.x, selectedFigure.y, selectedFigure.r+15,paint);
                paint = new Paint();
            }

        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        if (field==0){
            background = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
        }else if (field == 1){
            background = BitmapFactory.decodeResource(getResources(), R.drawable.concrete);
        }else{
            background = BitmapFactory.decodeResource(getResources(), R.drawable.parquet);
        }
        background = Bitmap.createScaledBitmap(background, width, height, false);

        initialPositions();
        if (rule == "goals"){
            (new MyThread(figures,gameSpeed,time, this, width, height)).start();
        }else{
            (new MyThread(figures,gameSpeed, this, width, height)).start();
       }

    }

    public void initialPositions(){

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), stateFlags[currState1]);
        bitmap = Bitmap.createScaledBitmap(bitmap, PLAYER_SIZE, PLAYER_SIZE, false);

        figures[0] = new Figure(0.15f*width, 0.15f*height, bitmap, "player", player1Name);
        figures[1] = new Figure(0.3f*width, 0.5f*height, bitmap, "player", player1Name);
        figures[2] = new Figure(0.15f*width, 0.85f*height, bitmap, "player", player1Name);

        bitmap = BitmapFactory.decodeResource(getResources(), stateFlags[currState2]);
        bitmap = Bitmap.createScaledBitmap(bitmap, PLAYER_SIZE, PLAYER_SIZE, false);


        figures[3] = new Figure(0.85f*width, 0.15f*height, bitmap, "player", player2Name);
        figures[4] = new Figure(0.7f*width, 0.5f*height, bitmap, "player", player2Name);
        figures[5] = new Figure(0.85f*width, 0.85f*height, bitmap, "player", player2Name);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        bitmap = Bitmap.createScaledBitmap(bitmap, BALL_SIZE, BALL_SIZE, false);

        figures[6] = new Figure(0.5f*width, 0.5f*height, bitmap, "ball", "ball");


    }

    public void setParams(int field, String rule, String time, String goals, int gameSpeed, int currState1, int currState2, String player1Name, String player2Name) {
        this.field = field;
        this.rule = rule;
        this.time = time;
        this.goals= goals;
        this.gameSpeed = gameSpeed;
        this.currState1 = currState1;
        this.currState2 = currState2;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    public void down(float x, float y) {
        for (int i = 0; i < figures.length-1; i++){
            if (x <= figures[i].x+figures[i].r && x >= figures[i].x-figures[i].r && y <= figures[i].y+figures[i].r && y >= figures[i].y-figures[i].r){
                selectedFigure = figures[i];
                selectedFigure.selected = true;
                break;
            }
        }
    }

    public void up(float x, float y) {
        for (int i = 0; i < figures.length-1; i++){
            if (figures[i].selected){
                figures[i].selected = false;
                //dorada

                float distance = (float)Math.sqrt((y -  figures[i].y) * (y -  figures[i].y) + (x - figures[i].x) * (x -  figures[i].x));

                figures[i].dx = (x - figures[i].x)*0.07f;
                figures[i].dy = (y - figures[i].y)*0.07f;
                if (distance > MAX){
                    float div = MAX/distance;
                    figures[i].dx *= div;
                    figures[i].dy *= div;
                }

                figures[i].calcSteps();
            }
        }
    }
}
