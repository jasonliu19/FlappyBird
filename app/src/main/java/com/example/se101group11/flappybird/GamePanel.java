package com.example.se101group11.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jason on 22/10/17.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread thread;

    public static int score;
    public static int highScore;

    private RectPlayer player;
    private ObstacleManager obstacleManager;
    private Bitmap background= BitmapFactory.decodeResource(getResources(), R.drawable.bg);

    private Bitmap zeroPNG = BitmapFactory.decodeResource(getResources(), R.drawable.zero);
    private Bitmap onePNG = BitmapFactory.decodeResource(getResources(), R.drawable.one);
    private Bitmap twoPNG = BitmapFactory.decodeResource(getResources(), R.drawable.two);
    private Bitmap threePNG = BitmapFactory.decodeResource(getResources(), R.drawable.three);
    private Bitmap fourPNG = BitmapFactory.decodeResource(getResources(), R.drawable.four);
    private Bitmap fivePNG = BitmapFactory.decodeResource(getResources(), R.drawable.five);
    private Bitmap sixPNG = BitmapFactory.decodeResource(getResources(), R.drawable.six);
    private Bitmap sevenPNG = BitmapFactory.decodeResource(getResources(), R.drawable.seven);
    private Bitmap eightPNG = BitmapFactory.decodeResource(getResources(), R.drawable.eight);
    private Bitmap ninePNG = BitmapFactory.decodeResource(getResources(), R.drawable.nine);


    private Bitmap menuButton = BitmapFactory.decodeResource(getResources(), R.drawable.menu);
    private Bitmap playAgain = BitmapFactory.decodeResource(getResources(), R.drawable.button);
    private Bitmap scoreBoard = BitmapFactory.decodeResource(getResources(), R.drawable.menuscore);
    private Bitmap gameOverPic = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
    private Bitmap tutorialPic = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial);

    private boolean gameStarted = false;
    private boolean gameOver = false;

    public GamePanel(Context context){
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        score = 0;
        highScore = 0;

        player = new RectPlayer(new Rect(100, 100, 200, 200), BitmapFactory.decodeResource(getResources(), R.drawable.bird));
        Obstacle.bottomTubeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bottomtube);
        Obstacle.topTubeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.toptube);
        reset();
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

