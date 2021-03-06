package edu.up.threethirteengame.TTGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.threethirteengame.R;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameState;

/**
 * @description GameBoard class displays the Graphical User Interface on the screen
 * @author Nick Ohara, Shane Matsushima, Adrian Muth, Lindsey Warren
 * @version 11/3/20
 */
public class GameBoard extends SurfaceView implements Serializable {

    /**
     * External Citation
     * Problem: Needed help understanding the relationship between GameBoard and HumanPlayer
     * Source: Dylan Pascua (previous CS301 student)
     * Solution: figured it out with his help
     */

    //we need the current GameState to access the player's hand
    TTGameState ttGameState = null;

    //instance variables that will define locations on the GameBoard
    float sectionWidth;
    float sectionHeight;

    //padding for displaying cards
    float padx = 35;
    float pady = 4;

    //paints for drawing the card borders depending on group
    Paint blue = new Paint();
    Paint magenta = new Paint();
    Paint orange = new Paint();
    Paint purple = new Paint();
    Paint black = new Paint();
    Paint red = new Paint();
    Paint yellow = new Paint();

    //used to rotate the card images
    Matrix rotate = new Matrix();

    //the empty card if the player needs to draw before discarding
    Card emptyCard = new Card(1,'e',0);

    int playerNum = 0;

    /**
     * constructors that were inherited from SurfaceView class
     */
    public GameBoard(Context context) {
        super(context);
    }

    public GameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * allows the GameState to be set
     * @param currentGameState the current GameState is sent from TTHumanPlayer when they receiveInfo
     */
    public void setTtGameState(TTGameState currentGameState){
        ttGameState = currentGameState;
    }

    /**
     * This function draws the specified card object to a given x and y on a given canvas
     * works for both front and back showing cards
     * @param canvas the canvas that's to be drawn on
     * @param x the x location of the card
     * @param y the y location of the card
     * @param card the card to be drawn
     */
    public void drawCard(Canvas canvas, float x, float y, Card card){

        /** External Citation:
         * Date: 9/18/20
         * Problem: Didn't know how to create Bitmap image
         * Resource: Professor Nuxoll
         * Solution: used his code
         */

        Bitmap tempObj = BitmapFactory.decodeResource(getResources(), card.getCardId());
        Bitmap cardObj = Bitmap.createScaledBitmap(tempObj,card.getWidth(),card.getHeight(),true);

        //draws the card first, then the border
        canvas.drawBitmap(cardObj, x, y, null);
        drawCardBorder(canvas,x,y,card);
    }

