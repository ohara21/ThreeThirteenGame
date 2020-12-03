package edu.up.threethirteengame.TTGame;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import edu.up.threethirteengame.game.GameFramework.GameComputerPlayer;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.NotYourTurnInfo;

/**
 * @description: TTComputerPlayerDumb class takes actions for the Easy AI player
 * @author: Nick Ohara, Adrian Muth, Shane Matsushima, Lindsey Warren
 * @version: 10/20/2020
 */
public class TTComputerPlayerDumb extends GameComputerPlayer {

    //the local game state
    private TTGameState newState = null;

    //groupings for the dumb AI
    private ArrayList<ArrayList<Card>> setGroup = new ArrayList<>();


    /**
     * constructor for the dumb AI
     *
     * @param name the player's name (e.g., "John")
     */
    public TTComputerPlayerDumb(String name) { super(name); }

    /**
     * receives info from the LocalGame to make a move
     * @param info
     */
    @Override
    protected void receiveInfo(GameInfo info) {

        //dumb ai could also just discard randomly from hand
        Log.d("Received"," info in TTComputerPlayer");
        Random rand = new Random();

        //ignore if instance of NotYourTurnInfo
        if(info instanceof NotYourTurnInfo){
            return;
        }

        //ignore if not an instance of TTGameState
        if(!(info instanceof TTGameState)){
            return;
        }

        //initialize the game state
        newState = (TTGameState)info;

        //return if it's not the Computer Player's turn
        if (newState.getPlayerTurn() != playerNum){
//            Log.d("Computer Player","it is not your turn AI!");
//            Log.d("Computer Player",newState.toString());
            return;
        }

        if(newState.playerDrawDeck()){
            Log.d("Computer Player","Top of discard "+newState.getDiscardPile().get(newState.getDiscardPile().size()-1).getCardRank()+newState.getDiscardPile().get(newState.getDiscardPile().size()-1).getCardSuit());
            Log.d("Computer Player"," is drawing from deck: "+newState.getDeck().get(newState.getDeck().size()-1).getCardRank()+newState.getDeck().get(newState.getDeck().size()-1).getCardSuit());
            game.sendAction(new TTDrawDeckAction(this));
            return;
        }
        else if(newState.canPlayerGoOut()){
            Log.d("Computer PLayer", " was able to and got out");
            game.sendAction(new TTGoOutAction(this));
            return;
        }
        else {
            Card discard;
            int handSize = newState.currentPlayerHand().getSize();
            int randomIndex = rand.nextInt(handSize-1);
            discard = newState.currentPlayerHand().getCard(randomIndex);
            optimizeHand(newState);
            for(ArrayList<Card> group: setGroup){
                game.sendAction(new TTAddGroupAction(this, group));
            }
            Log.d("Computer Player"," is discarding a random card");
            Log.d("Computer Player","discarding "+discard.getCardRank()+discard.getCardSuit());
            //sleep(2);
            game.sendAction(new TTDiscardAction(this, discard));
        }

    }

    /**
     *
     * dumbed down version of optimize hand algorithm for dumb ai
     * @param gameState
     */
    public void optimizeHand(TTGameState gameState){
        Hand computerHand = new Hand(gameState.currentPlayerHand());
        this.setGroup = checkForSet(computerHand);
    }

    /**
     * Returns groups of set
     * @param hand
     * @return
     */
    public ArrayList<ArrayList<Card>> checkForSet(Hand hand){
        ArrayList<ArrayList<Card>> set = new ArrayList<>();
        ArrayList<Card> tempHand = hand.sortByRank(hand.getHand());
        ArrayList<Card> tempGroup = new ArrayList<>();

        for(int i = 1; i <= 13; i++){
            for(Card c: tempHand){
                if(c.getCardRank() == i){
                    tempGroup.add(c);
                }
            }
            if(tempGroup.size() >= 3){
                //Log.d("TTComputerPlayerSmart:","checkForSet() found a set to add rank:"+i);
                set.add(new ArrayList<Card>(tempGroup));
            }
            tempGroup.clear();
        }

        return set;
    }



}
