package com.example.cira.pocketsoccer.game;

import android.util.Log;

public class MyThread extends Thread {
    private Figure[] figures;
    private int frameRate;
    private String time;
    private MyCustomView myCustomView;
    private boolean pickTimeForDraw = true, active = true;
    private long startTimeForDraw;
    private int width, height;

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

    }

    @Override
    public void run() {

        while (active){
            if (pickTimeForDraw){
                startTimeForDraw = System.currentTimeMillis();
                pickTimeForDraw = false;
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

                myCustomView.invalidate();
                pickTimeForDraw = true;
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
        if (figures[i].type=="ball"){
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
        if (figures[i].type=="ball"){
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
        if (figures[i].type=="ball"){
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
        if (figures[i].type=="ball"){
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
                figures[i].x -= figures[i].dx/2;
                figures[i].y -= figures[i].dy/2;
                figures[i].dy = figures[i].dy * -1;
            }
        }
    }

    private void checkCrashDownWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].y+figures[i].r >= height){
                figures[i].x -= figures[i].dx/2;
                figures[i].y -= figures[i].dy/2;
                figures[i].dy = figures[i].dy * -1;
            }
        }
    }

    private void checkCrashLeftWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].x-figures[i].r <= 0){
                figures[i].x -= figures[i].dx/2;
                figures[i].y -= figures[i].dy/2;
                figures[i].dx = figures[i].dx * -1;
            }
        }
    }

    private void checkCrashRightWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].x+figures[i].r >= width){
                figures[i].x -= figures[i].dx/2;
                figures[i].y -= figures[i].dy/2;
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
