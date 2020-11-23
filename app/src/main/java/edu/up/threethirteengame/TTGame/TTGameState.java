package edu.up.threethirteengame.TTGame;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.LocalGame;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameState;

/**
 * @description GameState class contains information about the
 * current state of the game and all game logic
 * @author Nick Ohara, Adrian Muth, Shane Matsushima, Lindsey Warren
 * @version 10/20/2020
 */
public class TTGameState extends GameState {

    //creating a deck of 52 cards
    private static char[] suite = new char[] {'c','s','h','d'};
    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<Card> discardPile = new ArrayList<Card>();
    private Hand player0Hand  = new Hand(); // User Hand
    private Hand player1Hand = new Hand(); // Computer Hand
    public static final int MAX_NUM_GROUPS = 4;

    //the current round number
    private int roundNum;

    //if the current round is over
    private boolean roundOver;

    //player's running score, updated after each round
    private int player0Score;
    private int player1Score;

    //player's number of turn taken is updated after they draw a card
    //does not count turns when the other player Goes Out and you have another turn
    private int player0TurnsTaken;
    private int player1TurnsTaken;

    //if the player has drawn a card for the current round
    private boolean player0Drawn;
    private boolean player1Drawn;

    //if the player has GoneOut for the current round
    private boolean player0GoneOut;
    private boolean player1GoneOut;

    // if the player has discarded for the current round
    private boolean player0Discard;
    private boolean player1Discard;

    //the current player's turn
    private int playerTurn;

    //the card value of the card that can be used as any card in a group for a given round
    private int wildCard;

    // used to help update the text view about computer player's actions
    private int actionTextVal;
    private boolean player0Acted; // true if action was taken by player 0, false if taken by player 1

    /**
     * GameState initialization constructor
     */
    public TTGameState() {
        //populate deck with 52 card objects then shuffle deck randomly
        for(int s = 0; s < 4; s++) {
            for (int v = 1; v <= 13; v++) {
                deck.add(new Card(1, suite[s], v));
            }
        }
        Collections.shuffle(deck);
        Collections.shuffle(deck);

        //sets round number and the wild card
        roundNum = 1;
        roundOver = false;
        wildCard = roundNum + 2;

        //populate player 0 and player 1 hands with three cards from deck
        dealHand();

        //each player starts with a score of 0
        player0Score = 0;
        player1Score = 0;
        player0TurnsTaken = 0;
        player1TurnsTaken = 0;
        player0Drawn = false;
        player1Drawn = false;
        player0GoneOut = false;
        player1GoneOut = false;
        player0Discard = false;
        player1Discard = false;

        //player 0 goes first
        playerTurn = 0;

        //start with default text view
        actionTextVal = 0;
    }

    /**
     * GameState clone constructor
     * @param orig
     */
    public TTGameState(TTGameState orig) {
        //copy deck ArrayList
        this.deck = new ArrayList<>();
        for(Card cDeck : orig.deck){
            this.deck.add(new Card(cDeck));
        }

        //copy discard ArrayList
        this.discardPile = new ArrayList<>();
        for(Card cDisc : orig.discardPile){
            this.discardPile.add(new Card (cDisc));
        }

        //copy the other instance variables
        this.player0Hand = new Hand(orig.getPlayer0Hand());
        this.player1Hand = new Hand(orig.getPlayer1Hand());
        this.player0Score = orig.getPlayer0Score();
        this.player1Score = orig.getPlayer1Score();
        this.player0TurnsTaken = orig.getPlayer0TurnsTaken();
        this.player1TurnsTaken = orig.getPlayer1TurnsTaken();
        this.player0Drawn = orig.getPlayer0Drawn();
        this.player1Drawn = orig.getPlayer1Drawn();
        this.player0GoneOut = orig.player0GoneOut;
        this.player1GoneOut = orig.player1GoneOut;
        this.player0Discard = orig.player0Discard;
        this.player1Discard = orig.player1Discard;
        this.roundNum = orig.getRoundNum();
        this.roundOver = orig.getRoundOver();
        this.playerTurn = orig.getPlayerTurn();
        this.wildCard = orig.getWildCard();
        this.actionTextVal = orig.getActionTextVal();
    }

