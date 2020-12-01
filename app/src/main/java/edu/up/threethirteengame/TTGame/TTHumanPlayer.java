package edu.up.threethirteengame.TTGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import edu.up.threethirteengame.R;
import edu.up.threethirteengame.game.GameFramework.GameHumanPlayer;
import edu.up.threethirteengame.game.GameFramework.GameMainActivity;
import edu.up.threethirteengame.game.GameFramework.GamePlayer;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.IllegalMoveInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.NotYourTurnInfo;

public class TTHumanPlayer extends GameHumanPlayer implements View.OnClickListener, View.OnTouchListener {

    //our game state
    private TTGameState state;

    //our activity
    private Activity myActivity = null;

    //player names
    private String p0Name;
    private String p1Name;

    //our buttons
    private Button helpButton;
    private Button quitButton;
    private Button restartButton;
    private Button goOutButton;
    private Button discardButton;
    private Button removeGroupButton;
    private Button addGroupButton;

    //text views
    private TextView roundText;
    private TextView p0ScoreText;
    private TextView p1ScoreText;
    private TextView wildCardText;
    private TextView actionInfoText;
    private GameBoard gameBoard;

    // empty cards used to get width and height
    private Card card = new Card(1);
    private Card backCard = new Card(0);

    // hand to hold selected cards to form groups
    private Hand selectHand = new Hand();

    // determines contents of action text view
    private int actionInfoTextValue = 0;
    private int previousPlayer0Score = 0;
    private int previousPlayer1Score = 0;
    private int player0ScoreIncrease = 0;
    private int player1ScoreIncrease = 0;
    private boolean player0Acted;

    /**
     * constructor
     * @param name: the player's name
     */
    public TTHumanPlayer(String name){
        super(name);
    }