private boolean withinBitmap(int x, int y, Bitmap bitmap, int leftX, int topY){
    return x < leftX + bitmap.getWidth() && x > leftX && y < topY + bitmap.getHeight() && y > topY;
}

    public void reset(){
        player.init();
        obstacleManager = new ObstacleManager(200, 250, 75);
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

        while(retry)
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
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                if(!gameOver && gameStarted)
                {
                    player.jump();
                }

                else if(gameOver)
                {
                    if (withinBitmap(x, y, playAgain,Constants.SCREEN_WIDTH/2 - playAgain.getWidth()/2, Constants.SCREEN_HEIGHT/2 + 2*playAgain.getHeight()/2)) {
                        reset();
                        gameOver = false;
                        player.jump();
                    }

                    if (withinBitmap(x, y, menuButton, Constants.SCREEN_WIDTH/2 - menuButton.getWidth()/2, 3*Constants.SCREEN_HEIGHT/4)){
                        reset();
                        gameStarted = false;
                        gameOver = false;
                    }
                }

                else if (!gameStarted){
                    reset();
                    gameStarted = true;
                    player.jump();
                }
        }

        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
        if (!gameOver && gameStarted){
            player.update();
            obstacleManager.update();
            if (obstacleManager.playerCollide(player)||player.getRectangle().bottom > Constants.SCREEN_HEIGHT)
                gameOver = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        canvas.drawBitmap(background, 0, 0, null);

        obstacleManager.draw(canvas);
        player.draw(canvas);

        if(!gameOver)
            drawScore(canvas, 0, 25, score);

        if (gameOver){
            canvas.drawBitmap(gameOverPic, Constants.SCREEN_WIDTH/2 - gameOverPic.getWidth()/2, Constants.SCREEN_HEIGHT/2 - 50 -scoreBoard.getHeight(), null);
            canvas.drawBitmap(playAgain, Constants.SCREEN_WIDTH/2 - playAgain.getWidth()/2, Constants.SCREEN_HEIGHT/2 + 2*playAgain.getHeight()/2, null);
            canvas.drawBitmap(menuButton, Constants.SCREEN_WIDTH/2 - menuButton.getWidth()/2, 3*Constants.SCREEN_HEIGHT/4, null);
            canvas.drawBitmap(scoreBoard, Constants.SCREEN_WIDTH/2 - scoreBoard.getWidth()/2, Constants.SCREEN_HEIGHT/2 - scoreBoard.getHeight()/2 - playAgain.getHeight()/2, null);
            if(getScoreLength(score) >= 2)
                drawScore(canvas, Constants.SCREEN_WIDTH/2 + scoreBoard.getWidth()/5 + 5 + 20 - getScoreLength(score)*15,
                        Constants.SCREEN_HEIGHT/2 - scoreBoard.getHeight()/3 - playAgain.getHeight()/3+1, score);
            else
                drawScore(canvas, Constants.SCREEN_WIDTH/2 + scoreBoard.getWidth()/5 + 5 ,
                        Constants.SCREEN_HEIGHT/2 - scoreBoard.getHeight()/3 - playAgain.getHeight()/3+1, score);

            if(getScoreLength(highScore) >= 2)
                drawScore(canvas, Constants.SCREEN_WIDTH/2 + scoreBoard.getWidth()/5 + 5 + 20 - getScoreLength(highScore)*15,
                        Constants.SCREEN_HEIGHT/2 - scoreBoard.getHeight()/3 - playAgain.getHeight()/3+55, highScore);
            else
                drawScore(canvas, Constants.SCREEN_WIDTH/2 + scoreBoard.getWidth()/5 + 5 ,
                        Constants.SCREEN_HEIGHT/2 - scoreBoard.getHeight()/3 - playAgain.getHeight()/3+55, highScore);

            System.out.println(highScore);
        } else if(!gameStarted){
            Paint paint = new Paint();
            canvas.drawBitmap(tutorialPic, Constants.SCREEN_WIDTH/2 - tutorialPic.getWidth()/2, Constants.SCREEN_HEIGHT/2 - tutorialPic.getHeight()/2, paint);
        }


    }

    private int getScoreLength(int thisScore)
    {
        int tempScore = thisScore;
        int scoreLength = 0;
        do
        {
            tempScore /= 10;
            scoreLength++;
        } while (tempScore != 0);
        return scoreLength;
    }

    private void drawScore(Canvas canvas, int startx, int starty, int thisScore){
        Paint paint = new Paint();

        int scoreLength = getScoreLength(thisScore);
        int digitWidth = 20;
        int tempScore = thisScore;

        for(int i = scoreLength; i > 0; i--)
        {
            int thisDigit = tempScore % 10;
            tempScore /= 10;
            switch (thisDigit)
            {
                case 0:
                    canvas.drawBitmap(zeroPNG, startx + digitWidth*i, starty, paint);
                    break;
                case 1:
                    canvas.drawBitmap(onePNG, startx + digitWidth*i, starty, paint);
                    break;
                case 2:
                    canvas.drawBitmap(twoPNG, startx + digitWidth*i, starty, paint);
                    break;
                case 3:
                    canvas.drawBitmap(threePNG, startx + digitWidth*i, starty, paint);
                    break;
                case 4:
                    canvas.drawBitmap(fourPNG, startx + digitWidth*i, starty, paint);
                    break;
                case 5:
                    canvas.drawBitmap(fivePNG, startx + digitWidth*i, starty, paint);
                    break;
                case 6:
                    canvas.drawBitmap(sixPNG, startx + digitWidth*i, starty, paint);
                    break;
                case 7:
                    canvas.drawBitmap(sevenPNG, startx + digitWidth*i, starty, paint);
                    break;
                case 8:
                    canvas.drawBitmap(eightPNG, startx + digitWidth*i, starty, paint);
                    break;
                case 9:
                    canvas.drawBitmap(ninePNG, startx + digitWidth*i, starty, paint);
                    break;
            }
        }
    }
}
