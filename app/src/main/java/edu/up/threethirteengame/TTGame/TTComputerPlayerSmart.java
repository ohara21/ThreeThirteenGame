package edu.up.threethirteengame.TTGame;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.up.threethirteengame.game.GameFramework.GameComputerPlayer;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.NotYourTurnInfo;

/**
 * @description: Computer Player Smart class contains the AI actions and optimization for a smart AI opponent
 * @author: Nick Ohara, Shane Matsushima, Adrian Muth, Lindsey Warren
 * @version: 10/24/2020
 */
public class TTComputerPlayerSmart extends GameComputerPlayer {

    //the GameState to be initialized from the LocalGame
    private TTGameState newState = null;

    //an arrayList of the cards that are needed
    private ArrayList<Card> needCard = new ArrayList<>();

    //the groupings for the Smart AI (later sent to the local game to be added to their real hand)
    private ArrayList<ArrayList<Card>> compGroup = new ArrayList<>();

    //an arrayList of the cards that can be discarded first
    private ArrayList<Card> canDiscard = new ArrayList<>();

    /**
     * constructor for the smart computer player
     *
     * @param name the player's name (e.g., "John")
     */
    public TTComputerPlayerSmart(String name) {
        super(name);
    }

    /**
     * receives info from the LocalGame to make a move
     * @param info
     */
    @Override
    protected void receiveInfo(GameInfo info) {

        //ignore if instance of NotYourTurnInfo
        if (info instanceof NotYourTurnInfo){
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
            return;
        }

        //check discard with needed list and draw from discard if can, else draw from deck
        if(!needCard.isEmpty()) {
            if (isNeeded(needCard, newState.getTopDiscard().getCardRank()) && newState.playerDrawDiscard()) {
                game.sendAction(new TTDrawDiscardAction(this));
            }
            else if(newState.playerDrawDeck()) {
                game.sendAction(new TTDrawDeckAction(this));
            }
        }
        else if(newState.playerDrawDeck()) {
            game.sendAction(new TTDrawDeckAction(this));
        }

        //update need list using optimization a
        // algorithm and adds new groupings to gameState
        needCard = optimizeHand(newState);
        System.out.println("Need: ");
        for(Card c: needCard) {
            System.out.print(" "+c.getCardRank()+c.getCardSuit());
        }
        System.out.println();
        if(!compGroup.isEmpty()) {
            Log.d("TTComputerPlayerSmart:", "Size of compGroup: " + compGroup.size());
            for (ArrayList<Card> groups : compGroup) {
                if (!isIn(newState.currentPlayerHand().getGroupings(), groups) && !groups.isEmpty()) {
                    Log.d("TTComputerPlayerSmart:", "Sending group:");
                    System.out.println("Group Sending: ");
                    for (Card c : groups) {
                        System.out.print(" " + c.getCardRank() + c.getCardSuit());
                    }
                    System.out.println();
                    compGroup.remove(groups);
                    game.sendAction(new TTAddGroupAction(this, groups));
                }
            }
        }

        //try to go out
        if(newState.canPlayerGoOut()){
            game.sendAction(new TTGoOutAction(this));
            return;
        }

        //discard card not apart of needed list or apart of a group
        else{
            Card discard;
            if(!canDiscard.isEmpty()) {
                discard = canDiscard.get(0);
            }
            else {
                //cycle through the player's hand for the card with the largest rank
                discard = null;
                int highestRank = 0;
                Log.d("TTComputerPlayerSmart:", "current player turn "+newState.getPlayerTurn());
                for(Card c : newState.currentPlayerHand().getHand()){
                   if(c.getCardRank() == newState.getWildCard()){
                        //don't discard the wild card
                        continue;
                    }
                    else if(c.getCardRank() > highestRank){
                        //generally discard the higher cards
                        Log.d("TTComputerPlayerSmart:","found a card with high rank");
                        highestRank = c.getCardRank();
                        discard = c;
                    }
                }
                if(discard == null){
                    Log.d("TTComputerPlayerSmart:","should hardly ever reach this case for discarding");
                    discard = newState.currentPlayerHand().getHand().get(0);
                }
            }
            game.sendAction(new TTDiscardAction(this, discard));
        }

    }

