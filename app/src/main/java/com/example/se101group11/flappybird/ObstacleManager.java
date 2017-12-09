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

    private long startTime;

    private boolean addedScore = false;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight){
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
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
            obstacles.add(new Obstacle(obstacleHeight, currX, yStart, playerGap));
            currX -= obstacleHeight + obstacleGap;
        }
    }

    public void update(){
        int elapsedTime =(int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = 3f*Constants.SCREEN_WIDTH/10000.0f;
        for(Obstacle ob: obstacles){
            ob.incrementX(speed * elapsedTime);
        }
        if (obstacles.get(obstacles.size()-1).getTopRect().right <= -50){
            int yStart = (int) (Math.random()*(Constants.SCREEN_HEIGHT - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, obstacles.get(0).getTopRect().right + obstacleHeight + obstacleGap, yStart, playerGap));
            obstacles.remove(obstacles.size() - 1);
            addedScore = false;
        }

        if (obstacles.get(obstacles.size()-1).getTopRect().right <= Constants.SCREEN_WIDTH/2 && !addedScore){
            GamePanel.score++;
            if (GamePanel.score > GamePanel.highScore){
                GamePanel.highScore++;
            }
            addedScore = true;
        }
    }

    public void draw(Canvas canvas){
        for (Obstacle ob: obstacles){
            ob.draw(canvas);
        }
    }
}
