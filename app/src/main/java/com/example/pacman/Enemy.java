package com.example.pacman;

import android.graphics.Bitmap;

public abstract class Enemy {
    private Direction direction = Direction.RIGHT;
    private boolean visible = false;
    float x;
    float y;
    float vel;

    static Bitmap scareSprite;

    public void setDirection(Direction dir) {
        this.direction = dir;
    }
    public Direction getDirection() {
        return direction;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public boolean getVisible() { return visible; }

    public abstract PointType getEnemyType();
    public abstract void moveAlgo1(Pacman p);
    public abstract Bitmap getBitmap();

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        vel = GameView.mBoxSize/8.0f;
    }

    public void move() {
        switch (direction) {
            case UP:
                y = (y - vel < 0) ? (GameView.MAP_SIZE * GameView.mBoxSize - vel) : y - vel;
                break;
            case DOWN:
                y = (y + vel >= GameView.MAP_SIZE * GameView.mBoxSize) ? vel : y + vel;
                break;
            case LEFT:
                x = (x - vel < 0) ? (GameView.MAP_SIZE * GameView.mBoxSize - vel) : x - vel;
                break;
            case RIGHT:
                x = (x + vel >= GameView.MAP_SIZE * GameView.mBoxSize) ? vel : x + vel;
                break;
        }
    }

    public static void setScaredBitmap(Bitmap b) {
        scareSprite = b;
    }
}
