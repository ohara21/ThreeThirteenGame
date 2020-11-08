package edu.up.threethirteengame.TTGame;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

/**
 * @description TTMoveAction represents the moves a player could make in our Three Thirteen game
 * @author Nick Ohara
 * @version 11/5/20
 */
public class TTMoveAction extends GameAction {

    private Card discard;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public TTMoveAction(GamePlayer player) {
        super(player);
    }

    public TTMoveAction(GamePlayer player, Card card){
        super(player);
        discard = card;
    }


    /**
     * @return whether this is a "add group" move
     */
    public boolean isAddGroup(){return false;}

    /**
     * @return whether this is a "discard" move
     */
    public boolean isDiscard(){return false;}

    /**
     * @return whether this is a "draw discard" move
     */
    public boolean isDrawDiscard(){return false;}

    /**
     * @return whether this is a "draw deck" move
     */
    public boolean isDrawDeck(){return false;}

    /**
     * @return whether this is a "go out" move
     */
    public boolean isGoOut(){return false;}

    public Card getDiscard(){return discard;}
}
