package com.example.se101group11.flappybird;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by jason on 22/10/17.
 */

public class ObstacleManager {
    //higher index = lower onscreen
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color){
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;
        startTime = System.currentTimeMillis();
        obstacles = new ArrayList<>();

        populateObstacles();

    }

    public boolean playerCollide(RectPlayer player){
        for (Obstacle ob : obstacles){
            if (ob.playerCollide(player))
                return true;

        }
        return false;
    }

    private void populateObstacles(){
        int currX = 2*Constants.SCREEN_WIDTH;
        while(currX > Constants.SCREEN_WIDTH){
            int yStart = (int) (Math.random()*(Constants.SCREEN_HEIGHT- playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, currX, yStart, playerGap));
            currX -= obstacleHeight + obstacleGap;
        }
    }

    public void update(){
        int elapsedTime =(int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = 3.0f*Constants.SCREEN_WIDTH/10000.0f;
        for(Obstacle ob: obstacles){
            ob.incrementX(speed * elapsedTime);
        }
        if (obstacles.get(obstacles.size()-1).getRectangle().right <= -50){
            int yStart = (int) (Math.random()*(Constants.SCREEN_HEIGHT - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, obstacles.get(0).getRectangle().right + obstacleHeight + obstacleGap, yStart, playerGap));
            obstacles.remove(obstacles.size() - 1);
        }

        if (obstacles.get(obstacles.size()-1).getRectangle().right <= Constants.SCREEN_WIDTH/2){
            GamePanel.score++;
        }
    }

    public void draw(Canvas canvas){
        for (Obstacle ob: obstacles){
            ob.draw(canvas);
        }
    }
}
