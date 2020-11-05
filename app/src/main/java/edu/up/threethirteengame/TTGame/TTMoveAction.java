package edu.up.threethirteengame.TTGame;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

public class TTMoveAction extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public TTMoveAction(GamePlayer player) {
        super(player);
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
}
