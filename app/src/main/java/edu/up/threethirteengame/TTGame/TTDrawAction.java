package edu.up.threethirteengame.TTGame;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

/**
 * TTDrawAction represents when a card is drawn from either the
 * discard pile or deck pile
 * @author Nick Ohara
 * @version 11/3/20
 */
public abstract class TTDrawAction extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public TTDrawAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return whether the card was drawn from the discard pile
     */
    public boolean isDrawDiscard(){return false;}

    /**
     * @return whether the card was drawn from the deck pile
     */
    public boolean isDrawDeck(){return false;}
}
