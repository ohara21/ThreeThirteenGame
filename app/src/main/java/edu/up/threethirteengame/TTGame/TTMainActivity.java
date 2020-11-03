package edu.up.threethirteengame.TTGame;

import androidx.appcompat.app.AppCompatActivity;
import edu.up.threethirteengame.R;
import edu.up.threethirteengame.game.GameFramework.GameMainActivity;
import edu.up.threethirteengame.game.GameFramework.LocalGame;
import edu.up.threethirteengame.game.GameFramework.gameConfiguration.GameConfig;
import android.os.Bundle;

/**
 * @description the primary activity for Three Thirteen
 * @author Nick Ohara, Shane Matsushima, Lindsey Warren, Adrian Muth
 * @version 11/3/20
 */
public class TTMainActivity extends GameMainActivity {

    @Override
    public GameConfig createDefaultConfig() {
        return null;
    }

    @Override
    public LocalGame createLocalGame() {
        return null;
    }
}