    /**
     * Returns the GUI's top view object
     *
     * @return
     * 		the top object in the GUI's view hierarchy
     */
    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    /**
     * callback method: we have received a message from the game
     *
     * @param info: the message we have received from the game
     */
    @Override
    public void receiveInfo(GameInfo info) {
        // ignore the message if it's not a CounterState message
        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
            return;
        }
        else if (!(info instanceof TTGameState)){
            return;
        }
        else{
            //this is the correct info message and this state needs to be updated
            this.state = (TTGameState)info;
            gameBoard.invalidate();

            if(this.state.getPlayerTurn() != playerNum){
                return;
            }
            //don't do anything if the gameBoard hasn't been initialized
            if(gameBoard == null){
                return;
            }
            Log.d("TTHumanPlayer", "action val: " + String.valueOf(state.getActionTextVal()));
            //send the current state to the GameBoard and redraws it
            gameBoard.setTtGameState(this.state);
            gameBoard.invalidate();
            // get current actionInfoTextValue
            actionInfoTextValue = state.getActionTextVal();
            updateDisplay();
        }
    }

    /**
     * updates the display by removing/adding cards, updating the scores and round number
     * setting textViews
     */
    protected void updateDisplay() {
        //Update the text boxes on the score board
        String roundString = "Round "+state.getRoundNum();
        String p0String = p0Name+": "+state.getPlayer0Score();
        String p1String = p1Name+": "+state.getPlayer1Score();
        roundText.setText(roundString);
        p0ScoreText.setText(p0String);
        p1ScoreText.setText(p1String);
        player0Acted = state.getPlayer0Acted();
        String usedName;
        if (player0Acted) {
            usedName = p0Name;
        } else {
            usedName = p1Name;
        }

        //set the wild card textView
        String wildText;
        if (state.getRoundNum() == 9) {
            wildText = "Wild card for this round is the Jack";
        } else if (state.getRoundNum() == 10) {
            wildText = "Wild card for this round is the Queen";
        }
        else if (state.getRoundNum() == 11) {
            wildText = "Wild card for this round is the King";
        }
        else{
            wildText = "Wild card for this round is "+state.getWildCard();
        }
        wildCardText.setText(wildText);

        // updates display based on if the computer has taken a major action, the round ends, or if the human attempts an illegal move
        switch (actionInfoTextValue) {
            case 1:
                actionInfoText.setText(usedName + " has drawn and discarded.");
                break;
            case 2:
                actionInfoText.setText(usedName + " has gone out.  There's one turn left.");
                break;
            case 3:
                player0ScoreIncrease = state.getPlayer0Score() - previousPlayer0Score;
                previousPlayer0Score = state.getPlayer0Score();
                player1ScoreIncrease = state.getPlayer1Score() - previousPlayer1Score;
                previousPlayer1Score = state.getPlayer1Score();
                String increaseScore = p0Name+" Score +"+player0ScoreIncrease+"   "+p1Name+" Score +"+player1ScoreIncrease;
                actionInfoText.setText(increaseScore);
                break;
            case 4:
                actionInfoText.setText("Attempted to draw from an empty draw deck, drew from discard pile instead.");
                break;
            case 5:
                actionInfoText.setText("You cannot draw another card.");
                break;
            case 6:
                actionInfoText.setText("Attempted to draw from an empty discard deck, drew from draw pile instead.");
                break;
            case 7:
                actionInfoText.setText("You cannot discard at this time.");
                break;
            case 8:
                actionInfoText.setText("You have no groups to go out with.");
                break;
            case 9:
                actionInfoText.setText("You cannot go out again.");
                break;
            case 10:
                actionInfoText.setText("You have too many left over cards to go out.");
                break;
            case 11:
                actionInfoText.setText("Impossible to go out at this time.");
                break;
            default:
                // no change
                break;
        }
        // Might have to add invalidate and do something w/ gameBoard
    }

    /**
     *
     * @param activity
     */
    @Override
    public void setAsGui(GameMainActivity activity) {
       // Getting xml elements
        myActivity = activity;
        myActivity.setContentView(R.layout.tt_human_player);
        
        roundText = (TextView) myActivity.findViewById(R.id.roundText);
        p0ScoreText = (TextView) myActivity.findViewById(R.id.yourScoreText);
        p1ScoreText = (TextView) myActivity.findViewById(R.id.opponScoreText);
        wildCardText = (TextView) myActivity.findViewById(R.id.wild_text);
        actionInfoText = (TextView) myActivity.findViewById(R.id.actionInfoText);

        //assign surfaceView
        gameBoard = myActivity.findViewById(R.id.surfaceView);

        //assign buttons
        helpButton = myActivity.findViewById(R.id.helpButton);
        quitButton = myActivity.findViewById(R.id.quitButton);
        restartButton = myActivity.findViewById(R.id.restartButton);
        goOutButton = myActivity.findViewById(R.id.goOutButton);
        discardButton = myActivity.findViewById(R.id.discardButton);
        addGroupButton = myActivity.findViewById(R.id.addGroupButton);
        removeGroupButton = myActivity.findViewById(R.id.removeGroupButton);

        //set onTouch listener
        gameBoard.setOnTouchListener(this);

        // Setting onClick listeners for buttons
        helpButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
        restartButton.setOnClickListener(this);
        goOutButton.setOnClickListener(this);
        discardButton.setOnClickListener(this);
        addGroupButton.setOnClickListener(this);
        removeGroupButton.setOnClickListener(this);

        // if the state is not null, simulate having just received the state so that
        // any state-related processing is done
        if (state != null) {
            receiveInfo(state);
        }
    }

    /**
     * initialize the player names to be displayed on the scoreboard
     */
    @Override
    protected void initAfterReady() {
        this.p0Name = allPlayerNames[0];
        this.p1Name = allPlayerNames[1];
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.helpButton):
                // display a popup window with rules

                // inflate
                LayoutInflater inflater = (LayoutInflater)
                        myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                /**
                 * External Citation
                 * Problem: Didn't know how to create pop up window
                 * Source: https://stackoverflow.com/questions/5944987/how-to-create-a-popup-window-popupwindow-in-android
                 * Solution: used example provided
                 */
                // create popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show popup window
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
                break;
            case (R.id.quitButton):
                // bring up confirmation menu
                myActivity.startActivity(new Intent(myActivity, QuitMenu.class));
                break;
            case (R.id.restartButton):
                // back to game config screen
                myActivity.recreate();
                break;
            case (R.id.goOutButton):
                game.sendAction(new TTGoOutAction(this));
                gameBoard.invalidate();
                break;
            case (R.id.discardButton):
                // Loop through all player cards until reaches clicked card
                //if only one card is clicked, discard
                //if more than one card is clicked, do nothing
                Card discardCard = null;
                boolean tooManyCards = false;
                for (int i = 0; i < state.currentPlayerHand().getSize(); i++) {
                    if (state.currentPlayerHand().getCard(i).getIsClick()) {
                        if(discardCard == null){
                            //the first clicked card was found
                            discardCard = state.currentPlayerHand().getCard(i);
                        }
                        else{
                            //more than one card was clicked
                            tooManyCards = true;
                            break;
                        }
                    }
                }

                //take the action if only one card was found
                if((discardCard != null) && !tooManyCards) {
                    game.sendAction(new TTDiscardAction(this, discardCard));
                }
                else{
                    //deselect all the cards if too many cards were selected
                    for(Card cSelect : state.currentPlayerHand().getHand()){
                        cSelect.setIsClick(false);
                    }
                }
                gameBoard.invalidate();
                break;
            case (R.id.addGroupButton):
                //1:check to make selected cards are not in a group
                //2:place selected cards in an ArrayList<Card> group
                //3:use createGrouping() to add the selected cards to 2D groupings in Hand

                ArrayList<Card> group = new ArrayList<>();
                int numCardsClicked =0;
                for (Card cardInHand : state.currentPlayerHand().getHand()) {
                    // if card is selected, add it to group
                    //if (cardInHand.getIsClick() && !state.isCardInGroup(cardInHand)) {
                    if (cardInHand.getIsClick()) {
                        Log.d("Human Player","adding card to temporary group");
                        group.add(cardInHand);
                        numCardsClicked++;
                    }
                }
                if(numCardsClicked >= 3){ // must be 3 cards
                    Log.d("Human Player","enough cards were clicked to add a group");
                    game.sendAction(new TTAddGroupAction(this, group));
                    gameBoard.invalidate();
                }
                gameBoard.invalidate();
                break;
            case (R.id.removeGroupButton):
                // if card is selected, remove from group
                for (int i = 0; i < state.currentPlayerHand().getSize(); i++) {
                    if (state.currentPlayerHand().getCard(i).getIsClick()) {
                        //the first card found is to be removed from its group
                        Log.d("HumanPlayer","sending remove group action");
                        game.sendAction(new TTRemoveGroupAction(this, state.currentPlayerHand().getCard(i)));
                        break;
                    }
                }
                gameBoard.invalidate();
                break;
            default:
                // do nothing
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int)motionEvent.getX();
        int y = (int)motionEvent.getY();
        int xLowerBound;
        int xUpperBound;
        int yLowerBound;
        int yUpperBound;
        int gridLocation = 0;
        // check if click on draw or discard pile
        if (x < card.getHeight() + 128 && x > 128 && y < card.getWidth() + 74 && y > 74) {
            Log.d("h", "clicked on discard");
            //send a draw from discard action to the local game
            game.sendAction(new TTDrawDiscardAction(this));
        }
        if (x < backCard.getHeight() + 630 && x > 630 && y < backCard.getWidth() + 74 && y > 74) {
            Log.d("H", "clicked on deck");
            //send a draw from deck action to the local game
            game.sendAction(new TTDrawDeckAction(this));
        }
        // search for card based on grid location
        boolean completeBreak = false;
        for (int row = 1; row < 5; row++) {
            for (int col = 0; col < 4; col++) {
                //there is no card displayed in this position so skip it
                if((row == 4) && (col ==0)){
                    continue;
                }

                //the least amount of cards a player should have is 3
                if(gridLocation >= state.currentPlayerHand().getHand().size()){
                    completeBreak = true;
                    break;
                }

                xLowerBound = (int) (col * gameBoard.sectionWidth + gameBoard.padx);
                xUpperBound = xLowerBound + card.getWidth();
                yLowerBound = (int) (row * gameBoard.sectionHeight + gameBoard.pady);
                yUpperBound = yLowerBound + card.getHeight();
                if (x < xUpperBound && x > xLowerBound && y < yUpperBound && y > yLowerBound) {
                    // click card to select it, click again to de-select
                    if (state.currentPlayerHand().getCard(gridLocation).getIsClick()) {
                        state.currentPlayerHand().getCard(gridLocation).setIsClick(false);
                    } else {
                        state.currentPlayerHand().getCard(gridLocation).setIsClick(true);
                    }
                    completeBreak = true;
                }

                //update gridlocation after so we don't get index out of bounds accessing hand
                gridLocation++;

                //account for the last space on the board
                if(gridLocation == 14){
                    completeBreak =true;
                }
            }

            //check in the outer loop as well
            //the least amount of cards a player should have is 3
            if(completeBreak){
                break;
            }
        }

        //redraw the GameBoard
        gameBoard.invalidate();
        return false;
    }
}

