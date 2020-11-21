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

    private int wildCard;

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

        //wild card is 3 upon game initialization
        this.wildCard = 3;
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

    public void setWildCard(int wildCard){
        this.wildCard = wildCard;
    }

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
        //wild card is set before this method is called in TTGameState canPlayerGoOut()

        //checks to make sure set isn't empty and is not null pointer
        if(set.isEmpty() || (set == null)){
            return false;
        }

        //the difference between each consecutive card should be 0 in a set
        int[] checkSet = checkGroup(set);
        //Log.d("Hand","checkIfSet(): starting the check");
        //Log.d("Hand","checkIfSet(): current wild card "+this.wildCard);
        for(int i=0; i<checkSet.length;i++){
            if(checkSet[i] != 0){
                //check for the wild card
                //Log.d("Hand","checkIfSet(): checking for a wild card, checkSet[i]= "+checkSet[i]);
                if((set.get(i).getCardRank() == this.wildCard) || (set.get(i+1).getCardRank() == this.wildCard)){
                    //one of the cards was a wild
                    //Log.d("Hand","checkIfSet(): found a wild card");
                    continue;
                }

                //a wild card wasn't found
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
        //wild card is set before this method is called in TTGameState canPlayerGoOut()

        //checks to make sure run isn't empty and is not null pointer
        if(run.isEmpty() || (run == null)){
            return false;
        }

        //initialize an "empty" suit
        char checkSuit = 'e';
        int numWildCards = 0;

        //iterate through the run to check if they all have the same suit
        for(Card c : run){
            //if the card is wild, ignore it
            if(c.getCardRank() == this.wildCard){
                numWildCards++;
                continue;
            }
            else if(checkSuit == 'e'){
                //initialize the first non wild card
                checkSuit = c.getCardSuit();
            }

            //all non wild cards should have the same suit
            if(c.getCardSuit() != checkSuit){
                return false;
            }
        }

        //check to make sure the difference between consecutive cards is 1 without wild cards
        ArrayList<Integer> checkRun = checkGroupNoWild(run);
        for(Integer diff : checkRun){
            //if the diff != 1, a wild card can be used or the run is invalid
            if(diff != 1){
                //the run is invalid if the player doesn't have any wilds in this group
                if(numWildCards == 0){
                    //Log.d("Hand","checkRun(): don't have any wild cards to use");
                    return false;
                }

                //there isn't enough wild cards in this group to account
                //for the difference between cards
                if(diff > numWildCards+1){
                    //Log.d("Hand","checkRun(): not enough wild cards");
                    //Log.d("Hand","checkRun(): diff "+diff+" numWildCards "+numWildCards);
                    return false;
                }

                //a wild card was found update the number of Wild cards available
                numWildCards = diff-1;
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
        //wild card is set before this method is called in TTGameState canPlayerGoOut()

        int[] groupDiff = new int[group.size()-1];
        ArrayList<Card> sortedHand = sortByRank(group);
        for(int i=0; i<groupDiff.length; i++){
            groupDiff[i] = sortedHand.get(i+1).getCardRank()-sortedHand.get(i).getCardRank();
        }
        return groupDiff;
    }

    /**
     * makes a difference array with the wilds taken out
     * used in checkIfRun()
     * @param group the provided group to create difference array
     * @return the difference array not including wilds
     */
    public ArrayList<Integer> checkGroupNoWild(ArrayList<Card> group){

        //sort the given hand by rank
        ArrayList<Card> sortedHand = sortByRank(group);
        ArrayList<Integer> groupDiff = new ArrayList<>();

        //iterate through the sorted group
        int previousCardRank = 0;
        for(Card c : sortedHand){
            if(c.getCardRank() == this.wildCard) {
                //ignore the wild card
                continue;
            }
            else if(previousCardRank == 0){
                //initialize the standard card rank with the first non wild card encountered
                previousCardRank = c.getCardRank();
                continue;
            }

            //this is not a wild card and the difference between the current card
            //and this on will be added to the array list
            groupDiff.add(c.getCardRank() - previousCardRank);
            previousCardRank = c.getCardRank();
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
            //Log.d("Hand","the group passed in was null or empty");
            return false;
        }

        //Log.d("Hand","the size of groupings"+groupings.size());
        //checks to make sure the groupings is not already full
        boolean hasEmptyGroup = false;
        for(ArrayList<Card> checkGroups : groupings){
            if(checkGroups.isEmpty()){
                hasEmptyGroup =true;
                break;
            }
        }
        if(!hasEmptyGroup){
            //Log.d("Hand","the groupings is already full");
            return false;
        }

        //check to make sure each card is not in a current group
        for(Card c : group){
            if(isCardInGroup(c)){
                Log.d("Hand","there is an intersecting grouping");
                removeGrouping(c); // remove so that the new group can be created
            }
        }

        //this group can be created
        //Log.d("Hand","the group was successfully made");
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
            Log.d("Hand","the card was not found in a group");
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