    /**
     * all getter methods for GameState class
     * @return
     */
    public ArrayList<Card> getDeck() {
        return deck;
    }

    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }

    public Card getTopDiscard(){return discardPile.get(discardPile.size() -1);}

    public Hand getPlayer1Hand() {
        return player1Hand;
    }

    public Hand getPlayer0Hand() {
        return  player0Hand;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer0Score() {
        return player0Score;
    }

    public int getPlayer0TurnsTaken() {
        return player0TurnsTaken;
    }

    public int getPlayer1TurnsTaken() {
        return player1TurnsTaken;
    }

    public boolean getPlayer0Drawn() { return player0Drawn; }

    public boolean getPlayer1Drawn() { return player1Drawn; }

    public boolean getIsPlayer0GoneOut() {
        return player0GoneOut;
    }

    public boolean getIsPlayer1GoneOut() {
        return player1GoneOut;
    }

    public boolean getPlayer0Discard() { return player0Discard; }

    public boolean getPlayer1Discard() { return player1Discard; }

    public int getRoundNum() {
        return roundNum;
    }

    public int getPlayerTurn() {return playerTurn;}

    public int getWildCard() {
        return wildCard;
    }

    public boolean getRoundOver() { return roundOver; }

    public boolean getPlayer0Acted() {return player0Acted;}

    /**
     * gets the number of cards in a given player's hand
     * @param player
     * @return number of cards in hand
     */
    public int getNumCards(int player){
        if(player == 0){
            return player0Hand.getSize();
        }
        else if(player == 1){
            return player1Hand.getSize();
        }
        return 0;
    }

    /**
     * all setter methods for GameState class
     * @param deck
     */
    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public void setDiscardPile(ArrayList<Card> discardPile) {
        this.discardPile = discardPile;
    }

    public void setPlayer1Hand(Hand player1Hand) {
        this.player1Hand = player1Hand;
    }

    public void setPlayer0Hand(Hand player0Hand) {
        this.player0Hand = player0Hand;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public void setPlayer0Score(int player0Score) {
        this.player0Score = player0Score;
    }

    public void setWildCard(int wildCard) {
        this.wildCard = wildCard;
    }

    public void setPlayer0Drawn(boolean isDrawn) {this.player0Drawn = isDrawn; }

    public void setPlayer1Drawn(boolean isDrawn) {this.player1Drawn = isDrawn; }

    public void setPlayer0Discard(boolean isDiscard) {this.player0Discard = isDiscard; }

    public void setPlayer1Discard(boolean isDiscard) {this.player1Discard = isDiscard; }

    /**
     * changes which player can take actions
     */
    public void nextTurn(){
        if(playerTurn == 1)
            playerTurn = 0;
        else
            playerTurn = 1;
    }

    /**
     * checks if the round is over
     * the round number is dependent on how many turns each player took
     */
    public boolean isRoundOver() {

        //the round is NOT over if none of the players have gone out
        if (!player0GoneOut && !player1GoneOut) {
            return false;
        }

        //if the other player has gone out, it's this player's last turn
        if((playerTurn == 0) && (player0GoneOut)){
            return true;
        }
        else if ((playerTurn == 1) && (player1GoneOut)){
            return true;
        }

        return false;
    }

    /**
     * if the round is over, the player's scores, if they went out in this round,
     * and round number need to be updated
     */
    public void updateGameState(){
        //do nothing if the round isn't over
        if(!isRoundOver()){
            return;
        }

        //update player's scores
        updateScores();

        //update round num
        if (roundNum != 11) {
            roundNum++;
            dealHand();
        } 

        //it is a new round so reset that player's went out and drew cards
        player0GoneOut = false;
        player1GoneOut = false;
        player0Drawn = false;
        player1Drawn = false;


    }

    /**
     * updates the players scores
     * method called when the round is over
     */
    public void updateScores(){

        //check if each card is in a valid group player 0
        for(ArrayList<Card> group0 : player0Hand.getGroupings()){
            if(player0Hand.checkIfRun(group0) || player0Hand.checkIfSet(group0)){
                //found a valid group
                for(Card c0 : group0){
                    //set the card in the group to true
                    c0.setInValidGroup(true);

                    //need to find the card in the players hand now because they are not
                    //not stored in the same memory
                    for(Card cardInHand0 :  player0Hand.getHand()){
                        if((c0.getCardSuit() == cardInHand0.getCardSuit()) && (c0.getCardRank() == cardInHand0.getCardRank())){
                            //set the card in the player's hand to true
                            cardInHand0.setInValidGroup(true);
                        }
                    }
                }
            }
        }

        //update player 0's score
        int roundScore = 0;
        for(Card c0 : player0Hand.getHand()){
            if(!c0.getInValidGroup()) {
                roundScore += c0.getCardRank();
            }
        }
        player0Score += roundScore;

        //check if each card is in a valid group for player 1
        for(ArrayList<Card> group1 : player1Hand.getGroupings()){
            if(player1Hand.checkIfRun(group1) || player1Hand.checkIfSet(group1)){
                //found a valid group
                for(Card c1 : group1){
                    //set the card in the group to true
                    c1.setInValidGroup(true);

                    //need to find the card in the players hand now because they are not
                    //stored in the same memory
                    for(Card cardInHand1 :  player1Hand.getHand()){
                        if((c1.getCardSuit() == cardInHand1.getCardSuit()) && (c1.getCardRank() == cardInHand1.getCardRank())){
                            //set the card in the player's hand to true
                            cardInHand1.setInValidGroup(true);
                        }
                    }
                }
            }
        }

        //update player 1's score
        roundScore = 0;
        for(Card c1 : player1Hand.getHand()){
            if(!c1.getInValidGroup()) {
                roundScore += c1.getCardRank();
            }
        }
        player1Score += roundScore;
        // informs player of updated scores
        actionTextVal = 3;
    }

    @Override
    public String toString() {
        String round = "Round number: " + roundNum;
        String deckSize = "Deck card amount: " + deck.size();
        String discardSize = "Discard pile amount: " + discardPile.size();
        String playerCard = "Player 0 card amount: " + player0Hand.getSize();
        String computerCard = "Player 1 card amount: " + player1Hand.getSize();
        String turn = "Player " + playerTurn + " turn";
        String playerScoreString = "Player 0 score: " + player0Score;
        String computerScoreString = "Player 1 score: " + player1Score;

        String player0cards = "Player 0 cards:";
        String player1cards = "Player 1 cards:";
        String cardVal;
        for(Card c : player0Hand.getHand()){
            cardVal = " "+c.getCardRank()+c.getCardSuit();
            player0cards = player0cards.concat(cardVal);
        }
        for(Card c : player1Hand.getHand()){
            cardVal = " "+c.getCardRank()+c.getCardSuit();
            player1cards = player1cards.concat(cardVal);
        }

        int group0size =0;
        int group1size=0;
        if(!player0Hand.getGroupings().isEmpty()){
            for(ArrayList<Card> group0 : player0Hand.getGroupings()){
                if(!group0.isEmpty()) {
                    group0size++;
                }
            }
        }
        if(!player1Hand.getGroupings().isEmpty()){
            for(ArrayList<Card> group1 : player1Hand.getGroupings()){
                if(!group1.isEmpty()) {
                    group1size++;
                }
            }
        }
        String playerGroup = "Player 0 num groups: " + group0size;
        String computerGroup = "Player 1 num groups: " + group1size;
        String toString = round + "\n" + deckSize + "\n" + discardSize + "\n" + playerCard + "\n"
                + computerCard + "\n" + turn + "\n" + playerScoreString + "\n"
                + computerScoreString + "\n" + playerGroup + "\n" + computerGroup +"\n"
                + player0cards + "\n" + player1cards;
        return toString;
    }

    /**
     * action method that determines if the player can draw a card from deck
     * @return
     */
    public boolean playerDrawDeck(){
        //checks if there are cards in deck
        if(deck.size() == 0){
            Log.d("playerDrawDeck()","deck is apparently empty, drawing from discard");
            //have to draw from discard
            // empty draw deck message
            actionTextVal = 4;
            return false;
        }

        //can't draw a card if you already have enough cards in your hand
        if(currentPlayerHand().getHand().size() == (roundNum+3)){
            // too many cards to draw message
            actionTextVal = 5;
            return false;
        }

        //can go again if the other player has Gone Out, you have NOT GoneOut yet, and
        //you have already taken your turn
        if(this.playerTurn == 0){
            //player 0's turn
            if(player1GoneOut && !player0GoneOut && (player0TurnsTaken == roundNum)){
                //removes card from deck and adds it to the current player's hand
                currentPlayerHand().addToHand(deck.get(deck.size()-1));
                deck.remove(deck.size()-1);
                return true;
            }
        }
        else {
            //player 1's turn
            if(player0GoneOut && !player1GoneOut && (player1TurnsTaken == roundNum)){
                //removes card from deck and adds it to the current player's hand
                currentPlayerHand().addToHand(deck.get(deck.size()-1));
                deck.remove(deck.size()-1);
                return true;
            }
        }


        //removes card from deck and adds it to the current player's hand
        currentPlayerHand().addToHand(deck.get(deck.size()-1));
        deck.remove(deck.size()-1);
        if (this.playerTurn == 0) {
            player0TurnsTaken++;
        } else if (this.playerTurn == 1) {
            player1TurnsTaken++;
        }
        return true;
    }//playerDrawDeck

    /**
     *  action method that determines if the player can draw a card from discard pile
     * @return
     */
    public boolean playerDrawDiscard(){
        //checks if there are cards in discard
        if(discardPile.size() == 0){
            Log.d("playerDrawDiscard()","discard is apparently empty, drawing from deck");
            //have to draw from discard
            // empty discard deck message
            actionTextVal = 6;
            return false;
        }

        //can't draw a card if you already have enough cards in your hand
        if(currentPlayerHand().getHand().size() == (roundNum+3)){
            // too many cards to draw message
            actionTextVal = 5;
            return false;
        }

        //can go again if the other player has Gone Out, you have NOT GoneOut yet, and
        //you have already taken your turn
        if(this.playerTurn == 0){
            //player 0's turn
            if(player1GoneOut && !player0GoneOut && (player0TurnsTaken == roundNum)){
                //removes card from deck and adds it to the current player's hand
                currentPlayerHand().addToHand(discardPile.get(discardPile.size()-1));
                discardPile.remove(discardPile.size()-1);
                return true;
            }
        }
        else {
            //player 1's turn
            if(player0GoneOut && !player1GoneOut && (player1TurnsTaken == roundNum)){
                //removes card from deck and adds it to the current player's hand
                currentPlayerHand().addToHand(discardPile.get(discardPile.size()-1));
                discardPile.remove(discardPile.size()-1);
                return true;
            }
        }

        //the current player is drawing for the first time in this round
        //removes card from discard pile and adds it to the current player's hand
        currentPlayerHand().addToHand(discardPile.get(discardPile.size()-1));
        discardPile.remove(discardPile.size()-1);
        if (this.playerTurn == 0) {
            player0TurnsTaken++;
        } else if (this.playerTurn == 1) {
            player1TurnsTaken++;
        }
        return true;
    }

    /**
     * determines if player can discard a card from their current hand after drawing card
     * @return if they have enough cards to discard
     */
    public boolean playerDiscard(Card c){
        //checks if the card exists
        if(c == null){
            // no card to discard message
            actionTextVal = 7;
            return false;
        }

        //checks if it is currently the player's turn and they have enough cards
        //Log.d("TTGameState","playerDiscard(): current player "+playerTurn+" hand size "+currentPlayerHand().getSize());
        if(currentPlayerHand().getSize() == (this.roundNum+3)){
            //Log.d("TTGameState","playerDiscard(): entered if statement to find the card in hand");
            for(Card test : currentPlayerHand().getHand()){
                //Log.d("TTGameState","playerDiscard(): card in player 0 hand "+test.getCardRank()+test.getCardSuit());
            }
            //Log.d("TTGameState","playerDiscard(): ");
            for(Card test1 : player1Hand.getHand()){
                //Log.d("TTGameState","playerDiscard(): card in player 1 hand "+test1.getCardRank()+test1.getCardSuit());
            }
            if(isCardInHand(c)) {
                return true;
            }
        }

        // illegal move attempted
        actionTextVal = 7;
        return false;
    }

    /**
     * action method that discards card
     * looks through current player's hand and removes card to discard pile
     * @param discardCard the card to be discarded from user's hand
     */
    public void discardCard(Card discardCard){
        //checks if the card exists
        if(discardCard == null){
            Log.d("discardCard()","card to remove wasn't found");
            return;
        }

        //checks if the player can discard
        if(!playerDiscard(discardCard)){
            return;
        }

        //iterates through current player's hand for the card to discard
        //removes the card from the players hand and adds it to the discard pile
        for(Card c : currentPlayerHand().getHand()){
            if((discardCard.getCardRank() == c.getCardRank() && (discardCard.getCardSuit() == c.getCardSuit()))){
                Log.d("GameState","a card is being discarded "+discardCard.getCardRank()+discardCard.getCardSuit());
                discardPile.add(c);
                Log.d("GameState","the top of discard pile "+discardPile.get(discardPile.size()-1).getCardRank()+discardPile.get(discardPile.size()-1).getCardSuit());

                //check if this card is in a group before discarding
                if(isCardInGroup(discardCard)){
                    //remove it from its group
                    removeGrouping(discardCard);
                }
                currentPlayerHand().getHand().remove(c);
                break;
            }
        }
        //move the cards from the discard pile to the deck if the deck is empty
        discardToDeck();

        // records if a player has discarded
        actionTextVal = 1;
        if (getPlayerTurn() == 0) {
            player0Acted = true;
        } else if (getPlayerTurn() == 1) {
            player0Acted = false;
        }
        //the player's turn always ends when they discard
        nextTurn();

        //the round may be over so the GameState may need to be updated
        updateGameState();
    }

    /**
     * determines if player can Go Out
     * Go Out: all of player's cards except one must be in run/set to Go Out
     * @return whether the player can Go Out or not
     */
    public boolean canPlayerGoOut(){
        Log.d("canPlayerGoOut()", "testing if possible");
        //check to make sure there is at least one group in 2D groupings
        if(currentPlayerHand().getGroupings().isEmpty()){
            Log.d("canPlayerGoOut()", "no groupings");
            // no groupings to go out with message
            actionTextVal = 8;
            return false;
        }
        //if(currentPlayerHand().getGroupings().get(MAX_NUM_GROUPS-1).isEmpty()){
            //Log.d("canPlayerGoOut()","last group is empty");
            //return false;
        //}

        //checks to make sure they haven't already Gone Out this round
        if(playerTurn == 0){
            if(player0GoneOut){
                Log.d("canPlayerGoOut()","player 0 has already gone out");
                // already gone out message
                actionTextVal = 9;
                return false;
            }
        }
        else{
            if(player1GoneOut){
                Log.d("canPlayerGoOut()","player 1 has already gone out");
                // already gone out message
                actionTextVal = 9;
                return false;
            }
        }

        //the player may be able to Go Out based on groupings
        //there should only be one card that isn't in a group
        Card discardGoOut = null;

        //for each card in the players hand
        for(Card cHand: currentPlayerHand().getHand()){
            //check if that card is in a group
            if(!isCardInGroup(cHand)){
                //the card in the player's hand isn't in a group
                //therefore, this card is to be discarded
                if(discardGoOut == null){
                    //so far, there is only one card to discard
                    discardGoOut = cHand;
                }
                else{
                    //there is more than one card in the groupings that isn't in the player's hand
                    //therefore, we don't take any action and they can't really Go Out
                    Log.d("canPlayerGoOut()","more than one card to be removed");
                    // no groups to go out with message
                    actionTextVal = 8;
                    return false;
                }
            }
        }

        //set the wild card in the current player's hand
        currentPlayerHand().setWildCard(this.wildCard);

        //checks the groupings and sees if the card can be discarded
        if(playerDiscard(discardGoOut)){
            int numValidGroups = 0;
            int numGroups = 0;
            //iterate through player's groupings to check for valid runs and sets
            for(ArrayList<Card> groups : currentPlayerHand().getGroupings()){
                if(currentPlayerHand().checkIfRun(groups) || currentPlayerHand().checkIfSet(groups)) {
                    numValidGroups++;
                    numGroups++;
                }
                else if(!groups.isEmpty()){
                    numGroups++;
                }
            }

            //check that all the groups are valid
            if(numValidGroups==numGroups){
                Log.d("canPlayerGoOut()", "go out possible");
                return true;
            }
        }
        Log.d("canPlayerGoOut()", "go out not possible");
        // illegal move attempted
        actionTextVal = 11;
        return false;
    }

    /**
     * action method for the current player to go out
     */
    public void goOut(){
        //check if player can Go Out, all cards are in a group except one
        if(!this.canPlayerGoOut()){
            return;
        }

        //the player IS able to Go Out at this point
        //there should only be one card that isn't in a group
        Card discardGoOut = null;

        //for each card in the players hand
        for(Card cHand: currentPlayerHand().getHand()){
            //check if that card is in a group
            if(!isCardInGroup(cHand)){
                //the card in the player's hand isn't in a group
                //therefore, this card is to be discarded
                discardGoOut = cHand;
            }
        }

        //remove the card from the user's hand and place on discard pile
        if(discardGoOut != null) {
            //current player discards the remaining card and its the next players turn
            discardCard(discardGoOut);

            //records if a player has gone out
            actionTextVal = 2;
            if (getPlayerTurn() == 0) {
                player0Acted = true;
            } else if (getPlayerTurn() == 1) {
                player0Acted = false;
            }
            //player has gone out for this round and the other player should have another turn
            //if they already went
            if(this.playerTurn == 0){
                player1GoneOut = true;
            }
            else{
                player0GoneOut = true;
            }
        }
        else{
            //should not get here
            Log.d("goOut()"," could not find card to discard");
        }

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
            //Log.d("isCardInGroup()", "passed card was null");
            return false;
        }

        //check to make sure there are groups in the current player's hand
        if(currentPlayerHand().getGroupings().isEmpty()){
            //Log.d("isCardInGroup()", "current player's groupings is empty");
            return false;
        }

        //iterate through the current player's groupings for the given card
        //Log.d("isCardInGroup()", "trying to find the card in player's groups");
        for(ArrayList<Card> groups : currentPlayerHand().getGroupings()){
            for(Card c : groups){
                //if the given card has the same rank and suit as the card in groups, they have the card
                if((card.getCardRank() == c.getCardRank() && (card.getCardSuit() == c.getCardSuit()))){
                   // Log.d("isCardInGroup()", "found the card in player's groups");
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
        if(currentPlayerHand() == null){
            return false;
        }

        for(Card c : currentPlayerHand().getHand()){
            //if the given card has the same rank and suit as the card in player's hand, they have the card
            if((card.getCardRank() == c.getCardRank() && (card.getCardSuit() == c.getCardSuit()))){
                return true;
            }
        }
        //the card was not found
        return false;
    }//isCardInHand

    /**
     * Sets a card value to the wild card based on the hand count
     */
    public void setWild(){
        wildCard = roundNum + 2;
    }//setWild

    /**
     * adds card to a users hand
     * in round 1, player's receive 3 cards
     * in remaining rounds, player receives 1 card per round
     */
    public void dealHand(){
        //clear the player's hands, discard pile and deck
        player0Hand.getHand().clear();
        player1Hand.getHand().clear();
        discardPile.clear();
        deck.clear();

        //clear each player's groupings
        for(ArrayList<Card> group0 : player0Hand.getGroupings()){
            group0.clear();
        }
        for(ArrayList<Card> group1 : player1Hand.getGroupings()){
            group1.clear();
        }

        //make a fresh deck with 52 card objects then shuffle deck randomly
        for(int s = 0; s < 4; s++) {
            for (int v = 1; v <= 13; v++) {
                deck.add(new Card(1, suite[s], v));
            }
        }
        Collections.shuffle(deck);
        Collections.shuffle(deck);

        //place a card down for the discard pile
        discardPile.add(deck.get(deck.size()-1));
        deck.remove(deck.size()-1);

        //a new hand is dealt with each round
        for (int i = 0; i <= roundNum + 1; i++) {
            player0Hand.addToHand(deck.get(deck.size()-1));
            deck.remove(deck.size()-1);
            player1Hand.addToHand(deck.get(deck.size()-1));
            deck.remove(deck.size()-1);
        }

        //set the wild card
        setWild();
    }//dealHand

    /**
     * used when the deck is empty during a round and the cards in the discard pile need to be
     * reshuffled and placed back into the deck
     */
    public void discardToDeck(){
        //check to make sure that the deck is empty
        if(!deck.isEmpty()){
            return;
        }

        //move the cards in the discard pile to the deck
        for(Card c :discardPile){
            deck.add(c);
        }
        discardPile.clear();

        //shuffle the deck and move the top card from the deck to the discard pile
        Collections.shuffle(deck);
        Collections.shuffle(deck);
        discardPile.add(deck.get(deck.size()-1));
        deck.remove(deck.size()-1);
    }

    /**
     * returns the current player's hand depending on turn
     * @return current player's hand
     */
    public Hand currentPlayerHand(){
        if(this.playerTurn == 0){
            return player0Hand;
        }
        else {
            return player1Hand;
        }
    }//currentPlayerHand

    /**
     * creates a group in the current player's hand
     * @param group the group to be added to user's hand
     */
    public void createGrouping(ArrayList<Card> group){
        currentPlayerHand().createGrouping(group);
    }

    /**
     * user chooses a card in a group and removes the card from its group
     * @param cardToRemove the card to remove from the player's group
     */
    public void removeGrouping(Card cardToRemove) {
        currentPlayerHand().removeGrouping(cardToRemove);
    }

    public int getActionTextVal() {return actionTextVal;}

}
