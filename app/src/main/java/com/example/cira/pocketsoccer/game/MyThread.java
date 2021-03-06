package com.example.cira.pocketsoccer.game;

import android.media.MediaPlayer;
import android.util.Log;

import com.example.cira.pocketsoccer.R;

import java.util.Random;

public class MyThread extends Thread {
     MediaPlayer mpKick;

    private Figure[] figures;
    public int frameRate;
    private String time;
    private MyCustomView myCustomView;
    public boolean pickTimeForDraw = true, active = true, pickTimeForSecond=true, pickTimeForBot = true;
    public long startTimeForDraw, startTimeForSecond, startTimeForBot=System.currentTimeMillis()+9000, botResponse;
    private int width, height;
    public int count = 0;
    public boolean goal = false, flag=false;
    private static final float POST_MASS = 90*0.5f;

    private Coordinate[] goalPostCoordinates = new Coordinate[4];
    private Coordinate[] goalPostCoordinates2 = new Coordinate[4];
    private Coordinate[] goalPostCoordinates3 = new Coordinate[4];
    private Coordinate[] goalPostCoordinates4 = new Coordinate[4];
    private int[] rates={0,18,15,12, 9, 6};

    public MyThread(Figure[] figures, int gameSpeed, String time, MyCustomView myCustomView, int width, int height) {
        this.figures = figures;
        frameRate = rates[gameSpeed];
        this.time = time;
        this.myCustomView = myCustomView;
        this.width = width;
        this.height = height;
        goalPostCoordinates[0]=new Coordinate(0.055f*width,0.36f*height);
        goalPostCoordinates[1]=new Coordinate(0.055f*width,0.64f*height);
        goalPostCoordinates[2]= new Coordinate(0.94f*width,0.36f*height);
        goalPostCoordinates[3]=new Coordinate(0.94f*width,0.64f*height);

        goalPostCoordinates2[0]=new Coordinate(0.04f*width,0.36f*height);
        goalPostCoordinates2[1]=new Coordinate(0.04f*width,0.64f*height);
        goalPostCoordinates2[2]= new Coordinate(0.955f*width,0.36f*height);
        goalPostCoordinates2[3]=new Coordinate(0.955f*width,0.64f*height);

        goalPostCoordinates3[0]=new Coordinate(0.025f*width,0.36f*height);
        goalPostCoordinates3[1]=new Coordinate(0.025f*width,0.64f*height);
        goalPostCoordinates3[2]= new Coordinate(0.97f*width,0.36f*height);
        goalPostCoordinates3[3]=new Coordinate(0.97f*width,0.64f*height);

        goalPostCoordinates4[0]=new Coordinate(0.01f*width,0.36f*height);
        goalPostCoordinates4[1]=new Coordinate(0.01f*width,0.64f*height);
        goalPostCoordinates4[2]= new Coordinate(0.985f*width,0.36f*height);
        goalPostCoordinates4[3]=new Coordinate(0.985f*width,0.64f*height);

         mpKick = MediaPlayer.create(myCustomView.context, R.raw.kick);

        botResponse = 2000;
    }
    public MyThread(Figure[] figures, int gameSpeed, MyCustomView myCustomView, int width, int height) {
        this.figures = figures;
        frameRate = rates[gameSpeed];
        this.time = "";
        this.myCustomView = myCustomView;
        this.width = width;
        this.height = height;
        goalPostCoordinates[0]=new Coordinate(0.055f*width,0.36f*height);
        goalPostCoordinates[1]=new Coordinate(0.055f*width,0.64f*height);
        goalPostCoordinates[2]= new Coordinate(0.94f*width,0.36f*height);
        goalPostCoordinates[3]=new Coordinate(0.94f*width,0.64f*height);

        goalPostCoordinates2[0]=new Coordinate(0.04f*width,0.36f*height);
        goalPostCoordinates2[1]=new Coordinate(0.04f*width,0.64f*height);
        goalPostCoordinates2[2]= new Coordinate(0.955f*width,0.36f*height);
        goalPostCoordinates2[3]=new Coordinate(0.955f*width,0.64f*height);

        goalPostCoordinates3[0]=new Coordinate(0.025f*width,0.36f*height);
        goalPostCoordinates3[1]=new Coordinate(0.025f*width,0.64f*height);
        goalPostCoordinates3[2]= new Coordinate(0.97f*width,0.36f*height);
        goalPostCoordinates3[3]=new Coordinate(0.97f*width,0.64f*height);

        goalPostCoordinates4[0]=new Coordinate(0.01f*width,0.36f*height);
        goalPostCoordinates4[1]=new Coordinate(0.01f*width,0.64f*height);
        goalPostCoordinates4[2]= new Coordinate(0.985f*width,0.36f*height);
        goalPostCoordinates4[3]=new Coordinate(0.985f*width,0.64f*height);

         mpKick = MediaPlayer.create(myCustomView.context, R.raw.kick);
        botResponse = 2000;
    }

