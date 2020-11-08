package edu.up.threethirteengame.TTGame;
/**
 * @description: Hand class contains information and methods pertaining to each player's hand
 * @author: Nick Ohara, Adrian Muth, Shane Matsushima, Lindsey Warren
 * @version: 10/20/2020
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Hand {
    private ArrayList<Card> userHand = null;
    private ArrayList<ArrayList<Card>> groupings = null;
    private static final int MAX_NUM_GROUPS = 4;

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

    public ArrayList<Card> getHand(){return this.userHand;}

    public int getSize(){return this.userHand.size();}

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
    public ArrayList<Card> sortBySuit(final ArrayList<Card> hand){
        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                //TODO: this just sorts by suit, doesn't numerically sort in each suit
                return Integer.valueOf(card1.getCardSuit()).compareTo(Integer.valueOf(card2.getCardSuit()));
            }
        });
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
        int[] checkSet = checkHand(set);
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
            if(c.getCardRank() != checkSuit){
                return false;
            }
        }

        //check to make sure the difference between consecutive cards is 1
        int[] checkRun = checkHand(run);
        for(int i=0; i<checkRun.length;i++){
            if(checkRun[i] != 1){
                return false;
            }
        }
        return true;
    }

    /**
     * checks the given hand by returning an array with the differences between each consecutive card
     * @param hand a given user's hand
     * @return an int array with calculated differences in rank
     */
    public int[] checkHand(ArrayList<Card> hand){
        int[] checkHand = new int[hand.size()-1];
        ArrayList<Card> sortedHand = sortByRank(hand);
        for(int i=0; i<checkHand.length; i++){
            checkHand[i] = sortedHand.get(i+1).getCardRank()-sortedHand.get(i).getCardRank();
        }
        return checkHand;
    }

    /**
     * puts a selected group of cards into the specified user's grouping ArrayList
     * @param hand
     */
    public void createGrouping(ArrayList<Card> hand){
        //TODO: implement some error checks
        groupings.add(hand);
    }

    /**
     * user chooses a card in a group and removes the selected group from
     * their grouping ArrayList
     * @param cardToRemove
     */
    public void removeGrouping(Card cardToRemove){
        //TODO: implement error checks and finish method
    }

//    public void addToGroup(Card add, ArrayList<Card> group){
//        group.add(add);
//    }
//
//    public void removeFromGroup(Card remove, ArrayList<Card> group) {
//        group.remove(remove);
//    }
}
