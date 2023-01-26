
package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.media.Image;
import android.widget.ImageView;
import android.widget.Toast;
public class ConfigScreen extends AppCompatActivity {

    private int difficulty = 0;
    private ImageButton moreDifficulty;
    private ImageButton lessDifficulty;
    private TextView difficultyLevel;
    private TextView difficultyDescription;
    private String[] diffArr = {"3 Lives, +0 bonus points/pellet",
            "2 Lives, +25 bonus points/pellet",
            "1 Life, +50 bonus points/pellet"};
    private String name;
    private EditText nameInput;
    private Button nextButton;

    private int currentSprite = 0;
    /*private String[] sprites = {
            "ic_menu_edit",
            "ic_menu_delete",
            "ic_menu_directions"
    };*/
    private int[] sprites = {
            R.drawable.sprite_pacman,
            R.drawable.sprite_pacman_2
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_screen);

        difficultyLevel = (TextView) findViewById(R.id.difficultyLevel);
        moreDifficulty = (ImageButton) findViewById(R.id.moreDifficulty);
        lessDifficulty = (ImageButton) findViewById(R.id.lessDifficulty);
        difficultyDescription = (TextView) findViewById(R.id.difficultyDescription);
        difficultyDescription.setText(diffArr[difficulty]);
        nameInput = (EditText) findViewById(R.id.nameInput);
        nextButton = (Button) findViewById(R.id.goNext);

        moreDifficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (difficulty < 2) {
                    difficulty++;
                    difficultyLevel.setText(String.format("%d", difficulty));
                    difficultyDescription.setText(diffArr[difficulty]);
                }

            }
        });

        lessDifficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (difficulty > 0) {
                    difficulty--;
                    difficultyLevel.setText(String.format("%d", difficulty));
                    difficultyDescription.setText(diffArr[difficulty]);
                }

            }
        });
        ImageView spritePrevious = (ImageView) findViewById(R.id.spritePrevious);
        ImageView spriteNext = (ImageView) findViewById(R.id.spriteNext);
        spriteNext.setClickable(true);
        spritePrevious.setClickable(true);
        spriteNext.setOnClickListener((view) -> setPlayerSprite(1));
        spritePrevious.setOnClickListener((view) -> setPlayerSprite(-1));

        nextButton.setOnClickListener((view) ->  {
            if (nameInput.getText().toString() == null) {
                Toast.makeText(this, "Your name is null! Setting to default.",
                        Toast.LENGTH_SHORT).show();
                //set name to default
            } else if (nameInput.getText().toString().equals(" ")
                    || nameInput.getText().toString().equals("")) {
                Toast.makeText(this, "Your name is empty! Setting to default.",
                        Toast.LENGTH_SHORT).show();
                //set name to default
            } else {
                name = nameInput.getText().toString();
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("Name", name);
                intent.putExtra("Difficulty", difficulty);
                intent.putExtra("sprite_path", sprites[currentSprite]);
                startActivity(intent);
            }
        });

        setPlayerSprite(0);
    }

    void setPlayerSprite(int next) {
        currentSprite += next;
        if (currentSprite < 0) {
            currentSprite = sprites.length - 1;
        } else {
            currentSprite = currentSprite%(sprites.length);
        }

        ImageView sprite = (ImageView) findViewById(R.id.playerSprite);
        sprite.setImageResource(sprites[currentSprite]);
        //sprite.setImageResource(getResources().getIdentifier("@android:drawable/" + sprites[currentSprite], null, getPackageName()));
    }
}