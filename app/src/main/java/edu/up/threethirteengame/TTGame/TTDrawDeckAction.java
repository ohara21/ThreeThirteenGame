package edu.up.threethirteengame.TTGame;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;

/**
 * TTDrawDeckAction represents drawing a card from the deck
 */
public abstract class TTDrawDeckAction extends TTDrawAction {
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