    @Override
    public void run() {

        while (active){
            if (pickTimeForDraw){
                startTimeForDraw = System.currentTimeMillis();
                pickTimeForDraw = false;
            }

            if (pickTimeForSecond){
                startTimeForSecond = System.currentTimeMillis();
                pickTimeForSecond = false;
            }

            if (((myCustomView.turn.equals("left") && myCustomView.computer == 1) || (myCustomView.turn.equals("right") && myCustomView.computer == 2))&&pickTimeForBot){
                startTimeForBot = System.currentTimeMillis();
                pickTimeForBot = false;
            }

            if (System.currentTimeMillis()-startTimeForSecond >= 1000){
                if(!goal){
                    myCustomView.second();
                }


                if (count != 0){
                    count--;
                    if (count == 0){
                        myCustomView.startAgain();
                        goal=false;
                    }
                }
                pickTimeForSecond = true;

            }

            //for draw
            if(System.currentTimeMillis() - startTimeForDraw >= frameRate){

                moveAndSlowDown();
                checkCrashUpWall();
                checkCrashDownWall();
                checkCrashLeftWall();
                checkCrashRightWall();

                fixOverlap();

                checkCrashGoalpost();

                if (myCustomView.winner.equals("") && !flag){
                    checkLeftGoal();

                    checkRightGoal();

                }

                myCustomView.invalidate();
                pickTimeForDraw = true;
            }

            if (myCustomView.computer != 0){
                if (System.currentTimeMillis()-startTimeForBot >= botResponse){

                    botShoot();


                }

            }


        }

    }

    public void botShoot(){
        if ((myCustomView.turn.equals("left") && myCustomView.computer == 1) || (myCustomView.turn.equals("right") && myCustomView.computer == 2)){
            Random rand = new Random();
            int index = rand.nextInt(3);
            Figure fig;
            if (myCustomView.turn.equals("left")){
                fig = figures[index];
            }else{
                fig = figures[index+3];
            }
            if (myCustomView.winner.equals("") && !goal){
                myCustomView.down(fig.x,fig.y);
            }

            pickTimeForBot = true;
            startTimeForBot=System.currentTimeMillis()+9000;
        }
    }

    private void checkLeftGoal() {
        if (!goal){
            if (figures[6].x < goalPostCoordinates[0].x && figures[6].y > goalPostCoordinates[0].y && figures[6].y < goalPostCoordinates[1].y){

                myCustomView.rightScore++;
                myCustomView.goal();
                myCustomView.turn = "left";
                goal = true;
                if (myCustomView.computer == 2){
                    startTimeForBot=System.currentTimeMillis()+9000;
                }
            }
        }

    }



