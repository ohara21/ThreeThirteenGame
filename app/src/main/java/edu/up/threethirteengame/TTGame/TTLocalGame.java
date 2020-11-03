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

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

    }

    @Override
    protected boolean canMove(int playerIdx) {
        return false;
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
