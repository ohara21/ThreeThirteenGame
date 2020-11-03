package edu.up.threethirteengame.TTGame;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

/**
 * TTAddGroupAction is an action that represents a player adding selected cards
 * to a group
 * @author Nick Ohara
 * @version 11/3/2020
 */
public abstract class TTAddGroupAction extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public TTAddGroupAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return whether this action ia an "add group" action
     */
    public boolean isAddGroup(){ return true;}
}
