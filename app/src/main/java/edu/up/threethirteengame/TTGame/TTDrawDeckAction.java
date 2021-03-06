package edu.up.threethirteengame.TTGame;

import java.io.Serializable;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;

/**
 * @description TTDrawDeckAction represents drawing a card from the deck
 * @author Nick Ohara
 * @version 11/3/20
 */
public class TTDrawDeckAction extends TTMoveAction implements Serializable {
    /**
     * constructor for TTDrawDeckAction
     *
     * @param player the player who created the action
     */
    public TTDrawDeckAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return whether this is a "draw deck" move
     */
    public boolean isDrawDeck(){return true;}
}
