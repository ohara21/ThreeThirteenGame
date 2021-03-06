package edu.up.threethirteengame.TTGame;

import java.io.Serializable;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

/**
 * @description TTDiscardAction represents discarding a player's card to the discard pile
 * @author Nick Ohara
 * @version 11/3/20
 */
public class TTDiscardAction extends TTMoveAction implements Serializable {

    /**
     * constructor for TTDiscardAction
     *
     * @param player the player who created the action
     */
    public TTDiscardAction(GamePlayer player) { super(player); }

    /**
     * constructor for TTDiscardAction
     * @param player the player who created the action
     * @param c the card to be discarded
     */
    public TTDiscardAction(GamePlayer player, Card c){
        super(player, c, true);
    }


    /**
     * @return whether this action is a "discard" move
     */
    public boolean isDiscard(){return true;}

}
