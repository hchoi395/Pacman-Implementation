package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class EndScreenActivity extends AppCompatActivity {
    String name;
    int score;
    String[] scores = new String[11];
    int scoreNum = 0;
    int enemiesKilled;
    int time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);
        Intent prev_intent = getIntent();
        name = prev_intent.getStringExtra("Name");
        score = prev_intent.getIntExtra("Score", 0);
        enemiesKilled = prev_intent.getIntExtra("EnemiesKilled", 0);
        time = prev_intent.getIntExtra("Time", 0);
        findViewById(R.id.restartButton).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ConfigScreen.class);
            startActivity(intent);
        });
        findViewById(R.id.backToTitle).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("playIntro", true);
            startActivity(intent);
        });
        loadScores(name, score);
        //((ScoreView)findViewById(R.id.appScoreView)).init();
        LinearLayout layout = (LinearLayout) findViewById(R.id.scoreLayout);
        //loadScores(name, score);
        layout.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setMarginStart(400);
        int highestScoreViewSize = scores[0].substring(scores[0].indexOf(";")).length();
        for (int i = 0; i < 10; i++) {
            TextView text = new TextView(getApplicationContext());
            text.setId(View.generateViewId());
            if (scores[i] != null) {
                String[] showScore = scores[i].split(";", 2);

                text.setText(String.format("%-15s%" + highestScoreViewSize + "s", showScore[0], showScore[1]));
            }
            else text.setText(String.format("%-15s%s", "-----", "-----"));

            text.setTextSize(20);
            text.setTextColor(Color.WHITE);
            text.setLayoutParams(layoutParams);
            layout.addView(text);
        }
        TextView text = new TextView(getApplicationContext());
        text.setId(View.generateViewId());
        text.setTextSize(20);
        text.setTextColor(Color.WHITE);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setMargins(0, 60, 0, 0);
        text.setLayoutParams(layoutParams);
        text.setText(String.format("Your score: %d | Time: %d", score, time));
        layout.addView(text);

        //Enemies
        TextView textEnemies = new TextView(getApplicationContext());
        textEnemies.setId(View.generateViewId());
        textEnemies.setTextSize(20);
        textEnemies.setTextColor(Color.WHITE);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setMargins(0, 60, 0, 0);
        textEnemies.setLayoutParams(layoutParams);
        textEnemies.setText(String.format("Enemies killed: %d", enemiesKilled));
        layout.addView(textEnemies);

        TextView textWin = new TextView(getApplicationContext());
        textWin.setId(View.generateViewId());
        textWin.setTextSize(30);
        textWin.setTextColor(Color.WHITE);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setMargins(0, 60, 0, 0);
        textWin.setLayoutParams(layoutParams);
        textWin.setText((prev_intent.getExtras().getBoolean("Win")) ? "You Win" : "You Lose");
        layout.addView(textWin);
    }

    public void loadScores(String name, int score) {
        File scoreFile = new File(getFilesDir(), "scores.scrs");
        if (scoreFile.exists()) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(scoreFile));
                BufferedReader readScore = new BufferedReader(reader);
                String line;
                while ((line = readScore.readLine()) != null) {
                    scores[scoreNum] = new String(line);
                    scoreNum++;
                }
                readScore.close();
                reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("Score file not found");
            } catch (IOException e) {
                System.out.println("Read error");
            }
        }
        int replaceScore = scoreNum;
        //scores[scoreNum] = "";
        Log.d("ScoreNum: ", "" + scoreNum);
        for (int i = scoreNum; i > 0; i--) {
            int sc = Integer.parseInt(scores[i-1].substring(scores[i-1].indexOf(';') +1));
            Log.d("Score compare: ", sc + "" + score);
            if (sc < score) {
                scores[i] = scores[i-1];
                replaceScore = (i-1);
            } else {
                break;
            }
        }
        scores[replaceScore] = name + ";" + score;
        if (scoreNum < 10) scoreNum++;
        try {
            FileWriter writer = new FileWriter(scoreFile);
            for (int i = 0; i < scoreNum; i++) {
                writer.write(scores[i] + "\n");
                writer.flush();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Write error");
        }
    }
}