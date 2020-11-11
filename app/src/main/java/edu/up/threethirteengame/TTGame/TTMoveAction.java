package edu.up.threethirteengame.TTGame;

import java.util.ArrayList;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

/**
 * @description TTMoveAction represents the moves a player could make in our Three Thirteen game
 * @author Nick Ohara
 * @version 11/5/20
 */
public class TTMoveAction extends GameAction {

    private Card discard;
    private ArrayList<Card> addGroup;
    private Card removeGroup;

    /**
     * constructor for TTMoveAction
     *
     * @param player the player who created the action
     */
    public TTMoveAction(GamePlayer player) {
        super(player);
    }

    /**
     * constructor for TTMoveAction if there's a card to be discarded
     * @param player the player who created the action
     * @param card the card to be changed
     * @param ifDiscard whether the card to be changed is to be discarded or removed from group
     */
    public TTMoveAction(GamePlayer player, Card card, boolean ifDiscard){
        super(player);
        if(ifDiscard) {
            discard = card;
        }
        else{
            removeGroup = card;
        }
    }

    /**
     * constructor for TTMoveAction if there's a group to add to player
     * @param player the player who created the action
     * @param group the group to be added
     */
    public TTMoveAction(GamePlayer player, ArrayList<Card> group){
        super(player);
        addGroup = group;
    }


    /**
     * @return whether this is a "add group" move
     */
    public boolean isAddGroup(){return false;}

    /**
     * @return whether this is a "remove group" move
     */
    public boolean isRemoveGroup(){return false;}

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

    /**
     * @return the card to be discarded
     */
    public Card getDiscard(){return discard;}

    /**
     * @return the card to be removed from it's group
     */
    public Card getRemoveGroup(){return removeGroup;}

    /**
     * @return the group to be added to a player's Hand
     */
    public ArrayList<Card> getAddGroup(){return addGroup;}
}
