package edu.up.threethirteengame.TTGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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

    /**
     * External Citation
     * Problem: Needed help understanding the relationship between GameBoard and HumanPlayer
     * Source: Dylan Pascua (previous CS301 student)
     * Solution: figured it out with his help
     */

    //we need the current GameState to access the player's hand
    TTGameState ttGameState = null;

    //instance variables that will define locations on the GameBoard
    float viewWidth;
    float viewHeight;
    float padx = 35;
    float pady = 4;

    //paints for drawing the card borders depending on group
    Paint blue = new Paint();
    Paint yellow = new Paint();
    Paint orange = new Paint();
    Paint purple = new Paint();
    Paint black = new Paint();

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
     * allows the GameState to be set
     * @param currentGameState the current GameState is sent from TTHumanPlayer when they receiveInfo
     */
    public void setTtGameState(TTGameState currentGameState){
        ttGameState = currentGameState;
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

        Bitmap tempObj = BitmapFactory.decodeResource(getResources(), card.getCardId());
        Bitmap cardObj = Bitmap.createScaledBitmap(tempObj,card.getWidth(),card.getHeight(),true);

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
        yellow.setColor(Color.YELLOW);
        yellow.setStrokeWidth(5.0f);
        yellow.setStyle(Paint.Style.STROKE);
        orange.setColor(Color.rgb(255, 165, 0));
        orange.setStrokeWidth(5.0f);
        orange.setStyle(Paint.Style.STROKE);
        purple.setColor(Color.rgb(106,13,173));
        purple.setStrokeWidth(5.0f);
        purple.setStyle(Paint.Style.STROKE);
        black.setColor(Color.BLACK);
        black.setStrokeWidth(5.0f);
        black.setStyle(Paint.Style.STROKE);

        if(ttGameState.getPlayer0Hand().getHand() == null){
            //do nothing if player hand doesn't exist
            return;
        }
        else if(ttGameState.getPlayer0Hand().getHand().isEmpty()){
            //do nothing if player hand is empty
            return;
        }

        if(!ttGameState.isCardInGroup(card)){
            //draw a black border if it isn't in a group
            canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),black);
        }
        else{
            //find which group it's in
            int grpIdx = 0;
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

            switch (grpIdx){
                case 0:
                    canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),blue);
                    break;
                case 1:
                    canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),yellow);
                    break;
                case 2:
                    canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),orange);
                    break;
                case 3:
                    canvas.drawRect(x,y,x+card.getWidth(),y+card.getHeight(),purple);
                    break;
                default:
                    break;
            }
        }
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
        //black border for discarded card
        black.setColor(Color.BLACK);
        black.setStrokeWidth(5.0f);
        black.setStyle(Paint.Style.STROKE);

        //draw the rotated card
        Bitmap tempObj = BitmapFactory.decodeResource(getResources(), card.getCardId());
        Bitmap cardObj = Bitmap.createScaledBitmap(tempObj,card.getWidth(),card.getHeight(),true);
        Bitmap rotObj = Bitmap.createBitmap(cardObj, 0, 0, cardObj.getWidth(),cardObj.getHeight(), rotate, true);
        canvas.drawBitmap(rotObj, x , y,null);

        //only need to draw a border for a front facing card
        if(card.getCardType() == 1) {
            canvas.drawRect(x, y, x + card.getHeight(), y + card.getWidth(), black);
        }

    }

    /**
     * displays the GUI
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas){
        float sectionWidth = this.getWidth()/4;
        float sectionHeight = this.getHeight()/5;
        if(ttGameState == null){
            return;
        }
        ArrayList<Card> userHand = ttGameState.getPlayer0Hand().getHand();

        //Discard and Deck pile
        rotate.setRotate(90);
        drawRotCard(canvas, 128, 74, new Card(1, 'h', 10));
        drawRotCard(canvas, 630, 74, new Card(0));

        if(userHand != null && !userHand.isEmpty()) {
            int numCards = 0;

            //TODO: used for displaying the final board screen, remove later
            for(int i=0;i<11;i++){
                ttGameState.currentPlayerHand().addToHand(ttGameState.getDeck().get(0));
                ttGameState.getDeck().remove(0);
            }

            //Grid system used to showcase hand of user
            for (int row = 1; row < 5; row++) {
                for (int col = 0; col < 4; col++) {
                    if (numCards < userHand.size()) {
                        drawCard(canvas, col * sectionWidth + padx, row * sectionHeight + pady, userHand.get(numCards));
                        numCards++;
                    }
                }
            }
        }

    }

}
