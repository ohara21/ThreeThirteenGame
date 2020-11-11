package edu.up.threethirteengame.TTGame;

import android.util.Log;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.LocalGame;
import edu.up.threethirteengame.game.GameFramework.actionMessage.GameAction;

/**
 * @description The LocalGame class for a Three Thirteen game.  Defines and enforces
 * the game rules; handles interactions between players.
 *
 * @author Nick Ohara, Shane Matsushima, Lindsey Warren, Adrian Muth
 * @version 11/5/20
 */
public class TTLocalGame extends LocalGame {

    /** External Citation
     * Used Dr. Steven Vegdahl's Slapjack LocalGame class as a reference
     * Source: https://github.com/cs301up/SlapJack.git
     */

    // the game's state
    private TTGameState state;

    /**
     * Constructor for LocalGame creates a new GameState for the beginning of the game
     */
    public TTLocalGame(){
        super();
        state = new TTGameState();
    }//TTLocalGame

    /**
     * creates a deep copy of current game state and sends it to given player
     * @param p the player that the LocalGame needs to send the
     *          current GameState to
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        //if there is no state to send, ignore
        if (state == null) {
            return;
        }

        //create a deep copy of the state to send to the player
        TTGameState stateForPlayer = new TTGameState(state);
        p.sendInfo(stateForPlayer);
    }//sendUpdatedStateTo

    /**
     * can this player move
     * @param playerIdx
     * 		the player's player-number (ID)
     * @return whether the player can move or not
     */
    @Override
    protected boolean canMove(int playerIdx) {
        if (playerIdx < 0 || playerIdx > 1) {
            // if our player-number is out of range, return false
            return false;
        }
        else {
            // player can move if it's their turn
            return state.getPlayerTurn() == playerIdx;
        }
    }//canMove

    /**
     * the game is over when the Round is 11 and both players have thirteen cards in their hand
     * @return whether the game is over
     */
    @Override
    protected String checkIfGameOver() {
        //the game ends on round 11
        if(state.getRoundNum() == 11){
            //round 11 must be over
            if(!state.isRoundOver()){
                return null;
            }

            //both players must have 13 cards in their hand
            if((state.getNumCards(0) == 13) && (state.getNumCards(1) == 13)){
                if (state.getPlayer0Score() == state.getPlayer1Score()) {
                    return "It was a tie!  ";
                }
                else if(state.getPlayer0Score() > state.getPlayer1Score()){
                    return "Human Player has won!  ";
                }else{
                    return "Computer Player has won!  ";
                }
            }
        }
        return null;
    }//checkIfGameOver

    /**
     * There are several types of actions that could be sent:
     * discard, draw (from deck or discard pile), go out, and add group
     * @param action
     * 			The move that the player has sent to the game
     * @return whether the move was successful or not
     */
    @Override
    protected boolean makeMove(GameAction action) {
        // check that we have three thirteen action; if so cast it
        if (!(action instanceof TTMoveAction)) {
            return false;
        }
        TTMoveAction ttma = (TTMoveAction) action;

        // get the index of the player making the move; return false
        int thisPlayerIdx = getPlayerIdx(ttma.getPlayer());
        if ((thisPlayerIdx < 0) || (thisPlayerIdx > 1)) {
            // illegal player
            return false;
        }

        //game logic for these actions are located in the TTGameState
        if(ttma.isDiscard()){
            Log.d("Local Game","discard card in TTMoveAction "+ttma.getDiscard().getCardRank()+ttma.getDiscard().getCardSuit());
            //Card is passed in to discard from player's hand
            Log.d("Local Game","can the player discard: "+String.valueOf(state.playerDiscard(ttma.getDiscard())));
            state.discardCard(ttma.getDiscard());
            Log.d("Local Game","top discard pile "+state.getDiscardPile().get(state.getDiscardPile().size()-1).getCardRank()+state.getDiscardPile().get(state.getDiscardPile().size()-1).getCardSuit());

        }
        else if(ttma.isDrawDiscard()){
            state.playerDrawDiscard();
        }
        else if(ttma.isDrawDeck()){
            state.playerDrawDeck();
        }
        else if(ttma.isGoOut()){
            state.goOut();
        }
        else if(ttma.isAddGroup()){
            //ArrayList<Card> is passed in to create a group
            Log.d("Local Game","received group, will now call GameState method");
            state.createGrouping(ttma.getAddGroup());
            Log.d("Local Game",state.toString());
        }
        else if(ttma.isRemoveGroup()){
            //Card is passed in to remove from a group
            state.removeGrouping(ttma.getRemoveGroup());
        }
        else {
            //unexpected action
            return false;
        }

        //the move was successful at this point
        return true;
    }//makeMove

}//TTLocalGame
