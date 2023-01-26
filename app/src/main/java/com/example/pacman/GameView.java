package com.example.pacman;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

import android.os.Handler;
import android.widget.TextView;

/*
NOTE: This code was refactored and adapted from a youtube snake tutorial.
Link: https://www.youtube.com/watch?v=edUrUoZHouY&ab_channel=HDROIDStudio
 */

public class GameView extends View {
    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //Constants
    public static final int MAP_SIZE = 20;
    private static final int START_X = 5;
    private static final int START_Y = 10;

    //Tiles and Pacman
    private final Point[][] mPoints = new Point[MAP_SIZE][MAP_SIZE];
    private Pacman mPacMan;
    public int pelletCount = 0;
    private Direction mDir;
    private boolean mGameOver = false;
    private static MediaPlayer player;
    //Enemies
    private GreenGhost mGreen = new GreenGhost(this);
    private MagentaGhost mMagenta = new MagentaGhost(this);
    private RedGhost mRed = new RedGhost(this);
    private YellowGhost mYellow = new YellowGhost(this);

    private boolean mGameWin = false;
    //private PriorityQueue<Point> pelletQueue;
    public LinkedList<PointType> enemyQueue;
    public Enemy[] enemies;
    //Sizing
    public static int mBoxSize;
    private int mBoxPadding;
    public int spawnTimer = 10;
    private Paint mPaint = new Paint();

