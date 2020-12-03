package edu.up.threethirteengame.TTGame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import edu.up.threethirteengame.R;

/**
 * @description: QuitMenu class displays a new window if the user wants to quit the game
 * @author: Nick Ohara, Adrian Muth, Shane Matsushima, Lindsey Warren
 * @version: 10/20/2020
 */
public class QuitMenu extends AppCompatActivity {
    /**External Citation
     * Date: 11/10/20
     * Problem: how to make a quit menu
     * Source: Blokus Game by Dylan Pascua, Nicholas Baldwin, Justin Cao
     * Solution: looked at their implementation and figured it out
     */

    /**External Citation
     * Date: 11/21/20
     * Problem: how to completely exit the app
     * Source: https://stackoverflow.com/questions/3226495/how-to-exit-from-the-application-and-show-the-home-screen/9735524#9735524
     * Solution: read explanations and used code given
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

        //closes the app when "yes" button is pressed
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
            }
        });
    }
}
