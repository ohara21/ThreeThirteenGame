package edu.up.threethirteengame.TTGame;

import java.io.Serializable;

import edu.up.threethirteengame.R;

/**
 * @description: Card class contains information on each card used in Three Thirteen
 * @author: Nick Ohara, Adrian Muth, Shane Matsushima, Lindsey Warren
 * @version: 10/20/2020
 */
public class Card implements Serializable {

    /**
     * External Citation
     * Date: 11/9/20
     * Problem: needed card images
     * Source: https://github.com/hayeah/playing-cards-assets.git
     * Solution: used the card images from the link
     */

    // cardType designates if the card is:
    // front(1), back (2)
    private int cardType;
    private double scale = 0.91891;
    private final int CARDWIDTH = 222;
    private final int CARDHEIGHT = 323;
    private int finalCardWidth;
    private int finalCardHeight;
    private double backScale = 0.91891;
    private final int BACKWIDTH = 223;
    private final int BACKHEIGHT = 307;
    private int finalBackWidth;
    private int finalBackHeight;

    //card's rank and suit
    private int cardRank;
    private char cardSuit;

    //if the card is currently selected
    private boolean isClick;

    //if the card is in a valid group
    private boolean inValidGroup;

    //the card's location on the GameBoard's grid
    private int boardLocation = 0;

    private int cardId = R.drawable.back_vert;

    /**
     * Card constructor to initially create cards and scale them
     * @param cType
     * @param suit
     * @param value
     */
    public Card(int cType, char suit, int value){
        this.cardType = cType;
        this.cardRank = value;
        this.cardSuit = suit;
        scaleCard(this.cardType);
        if(cType == 1) {
            cardId = getId(suit, value);
        }
        this.isClick = false;
        this.inValidGroup = false;
    }

    /**
     * base constructor for the card object
     * @param cType
     */
    public Card(int cType){
        this.cardType = cType;
        scaleCard(this.cardType);
        this.isClick = false;
    }

    /**
     * copy constructor for Cards
     * @param orig
     */
    public Card(Card orig){
        this.cardType = orig.cardType;
        this.cardRank = orig.cardRank;
        this.cardSuit = orig.cardSuit;
        scaleCard(this.cardType);
        this.cardId = orig.cardId;
        this.isClick = false;
        this.inValidGroup = orig.inValidGroup;
    }

    /**
     * scales the card and accounts for the different orientations of each card
     * sets the height and width with respect to its orientation in the screen
     * @param cType
     */
    public void scaleCard(int cType){
        if(cType == 1){
            finalCardWidth = (int) (CARDWIDTH * scale);
            finalCardHeight = (int) (CARDHEIGHT * scale);
        }
        else{
            finalBackWidth = (int) (BACKWIDTH * backScale);
            finalBackHeight = (int) (BACKHEIGHT * backScale);
        }
    }

    /**
     * returns and sets whether the card has been clicked or not
     * @return
     */
    public boolean getIsClick() {return isClick;}

