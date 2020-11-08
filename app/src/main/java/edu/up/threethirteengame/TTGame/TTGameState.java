package edu.up.threethirteengame.TTGame;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameState;

/**
 * @description: GameState class contains information about the
 * current state of the game and all game logic
 * @author: Nick Ohara, Adrian Muth, Shane Matsushima, Lindsey Warren
 * @version: 10/20/2020
 */
public class TTGameState extends GameState {

    //creating a deck of 52 cards
    private static char[] suite = new char[] {'c','s','h','d'};
    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<Card> discardPile = new ArrayList<Card>();
    private Hand player0Hand  = new Hand(); // User Hand
    private Hand player1Hand = new Hand(); // Computer Hand

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

    //if the player has GoneOut for the current round
    private boolean player0GoneOut;
    private boolean player1GoneOut;

    //the current player's turn
    private int isPlayerTurn;

    //the card value of the card that can be used as any card in a group for a given round
    private int wildCard;

    /**
     * Gamestate initialization constructor
     */
    public TTGameState() {
        //populate deck with 52 card objects then shuffle deck randomly
        for(int s = 0; s < 4; s++) {
            for (int v = 1; v <= 13; v++) {
                deck.add(new Card(1, suite[s], v));
            }
        }
        Collections.shuffle(deck);

        //start the discard pile
        discardPile.add(deck.get(0));
        deck.remove(0);

        //sets round number and the wild card
        roundNum = 1;
        roundOver = false;
        wildCard = roundNum + 2;

        //populate player 0 and player 1 hands with three cards from deck
        dealHand(deck, player0Hand, roundNum);
        dealHand(deck, player1Hand, roundNum);

        //each player starts with a score of 0
        player0Score = 0;
        player1Score = 0;
        player0TurnsTaken = 0;
        player1TurnsTaken = 0;
        player0GoneOut = false;
        player1GoneOut = false;

        //player 0 goes first
        isPlayerTurn = 0;
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
        this.player0GoneOut = orig.player0GoneOut;
        this.player1GoneOut = orig.player1GoneOut;
        this.roundNum = orig.getRoundNum();
        this.roundOver = orig.getRoundOver();
        this.isPlayerTurn = orig.getIsPlayerTurn();
        this.wildCard = orig.getWildCard();
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

    public int getRoundNum() {
        return roundNum;
    }

    public int getIsPlayerTurn() {return isPlayerTurn;}

    public int getWildCard() {
        return wildCard;
    }

    public boolean getRoundOver() { return roundOver; }

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

    /**
     * changes which player can take actions
     */
    public void nextTurn(){
        if(isPlayerTurn == 1)
            isPlayerTurn = 0;
        else
            isPlayerTurn = 1;
    }

    /**
     * checks if the round is over
     * the round number is dependent on how many turns each player took
     */
    public boolean isRoundOver() {

        //both players need to have the same amount of turns
        if(player0TurnsTaken != player1TurnsTaken){
            return false;
        }

        //the amount of turns should be equal to the current round number
        if(player0TurnsTaken != roundNum){
            return false;
        }
        else if(player1TurnsTaken != roundNum){
            return false;
        }

        //the round is over if the previous statements weren't caught
        return true;

    }

    @Override
    public String toString() {
        String round = "Round number: " + roundNum;
        String deckSize = "Deck card amount: " + deck.size();
        String discardSize = "Discard pile amount: " + discardPile.size();
        String playerCard = "Player 0 card amount: " + player0Hand.getSize();
        String computerCard = "Player 1 card amount: " + player1Hand.getSize();
        String turn = "Player " + isPlayerTurn + " turn";
        String playerScoreString = "Player 0 score: " + player0Score;
        String computerScoreString = "Player 1 score: " + player1Score;
        String toString = round + "\n" + deckSize + "\n" + discardSize + "\n" + playerCard + "\n"
                + computerCard + "\n" + turn + "\n" + playerScoreString + "\n"
                + computerScoreString;
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
            playerDrawDiscard();
            return true;
        }

        //can go again if the other player has Gone Out, you have NOT GoneOut yet, and
        //you have already taken your turn
        if(this.isPlayerTurn == 0){
            //player 0's turn
            if(player1GoneOut && !player0GoneOut && (player0TurnsTaken == roundNum)){
                //removes card from deck and adds it to the current player's hand
                currentPlayerHand().addToHand(deck.get(0));
                deck.remove(0);
                return true;
            }
        }
        else {
            //player 1's turn
            if(player0GoneOut && !player1GoneOut && (player1TurnsTaken == roundNum)){
                //removes card from deck and adds it to the current player's hand
                currentPlayerHand().addToHand(deck.get(0));
                deck.remove(0);
                return true;
            }
        }


        //removes card from deck and adds it to the current player's hand
        currentPlayerHand().addToHand(deck.get(0));
        deck.remove(0);
        if (this.isPlayerTurn == 0) {
            player0TurnsTaken++;
        } else if (this.isPlayerTurn == 1) {
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
            playerDrawDeck();
            return true;
        }

        //removes card from discard pile and adds it to the current player's hand
        currentPlayerHand().addToHand(discardPile.get(0));
        discardPile.remove(0);
        if (this.getIsPlayerTurn() == 0) {
            player0TurnsTaken++;
        } else if (this.getIsPlayerTurn() == 1) {
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
            Log.d("playerDiscard()","card to remove wasn't found");
            return false;
        }

        //checks if it is currently the player's turn and they have enough cards
        if(currentPlayerHand().getSize() == (this.roundNum+3)){
            if(isCardInHand(c)) {
                return true;
            }
        }

        return false;
    }

