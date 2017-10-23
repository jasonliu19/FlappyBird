package com.example.se101group11.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by jason on 22/10/17.
 */

public class Obstacle implements GameObject{
    static Bitmap bitmap1;
    static Bitmap bitmap2;
    private Rect rectangle;
    private int color;
    private Rect rectangle2;

    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementX(float x){
        rectangle.left -= x;
        rectangle.right -=x;
        rectangle2.left -= x;
        rectangle2.right -=x;
    }

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap)
    {
        //l,t,r,b
        this.color = color;

        rectangle = new Rect(startX, 0, startX+rectHeight, startY);
        rectangle2 = new Rect(startX, startY + playerGap, startX + rectHeight, Constants.SCREEN_HEIGHT);
    }

    public boolean playerCollide(RectPlayer player){
//        if (rectangle.contains(player.getRectangle().left, player.getRectangle().top)
//                || rectangle.contains(player.getRectangle().right, player.getRectangle().top)
//                || rectangle.contains(player.getRectangle().left, player.getRectangle().bottom)
//                || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom))
//                return true;
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawBitmap(bitmap2, rectangle.left, rectangle.bottom-bitmap2.getHeight(),null);
        canvas.drawBitmap(bitmap1, rectangle2.left, rectangle2.top,null);
    }
}
