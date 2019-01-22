package com.example.cira.pocketsoccer.game;

import android.graphics.Bitmap;

public class Figure {
    private static final float STEP = 0.05f;
    private static final float STEP_BALL = 0.1f;

    public float x, y, dx, dy, r;
    public String type;
    public Bitmap bitmap;
    public String player;
    public boolean selected;
    public float mass;

    public float stepX, stepY;

    public Figure(float x, float y, Bitmap bitmap, String type, String player) {

        this.bitmap = bitmap;
        this.type = type;
        this.player = player;
        r = bitmap.getWidth()/2;
        selected = false;
        this.x = x;
        this.y = y;
        dx = dy = 0;
        if (type == "ball")
            mass = r/5;
        else{
            mass = r*0.7f;
        }


    }

    public float getX(){
        return x-r;
    }
    public float getY(){
        return y-r;
    }

    public void calcSteps(){
            if (type == "ball"){
                float div = dx/dy;

                if (div < 0)
                    div *=-1;

                if (div >= 1){
                    stepX = STEP_BALL;
                    stepY =  STEP_BALL*1/div;
                }else{
                    div = dy/dx;
                    if (div < 0)
                        div *=-1;
                    stepX = STEP_BALL*1/div;
                    stepY =  STEP_BALL;
                }
            }else{
                float div = dx/dy;

                if (div < 0)
                    div *=-1;

                if (div >= 1){
                    stepX = STEP;
                    stepY =  STEP*1/div;
                }else{
                    div = dy/dx;
                    if (div < 0)
                        div *=-1;
                    stepX = STEP*1/div;
                    stepY =  STEP;
                }
            }

    }

}
