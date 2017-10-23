package com.example.se101group11.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.StringRes;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jason on 22/10/17.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread thread;

    public static int score;


    private Rect r = new Rect();
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private Bitmap background= BitmapFactory.decodeResource(getResources(), R.drawable.bg);
    private boolean gameOver = false;

    public GamePanel(Context context){
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        score = 0;

        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0), BitmapFactory.decodeResource(getResources(), R.drawable.bird2));

        playerPoint = new Point(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT*3/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(100, 150,100, Color.BLACK);
        Obstacle.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.bottomtube);
        Obstacle.bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.toptube);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public void reset(){
   player.init();
        obstacleManager = new ObstacleManager(200, 250, 75, Color.BLACK);
        score = 0;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;

        while(true)
        {
            try{
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                if(!gameOver)
                {
                    player.jump();
                }

                if(gameOver)
                {
                    reset();
                    gameOver = false;
                }


        }

        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
        if (!gameOver){
            player.update(playerPoint);
            obstacleManager.update();
            if (obstacleManager.playerCollide(player)||player.getRectangle().bottom > Constants.SCREEN_HEIGHT)
                gameOver = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        canvas.drawBitmap(background, 0, 0, null);

        player.draw(canvas);
        obstacleManager.draw(canvas);

        drawScore(canvas);

        if (gameOver){
            Paint paint = new Paint();
            paint.setTextSize(50);
            paint.setColor(Color.BLUE);
            drawCentreText(canvas, paint, "GAME OVER");
        }
    }

    private void drawScore(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(30);
        String text = "" + score;
        canvas.drawText(text, 50, 50, paint);
    }

    private void drawCentreText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}
