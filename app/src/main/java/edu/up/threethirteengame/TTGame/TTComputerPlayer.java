package edu.up.threethirteengame.TTGame;

import java.util.ArrayList;

import edu.up.threethirteengame.game.GameFramework.GameComputerPlayer;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.NotYourTurnInfo;

public class TTComputerPlayer extends GameComputerPlayer {


    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public TTComputerPlayer(String name) { super(name); }

    @Override
    protected void receiveInfo(GameInfo info) {
        TTGameState newState = (TTGameState)info;
        if(info instanceof NotYourTurnInfo){
            return;
        }

        if(newState.getRoundOver() == true){
            newState.roundOver();
        }

         else if(newState.canPlayerGoOut()){
            newState.goOut();
        }
         else {
             newState.playerDrawDeck();
             optimizeHand(newState.getPlayer1Hand());
        }

    }

    private ArrayList<Card> optimizeHand(Hand hand){
        //TODO: Optimization Algorithm
        ArrayList<Card> needed = new ArrayList<Card>();
        return needed;
    }



}
