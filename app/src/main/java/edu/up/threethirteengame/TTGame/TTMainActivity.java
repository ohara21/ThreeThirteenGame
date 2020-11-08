package edu.up.threethirteengame.TTGame;

import androidx.appcompat.app.AppCompatActivity;
import edu.up.threethirteengame.R;
import edu.up.threethirteengame.game.GameFramework.GameMainActivity;
import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.LocalGame;
import edu.up.threethirteengame.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.threethirteengame.game.GameFramework.gameConfiguration.GamePlayerType;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * @description the primary activity for Three Thirteen
 * @author Nick Ohara, Shane Matsushima, Lindsey Warren, Adrian Muth
 * @version 11/3/20
 */
public class TTMainActivity extends GameMainActivity {

    /**
     * External Citation
     * Date: 11/8/20
     * Problem: did not know how to do our MainActivity
     * Source:https://github.com/cs301up/Counter.git
     * Solution: copied and modified code
     */
    public static final int PORT_NUMBER = 4752;

    /** a Three Thirteen game for two players. The default is human vs. computer */
    @Override
    public GameConfig createDefaultConfig() {

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // a human player player type (player type 0)
        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new TTHumanPlayer(name);
            }});

        // a computer player type (player type 1)
        playerTypes.add(new GamePlayerType("Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new TTComputerPlayer(name);
            }});

//        // a computer player type (player type 2)
//        playerTypes.add(new GamePlayerType("Computer Player (GUI)") {
//            public GamePlayer createPlayer(String name) {
//                return new TTComputerPlayer2(name);
//            }});

        // Create a game configuration class for Counter:
        // - player types as given above
        // - from 1 to 2 players
        // - name of game is "Counter Game"
        // - port number as defined above
        GameConfig defaultConfig = new GameConfig(playerTypes, 1, 2, "Counter Game",
                PORT_NUMBER);

        // Add the default players to the configuration
        defaultConfig.addPlayer("Human", 0); // player 1: a human player
        defaultConfig.addPlayer("Computer", 1); // player 2: a computer player

        // Set the default remote-player setup:
        // - player name: "Remote Player"
        // - IP code: (empty string)
        // - default player type: human player
        defaultConfig.setRemoteData("Remote Player", "", 0);

        // return the configuration
        return defaultConfig;
    }//createDefaultConfig

    @Override
    public LocalGame createLocalGame() {
        return new TTLocalGame();
    }
}