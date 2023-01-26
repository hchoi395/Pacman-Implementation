package com.example.pacman;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class YellowGhost extends Enemy {
    private GameView g;
    public int changeTarget = -1;
    private float target_x = 0f;
    private float target_y = 0f;
    private Bitmap sprite;
    public YellowGhost (GameView g) {
        this.g = g;
    }

    public void setBitmap(Bitmap sprite) {
        this.sprite = sprite;
    }

    @Override
    public Bitmap getBitmap() { return sprite; }

    @Override
    public PointType getEnemyType() {
        return PointType.ENEMYYELLOW;
    }

    public void moveAlgo1(Pacman p) {
        if (x%GameView.mBoxSize == 0 && y%GameView.mBoxSize == 0) {
            Random rand = new Random();
            if (changeTarget <= 0
                    || ((int)x/GameView.mBoxSize == (int)target_x/GameView.mBoxSize
                    && (int)y/GameView.mBoxSize == (int)target_y/GameView.mBoxSize)) {
                int targetSelection = rand.nextInt(3);
                if (targetSelection != 0) {
                    target_x = p.x;
                    target_y = p.y;
                } else {
                    target_x = rand.nextInt(20) * GameView.mBoxSize;
                    target_y = rand.nextInt(20) * GameView.mBoxSize;
                }
                changeTarget = 20;
            }
            float dir_x = x - (float) target_x;
            float dir_y = y - (float) target_y;
            ArrayList<Direction> available_dir = g.getEnemyPath(g.getPoint((int)x/GameView.mBoxSize, (int)y/GameView.mBoxSize), getDirection());

            if (Math.abs(dir_x) > Math.abs(dir_y)) {
                if (dir_x < 0 && available_dir.contains(Direction.RIGHT)) { //go right
                    setDirection(Direction.RIGHT);
                } else if (dir_x > 0 && available_dir.contains(Direction.LEFT)) {
                    setDirection(Direction.LEFT);
                } else {
                    if (dir_y < 0 && available_dir.contains(Direction.UP)) {
                        setDirection(Direction.UP);
                    } else if (available_dir.contains(Direction.DOWN)){
                        setDirection(Direction.DOWN);
                    } else {
                        setDirection(available_dir.get(rand.nextInt(available_dir.size())));
                    }
                }
            } else {
                if (dir_y < 0 && available_dir.contains(Direction.DOWN)) { //go right
                    setDirection(Direction.DOWN);
                } else if (dir_y > 0 && available_dir.contains(Direction.UP)) {
                    setDirection(Direction.UP);
                } else {
                    if (dir_x < 0 && available_dir.contains(Direction.RIGHT)) {
                        setDirection(Direction.RIGHT);
                    } else if (available_dir.contains(Direction.LEFT)){
                        setDirection(Direction.LEFT);
                    } else {
                        setDirection(available_dir.get(rand.nextInt(available_dir.size())));
                    }
                }
            }
            Log.d("Enemy:", "Enemy Direction: " + this.getDirection());
            changeTarget--;
        }
        move();
    }
}
