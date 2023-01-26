package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class MagentaGhost extends Enemy {
    private Bitmap sprite;
    private GameView g;
    public int changeDirection = 12;

    public MagentaGhost (GameView g) {
        this.g = g;
    }
    @Override
    public PointType getEnemyType() {
        return PointType.ENEMYMAG;
    }

    public void setBitmap(Bitmap sprite) {
        this.sprite = sprite;
    }

    @Override
    public Bitmap getBitmap() { return sprite; }

    public Direction[] prioritize(Direction dir, float dir_x, float dir_y) {
        Direction[] priority_dir = new Direction[4];
        if (dir == Direction.RIGHT || dir == Direction.LEFT) {
            if (dir_y < 0) {
                priority_dir[0] = Direction.DOWN;
                if (dir_x < 0) {
                    priority_dir[1] = Direction.LEFT;
                    priority_dir[2] = Direction.UP;
                    priority_dir[3] = Direction.RIGHT;
                } else {
                    priority_dir[1] = Direction.RIGHT;
                    priority_dir[2] = Direction.UP;
                    priority_dir[3] = Direction.LEFT;
                }
            } else {
                priority_dir[0] = Direction.UP;
                if (dir_x < 0) {
                    priority_dir[1] = Direction.LEFT;
                    priority_dir[2] = Direction.DOWN;
                    priority_dir[3] = Direction.RIGHT;
                } else {
                    priority_dir[1] = Direction.RIGHT;
                    priority_dir[2] = Direction.DOWN;
                    priority_dir[3] = Direction.LEFT;
                }
            }
        } else {
            if (dir_x < 0) {
                priority_dir[0] = Direction.LEFT;
                if (dir_y < 0) {
                    priority_dir[1] = Direction.DOWN;
                    priority_dir[2] = Direction.RIGHT;
                    priority_dir[3] = Direction.UP;
                } else {
                    priority_dir[1] = Direction.UP;
                    priority_dir[2] = Direction.RIGHT;
                    priority_dir[3] = Direction.DOWN;
                }
            } else {
                priority_dir[0] = Direction.RIGHT;
                if (dir_y < 0) {
                    priority_dir[1] = Direction.DOWN;
                    priority_dir[2] = Direction.LEFT;
                    priority_dir[3] = Direction.UP;
                } else {
                    priority_dir[1] = Direction.UP;
                    priority_dir[2] = Direction.LEFT;
                    priority_dir[3] = Direction.DOWN;
                }
            }
        }
        return priority_dir;
    }

    @Override
    public void moveAlgo1(Pacman p) {
        if (x%GameView.mBoxSize == 0 && y%GameView.mBoxSize == 0) {
            float dir_x = x - (float) p.x;
            float dir_y = y - (float) p.y;
            Random rand = new Random();
            ArrayList<Direction> available_dir = g.getEnemyPath(g.getPoint((int)x/GameView.mBoxSize, (int)y/GameView.mBoxSize));
            int rand_num = rand.nextInt(4);

            if (changeDirection == 0 || !available_dir.contains(this.getDirection())) {
                Log.d("Magenta ghost changeDir: ", "" + this.getDirection().name());
                if (rand_num < 2) {
                    //Go toward Pacman
                    Direction[] priorityDir = prioritize(this.getDirection(), dir_x, dir_y);
                    for (int i = 0; i < 4; i++) {
                        if (available_dir.contains(priorityDir[i])) {
                            this.setDirection(priorityDir[i]);
                            break;
                        }
                    }
                    /*if (available_dir.contains(Direction.LEFT) && x <= 0) {
                        this.setDirection(Direction.LEFT);
                    } else if (available_dir.contains(Direction.RIGHT) && x > 0) {
                        this.setDirection(Direction.RIGHT);
                    } else if (available_dir.contains(Direction.UP) && y <= 0) {
                        this.setDirection(Direction.UP);
                    } else if (available_dir.contains(Direction.DOWN) && y > 0) {
                        this.setDirection(Direction.DOWN);
                    } else {
                        int n = rand.nextInt(available_dir.size());
                        this.setDirection(available_dir.get(n));
                    }*/
                } else {
                    int n = rand.nextInt(available_dir.size());
                    this.setDirection(available_dir.get(n));
                }
                Log.d("Enemy:", "Enemy Direction: " + this.getDirection());
                changeDirection = rand.nextInt(5) + 3;
            } else {
                changeDirection--;
            }
        }
        move();
    }
}