    /**
     * sets if the card is clicked
     * @param isClick
     */
    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }

    /**
     * sets if this card is in a valid group
     * @param inValidGroup
     */
    public void setInValidGroup(boolean inValidGroup){this.inValidGroup = inValidGroup;}

    /**
     * gets if this card is in a valid group
     * @return
     */
    public boolean getInValidGroup(){return this.inValidGroup;}

    /**
     * returns the spot of the card on the board, if any
     */
    public int getBoardLocation() {return boardLocation;}

    /**
     * sets the spot of the card on the board, if any
     * @param boardLocation
     */
    public void setBoardLocation(int boardLocation) {this.boardLocation = boardLocation;}

    /**
     * returns the vertical height of the card depending on its type
     * @return the cards height
     */
    public int getHeight(){
        if(cardType == 1) {
            return this.finalCardHeight;
        }
        else{
            return this.finalBackHeight;
        }

    }

    /**
     * @return if its a front or back card
     */
    public int getCardType(){
        return this.cardType;
    }

    /**
     * @return the cardID
     */
    public int getCardId() {
        return cardId;
    }

    /**
     *
     * @return the horizontal width of the card depending on its type
     */
    public int getWidth(){
        if(cardType == 1) {
            return this.finalCardWidth;
        }
        else{
            return this.finalBackWidth;
        }

    }

    /**
     *
     * @return the horizontal width of the card depending on its type
     */
    public char getCardSuit() {
        return cardSuit;
    }

    /**
     *
     * @return card rank
     */
    public int getCardRank() {
        return cardRank;
    }

    /**
     * @param suit the suit of the card
     * @param value the rank of the card
     * @return the picture id based on the suit and value of the card
     */
    private int getId(char suit, int value){
        int cardId = 3;

        if((suit == 'e') && (value == 0)){
            return R.drawable.empty_card;
        }

        switch(suit){
            case 's':
                switch(value){
                    case 1:
                        cardId = R.drawable.ace_of_spades;
                        break;
                    case 2:
                        cardId = R.drawable.two_of_spades;
                        break;
                    case 3:
                        cardId = R.drawable.three_of_spades;
                        break;
                    case 4:
                        cardId = R.drawable.four_of_spades;
                        break;
                    case 5:
                        cardId = R.drawable.five_of_spades;
                        break;
                    case 6:
                        cardId = R.drawable.six_of_spades;
                        break;
                    case 7:
                        cardId = R.drawable.seven_of_spades;
                        break;
                    case 8:
                        cardId = R.drawable.eight_of_spades;
                        break;
                    case 9:
                        cardId = R.drawable.nine_of_spades;
                        break;
                    case 10:
                        cardId = R.drawable.ten_of_spades;
                        break;
                    case 11:
                        cardId = R.drawable.jack_of_spades;
                        break;
                    case 12:
                        cardId = R.drawable.queen_of_spades;
                        break;
                    case 13:
                        cardId = R.drawable.king_of_spades;
                        break;
                }
                break;
            case 'h':
                switch(value){
                    case 1:
                        cardId = R.drawable.ace_of_hearts;
                        break;
                    case 2:
                        cardId = R.drawable.two_of_hearts;
                        break;
                    case 3:
                        cardId = R.drawable.three_of_hearts;
                        break;
                    case 4:
                        cardId = R.drawable.four_of_hearts;
                        break;
                    case 5:
                        cardId = R.drawable.five_of_hearts;
                        break;
                    case 6:
                        cardId = R.drawable.six_of_hearts;
                        break;
                    case 7:
                        cardId = R.drawable.seven_of_hearts;
                        break;
                    case 8:
                        cardId = R.drawable.eight_of_hearts;
                        break;
                    case 9:
                        cardId = R.drawable.nine_of_hearts;
                        break;
                    case 10:
                        cardId = R.drawable.ten_of_hearts;
                        break;
                    case 11:
                        cardId = R.drawable.jack_of_hearts;
                        break;
                    case 12:
                        cardId = R.drawable.queen_of_hearts;
                        break;
                    case 13:
                        cardId = R.drawable.king_of_hearts;
                        break;

                }
                break;
            case 'c':
                switch(value){
                    case 1:
                        cardId = R.drawable.ace_of_clubs;
                        break;
                    case 2:
                        cardId = R.drawable.two_of_clubs;
                        break;
                    case 3:
                        cardId = R.drawable.three_of_clubs;
                        break;
                    case 4:
                        cardId = R.drawable.four_of_clubs;
                        break;
                    case 5:
                        cardId = R.drawable.five_of_clubs;
                        break;
                    case 6:
                        cardId = R.drawable.six_of_clubs;
                        break;
                    case 7:
                        cardId = R.drawable.seven_of_clubs;
                        break;
                    case 8:
                        cardId = R.drawable.eight_of_clubs;
                        break;
                    case 9:
                        cardId = R.drawable.nine_of_clubs;
                        break;
                    case 10:
                        cardId = R.drawable.ten_of_clubs;
                        break;
                    case 11:
                        cardId = R.drawable.jack_of_clubs;
                        break;
                    case 12:
                        cardId = R.drawable.queen_of_clubs;
                        break;
                    case 13:
                        cardId = R.drawable.king_of_clubs;
                        break;
                }
                break;
            case 'd':
                switch(value){
                    case 1:
                        cardId = R.drawable.ace_of_diamonds;
                        break;
                    case 2:
                        cardId = R.drawable.two_of_diamonds;
                        break;
                    case 3:
                        cardId = R.drawable.three_of_diamonds;
                        break;
                    case 4:
                        cardId = R.drawable.four_of_diamonds;
                        break;
                    case 5:
                        cardId = R.drawable.five_of_diamonds;
                        break;
                    case 6:
                        cardId = R.drawable.six_of_diamonds;
                        break;
                    case 7:
                        cardId = R.drawable.seven_of_diamonds;
                        break;
                    case 8:
                        cardId = R.drawable.eight_of_diamonds;
                        break;
                    case 9:
                        cardId = R.drawable.nine_of_diamonds;
                        break;
                    case 10:
                        cardId = R.drawable.ten_of_diamonds;
                        break;
                    case 11:
                        cardId = R.drawable.jack_of_diamonds;
                        break;
                    case 12:
                        cardId = R.drawable.queen_of_diamonds;
                        break;
                    case 13:
                        cardId = R.drawable.king_of_diamonds;
                        break;
                }
                break;

        }

        return cardId;
    }

}
