package edu.up.threethirteengame.TTGame;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

/**
 * @description TTDiscardAction represents discarding a player's card to the discard pile
 * @author Nick Ohara
 * @version 11/3/20
 */
public abstract class TTDiscardAction extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public TTDiscardAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return whether this action is a "discard" move
     */
    public boolean isDiscard(){return true;}
}
