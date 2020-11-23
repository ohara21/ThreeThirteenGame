package edu.up.threethirteengame.TTGame;

import java.util.ArrayList;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;

/**
 * @description TTRemoveGroupAction is an action that represents a player removing a group
 * @author Nick Ohara
 * @version 11/10/2020
 */
public class TTRemoveGroupAction extends TTMoveAction{

    /**
     * constructor for the TTRemoveGroupAction
     * @param player the player who took the action
     */
    public TTRemoveGroupAction(GamePlayer player) {
        super(player);
    }

    /**
     * constructor to remove the group
     * @param player the player who took the action
     * @param cardInGroup the group that this card is in will be removed
     */
    public TTRemoveGroupAction(GamePlayer player, Card cardInGroup){
        super(player, cardInGroup, false) ;
    }

    /**
     * @return whether this is a "remove group" move
     */
    public boolean isRemoveGroup(){return true;}
}
