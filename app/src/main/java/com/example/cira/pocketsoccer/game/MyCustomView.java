package com.example.cira.pocketsoccer.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.cira.pocketsoccer.MainActivity;
import com.example.cira.pocketsoccer.R;

import static android.content.Context.MODE_PRIVATE;

public class MyCustomView extends AppCompatImageView {
    MediaPlayer mpSecond;
    private static final int PLAYER_SIZE = 180;
    private static final int BALL_SIZE = 90;
    private static final int MAX = 250;
    private boolean pressed = false;
    private  Bitmap goalBitmap;
    public MyThread myThread;
    MediaPlayer mpStart;
    public int computer;
    MediaPlayer mpGoal;

    public Figure[] figures = new Figure[7];
    private int[] stateFlags = {R.drawable.state1,R.drawable.state2,R.drawable.state3,R.drawable.state4};
    public boolean restore = false;
    public String winner="";
    public int width, height;
    private Bitmap background, goalsBitmap =null, scoreboard;
    private int field, gameSpeed, currState1, currState2;
    public String rule,time, goals, player1Name,player2Name;
    private Paint paint;
    private Figure selectedFigure;

    public int seconds;
    public String turn;

    public int leftScore=0, rightScore=0;

    GameActivity context;
    public MyCustomView(Context context) {
        super(context);
        init();

    }

    public void setContext(GameActivity context){
        this.context = context;
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

        if(!restore){
            seconds = 5;
            turn="left";
        }

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawBitmap(background, 0, 0, paint);

        paint.setTextSize(200);

        paint.setColor(Color.YELLOW);
        paint.setAlpha(100);
        canvas.drawText(seconds+"",width/2-50,height/2+70,paint);
        paint = new Paint();

        for(int i =0; i < figures.length; i++){
            if (turn.equals("left")){
                if (i > 2 && i < 6){
                    ColorMatrix ma = new ColorMatrix();
                    ma.setSaturation(0);
                    paint.setColorFilter(new ColorMatrixColorFilter(ma));
                    canvas.drawBitmap(figures[i].bitmap, figures[i].getX(),  figures[i].getY(), paint);
                }else{
                    paint = new Paint();
                    canvas.drawBitmap(figures[i].bitmap, figures[i].getX(),  figures[i].getY(), paint);
                }

            }else{
                if (i < 3){
                    ColorMatrix ma = new ColorMatrix();
                    ma.setSaturation(0);
                    paint.setColorFilter(new ColorMatrixColorFilter(ma));
                    canvas.drawBitmap(figures[i].bitmap, figures[i].getX(),  figures[i].getY(), paint);
                }else{
                    paint = new Paint();
                    canvas.drawBitmap(figures[i].bitmap, figures[i].getX(),  figures[i].getY(), paint);
                }

            }


            if (figures[i].selected){
                paint.setColor(Color.GRAY);
                paint.setAlpha(150);
                canvas.drawCircle(selectedFigure.x, selectedFigure.y, selectedFigure.r+15,paint);
                paint = new Paint();
            }

        }
        canvas.drawBitmap(goalsBitmap, 0, 0, paint);
        paint.setAlpha(150);
        canvas.drawBitmap(scoreboard, 0.4f*width, 0,paint);
        paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        paint.setAlpha(150);

        canvas.drawText(leftScore+"",0.435f*width,100,paint);
        canvas.drawText(rightScore+"",0.535f*width,100,paint);
        paint = new Paint();

        if (rule.equals("time")){
            paint.setAlpha(150);
            canvas.drawBitmap(scoreboard, 0.4f*width, 0.87f*height,paint);
            paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.WHITE);
            paint.setAlpha(150);

            canvas.drawText(time,0.435f*width,0.98f*height,paint);
            paint = new Paint();
        }


        if (goalBitmap != null){
            paint.setAlpha(200);
            canvas.drawBitmap(goalBitmap, 0.25f*width, 0.15f*height, paint);
            if (!winner.equals("")){
                paint.setTextSize(100);
                paint.setColor(Color.WHITE);
                if (!winner.equals("draw")){
                    canvas.drawText(winner+" WON!",0.3f*width,0.55f*height,paint);
                }else{
                    canvas.drawText("DRAW",0.43f*width,0.55f*height,paint);
                }

            }

        }
        paint = new Paint();

