package edu.up.threethirteengame.TTGame;
/**
 * @description: Hand class contains information and methods pertaining to each player's hand
 * @author: Nick Ohara, Adrian Muth, Shane Matsushima, Lindsey Warren
 * @version: 10/20/2020
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Hand {
    private ArrayList<Card> userHand = null;
    private ArrayList<ArrayList<Card>> groupings = null;
    public static final int MAX_NUM_GROUPS = 4;

    /**
     * Hand Class default constructor
     * new instance of a hand
     * creates an ArrayList that contains players cards and
     * 2D ArrayList that contains groups of cards
     */
    public Hand(){
        this.userHand = new ArrayList<Card>();
        this.groupings = new ArrayList<ArrayList<Card>>(MAX_NUM_GROUPS);
        for(int i=0; i<MAX_NUM_GROUPS; i++){
            groupings.add(new ArrayList<Card>());
        }
    }

    /**
     * copy constructor
     * copies the hand and groupings ArrayLists
     * @param orig the original hand
     */
    public Hand(Hand orig){
        //copy the user hand
        this.userHand = new ArrayList<Card>();
        for(Card c : orig.userHand){
            this.userHand.add(new Card(c));
        }

        //copy the groupings if the original groupings isn't empty
        if(orig.groupings.isEmpty()) {
            //if the original groupings is empty, make a new one
            this.groupings = new ArrayList<ArrayList<Card>>(orig.groupings);
        }
        else {
            //if the original groupings is NOT empty, make a new one
            this.groupings = new ArrayList<ArrayList<Card>>(MAX_NUM_GROUPS);
            for(int i=0; i<MAX_NUM_GROUPS; i++){
                this.groupings.add(new ArrayList<Card>());
            }

            //iterate through the original groupings
            for (ArrayList<Card> origGroup : orig.groupings) {
                this.groupings.add(new ArrayList<Card>(origGroup));
            }
        }
    }

    public void addToHand(Card c){userHand.add(c);}

    public void setHand(ArrayList<Card> hand){this.userHand = hand;}

    public ArrayList<Card> getHand(){
        if(this.userHand != null) {
            return this.userHand;
        }
        return null;
    }

    public Card getCard(int index) {return this.userHand.get(index);}

    public int getSize(){return this.userHand.size();}

    public int getGroupingSize(){return this.groupings.size();}

    public ArrayList<ArrayList<Card>> getGroupings() {
        return groupings;
    }

    /**
     * sorts a given hand by their rank in ascending order
     * @param hand
     * @return a sorted array list of a given hand
     *
     * External Citation:
     * Problem: Wanted to sort an array list
     * Date: 10/11/20
     * Source:https://stackoverflow.com/questions/9109890/android-java-how-to-sort-a-list-of-objects-by-a-certain-value-within-the-object
     * Solution: used the code
     */
    public ArrayList<Card> sortByRank(final ArrayList<Card> hand){
        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                return Integer.valueOf(card1.getCardRank()).compareTo(Integer.valueOf(card2.getCardRank()));
            }
        });
        return hand;
    }

    /**
     * sorts a given hand by their suit
     * @param hand
     * @return a sorted array list of a given hand
     */
    //Break apart into groups by suit then sort by rank then combine back into a hand
    public ArrayList<Card> sortBySuit(final ArrayList<Card> hand){
        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                return Character.valueOf(card1.getCardSuit()).compareTo(Character.valueOf(card2.getCardSuit()));
            }
        });
        return hand;
    }

    /**
     * Sorts cards by suite and rank
     */
    public ArrayList<Card> sortBySuiteAndRank(ArrayList<Card> hand){
        return hand;
    }


    /**
     * checks if a given arrayList of cards is a valid set
     * @param set a given set
     * @return whether it's valid or not
     */
    public boolean checkIfSet(ArrayList<Card> set){

        //checks to make sure set isn't empty and is not null pointer
        if(set.isEmpty() || (set == null)){
            return false;
        }

        //the difference between each consecutive card should be 0 in a set
        int[] checkSet = checkGroup(set);
        for(int i=0; i<checkSet.length;i++){
            if(checkSet[i] != 0){
                return false;
            }
        }
        return true;
    }

    /**
     * checks if a given arrayList of cards is a valid run
     * @param run a given run
     * @return whether it's valid or not
     */
    public boolean checkIfRun(ArrayList<Card> run){
        //checks to make sure run isn't empty and is not null pointer
        if(run.isEmpty() || (run == null)){
            return false;
        }
        //get the first card's suit
        char checkSuit = run.get(0).getCardSuit();

        //iterate through the run to check if they all have the same suit
        for(Card c : run){
            if(c.getCardSuit() != checkSuit){
                return false;
            }
        }

        //check to make sure the difference between consecutive cards is 1
        int[] checkRun = checkGroup(run);
        for(int i=0; i<checkRun.length;i++){
            if(checkRun[i] != 1){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if wild card is in hand
     * @param wildCard
     * @return
     */
    public boolean hasWildCard(int wildCard){
        for(Card c: userHand){
            if(c.getCardRank() == wildCard){
                return true;
            }
        }
        return false;
    }

    /**
     * Finds number of wild cards in a given hand
     * @param wildCard
     * @return
     */
    public int wildCount(int wildCard){
        int count = 0;
        for(Card c: userHand){
            if(c.getCardRank() == wildCard){
                count++;
            }
        }
        return count;
    }

    /**
     * determines if the given card is in the user's groupings
     * used in Go Out
     * @param card the card in question
     * @return whether this card is in the current player's groupings
     */
    public boolean isCardInGroup(Card card){

        //checks to make sure card exists
        if(card == null){
            Log.d("isCardInGroup()", "passed card was null");
            return false;
        }

        //check to make sure there are groups in the current player's hand
        if(this.groupings.isEmpty()){
            return false;
        }

        //check to make sure there are groups in the current player's hand
        //if(this.groupings.get(MAX_NUM_GROUPS-1).isEmpty()){
            //return false;
        //}

        //iterate through the current player's groupings for the given card
        for(ArrayList<Card> groups : this.groupings){
            for(Card c : groups){
                //if the given card has the same rank and suit as the card in groups, they have the card
                if((card.getCardRank() == c.getCardRank() && (card.getCardSuit() == c.getCardSuit()))){
                    return true;
                }
            }
        }

        //the card was not found
        return false;
    }//isCardInGroup

    /**
     * determines if the given card is in the user's hand
     * @param card the card in question
     * @return whether the card was found in the user's hand
     */
    public boolean isCardInHand(Card card){
        //checks to make sure card exists
        if(card == null){
            Log.d("isCardInGroup()", "passed card was null");
            return false;
        }

        //check to make sure the player's hand is not null
        if(this.userHand == null){
            return false;
        }

        for(Card c : this.userHand){
            //if the given card has the same rank and suit as the card in player's hand, they have the card
            if((card.getCardRank() == c.getCardRank() && (card.getCardSuit() == c.getCardSuit()))){
                return true;
            }
        }
        //the card was not found
        return false;
    }//isCardInHand

    /**
     * checks the given group by returning an array with the differences between each consecutive card
     * @param group a given group
     * @return groupDiff: an int array with calculated differences in rank
     */
    public int[] checkGroup(ArrayList<Card> group){
        int[] groupDiff = new int[group.size()-1];
        ArrayList<Card> sortedHand = sortByRank(group);
        for(int i=0; i<groupDiff.length; i++){
            groupDiff[i] = sortedHand.get(i+1).getCardRank()-sortedHand.get(i).getCardRank();
        }
        return groupDiff;
    }

    /**
     * puts a selected group of cards into the specified user's grouping ArrayList
     * @param group
     */
    public boolean createGrouping(ArrayList<Card> group){
        //checks to make sure group is not null and isn't empty
        if((group == null) || group.isEmpty()){
            return false;
        }

        //checks to make sure the groupings is not already full
        boolean hasEmptyGroup = false;
        for(ArrayList<Card> checkGroups : groupings){
            if(checkGroups.isEmpty()){
                hasEmptyGroup =true;
                break;
            }
        }
        if(!hasEmptyGroup){
            return false;
        }

        //check to make sure each card is not in a current group
        for(Card c : group){
            if(isCardInGroup(c)){
                return false;
            }
        }

        //this group can be created
        groupings.add(group);
        return true;
    }

    /**
     * user chooses a card in a group and removes the selected group from
     * their grouping ArrayList
     * @param cardToRemove
     */
    public boolean removeGrouping(Card cardToRemove){
        //counter for group
        int groupNum = 0;
        boolean breakLoop = false;

        //check if the card is in a group
        if(!isCardInGroup(cardToRemove)){
            return false;
        }

        //find the card in the 2D groupings
        ArrayList<Card> groupToRemove = null;
        for (ArrayList<Card> groups : this.groupings) {
            groupToRemove = groups;
            for (Card c : groups) {
                if ((cardToRemove.getCardRank() == c.getCardRank() && (cardToRemove.getCardSuit() == c.getCardSuit()))) {
                    //found the card to remove from 2D groupings
                    Log.d("Hand","the size of the group to remove: "+groupToRemove.size());
                    if(groupToRemove.size() <= 3){
                        //we need to remove the whole group because it's too small
                        breakLoop =true;
                        break;
                    }
                    else {
                        //not necessary to remove whole group because its greater than 3
                        groups.remove(c);
                        return true;
                    }
                }
            }
            if (breakLoop) {
                break;
            }
        }

        if (groupToRemove != null) {
            this.groupings.remove(groupToRemove);
        }


        //remove the whole group if the group is too small
        //if (this.groupings.get(groupNum).size() < 3) {

            //for (int i = 0; i < groupToRemove.size() + 1; i++) {
                //groupToRemove.remove(0);
            //}
       // }

        return true;
    }
}
