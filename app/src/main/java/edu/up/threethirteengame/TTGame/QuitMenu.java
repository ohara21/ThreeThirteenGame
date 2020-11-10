package edu.up.threethirteengame.TTGame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import edu.up.threethirteengame.R;

public class QuitMenu extends AppCompatActivity {
    /**External Citation
     * Date: 11/10/20
     * Problem: how to make a quit menu
     * Source: Blokus Game by Dylan Pascua, Nicholas Baldwin, Justin Cao
     * Solution: looked at their implementation and figured it out
     */

    //buttons if you want to quit or not
    private Button yesButton;
    private Button noButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quit_menu);

        yesButton = findViewById(R.id.yes_quit_button);
        noButton = findViewById(R.id.no_quit_button);

        //closes activity when "no" button is touched
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ends game when "yes" button is pressed
        //and returns user to starting menu
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}
