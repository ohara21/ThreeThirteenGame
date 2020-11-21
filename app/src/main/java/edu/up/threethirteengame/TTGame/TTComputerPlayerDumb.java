package edu.up.threethirteengame.TTGame;

import android.util.Log;

import java.util.Random;

import edu.up.threethirteengame.game.GameFramework.GameComputerPlayer;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.NotYourTurnInfo;

public class TTComputerPlayerDumb extends GameComputerPlayer {

    //the local game state
    private TTGameState newState = null;


    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public TTComputerPlayerDumb(String name) { super(name); }

    @Override
    protected void receiveInfo(GameInfo info) {

        //dumb ai could also just discard randomly from hand

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
            Log.d("Computer Player","it is not your turn AI!");
            Log.d("Computer Player",newState.toString());
            return;
        }
        sleep(2);
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
            int handSize = newState.getPlayer1Hand().getSize();
            int randomIndex = rand.nextInt(handSize-1);
            discard = newState.getPlayer1Hand().getCard(randomIndex);
            Log.d("Computer Player"," is discarding a random card");
            Log.d("Computer Player","discarding "+discard.getCardRank()+discard.getCardSuit());
            game.sendAction(new TTDiscardAction(this, discard));
        }

    }



}
