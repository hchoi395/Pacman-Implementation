package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer introMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        //Sets onClick function for PLAY and QUIT button
        //playButton onClick
        ((Button) findViewById(R.id.playButton)).setOnClickListener(this);
        //quitButton onClick
        ((Button) findViewById(R.id.quitButton)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.optionsMenu)).setOnClickListener(this);

        int volume = getSharedPreferences("Settings", 0).getInt("volume", 5);

        //Intro music from Pacman. Copyright Bandai Namco
        introMusic = MediaPlayer.create(MainActivity.this, R.raw.intro);
        if (intent.getBooleanExtra("playIntro", true)) {
            introMusic.setVolume(.10f * volume / 5f, .10f * volume / 5f);
            introMusic.start();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.playButton): {
                //Switch to Config Screen, Game Screen for now.
                //Change GameActivity.class to Config class
                Intent intent = new Intent(getApplicationContext(), ConfigScreen.class);
                if (introMusic.isPlaying()) {
                    introMusic.stop();
                    introMusic.release();
                }
                startActivity(intent);
            }
                break;
            case (R.id.quitButton):
                //Quit game function, kills application
                if (introMusic.isPlaying()) {
                    introMusic.stop();
                    introMusic.release();
                }
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                break;
            case (R.id.optionsMenu): {
                Intent intent2 = new Intent(getApplicationContext(), OptionsActivity.class);
                startActivity(intent2);
            }
                break;
        }
    }
}
