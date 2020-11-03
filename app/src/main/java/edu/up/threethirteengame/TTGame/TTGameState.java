package edu.up.threethirteengame.TTGame;
/**
 * @description: GameState class contains information about the current state of the game
 * @author: Nick Ohara, Adrian Muth, Shane Matsushima, Lindsey Warren
 * @version: 10/20/2020
 */

import java.util.ArrayList;
import java.util.Collections;

import edu.up.threethirteengame.game.GameFramework.GameMainActivity;
import edu.up.threethirteengame.game.GameFramework.LocalGame;
import edu.up.threethirteengame.game.GameFramework.gameConfiguration.GameConfig;

public class TTGameState {

    //creating a deck of 52 cards
    private static char[] suite = new char[] {'c','s','h','d'};
    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<Card> discardPile = new ArrayList<Card>();
    private Hand player0Hand  = new Hand();
    private  Hand player1Hand = new Hand();
    private int roundNum;
    private int player0Score;
    private int player1Score;
    private int isPlayerTurn;
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
        wildCard = roundNum + 2;

        //populate player 0 and player 1 hands with three cards from deck
        dealHand(deck, player0Hand, roundNum);
        dealHand(deck, player1Hand, roundNum);

        //each player starts with a score of 0
        player0Score = 0;
        player1Score = 0;

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
        this.roundNum = orig.getRoundNum();
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

    public int getRoundNum() {
        return roundNum;
    }

    public int getIsPlayerTurn() {return isPlayerTurn;}

    public int getWildCard() {
        return wildCard;
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

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
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
     * determines if the player can draw a card from deck
     * @param gameState
     * @return
     */
    public boolean playerDrawDeck(TTGameState gameState){
        //checks if there are cards in deck
        if(deck.size() == 0){
            return false;
        }

        //removes card from deck and adds it to the current player's hand
        if(canMove(gameState)){
            currentPlayerHand().addToHand(deck.get(0));
            deck.remove(0);
            return true;
        }
        return false;
    }

    /**
     *  draw a card from discard pile
     * @param gameState
     * @return
     */
    public void playerDrawDiscard(TTGameState gameState){
        //checks if there are cards in discard
        if(discardPile.size() == 0){
            return;
        }

        //checks if it is currently the player's turn
        //removes card from discard pile and adds it to the current player's hand
        if(canMove(gameState) == true){
            currentPlayerHand().addToHand(discardPile.get(0));
            discardPile.remove(0);
        }
    }

    /**
     * determines if player can discard a card from their current hand after drawing card
     * @param gameState
     * @return
     */
    public boolean playerDiscard(TTGameState gameState){
        //checks if it is currently the player's turn
        if(canMove(gameState) && (currentPlayerHand().getSize() == (this.roundNum+3))){
            return true;
        }
        return false;
    }

    /**
     * determines if player can Go Out
     * Go Out: all of player's cards except one must be in run/set to Go Out
     * @param gameState
     * @return
     */
    public boolean playerGoOut(TTGameState gameState){
        //check to make sure there are groups
        if(currentPlayerHand().getGroupings().get(0).isEmpty()){
            return false;
        }

        //checks if it is currently the player's turn and can discard
        if(playerDiscard(gameState)){
            int numValidGroups = 0;
            int numGroups = 0;
            //iterate through player's groupings to check for valid runs and sets
            for(ArrayList<Card> groups : currentPlayerHand().getGroupings()){
                if(currentPlayerHand().checkIfRun(groups) || currentPlayerHand().checkIfSet(groups)){
                    numValidGroups++;
                }
                numGroups++;
            }

            //checkthat all the groups ar valid
            if(numValidGroups==numGroups){
                return true;
            }
        }
        return false;
    }

    /**
     * determines if the player can take action
     * @param gameState
     * @return
     */
    public boolean canMove(TTGameState gameState){
        if(gameState.getIsPlayerTurn() == this.isPlayerTurn){
            return true;
        }
        return false;
    }


    /**
     * Sets a card value to the wild card based on the hand count
     */
    public void setWild(){
        //TODO: set wild card to round # + 2
        wildCard = roundNum + 2;
    }

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
    }

    /**
     * looks through hand and removes card to discard pile
     * @param user
     * @param card
     */
    public void discardCard(Hand user, Card card){
        for(int i = 0; i < user.getSize(); i++){
            if(user.getHand().get(i) == card){
                discardPile.add(user.getHand().get(i));
                user.getHand().remove(i);

            }
        }
    }

    /**
     * returns the current player's hand depending on turn
     * @return
     */
    public Hand currentPlayerHand(){
        if(this.isPlayerTurn == 0){
            return player0Hand;
        }
        else{
            return player1Hand;
        }
    }
}