    private void checkRightGoal() {
        if(!goal){
            if (figures[6].x > goalPostCoordinates[2].x && figures[6].y > goalPostCoordinates[2].y && figures[6].y < goalPostCoordinates[3].y){

                myCustomView.leftScore++;
                myCustomView.goal();
                myCustomView.turn = "right";
                goal = true;
                if (myCustomView.computer == 1){
                    startTimeForBot=System.currentTimeMillis()+9000;
                }
            }
        }

    }

    private void checkCrashGoalpost() {
        for (int i =0; i < figures.length; i++){
            for (int j=0; j<goalPostCoordinates.length; j++){
                float distance = (float)Math.sqrt((goalPostCoordinates[j].y -  figures[i].y) * (goalPostCoordinates[j].y -  figures[i].y) + (goalPostCoordinates[j].x - figures[i].x) * (goalPostCoordinates[j].x -  figures[i].x));
                if (distance <= figures[i].r){
                    crashGoalpost(i,j);

                }
            }
            for (int j=0; j<goalPostCoordinates2.length; j++){
                float distance = (float)Math.sqrt((goalPostCoordinates2[j].y -  figures[i].y) * (goalPostCoordinates2[j].y -  figures[i].y) + (goalPostCoordinates2[j].x - figures[i].x) * (goalPostCoordinates2[j].x -  figures[i].x));
                if (distance <= figures[i].r){

                    crashGoalpost2(i,j);

                }
            }
            for (int j=0; j<goalPostCoordinates3.length; j++){
                float distance = (float)Math.sqrt((goalPostCoordinates3[j].y -  figures[i].y) * (goalPostCoordinates3[j].y -  figures[i].y) + (goalPostCoordinates3[j].x - figures[i].x) * (goalPostCoordinates3[j].x -  figures[i].x));
                if (distance <= figures[i].r){

                    crashGoalpost3(i,j);

                }
            }
            for (int j=0; j<goalPostCoordinates4.length; j++){
                float distance = (float)Math.sqrt((goalPostCoordinates4[j].y -  figures[i].y) * (goalPostCoordinates4[j].y -  figures[i].y) + (goalPostCoordinates4[j].x - figures[i].x) * (goalPostCoordinates4[j].x -  figures[i].x));
                if (distance <= figures[i].r){

                    crashGoalpost4(i,j);
                }
            }

        }
    }

    private void crash(int i, int j) {

        float distance = (float)Math.sqrt((figures[j].y -  figures[i].y) * (figures[j].y -  figures[i].y) + (figures[j].x - figures[i].x) * (figures[j].x -  figures[i].x));

        float nx  = ( figures[i].x- figures[j].x)/distance;
        float ny = ( figures[i].y -  figures[j].y)/distance;

        float tx = -ny;
        float ty = nx;

        float dpTan1 =  figures[j].dx *tx +  figures[j].dy *ty;
        float dpTan2 =  figures[i].dx *tx +  figures[i].dy *ty;

        float dpNorm1 =  figures[j].dx *nx +  figures[j].dy *ny;
        float dpNorm2 =  figures[i].dx *nx +  figures[i].dy *ny;

        float m1 = (dpNorm1 * (figures[j].mass - figures[i].mass)+2.0f*figures[i].mass*dpNorm2)/(figures[j].mass + figures[i].mass);
        float m2 = (dpNorm2 * (figures[i].mass - figures[j].mass)+2.0f*figures[j].mass*dpNorm1)/(figures[j].mass + figures[i].mass);

        figures[j].dx = tx * dpTan1 + nx*m1;
        figures[j].dy = ty * dpTan1 + ny*m1;
        figures[i].dx = tx * dpTan2 + nx*m2;
        figures[i].dy = ty * dpTan2 + ny*m2;

        figures[i].calcSteps();
        figures[j].calcSteps();

    }