        /*paint.setColor(Color.WHITE);
        paint.setStrokeWidth(11);
        canvas.drawLine(0f, 0.36f*height,0.055f*width, 0.36f*height, paint);
        canvas.drawLine(0f, 0.64f*height,0.055f*width, 0.64f*height, paint);
        canvas.drawLine(0.94f*width, 0.36f*height,width, 0.36f*height, paint);
        canvas.drawLine(0.94f*width, 0.64f*height,width, 0.64f*height, paint);
        paint = new Paint();*/
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
        if (rule.equals("goals")){
            myThread =new MyThread(figures,gameSpeed,time, this, width, height);
            myThread.start();
        }else{
            myThread =new MyThread(figures,gameSpeed, this, width, height);
            myThread.start();
       }

        if (restore){
            context.restore();

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

        goalsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.goals);
        goalsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.goals);
        goalsBitmap = Bitmap.createScaledBitmap(goalsBitmap, width, height, false);
        //bitmap = Bitmap.createScaledBitmap(bitmap, BALL_SIZE, BALL_SIZE, false);

        scoreboard =  BitmapFactory.decodeResource(getResources(), R.drawable.scoreboard);
        scoreboard = Bitmap.createScaledBitmap(scoreboard, 390, 150, false);
        seconds=5;
        mpStart = MediaPlayer.create(context, R.raw.start_pipe);
        mpStart.start();

    }

    public void restart(){
        for(int i=0; i < 7; i++){
            figures[i].dx= figures[i].dy =0;
        }
        mpStart.start();
        figures[0].x = 0.15f*width;
        figures[0].y = 0.15f*height;

        figures[1].x = 0.3f*width;
        figures[1].y = 0.5f*height;

        figures[2].x = 0.15f*width;
        figures[2].y = 0.85f*height;

        figures[3].x = 0.85f*width;
        figures[3].y = 0.15f*height;

        figures[4].x = 0.7f*width;
        figures[4].y = 0.5f*height;

        figures[5].x = 0.85f*width;
        figures[5].y = 0.85f*height;

        figures[6].x = 0.5f*width;
        figures[6].y = 0.5f*height;

        seconds=5;
    }

    public void setParams(int field, String rule, String time, String goals, int gameSpeed, int currState1, int currState2, String player1Name, String player2Name, GameActivity context, int computer) {
        this.field = field;
        this.rule = rule;
        this.time = time;
        this.goals= goals;
        this.gameSpeed = gameSpeed;
        this.currState1 = currState1;
        this.currState2 = currState2;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.context = context;
        this.computer = computer;

        mpSecond = MediaPlayer.create(context, R.raw.second);
    }

    public void down(float x, float y) {
        if (turn.equals("left")){

                for (int i = 0; i < 3; i++){
                    if (x <= figures[i].x+figures[i].r && x >= figures[i].x-figures[i].r && y <= figures[i].y+figures[i].r && y >= figures[i].y-figures[i].r){
                        selectedFigure = figures[i];
                        selectedFigure.selected = true;
                        pressed = true;
                        if(computer ==1){
                            up(figures[6].x,figures[6].y);
                        }
                        break;
                    }
                }


        }else{

                for (int i = 3; i < figures.length-1; i++){
                    if (x <= figures[i].x+figures[i].r && x >= figures[i].x-figures[i].r && y <= figures[i].y+figures[i].r && y >= figures[i].y-figures[i].r){
                        selectedFigure = figures[i];
                        selectedFigure.selected = true;
                        pressed = true;
                        if(computer ==2){
                            up(figures[6].x,figures[6].y);
                        }
                        break;
                    }
                }



        }

    }


    public void up(float x, float y) {
        if (pressed){
            if (turn.equals("left")){
                if (computer == 2){
                    myThread.pickTimeForBot = true;
                }
                turn = "right";
            }else{
                if (computer == 1){
                    myThread.pickTimeForBot = true;
                }
                turn = "left";
            }
            seconds = 5;
            pressed = false;
        }


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

    public void second() {
        if (winner.equals("")){
            seconds--;
            if (seconds == 0){
                if (turn.equals("left")){
                    if (computer == 2){
                        myThread.pickTimeForBot = true;
                    }

                    turn = "right";
                }else{
                    if (computer == 1){
                        myThread.pickTimeForBot = true;
                    }
                    turn = "left";
                }
                seconds = 5;
                if (selectedFigure != null){
                    selectedFigure.selected = false;
                    pressed = false;
                }
            }
            if (seconds < 3){

                mpSecond.start();
            }
            if (rule.equals("time")){
                String tmp[] = time.split(":");
                int sec = Integer.parseInt(tmp[1]);
                int min = Integer.parseInt(tmp[0]);

                if (--sec == -1){
                    if (--min == -1){
                        endGameOnTime();
                        sec =0; min= 0;
                    }else{
                        sec = 59;
                    }
                }
                if (sec < 10){
                    time = "0"+min+":0"+sec;
                }else{
                    time = "0"+min+":"+sec;
                }

            }
        }

    }

    public void endGameOnTime(){
        final MediaPlayer mp = MediaPlayer.create(context, R.raw.end);

        mp.start();
        SharedPreferences preferences = context.getSharedPreferences("resume", MODE_PRIVATE);
        preferences.edit().clear().commit();
        for (int i =0; i < figures.length; i++){
            figures[i].dx *= 0.2;
            figures[i].dy *= 0.2;
        }
        if (leftScore > rightScore){
            winner = player1Name;
        }else if (rightScore > leftScore){
            winner = player2Name;
        }else{
            winner="draw";

        }
        goalBitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.scoreboard);
        goalBitmap = Bitmap.createScaledBitmap(goalBitmap, 1000, 800, false);
        myThread.count = 5;
    }

    public void goal(){

        mpGoal = MediaPlayer.create(context, R.raw.goal);
        mpGoal.start();

        for (int i =0; i < figures.length; i++){
            figures[i].dx *= 0.2;
            figures[i].dy *= 0.2;
        }
        if (rule.equals("goals")){
            if ((leftScore+"").equals(goals)){
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.end);
                mp.start();
                SharedPreferences preferences = context.getSharedPreferences("resume", MODE_PRIVATE);
                preferences.edit().clear().commit();
                winner = player1Name;
                goalBitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.scoreboard);
                goalBitmap = Bitmap.createScaledBitmap(goalBitmap, 1000, 800, false);
                myThread.count = 5;
            }else if ((rightScore+"").equals(goals)){
                final MediaPlayer mp = MediaPlayer.create(context, R.raw.end);
                mp.start();
                SharedPreferences preferences = context.getSharedPreferences("resume", MODE_PRIVATE);
                preferences.edit().clear().commit();
                winner = player2Name;
                goalBitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.scoreboard);
                goalBitmap = Bitmap.createScaledBitmap(goalBitmap, 1000, 800, false);
                myThread.count = 5;
            }else{
                goalBitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.goal_sign);
                goalBitmap = Bitmap.createScaledBitmap(goalBitmap, 1000, 800, false);
                myThread.count = 4;
            }
        }else{
            goalBitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.goal_sign);
            goalBitmap = Bitmap.createScaledBitmap(goalBitmap, 1000, 800, false);
            myThread.count = 4;
        }


    }

    public void startAgain(){
        if (winner.equals("")){
            goalBitmap = null;
            myThread.flag =false;
            restart();
        }else{
            myThread.active = false;
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("endGame","yes");
            intent.putExtra("result",leftScore+":"+rightScore);
            intent.putExtra("players",player1Name+"  X  "+player2Name);
            context.setResult(Activity.RESULT_OK, intent);
            context.finish();


        }

    }

    public void decode() {
        height = this.getLayoutParams().height;
        width = this.getLayoutParams().width;

        if (field==0){
            background = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
        }else if (field == 1){
            background = BitmapFactory.decodeResource(getResources(), R.drawable.concrete);
        }else{
            background = BitmapFactory.decodeResource(getResources(), R.drawable.parquet);
        }
        background = Bitmap.createScaledBitmap(background, width, height, false);


        initialPositions();
        if (rule.equals("goals")){
            myThread =new MyThread(figures,gameSpeed,time, this, width, height);
            myThread.start();
        }else{
            myThread =new MyThread(figures,gameSpeed, this, width, height);
            myThread.start();
        }

    }
}
