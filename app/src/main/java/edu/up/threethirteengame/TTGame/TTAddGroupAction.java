package edu.up.threethirteengame.TTGame;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

/**
 * @description TTAddGroupAction is an action that represents a player adding selected cards
 * adding selected cards to a group
 * @author Nick Ohara
 * @version 11/3/2020
 */
public class TTAddGroupAction extends TTMoveAction implements Serializable {
    /**
     * constructor for TTAddGroupAction
     *
     * @param player the player who created the action
     * @param group the group to be added
     */
    public TTAddGroupAction(GamePlayer player, ArrayList<Card> group) {
        super(player, group);
    }

    /**
     * @return whether this action ia an "add group" action
     */
    public boolean isAddGroup(){ return true;}
}