    public void crashGoalpost(int i, int j){
        float distance = (float)Math.sqrt((goalPostCoordinates[j].y -  figures[i].y) * (goalPostCoordinates[j].y -  figures[i].y) + (goalPostCoordinates[j].x - figures[i].x) * (goalPostCoordinates[j].x -  figures[i].x));

        float nx  = ( figures[i].x- goalPostCoordinates[j].x)/distance;
        float ny = ( figures[i].y -  goalPostCoordinates[j].y)/distance;

        float tx = -ny;
        float ty = nx;

        float dpTan2 =  figures[i].dx *tx +  figures[i].dy *ty;
        float dpNorm2 =  figures[i].dx *nx +  figures[i].dy *ny;
        float m2;
        if (figures[i].type.equals("ball")){
            m2 = (dpNorm2 * (figures[i].mass - POST_MASS/2)+2.0f*POST_MASS*2.5f)/(POST_MASS + figures[i].mass);

        }else{
            m2 = (dpNorm2 * (figures[i].mass - POST_MASS)+2.0f*POST_MASS*7.5f)/(POST_MASS + figures[i].mass);

        }
        figures[i].dx = tx * dpTan2 + nx*m2;
        figures[i].dy = ty * dpTan2 + ny*m2;



        figures[i].calcSteps();

    }

    public void crashGoalpost2(int i, int j){
        float distance = (float)Math.sqrt((goalPostCoordinates2[j].y -  figures[i].y) * (goalPostCoordinates2[j].y -  figures[i].y) + (goalPostCoordinates2[j].x - figures[i].x) * (goalPostCoordinates2[j].x -  figures[i].x));

        float nx  = ( figures[i].x- goalPostCoordinates2[j].x)/distance;
        float ny = ( figures[i].y -  goalPostCoordinates2[j].y)/distance;

        float tx = -ny;
        float ty = nx;

        float dpTan2 =  figures[i].dx *tx +  figures[i].dy *ty;
        float dpNorm2 =  figures[i].dx *nx +  figures[i].dy *ny;
        float m2;
        if (figures[i].type.equals("ball")){
            m2 = (dpNorm2 * (figures[i].mass - POST_MASS)+2.0f*POST_MASS*2.5f)/(POST_MASS + figures[i].mass);

        }else{
            m2 = (dpNorm2 * (figures[i].mass - POST_MASS)+2.0f*POST_MASS*7.5f)/(POST_MASS + figures[i].mass);

        }
        figures[i].dx = tx * dpTan2 + nx*m2;
        figures[i].dy = ty * dpTan2 + ny*m2;



        figures[i].calcSteps();

    }

    public void crashGoalpost3(int i, int j){
        float distance = (float)Math.sqrt((goalPostCoordinates3[j].y -  figures[i].y) * (goalPostCoordinates3[j].y -  figures[i].y) + (goalPostCoordinates3[j].x - figures[i].x) * (goalPostCoordinates3[j].x -  figures[i].x));

        float nx  = ( figures[i].x- goalPostCoordinates3[j].x)/distance;
        float ny = ( figures[i].y -  goalPostCoordinates3[j].y)/distance;

        float tx = -ny;
        float ty = nx;

        float dpTan2 =  figures[i].dx *tx +  figures[i].dy *ty;
        float dpNorm2 =  figures[i].dx *nx +  figures[i].dy *ny;
        float m2;
        if (figures[i].type.equals("ball")){
            m2 = (dpNorm2 * (figures[i].mass - POST_MASS)+2.0f*POST_MASS*2.5f)/(POST_MASS + figures[i].mass);

        }else{
            m2 = (dpNorm2 * (figures[i].mass - POST_MASS)+2.0f*POST_MASS*7.5f)/(POST_MASS + figures[i].mass);

        }
        figures[i].dx = tx * dpTan2 + nx*m2;
        figures[i].dy = ty * dpTan2 + ny*m2;



        figures[i].calcSteps();

    }
    public void crashGoalpost4(int i, int j){
        float distance = (float)Math.sqrt((goalPostCoordinates4[j].y -  figures[i].y) * (goalPostCoordinates4[j].y -  figures[i].y) + (goalPostCoordinates4[j].x - figures[i].x) * (goalPostCoordinates4[j].x -  figures[i].x));

        float nx  = ( figures[i].x- goalPostCoordinates4[j].x)/distance;
        float ny = ( figures[i].y -  goalPostCoordinates4[j].y)/distance;

        float tx = -ny;
        float ty = nx;

        float dpTan2 =  figures[i].dx *tx +  figures[i].dy *ty;
        float dpNorm2 =  figures[i].dx *nx +  figures[i].dy *ny;
        float m2;
        if (figures[i].type.equals("ball")){
            m2 = (dpNorm2 * (figures[i].mass - POST_MASS)+2.0f*POST_MASS*2.5f)/(POST_MASS + figures[i].mass);

        }else{
            m2 = (dpNorm2 * (figures[i].mass - POST_MASS)+2.0f*POST_MASS*7.5f)/(POST_MASS + figures[i].mass);

        }
        figures[i].dx = tx * dpTan2 + nx*m2;
        figures[i].dy = ty * dpTan2 + ny*m2;



        figures[i].calcSteps();

    }

