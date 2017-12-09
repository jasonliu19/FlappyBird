package com.example.se101group11.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by jason on 22/10/17.
 */

public class RectPlayer implements GameObject {


    private int xPos = Constants.SCREEN_WIDTH/2;
    private float yPos;
    private final int accelY = 3;
    private int deltaY;
    private Bitmap bird;
    private Bitmap rotateBird;
    private Rect rectangle;

    public Rect getRectangle(){
        return rectangle;
    }

    public RectPlayer(Rect rectangle, Bitmap bitmap)
    {
        this.rectangle = rectangle;
        this.bird = bitmap;
        this.rotateBird = bitmap;

    }

    @Override
    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postRotate(Math.min(deltaY*2, 75));

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bird, bird.getWidth(), bird.getHeight(),true);
        rotateBird = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        canvas.drawBitmap(rotateBird, rectangle.left, rectangle.top, null);
    }

    public void init(){
        yPos=Constants.SCREEN_HEIGHT/2;
        deltaY=0;
        rectangle.set(xPos -bird.getWidth()/2,(int)( yPos-bird.getHeight()/2), xPos+bird.getWidth()/2, (int)(yPos+bird.getHeight()/2));
    }

    public void jump(){
        deltaY=-25;
    }

    @Override
    public void update(){
        if (yPos +deltaY< 0){
            deltaY =0;
        }

        yPos+= deltaY;
        deltaY+= accelY;
        rectangle.set(xPos -bird.getWidth()/2,(int)( yPos-bird.getHeight()/2), xPos+bird.getWidth()/2, (int)(yPos+bird.getHeight()/2));
    }
}
