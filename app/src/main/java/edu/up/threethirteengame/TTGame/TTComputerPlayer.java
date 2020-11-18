package edu.up.threethirteengame.TTGame;

import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import edu.up.threethirteengame.game.GameFramework.GameComputerPlayer;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.NotYourTurnInfo;

public class TTComputerPlayer extends GameComputerPlayer {

    //the local game state
    private TTGameState newState = null;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public TTComputerPlayer(String name) { super(name); }

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

        if(newState.playerDrawDeck()){
            Log.d("Computer Player","Top of discard "+newState.getDiscardPile().get(newState.getDiscardPile().size()-1).getCardRank()+newState.getDiscardPile().get(newState.getDiscardPile().size()-1).getCardSuit());
            Log.d("Computer Player"," is drawing from deck: "+newState.getDeck().get(newState.getDeck().size()-1).getCardRank()+newState.getDeck().get(newState.getDeck().size()-1).getCardSuit());
            game.sendAction(new TTDrawDeckAction(this));
            return;
        }
//        else if(newState.canPlayerGoOut()){
//            Log.d("Computer PLayer", " was able to and got out");
//            game.sendAction(new TTGoOutAction(this));
//            return;
//        }
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

    private ArrayList<Card> optimizeHand(TTGameState gameState){

        // returned array for bot to know what cards look for
        ArrayList<Card> needed = new ArrayList<>();

        // temp grouping used prior to settings groups for the bot
        ArrayList<ArrayList<Card>> tempGrouping = new ArrayList<>();

        // final grouping used to create groups in computer hand
        ArrayList<ArrayList<Card>> finalGrouping = new ArrayList<>();

        //copying current hand of computer
        Hand computerHand = gameState.getPlayer1Hand();

        // Check for Wild Card
        int wildValue = gameState.getWildCard();

        // Look for sets -> create grouping per set
        // Sort Cards based on value
        computerHand.setHand(computerHand.sortByRank(computerHand.getHand()));
        // Create temp group for set
        tempGrouping.add(checkForSet(computerHand));
        //TODO: Look for runs (disregarding common cards) -> create grouping per run
            // Need sorting based on suit and rank to complete

        // Check set and runs for similar cards
        ArrayList<Card> similar = checkForSimilar(tempGrouping);


        //Goes through list of similar cards
        for(Card c: similar){
            //Go through each grouping that has the similar card and puts the sums of individual groupings
            //into an array list to get compared to each other

            ArrayList<Integer> saveIndex = new ArrayList<>();
            ArrayList<Integer> sumHold = new ArrayList<>();

            for(int i = 0; i < tempGrouping.size(); i++){

                if(tempGrouping.get(i).indexOf(c) != -1) {
                    int sum = 0;
                    saveIndex.add(i);
                    for (Card k : tempGrouping.get(i)) {
                        sum += k.getCardRank();
                    }
                    sumHold.add(sum);
                }
            }

            //adds smallest sum grouping to final grouping and removes grouping fom temp group
            finalGrouping.add(tempGrouping.get(smallestSum(sumHold)));
            tempGrouping.remove(smallestSum(sumHold));

        }
        //TODO: check for incomplete run
        //Check for incomplete set

        ArrayList<Card> tempHand = computerHand.getHand();

        //gets rid of cards that are apart of a set or run
        for(Card c: computerHand.getHand()){
            for(ArrayList<Card> groups: finalGrouping){
                if(groups.contains(c)){
                    tempHand.remove(c);
                }
            }
        }

        //checks for cards that are the same rank and puts them incomplete temp set
        ArrayList<ArrayList<Card>> incompleteTemp = new ArrayList<>();
        ArrayList<Card> tempGroup = new ArrayList<>();

        for(Card c: tempHand){
            if((findCardByRank(tempHand, c.getCardRank()) != -1) && !incompleteTemp.contains(c)){
                tempGroup.add(c);
                tempGroup.add(tempHand.get(findCardByRank(tempHand, c.getCardRank())));
                incompleteTemp.add(tempGroup);
                tempGroup.clear();

            }
        }

        //Implements use of wild cards to incomplete sets and runs
        if(computerHand.hasWildCard(wildValue)){
            int wildCardCount = computerHand.wildCount(wildValue);
            for(ArrayList<Card> group: incompleteTemp){
                if(wildCardCount != 0) {
                    group.add(computerHand.getHand().get(findCardByRank(computerHand.getHand(), wildValue)));
                    wildCardCount--;
                }
                if(group.size() == 3){
                    finalGrouping.add(group);
                    tempGroup.remove(group);
                }
            }

        }
        // return cards not in group
        for(Card c: computerHand.getHand()){
            boolean outcast = false;
            for(int i = 0; i < finalGrouping.size(); i++){
                if(finalGrouping.get(i).indexOf(c) == -1){
                    outcast= true;
                }
            }
            if(outcast){
                needed.add(c);
            }
        }

        return needed;
    }

    /**
     * Returns group of set
     * @param hand
     * @return
     */
    private ArrayList<Card> checkForSet(Hand hand){
        ArrayList<Card> set = new ArrayList<>();
        int sameCount = 0;
        int startIndex;
        for(int i = 0; i < hand.getSize(); i++){
            startIndex = i;
            for(int j = 0; j < hand.getSize(); j++){
                if(hand.getCard(j) == hand.getCard(i)){
                    sameCount++;
                }
            }
            if(sameCount >= 3){
                for(int k = 0; k < sameCount; k++){
                    set.add(hand.getCard(startIndex + k));
                }
                return set;
            }
        }
        return set; // return empty if hits this return statement
    }

    /**
     * Returns group of run
     * @param hand
     * @return
     */
    private ArrayList<Card> checkForRun(Hand hand){
        ArrayList<Card> run = new ArrayList<>();


        return run;
    }

    /**
     * Finds similar cards in all groups (Mainly between sets and runs)
     * @param group
     * @return ArrayList of similar cards in run(s) and set(s)
     */
    private ArrayList<Card> checkForSimilar(ArrayList<ArrayList<Card>> group){

        ArrayList<Card> similar = new ArrayList<>();

        for(int i = 0; i < group.size(); i++){
            ArrayList<Card> temp1 = group.get(i);
            ArrayList<Card> temp2 = group.get(i+1);
            temp1.retainAll(temp2);
            for(Card c : temp1){
                similar.add(c);
            }
        }
        return similar;
    }

    /**
     * Finds index of smallest sum in array list
     * @param sum
     * @return
     */
    private int smallestSum(ArrayList<Integer> sum){
        int smallest = sum.get(0);
        for (int x : sum){
            if (x < smallest){
                smallest = x;
            }
        }
        return sum.indexOf(smallest);
    }

    /**
     * finds a specific card based on the rank
     * @param hand
     * @param rank
     * @return
     */
    private int findCardByRank(ArrayList<Card> hand, int rank){
        for(Card c: hand){
            if(c.getCardRank() == rank){
                return hand.indexOf(c);
            }
        }
        return -1; //if there is no card then returns -1
    }



}
