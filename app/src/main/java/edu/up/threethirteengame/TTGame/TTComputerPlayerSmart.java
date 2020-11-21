package edu.up.threethirteengame.TTGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.up.threethirteengame.game.GameFramework.GameComputerPlayer;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.NotYourTurnInfo;

public class TTComputerPlayerSmart extends GameComputerPlayer {

    private TTGameState newState = null;

    private ArrayList<Card> needCard = new ArrayList<>();

    private ArrayList<ArrayList<Card>> compGroup = new ArrayList<>();

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public TTComputerPlayerSmart(String name) {
        super(name);
    }

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

        //check discard with needed list and draw from discard if can else draw from deck
        if(isNeeded(needCard, newState.getTopDiscard().getCardRank()) && newState.playerDrawDiscard()){
            game.sendAction(new TTDrawDiscardAction(this));
        }
        else if(newState.playerDrawDeck()) {
            game.sendAction(new TTDrawDeckAction(this));
        }

        //update need list using optimization algorithm
        needCard = optimizeHand(newState);

        //try to go out
        if(newState.canPlayerGoOut()){
            game.sendAction(new TTGoOutAction(this));
        }

        //TODO: discard card not apart of needed list or apart of a group
        else{
            Card discard = needCard.get(0);
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

        ArrayList<ArrayList<Card>> runGroup = checkForRun(computerHand);
        for(ArrayList list: runGroup){
            tempGrouping.add(list);
        }

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

        for(ArrayList fGroup: finalGrouping){
            if(!isIn(this.compGroup, fGroup)) {
                compGroup.add(fGroup);
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
    private ArrayList<ArrayList<Card>> checkForRun(Hand hand){
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

        club = rankSort(club);
        clubRunIndex = findRunIndex(club);
        diamond = rankSort(diamond);
        diamondRunIndex = findRunIndex(diamond);
        heart = rankSort(heart);
        heartRunIndex = findRunIndex(heart);
        spade = rankSort(spade);
        spadeRunIndex = findRunIndex(spade);
        if(clubRunIndex[0] != -1) {
            for (int i = clubRunIndex[0]; i <= clubRunIndex[1]; i++) {
                clubRun.add(club.get(i));
            }
        }
        if(diamondRunIndex[0] != -1) {
            for (int i = diamondRunIndex[0]; i <= diamondRunIndex[1]; i++) {
                diamondRun.add(diamond.get(i));
            }
        }
        if(heartRunIndex[0] != -1) {
            for (int i = heartRunIndex[0]; i <= heartRunIndex[1]; i++) {
                heartRun.add(heart.get(i));
            }
        }
        if(spadeRunIndex[0] != -1) {
            for (int i = spadeRunIndex[0]; i <= spadeRunIndex[1]; i++) {
                spadeRun.add(club.get(i));
            }
        }

        runs.add(clubRun);
        runs.add(diamondRun);
        runs.add(heartRun);
        runs.add(spadeRun);

        return runs;
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


    public ArrayList<Card> rankSort(final ArrayList<Card> hand){
        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                return Integer.valueOf(card1.getCardRank()).compareTo(Integer.valueOf(card2.getCardRank()));
            }
        });
        return hand;
    }


    private int[] findRunIndex(ArrayList<Card> hand){
        ArrayList<Integer> groupDiff = new ArrayList<>();
        int[] index = new int[2];
        int previousCard = 0;

        if(hand.size() <= 2){
            index[0] = -1;
            return index;
        }

        for(Card c: hand){
            if(previousCard == 0){
                previousCard = c.getCardRank();
                continue;
            }

            groupDiff.add(c.getCardRank() - previousCard);
            previousCard = c.getCardRank();

        }

        previousCard = 0;
        index = findIndex(groupDiff, groupDiff.size(), 1);
        return index;
    }

    static int[] findIndex(ArrayList<Integer> a, int n, int key) {
        int[] index = new int[2];
        int start = -1;

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
        index[1] = end;
        return index;
    }

    private boolean isNeeded(ArrayList<Card> need, int rank){
        for(Card c: need){
            if(c.getCardRank() == rank){
                return true;
            }
        }
        return false;
    }

    private boolean isIn(ArrayList<ArrayList<Card>> finalGroup, ArrayList<Card> group){
        for(ArrayList fgroup: finalGroup){
            if(fgroup.equals(group)){
                return true;
            }
        }
        return false;
    }

    }
