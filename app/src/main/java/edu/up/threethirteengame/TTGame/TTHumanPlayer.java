package edu.up.threethirteengame.TTGame;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.PopupWindow;

import edu.up.threethirteengame.R;
import edu.up.threethirteengame.game.GameFramework.GameHumanPlayer;
import edu.up.threethirteengame.game.GameFramework.GameMainActivity;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.IllegalMoveInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.NotYourTurnInfo;

public class TTHumanPlayer extends GameHumanPlayer implements View.OnClickListener, View.OnTouchListener {

    //our game state
    private TTGameState state;

    //our activity
    private Activity myActivity = null;

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
    private TextView yourScoreText;
    private TextView opponScoreText;
    private GameBoard gameBoard;
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

            //don't do anything if the gameBoard hasn't been initialized
            if(gameBoard == null){
                return;
            }

            //send the current state to the GameBoard and redraws it
            gameBoard.setTtGameState(this.state);
            gameBoard.invalidate();
            updateDisplay();
        }
    }

    /**
     * updates the display by removing/adding cards, updating the scores and round number
     * setting textViews
     */
    protected void updateDisplay() {
        //setAsGui(myActivity);
        roundText.setText("Round " + state.getRoundNum());
        yourScoreText.setText("You: " + state.getPlayer0Score());
        opponScoreText.setText("Opponent's score: " + state.getPlayer1Score());
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
        yourScoreText = (TextView) myActivity.findViewById(R.id.yourScoreText);
        opponScoreText = (TextView) myActivity.findViewById(R.id.opponScoreText);

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
                // close program
                System.exit(0);
                break;
            case (R.id.restartButton):
                // back to game config screen
                break;
            case (R.id.goOutButton):
                state.goOut();
                break;
            case (R.id.discardButton):
                // if card is selected, discard it
                break;
            case (R.id.addGroupButton):
                // if card is selected, add it to group
                break;
            case (R.id.removeGroupButton):
                // if card is selected, remove from group
                break;
            default:
                // do nothing
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
