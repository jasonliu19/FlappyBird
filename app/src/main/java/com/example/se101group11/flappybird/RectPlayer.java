package com.example.se101group11.flappybird;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by jason on 22/10/17.
 */

public class RectPlayer implements GameObject {


    private int xPos = Constants.SCREEN_WIDTH/2;
    private float yPos;
    private int birdHeight;
    private int birdWidth;
    private final int accelY = 3;
    private int deltaY;
    private boolean jump = false;

    private Bitmap btmp;

    private Rect rectangle;
    private int color;

    public Rect getRectangle(){
        return rectangle;
    }

    public RectPlayer(Rect rectangle, int color, Bitmap btmp)
    {
        this.rectangle = rectangle;
        this.color = color;
        this.btmp = btmp;
        birdHeight=btmp.getHeight();
        birdWidth=btmp.getWidth();
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(color);
        Matrix matrix = new Matrix();


        matrix.postRotate(Math.min(deltaY*2, 75));

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(btmp,btmp.getWidth(),btmp.getHeight(),true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        canvas.drawBitmap(rotatedBitmap, rectangle.left, rectangle.top, null);

    }

    public void init(){
        yPos=Constants.SCREEN_HEIGHT/2;
        deltaY=0;
        rectangle.set(xPos -birdWidth/2,(int)( yPos-birdHeight/2), xPos+birdWidth/2, (int)(yPos+birdHeight/2));
    }

    @Override
    public void update() {


    }

    public void jump(){
        deltaY=-25;
    }

    public void update(Point point){
        if (yPos +deltaY< 0){
            deltaY =0;
        }

        yPos+= deltaY;
        deltaY+= accelY;
        rectangle.set(xPos -birdWidth/2,(int)( yPos-birdHeight/2), xPos+birdWidth/2, (int)(yPos+birdHeight/2));


    }
}
