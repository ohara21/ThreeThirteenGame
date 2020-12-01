package edu.up.threethirteengame.TTGame;

import edu.up.threethirteengame.game.GameFramework.GameMainActivity;
import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.LocalGame;
import edu.up.threethirteengame.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.threethirteengame.game.GameFramework.gameConfiguration.GamePlayerType;

import java.util.ArrayList;

/**
 * @description the primary activity for Three Thirteen
 * @author Nick Ohara, Shane Matsushima, Lindsey Warren, Adrian Muth
 * @version 11/3/20
 */

/**
 * @BetaRelease
 * Playable according to the rules of Three Thirteen
 * GUI functionality specified in the requirements are present. Added a text box that displays
 *      information regarding illegal moves, actions taken, etc. Added indicator if player needs to draw.
 *      Added text box that tells player what the wild card is for the round. Added confirmation screen
 *      for quit button.
 * Dumb AI: Functional according to requirements, but will never be able to score according to current implementation.
 *      This could happen in a real game, but the team will continue to make improvements so the Dumb AI can score sometimes
 * Smart AI: Functional according to requirements. The Smart AI has the ability to create groups of sets and runs in order
 *      to check if the AI is able to go out. It also is capable of understanding which cards are needed to complete a set and
 *      will check the discard pile to see if the card is suitable to compelete a set. The able to discard cards in hand does update
 *      per turn as the hand is always changing based on draws and discards.
 */
public class TTMainActivity extends GameMainActivity {

    /**
     * External Citation
     * Date: 11/8/20
     * Problem: did not know how to do our MainActivity
     * Source:https://github.com/cs301up/Counter.git
     * Solution: copied and modified code
     */
    public static final int PORT_NUMBER = 9893;

    /** a Three Thirteen game for two players. The default is human vs. computer */
    @Override
    public GameConfig createDefaultConfig() {

        // closes app if user has clicked "yes" on quit button
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
        }

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // a human player player type (player type 0)
        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new TTHumanPlayer(name);
            }});

        // a computer player type (player type 1)
        playerTypes.add(new GamePlayerType("Easy AI") {
            public GamePlayer createPlayer(String name) {
                return new TTComputerPlayerDumb(name);
            }});

        //a smart computer player
        playerTypes.add(new GamePlayerType("Difficult AI") {
            public GamePlayer createPlayer(String name) {
                return new TTComputerPlayerSmart(name);
            }});

//        // a computer player type (player type 2)
//        playerTypes.add(new GamePlayerType("Computer Player (GUI)") {
//            public GamePlayer createPlayer(String name) {
//                return new TTComputerPlayer2(name);
//            }});

        // Create a game configuration class for Three Thirteen:
        // - player types as given above
        // - from 1 to 2 players
        // - name of game is "Three Thirteen Game"
        // - port number as defined above
        GameConfig defaultConfig = new GameConfig(playerTypes, 1, 2, "Three Thirteen Game",
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