    public ArrayList<Card> optimizeHand(TTGameState gameState){

        // returned array for bot to know what cards look for
        ArrayList<Card> needed = new ArrayList<>();

        // temp grouping used prior to settings groups for the bot
        ArrayList<ArrayList<Card>> tempGrouping = new ArrayList<>();

        // final grouping used to create groups in computer hand
        ArrayList<ArrayList<Card>> finalGrouping = new ArrayList<>();

        //TODO: Take out System.out.prints when debugging is complete

        // Check for Wild Card
        int wildValue = gameState.getWildCard();
        System.out.println("Wild Card Value: "+wildValue);
        //copying current hand of computer
        Hand computerHand = new Hand(gameState.currentPlayerHand());

        //copying current hand of computer without wilds
        Hand withoutWild = new Hand(removeWild(wildValue, computerHand));



        // Look for sets -> create grouping per set

        ArrayList<ArrayList<Card>> setGroup = checkForSet(withoutWild);
        System.out.println("Size of setGroup: "+setGroup.size());
        for(ArrayList list: setGroup){
            tempGrouping.add(list);
        }

        System.out.println("Set Groups:");
        for(ArrayList<Card> group: tempGrouping){
            System.out.print("Set group: ");
            for(Card c: group){
                System.out.print(" "+c.getCardRank()+c.getCardSuit());
            }
            System.out.println();
        }

        //Look for runs (disregarding common cards) -> create grouping per run
        ArrayList<ArrayList<Card>> runGroup = checkForRun(withoutWild);
        System.out.println("Size of runGroup: "+runGroup.size());
        for(ArrayList list: runGroup){
            tempGrouping.add(list);
        }


        System.out.println("Set and Run  Groups:");
        for(ArrayList<Card> group: tempGrouping){
            System.out.print("Set and Run group: ");
            for(Card c: group){
                System.out.print(" "+c.getCardRank()+c.getCardSuit());
            }
            System.out.println();
        }

        // Check if there are both run and set groups
        if(runGroup.size() != 0 && setGroup.size() != 0) {

            // Check set and runs for similar cards
            ArrayList<Card> similar = checkForSimilar(tempGrouping);


            //Goes through list of similar cards
            for (Card c : similar) {
                //Go through each grouping that has the similar card and puts the sums of individual groupings
                //into an array list to get compared to each other
                ArrayList<Integer> sumHold = new ArrayList<>();

                for (int i = 0; i < tempGrouping.size() - 1; i++) {

                    if (tempGrouping.get(i).indexOf(c) != -1) {
                        int sum = 0;
                        for (Card k : tempGrouping.get(i)) {
                            sum += k.getCardRank();
                        }
                        sumHold.add(sum);
                    }
                }

                //adds smallest sum grouping to final grouping and removes grouping from temp group
                if (!sumHold.isEmpty()) {
                    finalGrouping.add(tempGrouping.get(smallestSum(sumHold)));
                }
                tempGrouping.clear();
            }
        }
        else {
            for(ArrayList<Card> group: tempGrouping){
                finalGrouping.add(group);
            }
            tempGrouping.clear();
        }

        for(ArrayList<Card> group: finalGrouping){
            System.out.println("Final Grouping: ");
            for(Card c: group){
                System.out.print(" "+c.getCardRank()+c.getCardSuit());
            }
            System.out.println();
        }

        if(finalGrouping.size() == 0){
            System.out.println("No groups formed in final");
        }


        //Check for incomplete set
        ArrayList<Card> newTempHand = computerHand.getHand();

        //gets rid of cards that are apart of a set or run
        ArrayList<Card> tempHand = new ArrayList<>();

        for(Card c: newTempHand){
            if(!compareGroupingToCard(finalGrouping, c)) {
                tempHand.add(c);
            }
        }

        //checks for cards that are the same rank and puts them into incomplete temp set
        ArrayList<ArrayList<Card>> incompleteTemp = new ArrayList<>();
        ArrayList<Card> tempGroup = new ArrayList<>();

        for(Card c: tempHand){

            if(findSimilarCard(tempHand, c) != -1 && !compareGroupingToCard(incompleteTemp, c) && c.getCardRank() != wildValue){

                tempGroup.add(c);
                tempGroup.add(tempHand.get(findSimilarCard(tempHand, c)));
                incompleteTemp.add( new ArrayList<Card>(tempGroup));
                tempGroup.clear();

            }
        }

        for(ArrayList<Card> group: incompleteTemp){
            System.out.print(" Incomplete Group: ");
            for(Card c: group){
                System.out.print(" "+c.getCardRank()+c.getCardSuit());
            }
            System.out.println();
        }


        //Implements use of wild cards to incomplete sets
        ArrayList<Card> tempComputerHand = computerHand.getHand();

        if(computerHand.hasWildCard(wildValue)){

            int wildCardCount = computerHand.wildCount(wildValue);

            if(wildCardCount > incompleteTemp.size()){
                for(ArrayList<Card> group: incompleteTemp){
                    group.add(tempComputerHand.get(findCardByRank(tempComputerHand, wildValue)));
                    tempComputerHand.remove(tempComputerHand.get(findCardByRank(tempComputerHand, wildValue)));
                    wildCardCount--;
                }
                for(int i = 0; i < incompleteTemp.size(); i++){
                    if(wildCardCount > 0){
                        incompleteTemp.get(i).add(tempComputerHand.get(findCardByRank(tempComputerHand, wildValue)));
                        tempComputerHand.remove(tempComputerHand.get(findCardByRank(tempComputerHand, wildValue)));
                    }
                }

                for(ArrayList<Card> group: incompleteTemp){
                    if(group.size() >= 3){
                        finalGrouping.add(group);
                        tempGrouping.remove(group);

                    }
                }

            }
            else {
                for (ArrayList<Card> group : incompleteTemp) {

                    if (wildCardCount != 0) {
                        group.add(computerHand.getHand().get(findCardByRank(tempComputerHand, wildValue)));
                        tempComputerHand.remove(tempComputerHand.get(findCardByRank(tempComputerHand, wildValue)));
                        wildCardCount--;
                    }

                    if (group.size() == 3) {
                        finalGrouping.add(group);
                        tempGroup.remove(group);
                    }
                }
            }
        }

        //finds cards not in group and adds it to the can discard pile
        this.canDiscard.clear();
        for(Card c: computerHand.getHand()){
            if(!compareGroupingToCard(finalGrouping, c) && !compareGroupingToCard(incompleteTemp, c) && c.getCardRank() != wildValue){
                this.canDiscard.add(c);
            }
        }

        //find cards that are needed for set and run
        ArrayList<Card> tempNeed = addToNeedRun(this.canDiscard);
        for(Card c: tempNeed){
            if(!compareCardToGroup(c, needed)){
                needed.add(c);
            }

        }
        tempNeed.clear();
        tempNeed = addToNeedSet(incompleteTemp);
        for(Card c: tempNeed){
            if(!compareCardToGroup(c, needed)){
                needed.add(c);
            }
        }


        //if new group is formed, add confirmed group to the computers groups
        for(ArrayList fGroup: finalGrouping){
            if(!isIn(this.compGroup, fGroup) && fGroup.size()>2) {
                compGroup.add(fGroup);
            }
        }

        //returns needed cards so the compPlayer can check discard pile
        return needed;
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

    public ArrayList<ArrayList<Card>> getCompGroup(){return this.compGroup;}

    /**
     * Returns group of runs
     * @param hand
     * @return
     */
    public ArrayList<ArrayList<Card>> checkForRun(Hand hand){
        ArrayList<ArrayList<Card>> runs = new ArrayList<>();
        ArrayList<Card> club = new ArrayList<>();
        ArrayList<Card> diamond = new ArrayList<>();
        ArrayList<Card> heart = new ArrayList<>();
        ArrayList<Card> spade = new ArrayList<>();
        ArrayList<Card> clubRun = new ArrayList<>();
        ArrayList<Card> diamondRun = new ArrayList<>();
        ArrayList<Card> heartRun = new ArrayList<>();
        ArrayList<Card> spadeRun = new ArrayList<>();

        int[] clubRunIndex = new int[2];
        int[] diamondRunIndex = new int[2];
        int[] heartRunIndex = new int[2];
        int[] spadeRunIndex = new int[2];

        //splits hand into sub groups based on suite
        for(Card c: hand.getHand()){
            switch(c.getCardSuit()){
                case 's':
                    spade.add(c);
                    break;
                case 'h':
                    heart.add(c);
                    break;
                case 'd':
                    diamond.add(c);
                    break;
                case 'c':
                    club.add(c);
                    break;
            }
        }

        //sorts suites by rank and find starting and ending index of consecutive 1's

        //****CLUB****//
        club = rankSort(club);
        clubRunIndex = findRunIndex(club);

        //****DIAMOND****//
        diamond = rankSort(diamond);
        diamondRunIndex = findRunIndex(diamond);

        //****HEART****//
        heart = rankSort(heart);
        heartRunIndex = findRunIndex(heart);

        //****SPADE****//
        spade = rankSort(spade);
        spadeRunIndex = findRunIndex(spade);

        //adds found runs in a group
        if(clubRunIndex[0] != -1 && clubRunIndex[0] != clubRunIndex[1]) {
            for (int i = clubRunIndex[0]; i <= clubRunIndex[1]; i++) {
                clubRun.add(club.get(i));
            }
        }
        if(diamondRunIndex[0] != -1 && diamondRunIndex[0] != diamondRunIndex[1]) {
            for (int i = diamondRunIndex[0]; i <= diamondRunIndex[1]; i++) {
                diamondRun.add(diamond.get(i));
            }
        }
        if(heartRunIndex[0] != -1 && heartRunIndex[0] != heartRunIndex[1]) {
            for (int i = heartRunIndex[0]; i <= heartRunIndex[1]; i++) {
                heartRun.add(heart.get(i));
            }
        }
        if(spadeRunIndex[0] != -1 && spadeRunIndex[0] != spadeRunIndex[1]) {
            for (int i = spadeRunIndex[0]; i <= spadeRunIndex[1]; i++) {
                spadeRun.add(spade.get(i));
            }
        }

        //adds groups of separate suites into final grouping to return
        if(!clubRun.isEmpty()) {
            runs.add(clubRun);
        }
        if(!diamondRun.isEmpty()) {
            runs.add(diamondRun);
        }
        if(!heartRun.isEmpty()) {
            runs.add(heartRun);
        }
        if(!spadeRun.isEmpty()){
            runs.add(spadeRun);
        }


        //returns all runs found in hand
        return runs;
    }

    /**
     * Finds similar cards in all groups (Mainly between sets and runs)
     * @param group
     * @return ArrayList of similar cards in run(s) and set(s)
     */
    public ArrayList<Card> checkForSimilar(ArrayList<ArrayList<Card>> group){

        ArrayList<Card> similar = new ArrayList<>();

        //groups are added to the end, so check by decrementing index
        for(int i = group.size()-1; i > 0; i--){

            //maintain the first group as we compare it to the other groups
            ArrayList<Card> temp1 = new ArrayList<>(group.get(i));
            for(int j= i-1;j>=0;j--) {

                ArrayList<Card> temp2 = new ArrayList<>(group.get(j));

                temp2.retainAll(temp1);
                similar.addAll(temp2);
            }
        }
        return similar;
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
        return -1;
    }

    /**
     * finds similar cards based on rank and suit
     * @param hand
     * @param input
     * @return
     */
    private int findSimilarCard(ArrayList<Card> hand, Card input){
        for(Card c: hand){
            if(c.getCardRank() == input.getCardRank() && c.getCardSuit() != input.getCardSuit()){
                return hand.indexOf(c);
            }
        }
        return -1; //if there is no card then returns -1
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
     * sorts cards based on rank value
     * @param hand
     * @return
     */
    private ArrayList<Card> rankSort(final ArrayList<Card> hand){
        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                return Integer.valueOf(card1.getCardRank()).compareTo(Integer.valueOf(card2.getCardRank()));
            }
        });
        return hand;
    }

    /**
     * finds index of runs based on arraylist given
     * @param hand
     * @return
     */
    private int[] findRunIndex(ArrayList<Card> hand){
        ArrayList<Integer> groupDiff = new ArrayList<>();
        int[] index = new int[2];
        int previousCard = 0;

        if(hand.size() <= 2){
            index[0] = -1;
            return index;
        }

        char cardSuitTest = 'e';
        for(Card c: hand){
            if(previousCard == 0){
                previousCard = c.getCardRank();
                cardSuitTest = c.getCardSuit();
                continue;
            }

            groupDiff.add(c.getCardRank() - previousCard);
            previousCard = c.getCardRank();

        }

        index = findIndex(groupDiff, groupDiff.size(), 1);
        System.out.println("findRunIndex() for "+cardSuitTest+": "+index[0]+" "+index[1]);
        return index;
    }

    /**
     * Finds starting and ending index of similar values in an array
     * @param a
     * @param n
     * @param key
     * @return
     *
     * External Citation:
     * Problem: Returning starting and ending index of consecutive values
     * Date: 11/21/2020
     * Source: https://www.geeksforgeeks.org/find-start-ending-index-element-unsorted-array/
     * Solution: used the code
     */
    static int[] findIndex(ArrayList<Integer> a, int n, int key) {
        int[] index = new int[2];
        int start = 0;

        // Traverse from beginning to find
        // first occurrence
        for (int i = 0; i < n; i++) {
            if (a.get(i) == key) {
                start = i;
                break;
            }
        }


        // Traverse from end to find last
        // occurrence.
        int end = start;
        for (int i = n - 1; i >= start; i--) {
            if (a.get(i) == key) {
                end = i;
                break;
            }
        }
        index[0] = start;
        index[1] = end+1;
        return index;
    }

    /**
     * Used to check if top of discard is a needed card
     * @param need
     * @param rank
     * @return
     */
    private boolean isNeeded(ArrayList<Card> need, int rank){
        for(Card c: need){
            if(c.getCardRank() == rank){
                return true;
            }
        }
        return false;
    }

    /**
     * Finds cards that are needed to complete sets
     * @param incomplete
     * @return
     */
    private ArrayList<Card> addToNeedSet(ArrayList<ArrayList<Card>> incomplete){
        ArrayList<Card> need = new ArrayList<>();

        for(ArrayList<Card> temp: incomplete) {
            int rank = temp.get(0).getCardRank();
            switch (temp.get(0).getCardSuit()) {
                case 'd':
                    switch (temp.get(1).getCardSuit()) {
                        case 'h':
                            need.add(new Card(1, 's', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'c', temp.get(0).getCardRank()));
                            break;
                        case 's':
                            need.add(new Card(1, 'h', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'c', temp.get(0).getCardRank()));
                            break;
                        case 'c':
                            need.add(new Card(1, 'h', temp.get(0).getCardRank()));
                            need.add(new Card(1, 's', temp.get(0).getCardRank()));
                            break;
                    }
                case 'h':
                    switch (temp.get(1).getCardSuit()) {
                        case 'd':
                            need.add(new Card(1, 's', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'c', temp.get(0).getCardRank()));
                            break;
                        case 's':
                            need.add(new Card(1, 'd', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'c', temp.get(0).getCardRank()));
                            break;
                        case 'c':
                            need.add(new Card(1, 's', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'd', temp.get(0).getCardRank()));
                            break;
                    }
                case 's':
                    switch (temp.get(1).getCardSuit()) {
                        case 'h':
                            need.add(new Card(1, 'd', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'c', temp.get(0).getCardRank()));
                            break;
                        case 'd':
                            need.add(new Card(1, 'h', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'c', temp.get(0).getCardRank()));
                            break;
                        case 'c':
                            need.add(new Card(1, 'h', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'd', temp.get(0).getCardRank()));
                            break;
                    }
                case 'c':
                    switch (temp.get(1).getCardSuit()) {
                        case 'h':
                            need.add(new Card(1, 's', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'd', temp.get(0).getCardRank()));
                            break;
                        case 's':
                            need.add(new Card(1, 'h', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'd', temp.get(0).getCardRank()));
                            break;
                        case 'd':
                            need.add(new Card(1, 's', temp.get(0).getCardRank()));
                            need.add(new Card(1, 'h', temp.get(0).getCardRank()));
                            break;
                    }
            }
        }
        return need;
    }

    /**
     * Returns cards needed to complete runs
     * @param hand
     * @return
     */
    private ArrayList<Card> addToNeedRun(ArrayList<Card> hand){
        ArrayList<Card> need = new ArrayList<>();
        ArrayList<Card> club = new ArrayList<>();
        ArrayList<Card> diamond = new ArrayList<>();
        ArrayList<Card> heart = new ArrayList<>();
        ArrayList<Card> spade = new ArrayList<>();
        ArrayList<Integer> clubDiff = new ArrayList<>();
        ArrayList<Integer> diamondDiff = new ArrayList<>();
        ArrayList<Integer> heartDiff = new ArrayList<>();
        ArrayList<Integer> spadeDiff = new ArrayList<>();

        //splits hand into sub groups based on suite
        for(Card c: hand){
            switch(c.getCardSuit()){
                case 's':
                    spade.add(c);
                    break;
                case 'h':
                    heart.add(c);
                    break;
                case 'd':
                    diamond.add(c);
                    break;
                case 'c':
                    club.add(c);
                    break;
            }
        }

        spade = rankSort(spade);
        spadeDiff = getDifference(spade);
        heart = rankSort(heart);
        heartDiff = getDifference(heart);
        diamond = rankSort(diamond);
        diamondDiff = getDifference(diamond);
        club = rankSort(club);
        clubDiff = getDifference(club);

        for(int i: spadeDiff){
            if(i == 1 ){
                need.add(new Card(1, 's', spade.get(i).getCardRank() - 1));
                if(spade.get(i+1).getCardRank() != 13) {
                    need.add(new Card(1, 's', spade.get(i + 1).getCardRank() + 1));
                }

            }
            if(i == 2){
                need.add(new Card(1, 's', spade.get(i).getCardRank()+1));
            }
        }

        for(int i: heartDiff){
            if(i == 1 ){
                need.add(new Card(1, 'h', heart.get(i).getCardRank() - 1));
                if(heart.get(i+1).getCardRank() != 13) {
                    need.add(new Card(1, 'h', heart.get(i + 1).getCardRank() + 1));
                }

            }
            if(i == 2){
                need.add(new Card(1, 'h', heart.get(i).getCardRank()+1));
            }
        }

        for(int i: diamondDiff){
            if(i == 1 ){
                need.add(new Card(1, 'd', diamond.get(i).getCardRank() - 1));
                if(spade.get(i+1).getCardRank() != 13) {
                    need.add(new Card(1, 'd', diamond.get(i + 1).getCardRank() + 1));
                }

            }
            if(i == 2){
                need.add(new Card(1, 'd', diamond.get(i).getCardRank()+1));
            }
        }

        for(int i: clubDiff){
            if(i == 1 ){
                need.add(new Card(1, 'c', club.get(i).getCardRank() - 1));
                if(spade.get(i+1).getCardRank() != 13) {
                    need.add(new Card(1, 'c', club.get(i + 1).getCardRank() + 1));
                }

            }
            if(i == 2){
                need.add(new Card(1, 'c', club.get(i).getCardRank()+1));
            }
        }


        return need;
    }

    /**
     * checks if groups created from optimization hand is already a group of the computer player
     * @param finalGroup
     * @param group
     * @return
     */
    private boolean isIn(ArrayList<ArrayList<Card>> finalGroup, ArrayList<Card> group){
        boolean statement = false;
        for(ArrayList<Card> fgroup: finalGroup){
            if(fgroup.size() == group.size()){
                for(int i = 0; i < group.size(); i++){
                    if(fgroup.get(i).getCardRank() == group.get(i).getCardRank() && fgroup.get(i).getCardSuit() == group.get(i).getCardSuit()){
                        statement = true;
                    }
                    else{
                        statement = false;
                    }
                }
            }

        }
        return statement;
    }

    /**
     * deep search of card in a group of groups
     * @param groups
     * @param card
     * @return
     */
    private boolean compareGroupingToCard(ArrayList<ArrayList<Card>> groups, Card card){
        for(ArrayList<Card> group: groups){
            for(Card c: group){
                if(c.getCardSuit() == card.getCardSuit() && c.getCardRank() == card.getCardRank()){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean compareCardToGroup(Card card, ArrayList<Card> group){
        for(Card c: group){
            if(c.getCardRank() == card.getCardRank() && c.getCardSuit() == c.getCardSuit()){
                return true;
            }
        }
        return false;
    }

    /**
     * finds differences of a hand based on card rank
     * @param hand
     * @return
     */
    private ArrayList<Integer> getDifference(ArrayList<Card> hand){
        ArrayList<Integer> diff = new ArrayList<>();
        if(hand.size() == 0){
            diff.add(-1);
            return diff;
        }
        for(int i = 0; i < hand.size(); i++){
            if(hand.get(i) != hand.get(hand.size()-1)){
                diff.add(hand.get(i).getCardRank() - hand.get(i+1).getCardRank());
            }
        }
        return diff;
    }

    /**
     * Removes a wild card from the AI's hand based on the wild card rank
     * @param wildRank
     * @param hand
     * @return
     */
    public Hand removeWild(int wildRank, Hand hand){
        Hand withoutWild = new Hand();
        ArrayList<Card> tempHand = hand.getHand();
        ArrayList<Card> tempWithoutWild = new ArrayList<>();
        for(Card c: tempHand){
            if(c.getCardRank() != wildRank){
                tempWithoutWild.add(c);
            }
        }
        withoutWild.setHand(tempWithoutWild);
        return withoutWild;
    }


    }


