package edu.up.threethirteengame.TTGame;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.up.threethirteengame.R;
import edu.up.threethirteengame.game.GameFramework.GameHumanPlayer;
import edu.up.threethirteengame.game.GameFramework.GameMainActivity;
import edu.up.threethirteengame.game.GameFramework.animation.AnimationSurface;
import edu.up.threethirteengame.game.GameFramework.animation.Animator;
import edu.up.threethirteengame.game.GameFramework.infoMessage.GameInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.IllegalMoveInfo;
import edu.up.threethirteengame.game.GameFramework.infoMessage.NotYourTurnInfo;

public class TTHumanPlayer extends GameHumanPlayer{

    //our game state
    private TTGameState state;

    //our activity
    private Activity myActivity;

    //our buttons
    private Button helpButton;
    private Button quitButton;
    private Button restartButton;
    private Button goOutButton;
    private Button discardButton;
    private Button selectCardsButton;
    private Button removeGroupButton;
    private Button addGroupButton;

    //text views
    private TextView roundText;
    private TextView yourScoreText;
    private TextView opponScoreText;
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
        if (!(info instanceof TTGameState)){
            return;
        }
        else{
            //this is the correct info message and this state needs to be updated
            this.state = (TTGameState)info;
            updateDisplay();
        }
    }

    /**
     * updates the display by removing/adding cards, updating the scores and round number
     */
    protected void updateDisplay() {
        //setAsGui(myActivity);
        roundText.setText("Round " + state.getRoundNum());
        yourScoreText.setText("You: " + state.getPlayer0Score());
        opponScoreText.setText("Opponent's score: " + state.getPlayer1Score());
    }

    /**
     *
     * @param activity
     */
    @Override
    public void setAsGui(GameMainActivity activity) {
        myActivity.setContentView(R.layout.activity_main);
        roundText = (TextView) myActivity.findViewById(R.id.roundText);
        yourScoreText = (TextView) myActivity.findViewById(R.id.yourScoreText);
        opponScoreText = (TextView) myActivity.findViewById(R.id.opponScoreText);
    }

}
