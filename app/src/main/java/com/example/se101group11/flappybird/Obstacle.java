package com.example.se101group11.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by jason on 22/10/17.
 */

public class Obstacle implements GameObject{
    static Bitmap bottomTubeBitmap;
    static Bitmap topTubeBitmap;
    private Bitmap croppedTop;
    private Rect topRect;
    private Rect botRect;

    public Rect getTopRect() {
        return topRect;
    }

    public void incrementX(float x){
        topRect.left -= (int)x;
        topRect.right -=(int)x;
        botRect.left -= (int)x;
        botRect.right -=(int)x;
    }

    public Obstacle(int rectHeight, int startX, int startY, int playerGap)
    {
        //l,t,r,b
        topRect = new Rect(startX, 0, startX+rectHeight, startY);
        botRect = new Rect(startX, startY + playerGap, startX + rectHeight, Constants.SCREEN_HEIGHT);
        System.out.println(topTubeBitmap.getHeight()- topRect.bottom);
        if (topTubeBitmap.getHeight() -topRect.bottom < 0){
            topRect.bottom = 1;
        }
        croppedTop = Bitmap.createBitmap(topTubeBitmap,0,topTubeBitmap.getHeight()- topRect.bottom, topTubeBitmap.getWidth(), topRect.bottom);

    }

    public boolean playerCollide(RectPlayer player){
        return Rect.intersects(topRect, player.getRectangle()) || Rect.intersects(botRect, player.getRectangle());
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(croppedTop, topRect.left, 1,null);
        canvas.drawBitmap(bottomTubeBitmap, botRect.left, botRect.top,null);

    }
}
