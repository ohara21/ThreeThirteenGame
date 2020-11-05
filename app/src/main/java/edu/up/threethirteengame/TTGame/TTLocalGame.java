package edu.up.threethirteengame.TTGame;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.LocalGame;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

/**
 * @description The LocalGame class for a Three Thirteen game.  Defines and enforces
 * the game rules; handles interactions between players.
 *
 * @author Nick Ohara, Shane Matsushima, Lindsey Warren, Adrian Muth
 * @version 11/3/20
 */
public class TTLocalGame extends LocalGame {

    /** External Citation
     * Used Dr. Steven Vegdahl's Slapjack LocalGame class as a reference
     * Source: https://github.com/cs301up/SlapJack.git
     */

    // the game's state
    private TTGameState state;

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

    }

    /**
     * can this player move
     * @param playerIdx
     * 		the player's player-number (ID)
     * @return
     */
    @Override
    protected boolean canMove(int playerIdx) {
        if (playerIdx < 0 || playerIdx > 1) {
            // if our player-number is out of range, return false
            return false;
        }
        else {
            // player can move if it's their turn
            return state.canMove(state);
        }
    }

    @Override
    protected String checkIfGameOver() {
        return null;
    }

    @Override
    protected boolean makeMove(GameAction action) {
        return false;
    }
}
