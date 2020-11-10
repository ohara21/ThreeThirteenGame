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
        for(int i=1; i<MAX_NUM_GROUPS; i++){
            groupings.add(new ArrayList<Card>());
        }
    }

    /**
     * copy constructor
     * copies the hand and groupings ArrayLists
     * @param orig
     */
    public Hand(Hand orig){
        //copy the user hand
        this.userHand = new ArrayList<Card>();
        for(Card c : orig.userHand){
            this.userHand.add(new Card(c));
        }

        //copy the groupings
        int count = 0;
        this.groupings = new ArrayList<ArrayList<Card>>(MAX_NUM_GROUPS);
        for(ArrayList<Card> group: orig.groupings){
            for(Card c : group){
                this.groupings.get(count).add(new Card(c));
            }
            count++;
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
    //TODO: Failed Unit Test
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
    //TODO: Should sort by suit and rank
    //Break apart into groups by suit then sort by rank then combine back into a hand
    public ArrayList<Card> sortBySuit(final ArrayList<Card> hand){
        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                //TODO: this just sorts by suit, doesn't numerically sort in each suit
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
        if(this.groupings.get(MAX_NUM_GROUPS-1).isEmpty()){
            return false;
        }

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
    public void removeGrouping(Card cardToRemove){
        //TODO: implement error checks and finish method
        if(isCardInGroup(cardToRemove)) {
            for(ArrayList<Card> groups : this.groupings){
                for(Card c : groups) {
                    if (c == cardToRemove) {
                        groups.remove(cardToRemove);
                    }
                }
            }
        }
    }
}