    /**
     * draws a border around the card
     * @param canvas what the card is to be drawn on
     * @param x the x location of the card
     * @param y the y location of the card
     */
    public void drawCardBorder(Canvas canvas, float x, float y, Card card){
        //set the paint style for the borders
        blue.setColor(Color.BLUE);
        blue.setStrokeWidth(5.0f);
        blue.setStyle(Paint.Style.STROKE);
        magenta.setColor(Color.MAGENTA);
        magenta.setStrokeWidth(5.0f);
        magenta.setStyle(Paint.Style.STROKE);
        orange.setColor(Color.rgb(255, 125, 0));
        orange.setStrokeWidth(5.0f);
        orange.setStyle(Paint.Style.STROKE);
        purple.setColor(Color.rgb(106,13,173));
        purple.setStrokeWidth(5.0f);
        purple.setStyle(Paint.Style.STROKE);
        black.setColor(Color.BLACK);
        black.setStrokeWidth(5.0f);
        black.setStyle(Paint.Style.STROKE);
        red.setColor(Color.RED);
        red.setStrokeWidth(7.0f);
        red.setStyle(Paint.Style.STROKE);
        yellow.setColor(Color.rgb(255,255,0));
        yellow.setStrokeWidth(7.0f);
        yellow.setStyle(Paint.Style.STROKE);

        if(ttGameState.getPlayer0Hand().getHand() == null){
            //do nothing if player hand doesn't exist
            return;
        }
        else if(ttGameState.getPlayer0Hand().getHand().isEmpty()){
            //do nothing if player hand is empty
            return;
        }

        if(card.getCardSuit() == 'e'){
            canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),yellow);
            return;
        }

        if (card.getIsClick()) {
            //Log.d("GameBoard","card "+card.getCardRank()+card.getCardSuit()+" card is clicked");
            canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),red);
        } else if(!ttGameState.isCardInGroup(card)){
            //draw a black border if it isn't in a group
            //Log.d("GameBoard","card "+card.getCardRank()+card.getCardSuit()+" is not in group yet");
            canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),black);
            return;
        }
        else{
            //find which group it's in
            int grpIdx = 1;
            boolean flag = false;

            //iterate through the current player's groupings for the given card
            for(ArrayList<Card> groups : ttGameState.currentPlayerHand().getGroupings()){
                for(Card c : groups){
                    //if the given card has the same rank and suit as the card in groups, they have the card
                    if((card.getCardRank() == c.getCardRank() && (card.getCardSuit() == c.getCardSuit()))){
                        flag = true;
                    }
                }
                //check if the card was found first
                if(flag){
                    //break out of the outer for loop
                    break;
                }
                //increment grpIdx every time we check the next group
                grpIdx++;
            }
            grpIdx = ttGameState.currentPlayerHand().getGroupingSize() - grpIdx;
            switch (grpIdx){
                case 0:
                    canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),blue);
                    break;
                case 1:
                    canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),magenta);
                    break;
                case 2:
                    canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),orange);
                    break;
                case 3:
                    canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),purple);
                    break;
                default:
                    Log.d("GameBoard","the group Index is off: "+grpIdx);
                    break;
            }
        }
    }

    /**
     * draws a rotated card, used for the discard and stock pile
     * @param canvas the canvas that's to be drawn on
     * @param x the x location of the card
     * @param y the y location of the card
     * @param card the card to be drawn
     */
    public void drawRotCard(Canvas canvas, float x, float y, Card card){
        /**
         * External Citation:
         * Date: 9/18/20
         * Problem: Didn't know how to rotate Bitmap image
         * Resource: https://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees
         * Solution: used the method suggested
         */

        //black border for discarded card
        black.setColor(Color.BLACK);
        black.setStrokeWidth(5.0f);
        black.setStyle(Paint.Style.STROKE);
        yellow.setColor(Color.rgb(255,255,0));
        yellow.setStrokeWidth(7.0f);
        yellow.setStyle(Paint.Style.STROKE);

        //draw the rotated card
        Bitmap tempObj = BitmapFactory.decodeResource(getResources(), card.getCardId());
        Bitmap cardObj = Bitmap.createScaledBitmap(tempObj,card.getWidth(),card.getHeight(),true);
        Bitmap rotObj = Bitmap.createBitmap(cardObj, 0, 0, cardObj.getWidth(),cardObj.getHeight(), rotate, true);
        canvas.drawBitmap(rotObj, x , y,null);

        if (ttGameState.currentPlayerHand().getHand().size() == ttGameState.getRoundNum() + 2) {
            canvas.drawRect(x, y, x + card.getHeight(), y + card.getWidth(), yellow);
        }
        else {
            canvas.drawRect(x, y, x + card.getHeight(), y + card.getWidth(), black);
        }

    }

    /**
     * returns card corresponding to ID
     * @param idNum
     * @return Card
     */
    public Card findCardById(int idNum) {
        ArrayList<Card> userHand = ttGameState.currentPlayerHand().getHand();
        for (Card card: userHand) {
            int currId = card.getCardId();
            if (idNum == currId) {
                return card;
            }
        }
        // this should never happen
        return new Card(1);
    }

    /**
     * draws the cards on the SurfaceView in a grid-like pattern
     * @param canvas to be drawn on
     */
    @Override
    public void onDraw(Canvas canvas){
        sectionWidth = this.getWidth()/4;
        sectionHeight = this.getHeight()/5;
        if(ttGameState == null){
            return;
        }
        ArrayList<Card> userHand = null;
        if(playerNum == 0){
            userHand = ttGameState.getPlayer0Hand().getHand();
        }
        else{
            userHand = ttGameState.getPlayer1Hand().getHand();
        }

        //Discard and Deck pile
        rotate.setRotate(90);

        //draw the discard pile
        if(!ttGameState.getDiscardPile().isEmpty()) {
            drawRotCard(canvas, 128, 74, ttGameState.getDiscardPile().get(ttGameState.getDiscardPile().size()-1));
        }

        drawRotCard(canvas, 630, 74, new Card(0));

        if(userHand != null && !userHand.isEmpty()) {
            int numCards = 0;

            //Grid system used to showcase hand of user
            for (int row = 1; row < 5; row++) {
                for (int col = 0; col < 4; col++) {
                    //skip drawing the first card on the last row
                    if((row == 4) && (col ==0)){
                        continue;
                    }

                    //draw the card
                    if (numCards < userHand.size()) {
                        drawCard(canvas, col * sectionWidth + padx, row * sectionHeight + pady, userHand.get(numCards));
                        userHand.get(numCards).setBoardLocation(numCards);
                        numCards++;
                    }
                    else if (numCards == ttGameState.getRoundNum()+2) {
                        drawCard(canvas, col * sectionWidth + padx, row * sectionHeight + pady, emptyCard);
                        numCards++;
                    }
                }
            }
        }
    }//onDraw

    public void setPlayerToDraw(int playerNum){
        this.playerNum = playerNum;
    }

}