    /**
     * action method that discards card
     * looks through current player's hand and removes card to discard pile
     * @param card the card to be discarded from user's hand
     */
    public void discardCard(Card card){
        //checks if the card exists
        if(card == null){
            Log.d("discardCard()","card to remove wasn't found");
            return;
        }
        //checks if the player can discard
        if(!playerDiscard(card)){
            return;
        }

        //iterates through current player's hand for the card to discard
        //removes the card from the players hand and adds it to the discard pile
        for(Card c : currentPlayerHand().getHand()){
            if(card == c){
                discardPile.add(card);
                currentPlayerHand().getHand().remove(card);

                //the player's turn always ends when they discard
                nextTurn();
            }
        }
    }

    /**
     * determines if player can Go Out
     * Go Out: all of player's cards except one must be in run/set to Go Out
     * @return whether the player can Go Out or not
     */
    public boolean canPlayerGoOut(){
        //check to make sure there are groups
        if(currentPlayerHand().getGroupings().get(0).isEmpty()){
            return false;
        }

        //checks to make sure they haven't already Gone Out this round
        if(isPlayerTurn == 0){
            if(player0GoneOut){
                return false;
            }
        }
        else{
            if(player1GoneOut){
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
                    return false;
                }
            }
        }

        //checks the groupings and sees if the card can be discarded
        if(playerDiscard(discardGoOut)){
            int numValidGroups = 0;
            int numGroups = 0;
            //iterate through player's groupings to check for valid runs and sets
            for(ArrayList<Card> groups : currentPlayerHand().getGroupings()){
                if(currentPlayerHand().checkIfRun(groups) || currentPlayerHand().checkIfSet(groups)){
                    numValidGroups++;
                }
                numGroups++;
            }

            //check that all the groups are valid
            if(numValidGroups==numGroups){
                return true;
            }
        }
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

            //player has gone out for this round and the other player should have another turn
            //if they already went
            if(this.isPlayerTurn == 0){
                player0GoneOut = true;
            }
            else{
                player1GoneOut = true;
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
            Log.d("isCardInGroup()", "passed card was null");
            return false;
        }

        //check to make sure there are groups in the current player's hand
        if(currentPlayerHand().getGroupings().get(0).isEmpty()){
            return false;
        }

        //iterate through the current player's groupings for the given card
        for(ArrayList<Card> groups : currentPlayerHand().getGroupings()){
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
    public void dealHand(ArrayList<Card> inputDeck, Hand user, int round){
        setWild();
        if(round != roundNum){
            return;
        }
        if(round == 1) {
            for (int i = 0; i <= round + 1; i++) {
                user.addToHand(inputDeck.get(0));
                inputDeck.remove(0);
            }
        }
        else{
            user.addToHand(inputDeck.get(0));
            inputDeck.remove(0);
        }
    }//dealHand

    /**
     * returns the current player's hand depending on turn
     * @return current player's hand
     */
    public Hand currentPlayerHand(){
        if(this.isPlayerTurn == 0){
            return player0Hand;
        }
        else {
            return player1Hand;
        }
    }//currentPlayerHand

}
