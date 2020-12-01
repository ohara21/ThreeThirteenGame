package edu.up.threethirteengame.TTGame;

import java.io.Serializable;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;

/**
 * @description TTDrawDiscardAction represents card being drawn from discard pile
 * @author Nick Ohara
 * @version 11/3/20
 */
public class TTDrawDiscardAction extends TTMoveAction implements Serializable {

    /**
     * constructor for TTDrawDiscardAction class
     *
     * @param player the player who created the action
     */
    public TTDrawDiscardAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return whether this move is a "draw discard" move
     */
    public boolean isDrawDiscard(){return true;}
}