    //private Handler mHandler;
    public void init(Pacman pacman) {
        mBoxSize = getContext().getResources().getDimensionPixelSize(R.dimen.game_size) / MAP_SIZE;
        mDir = Direction.RIGHT;
        mBoxPadding = mBoxSize / 20;
        mPacMan = pacman;
        mPacMan.setBoxSize(mBoxSize);
        //pelletQueue = new PriorityQueue<>((a, b) -> (a.x - b.x + b.y - a.y)%10);
        enemyQueue = new LinkedList<PointType>();
        enemyQueue.add(PointType.ENEMYMAG);
        enemyQueue.add(PointType.ENEMYGREEN);
        enemyQueue.add(PointType.ENEMYYELLOW);
        enemyQueue.add(PointType.ENEMYRED);
        Enemy.setScaredBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.scare_ghost));
        mMagenta.setBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ghost_0));
        mRed.setBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ghost_1));
        mGreen.setBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ghost_2));
        mYellow.setBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ghost_3));
        //mMagenta.setVisible(true);
        enemies = new Enemy[]{mMagenta, mRed, mGreen, mYellow};
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                mPoints[i][j] = new Point(j, i);
            }
        }

        Map_Layout map_layout = new Map_Layout(2);

        int[][] mLayout = map_layout.getMap_layout();

        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                Point point = getPoint(j, i);
                switch (mLayout[i][j]) {
                    case 0:
                        point.type = PointType.EMPTY;
                        break;
                    case 1:
                        point.type = PointType.WALL;
                        break;
                    case 2:
                        point.type = PointType.PELLET;
                        pelletCount++;
                        break;
                    case 3:
                        point.type = PointType.DOUBLE_PELLET;
                        pelletCount++;
                        break;
                    case 4:
                        point.type = PointType.SPEED_PELLET;
                        pelletCount++;
                        break;
                    case 5:
                        point.type = PointType.INVINCIBLE_PELLET;
                        pelletCount++;
                        break;

                }
            }
        }
        Log.d("Box size: ", "" + mBoxSize);
        mPacMan.setLocation(mBoxSize * 9, mBoxSize * 14);
        mPacMan.setSpawnPoint(mBoxSize * 9, mBoxSize * 14);
        mMagenta.setLocation(mBoxSize * 8, mBoxSize * 7);
        mGreen.setLocation(mBoxSize * 8, mBoxSize * 7);
        mYellow.setLocation(mBoxSize * 8, mBoxSize * 7);
        mRed.setLocation(mBoxSize * 8, mBoxSize * 7);
    }

    public void resetMap() {
        mPacMan.respawn();
        mGreen.setVisible(false);
        mRed.setVisible(false);
        mMagenta.setVisible(false);
        mYellow.setVisible(false);
        mMagenta.setLocation(mBoxSize * 8, mBoxSize * 7);
        mGreen.setLocation(mBoxSize * 8, mBoxSize * 7);
        mYellow.setLocation(mBoxSize * 8, mBoxSize * 7);
        mRed.setLocation(mBoxSize * 8, mBoxSize * 7);
        enemyQueue.clear();
        enemyQueue.add(PointType.ENEMYMAG);
        enemyQueue.add(PointType.ENEMYGREEN);
        enemyQueue.add(PointType.ENEMYYELLOW);
        enemyQueue.add(PointType.ENEMYRED);

    }


    public Point getPoint(int x, int y) {
        return mPoints[y][x];
    }

    public void spawnGhost(int i, int j) {
        if (!enemyQueue.isEmpty() && spawnTimer <= 0) {
            //Log.d("QUEUE", "" + enemyQueue.getFirst());
            spawnTimer = 60;
            Point point = getPoint(j, i);
            if (mPacMan.x != i * 1f * mBoxSize && mPacMan.y != i * mBoxSize * 1f) {
                PointType enemy = enemyQueue.remove();
                //Log.d("Spawning Enemy", "" + enemy);
                switch (enemy) {
                    case ENEMYGREEN:
                        mGreen.setVisible(true);
                        break;
                    case ENEMYRED:
                        mRed.setVisible(true);
                        break;
                    case ENEMYMAG:
                        mMagenta.setVisible(true);
                        break;
                    case ENEMYYELLOW:
                        mYellow.setVisible(true);
                        break;
                }
            }
        }
        spawnTimer--;

        //Log.d("Spawn Timer: ", "" + spawnTimer);
    }

    public void next(Direction inputDirection) {
        spawnGhost(8, 7);
        mPacMan.next(inputDirection);
        enemyNext();
        if (mPacMan.lives <= 0) {
            mGameOver = true;
        } else if (pelletCount <= 0) {
            mGameWin = true;
        }
    }

    public void enemyNext() {
        //enemyNext(mGreen);
        //enemyNext(mRed);
        //enemyNext(mMagenta);
        if (mMagenta.getVisible()) mMagenta.moveAlgo1(mPacMan);
        if (mRed.getVisible()) mRed.moveAlgo1(mPacMan);
        if (mGreen.getVisible()) mGreen.moveAlgo1(mPacMan);
        if (mYellow.getVisible()) mYellow.moveAlgo1(mPacMan);
    }

    public void setDirection(Direction dir) {
        mDir = dir;
    }

    public Point getNext(Point point, Direction nextDirection) {
        int x = point.x;
        int y = point.y;

        switch (nextDirection) {
            case UP:
                y = y == 0 ? MAP_SIZE - 1 : y - 1;
                break;
            case DOWN:
                y = y == MAP_SIZE - 1 ? 0 : y + 1;
                break;
            case LEFT:
                x = x == 0 ? MAP_SIZE - 1 : x - 1;
                break;
            case RIGHT:
                x = x == MAP_SIZE - 1 ? 0 : x + 1;
                break;
        }
        return getPoint(x, y);
    }

    public boolean isGameOver() {
        return mGameOver;
    }

    public boolean isGameWin() {
        return mGameWin;
    }

    public ArrayList<Direction> getEnemyPath(Point enemyPoint) {
        ArrayList<Direction> paths = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            Point enemyNext = getNext(enemyPoint, direction);
            if (enemyNext.type != PointType.WALL) {
                paths.add(direction);
            }
        }
        return paths;
    }


    public Direction opposite(Direction dir) {
        Direction skip = Direction.DOWN;
        switch (dir) {
            case UP:
                skip = Direction.DOWN;
                break;
            case DOWN:
                skip = Direction.UP;
                break;
            case LEFT:
                skip = Direction.RIGHT;
                break;
            case RIGHT:
                skip = Direction.LEFT;
                break;
        }
        return skip;
    }

    public ArrayList<Direction> getEnemyPath(Point enemyPoint, Direction dir){
        ArrayList<Direction> paths = new ArrayList<>();
        Direction skip = opposite(dir);
        /*switch (dir) {
            case UP:
                skip = Direction.DOWN;
                break;
            case DOWN:
                skip = Direction.UP;
                break;
            case LEFT:
                skip = Direction.RIGHT;
                break;
            case RIGHT:
                skip = Direction.LEFT;
                break;
        }*/

        for (Direction direction : Direction.values()) {
            if (direction != skip) {
                Point enemyNext = getNext(enemyPoint, direction);
                if (enemyNext.type != PointType.WALL) {
                    paths.add(direction);
                }
            }
        }
        return paths;
    }

    public void playSound(int id) {
        player = MediaPlayer.create(this.getContext(), id);
        player.setOnCompletionListener(p1 -> p1.release());
        player.setVolume(.5f * GameActivity.volume/5f, .5f * GameActivity.volume/5f);
        player.start();
        if (id == R.raw.lose) {
            while (player.isPlaying());
        }
        return;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        canvas.drawPaint(mPaint);
        float ssize = mBoxSize * .4f;
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                Point cur = getPoint(x, y);
                int left = mBoxSize * x;
                int right = left + mBoxSize;
                int top = mBoxSize * y;
                int bottom = top + mBoxSize;
                switch (getPoint(x, y).type) {
                    case PELLET:
                        mPaint.setStyle(Paint.Style.FILL);
                        if (mPacMan.getDouble()) {
                            mPaint.setColor(Color.parseColor("#FC9D03"));
                        } else {
                            mPaint.setColor(Color.WHITE);
                        }
                        canvas.drawRect(left + (mBoxSize / 2f - ssize / 2), top + (mBoxSize / 2f - ssize / 2), left + (mBoxSize / 2f + ssize / 2), top + (mBoxSize / 2f + ssize / 2), mPaint);
                        break;
                    case DOUBLE_PELLET:
                        mPaint.setStyle(Paint.Style.FILL);
                        mPaint.setColor(Color.parseColor("#FC9D03"));
                        //canvas.drawOval(left, top, right, bottom, mPaint);
                        canvas.drawCircle(left + mBoxSize/2f, top + mBoxSize/2f, mBoxSize/3f, mPaint);
                        break;
                    case SPEED_PELLET:
                        mPaint.setStyle(Paint.Style.FILL);
                        mPaint.setColor(Color.parseColor("#40E0D0"));
                        //canvas.drawOval(left, top, right, bottom, mPaint);
                        canvas.drawCircle(left + mBoxSize/2f, top + mBoxSize/2f, mBoxSize/3f, mPaint);
                        break;
                    case INVINCIBLE_PELLET:
                        mPaint.setStyle(Paint.Style.FILL);
                        mPaint.setColor(Color.parseColor("#FF0000"));
                        //canvas.drawOval(left, top, right, bottom, mPaint);
                        canvas.drawCircle(left + mBoxSize / 2f, top + mBoxSize / 2f, mBoxSize / 3f, mPaint);
                        break;
                    case WALL:
                        mPaint.setStyle(Paint.Style.STROKE);
                        mPaint.setStrokeWidth(10f);
                        mPaint.setColor(Color.BLUE);
                        canvas.drawRect(left, top, right, bottom, mPaint);
                        break;
                }
            }

        }

        float left = (float) mPacMan.x;
        float right = left + mBoxSize;
        float top = (float) mPacMan.y;
        float bottom = top + mBoxSize;
        canvas.drawBitmap(mPacMan.getBitmap(), null, new RectF(left, top, right, bottom), mPaint);

        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].getVisible()) {
                left = enemies[i].x;
                right = left + mBoxSize;
                top = enemies[i].y;
                bottom = top + mBoxSize;
                //canvas.draw
                if (!mPacMan.getSuper()) {
                    canvas.drawBitmap(enemies[i].getBitmap(), null, new RectF(left, top, right, bottom), mPaint);
                } else {
                    canvas.drawBitmap(Enemy.scareSprite, null, new RectF(left, top, right, bottom), mPaint);
                }
            }
        }


        /*mPaint.setColor( Color.RED );
        mPaint.setStrokeWidth( 1.5f );
        mPaint.setStyle(Paint.Style.STROKE);

        canvas.drawRect( 0, 0, getWidth(), getHeight(), mPaint);*/

        //Text Display
        TextView scoreText = (TextView) ((GameActivity) getContext()).findViewById(R.id.scoreText);
        scoreText.setText("Score: " + mPacMan.score);
        TextView livesText = (TextView) ((GameActivity) getContext()).findViewById(R.id.livesText);
        livesText.setText("Lives: " + mPacMan.lives);

    }

}
