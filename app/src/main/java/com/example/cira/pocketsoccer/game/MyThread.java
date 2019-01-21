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

                myCustomView.invalidate();
                pickTimeForDraw = true;
            }
        }

    }



    private void checkCrashUpWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].y-figures[i].r <=0){
                figures[i].dy = figures[i].dy * -1;
            }
        }
    }

    private void checkCrashDownWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].y+figures[i].r >= height){
                figures[i].dy = figures[i].dy * -1;
            }
        }
    }

    private void checkCrashLeftWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].x-figures[i].r <= 0){
                figures[i].dx = figures[i].dx * -1;
            }
        }
    }

    private void checkCrashRightWall() {
        for (int i = 0; i < figures.length; i++){
            if (figures[i].x+figures[i].r >= width){
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
