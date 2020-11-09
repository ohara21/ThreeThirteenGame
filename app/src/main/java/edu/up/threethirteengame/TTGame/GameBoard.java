package edu.up.threethirteengame.TTGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.SurfaceView;
import java.util.ArrayList;

import edu.up.threethirteengame.game.GameFramework.infoMessage.GameState;

/**
 * @description GameBoard class displays the Graphical User Interface on the screen
 * @author Nick Ohara, Shane Matsushima, Adrian Muth, Lindsey Warren
 * @version 11/3/20
 */
public class GameBoard extends SurfaceView {

    //instance variables that will define locations on the GameBoard
    float viewWidth;
    float viewHeight;
    float midWidth;
    float midHeight;
    float discardWidth;
    float stockWidth;
    float topY;
    float topY2;
    float botY;
    float botY2;

    //used to rotate the card images
    Matrix rotate = new Matrix();

    //creating a deck of 52 cards
    char suite[] = new char[] {'c','s','h','d'};
    ArrayList<Card> deck = new ArrayList<Card>();

    //create all 53 card objects
    Card backCard = new Card(2);
    Card aceClubsCard = new Card(1,'c',1);
    Card aceHeartsCard = new Card(1,'h',1);
    Card aceSpadesCard = new Card(1,'s',1);
    Card aceDiamondsCard = new Card(1,'d',1);
    Card twoClubsCard = new Card(1,'c',2);
    Card twoHeartsCard = new Card(1,'h',2);
    Card twoSpadesCard = new Card(1,'s',2);
    Card twoDiamondsCard = new Card(1,'d',2);
    Card threeClubsCard = new Card(1,'c',3);
    Card threeHeartsCard = new Card(1,'h',3);
    Card threeSpadesCard = new Card(1,'s',3);
    Card threeDiamondsCard = new Card(1,'d',3);
    Card fourClubsCard = new Card(1,'c',4);
    Card fourHeartsCard = new Card(1,'h',4);
    Card fourSpadesCard = new Card(1,'s',4);
    Card fourDiamondsCard = new Card(1,'d',4);
    Card fiveClubsCard = new Card(1,'c',5);
    Card fiveHeartsCard = new Card(1,'h',5);
    Card fiveSpadesCard = new Card(1,'s',5);
    Card fiveDiamondsCard = new Card(1,'d',5);
    Card sixClubsCard = new Card(1,'c',6);
    Card sixHeartsCard = new Card(1,'h',6);
    Card sixSpadesCard = new Card(1,'s',6);
    Card sixDiamondsCard = new Card(1,'d',6);
    Card sevenClubsCard = new Card(1,'c',7);
    Card sevenHeartsCard = new Card(1,'h',7);
    Card sevenSpadesCard = new Card(1,'s',7);
    Card sevenDiamondsCard = new Card(1,'d',7);
    Card eightClubsCard = new Card(1,'c',8);
    Card eightHeartsCard = new Card(1,'h',8);
    Card eightSpadesCard = new Card(1,'s',8);
    Card eightDiamondsCard = new Card(1,'d',8);
    Card nineClubsCard = new Card(1,'c',9);
    Card nineHeartsCard = new Card(1,'h',9);
    Card nineSpadesCard = new Card(1,'s',9);
    Card nineDiamondsCard = new Card(1,'d',9);
    Card tenClubsCard = new Card(1,'c',10);
    Card tenHeartsCard = new Card(1,'h',10);
    Card tenSpadesCard = new Card(1,'s',10);
    Card tenDiamondsCard = new Card(1,'d',10);
    Card jackClubsCard = new Card(1,'c',11);
    Card jackHeartsCard = new Card(1,'h',11);
    Card jackSpadesCard = new Card(1,'s',11);
    Card jackDiamondsCard = new Card(1,'d',11);
    Card queenClubsCard = new Card(1,'c',12);
    Card queenHeartsCard = new Card(1,'h',12);
    Card queenSpadesCard = new Card(1,'s',12);
    Card queenDiamondsCard = new Card(1,'d',12);
    Card kingClubsCard = new Card(1,'c',13);
    Card kingHeartsCard = new Card(1,'h',13);
    Card kingSpadesCard = new Card(1,'s',13);
    Card kingDiamondsCard = new Card(1,'d',13);

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
     * This function draws the specified card object to a given x and y on a given canvas
     * Works for both front and back showing cards
     */
    /** External Citation:
     * Date: 9/18/20
     * Problem: Didn't know how to create Bitmap image
     * Resource: Professor Nuxoll
     * Solution: used his code
     */
    public void drawCard(Canvas canvas, float x, float y, Card card){


        Bitmap tempObj = BitmapFactory.decodeResource(getResources(), card.cardId);
        Bitmap cardObj = Bitmap.createScaledBitmap(tempObj,card.getWidth(),card.getHeight(),true);

        canvas.drawBitmap(cardObj, x, y, null);

    }

    /**
     * draws a rotated card, used for the discard and stock pile
     * @param canvas
     * @param x
     * @param y
     * @param card
     */
    /**
     * External Citation:
     * Date: 9/18/20
     * Problem: Didn't know how to rotate Bitmap image
     * Resource: https://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees
     * Solution: used the method suggested
     */
    public void drawRotCard(Canvas canvas, float x, float y, Card card){

        Bitmap tempObj = BitmapFactory.decodeResource(getResources(), card.cardId);
        Bitmap cardObj = Bitmap.createScaledBitmap(tempObj,card.getWidth(),card.getHeight(),true);
        Bitmap rotObj = Bitmap.createBitmap(cardObj, 0, 0, cardObj.getWidth(),cardObj.getHeight(), rotate, true);
        canvas.drawBitmap(rotObj, x , y,null);

    }

    /**
     * displays the GUI
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas){
        //creates all 52 cards and places them into an array list called deck
        for(int s = 0; s < 4; s++){
            for(int v = 1; v <= 13; v++){
                deck.add(new Card(1,suite[s],v));
            }
        }

        //initialize coordinates based on the GameBoard's dimensions
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        midWidth = viewWidth/2.0f;
        midHeight = viewHeight/2.0f - tenClubsCard.getWidth()/2.0f;
        discardWidth = midWidth - tenClubsCard.getHeight() - 50.0f;
        stockWidth = midWidth + 50.0f;
        topY = midHeight - tenClubsCard.getHeight() - 75.0f;
        topY2 = topY -tenClubsCard.getHeight()/3.0f;
        botY = midHeight + tenClubsCard.getHeight() + 75.0f;
        botY2 = botY - tenClubsCard.getHeight()/3.0f;

        //rotate the matrix 90 degrees
        rotate.setRotate(90);

        //draw the discard and stock piles
        drawRotCard(canvas,discardWidth,midHeight,tenClubsCard);
        drawRotCard(canvas, stockWidth, midHeight, backCard);

        //draws opponent's hand


        //draws player's hand


    }







}