    private void fixOverlap() {
        for (int i =0; i < figures.length-1; i++){
            for (int j = i+1; j<figures.length; j++){
                //overlaping
                float distance = (float)Math.sqrt((figures[j].y -  figures[i].y) * (figures[j].y -  figures[i].y) + (figures[j].x - figures[i].x) * (figures[j].x -  figures[i].x));
                if ( distance <= figures[i].r+figures[j].r){

                    if (j==6){

                        mpKick.start();
                    }
                    float overlap = (distance - figures[i].r - figures[j].r)*0.5f;

                    figures[i].x -= overlap*(figures[i].x - figures[j].x) / distance;
                    figures[i].y -= overlap*(figures[i].y - figures[j].y) / distance;

                    figures[j].x += overlap*(figures[i].x - figures[j].x) / distance;
                    figures[j].y += overlap*(figures[i].y - figures[j].y) / distance;

                    crash(i,j);
                }
            }
        }
    }


    private void checkCrashUpWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].y-figures[i].r <=0){
                figures[i].x -= figures[i].dx;
                figures[i].y -= figures[i].dy;
                figures[i].dy = figures[i].dy * -1;
            }
        }
    }

    private void checkCrashDownWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].y+figures[i].r >= height){
                figures[i].x -= figures[i].dx;
                figures[i].y -= figures[i].dy;
                figures[i].dy = figures[i].dy * -1;
            }
        }
    }

    private void checkCrashLeftWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].x-figures[i].r <= 0){
                figures[i].x -= figures[i].dx;
                figures[i].y -= figures[i].dy;
                figures[i].dx = figures[i].dx * -1;
            }
        }
    }

    private void checkCrashRightWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].x+figures[i].r >= width){
                figures[i].x -= figures[i].dx;
                figures[i].y -= figures[i].dy;
                figures[i].dx = figures[i].dx * -1;
            }
        }
    }

    private void moveAndSlowDown() {
        for (int i = 0; i < figures.length; i++){
            figures[i].x += figures[i].dx;
            figures[i].y += figures[i].dy;

            if (figures[i].dx > 0){
                if (figures[i].dx - figures[i].stepX < 0)
                    figures[i].dx =0;
                else
                    figures[i].dx -= figures[i].stepX;
            }else if (figures[i].dx < 0){
                if (figures[i].dx + figures[i].stepX > 0)
                    figures[i].dx =0;
                else
                    figures[i].dx += figures[i].stepX;
            }

            if (figures[i].dy > 0){
                if (figures[i].dy - figures[i].stepY < 0)
                    figures[i].dy =0;
                else
                    figures[i].dy -= figures[i].stepY;
            }else if (figures[i].dy < 0){
                if (figures[i].dy + figures[i].stepY > 0)
                    figures[i].dy =0;
                else
                    figures[i].dy += figures[i].stepY;
            }


        }
    }

}
