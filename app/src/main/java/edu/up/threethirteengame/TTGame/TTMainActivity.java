package edu.up.threethirteengame.TTGame;

import androidx.appcompat.app.AppCompatActivity;
import edu.up.threethirteengame.R;
import edu.up.threethirteengame.game.GameFramework.GameMainActivity;
import edu.up.threethirteengame.game.GameFramework.LocalGame;
import edu.up.threethirteengame.game.GameFramework.gameConfiguration.GameConfig;

import android.os.Bundle;

public class TTMainActivity extends GameMainActivity {

    @Override
    public GameConfig createDefaultConfig() {
        return null;
    }

    @Override
    public LocalGame createLocalGame() {
        return null;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
}