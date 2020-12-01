package edu.up.threethirteengame.TTGame;

import java.io.Serializable;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

/**
 * @description TTGoOutAction is an action that represents a player attempting to Go Out
 * @author Nick Ohara
 * @version 11/3/20
 */
public class TTGoOutAction extends TTMoveAction implements Serializable {
    /**
     * constructor for TTGoOutAction
     *
     * @param player the player who created the action
     */
    public TTGoOutAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return whether this action is a "Go Out" move
     */
    public boolean isGoOut(){return true;}
}
