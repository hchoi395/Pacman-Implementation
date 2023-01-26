package com.example.pacman;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Random;

public class RedGhost extends Enemy {
    int changeDirection = 8;
    Bitmap sprite;
    GameView g;
    public RedGhost(GameView view) {
        g = view;
    }

    public void setBitmap(Bitmap sprite) {
        this.sprite = sprite;
    }

    @Override
    public Bitmap getBitmap() { return sprite; }

    @Override
    public PointType getEnemyType() {
        return PointType.ENEMYRED;
    }

    public void moveAlgo1(Pacman p) {
        if (x%GameView.mBoxSize == 0 && y%GameView.mBoxSize == 0) {
            Random rand = new Random();
            ArrayList<Direction> available_dir = g.getEnemyPath(g.getPoint((int)x/GameView.mBoxSize, (int)y/GameView.mBoxSize), getDirection());
            //removeOppositeDirection(available_dir);
            int n = rand.nextInt(available_dir.size());
            if (changeDirection == 0 || !available_dir.contains(this.getDirection())) {
                this.setDirection(available_dir.get(n));
                changeDirection = rand.nextInt(6) + 6;
            } else {
                changeDirection--;
            }
        }
        move();
    }
}
