package edu.up.threethirteengame.TTGame;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @description TTGameStateTest tests the game logic of our Three Thirteen game
 * @author Nick Ohara
 * @version 11/8/2020
 */
public class TTGameStateTest {

    @Test
    public void isRoundOver() throws Exception{
        TTGameState ttGameState = new TTGameState();
        assertFalse(ttGameState.isRoundOver());
    }

    @Test
    public void updateGameState() throws Exception {
        TTGameState ttGameState = new TTGameState();
    }

    @Test
    public void updateScores() throws Exception{
        TTGameState ttGameState = new TTGameState();
    }

    @Test
    public void playerDrawDeck() throws Exception{
        //create new GameState and check if piles have the right number of cards
        TTGameState ttGameState = new TTGameState();
        assertEquals(3, ttGameState.getPlayer0Hand().getHand().size());
        assertEquals(3,ttGameState.getPlayer1Hand().getHand().size());
        assertEquals(45,ttGameState.getDeck().size());
        assertEquals(1,ttGameState.getDiscardPile().size());

        ArrayList<Card> p0Hand = ttGameState.getPlayer0Hand().getHand();
        Card deckTop = ttGameState.getDeck().get(ttGameState.getDeck().size()-1);
        int topRank = deckTop.getCardRank();
        int topSuit = deckTop.getCardSuit();

        //Player0 should be able to draw from the deck
        assertTrue(ttGameState.playerDrawDeck());

        //check pile sizes
        assertEquals(4,p0Hand.size());
        assertEquals(44, ttGameState.getDeck().size());

        //the card removed from the deck should be the card placed in player 0's hand
        assertEquals(topRank,p0Hand.get(p0Hand.size()-1).getCardRank());
        assertEquals(topSuit,p0Hand.get(p0Hand.size()-1).getCardSuit());
        assertEquals(p0Hand.get(p0Hand.size()-1), deckTop);

        //Player 1 tries to draw but shouldn't be able to until player 0 discards
        assertFalse(ttGameState.playerDrawDeck());
        assertFalse(ttGameState.playerDrawDiscard());
    }

    @Test
    public void playerDrawDiscard() throws Exception{
        //create new GameState and check if piles have the right number of cards
        TTGameState ttGameState = new TTGameState();
        assertEquals(3, ttGameState.getPlayer0Hand().getHand().size());
        assertEquals(3,ttGameState.getPlayer1Hand().getHand().size());
        assertEquals(45,ttGameState.getDeck().size());
        assertEquals(1,ttGameState.getDiscardPile().size());

        ArrayList<Card> p0Hand = ttGameState.getPlayer0Hand().getHand();
        Card discardTop = ttGameState.getDiscardPile().get(ttGameState.getDiscardPile().size()-1);
        int topRank = discardTop.getCardRank();
        int topSuit = discardTop.getCardSuit();

        //Player0 should be able to draw from the deck
        assertTrue(ttGameState.playerDrawDiscard());

        //check pile sizes
        assertEquals(4,p0Hand.size());
        assertEquals(0, ttGameState.getDiscardPile().size());

        //the card removed from the deck should be the card placed in player 0's hand
        assertEquals(topRank,p0Hand.get(p0Hand.size()-1).getCardRank());
        assertEquals(topSuit,p0Hand.get(p0Hand.size()-1).getCardSuit());
        assertEquals(p0Hand.get(p0Hand.size()-1), discardTop);

    }

    @Test
    public void discardCard() throws Exception{
        //create new GameState
        TTGameState ttGameState = new TTGameState();

        //player will draw from discard pile
        ArrayList<Card> p0Hand = ttGameState.getPlayer0Hand().getHand();
        Card discardTop = ttGameState.getDiscardPile().get(ttGameState.getDiscardPile().size()-1);
        int topRank = discardTop.getCardRank();
        int topSuit = discardTop.getCardSuit();

        //Player0 should be able to draw from the discard
        assertTrue(ttGameState.playerDrawDiscard());

        //check pile sizes
        assertEquals(4,p0Hand.size());
        assertEquals(0, ttGameState.getDiscardPile().size());

        //the card removed from the deck should be the card placed in player 0's hand
        assertEquals(topRank,p0Hand.get(p0Hand.size()-1).getCardRank());
        assertEquals(topSuit,p0Hand.get(p0Hand.size()-1).getCardSuit());
        assertEquals(p0Hand.get(p0Hand.size()-1), discardTop);

        //Player0 should be able to discard, remove the bottom card in hand
        Card discardBot = p0Hand.get(0);
        int discardRank = discardBot.getCardRank();
        int discardSuit = discardBot.getCardSuit();
        assertTrue(ttGameState.playerDiscard(p0Hand.get(0)));
        ttGameState.discardCard(p0Hand.get(0));

        //check pile sizes
        assertEquals(3,p0Hand.size());
        assertEquals(1, ttGameState.getDiscardPile().size());

        //the card removed from bottom of player 0's hand should be on the top of discard pile
        assertEquals(discardRank,ttGameState.getDiscardPile().get(ttGameState.getDiscardPile().size()-1).getCardRank());
        assertEquals(discardSuit,ttGameState.getDiscardPile().get(ttGameState.getDiscardPile().size()-1).getCardSuit());
        assertEquals(discardBot,ttGameState.getDiscardPile().get(ttGameState.getDiscardPile().size()-1));
    }

    @Test
    public void canPlayerGoOut() throws Exception{
        //create new GameState
        TTGameState ttGameState = new TTGameState();

        //need to populate player 0's hand with cards that can be placed in a group
        //this would not normally happen in a game
        ArrayList<Card> p0Hand = ttGameState.getPlayer0Hand().getHand();
        ArrayList<Card> testDeck = ttGameState.getDeck();
        for(int i=0; i<3; i++){
            //remove each card in player 0's hand and place back in deck
            testDeck.add(p0Hand.get(0));
            p0Hand.remove(0);
        }
        assertTrue(p0Hand.isEmpty());

        //look for three Aces in deck and place in player 0's hand
        int count = 0;
        //do it this way so no ConcurrentModificationError
        for(Card c : testDeck){
            if((c.getCardRank() == 1) && (count<3)){
                p0Hand.add(c);
                count++;
            }
        }
        //unlikely that we won't find 3 Aces in deck, but possible
        for(Card a : p0Hand){
            assertNotEquals(null,a);
            assertEquals(1,a.getCardRank());
            testDeck.remove(a);
        }
        assertEquals(3,p0Hand.size());
        assertEquals(45,ttGameState.getDeck().size());

        //now player 0 has 3 aces at the "start" of the game
        ArrayList<Card> aceGroup = new ArrayList<>(p0Hand);
        assertNotEquals(null,aceGroup);
        ttGameState.getPlayer0Hand().createGrouping(aceGroup);
        assertTrue(ttGameState.playerDrawDeck());

        //player 4 should have 4 cards
        assertEquals(4,p0Hand.size());

        //player 0  should have one grouping in the last index of 2D Groupings
        assertFalse(ttGameState.getPlayer0Hand().getGroupings().get(ttGameState.MAX_NUM_GROUPS-1).isEmpty());
        assertEquals(3,ttGameState.getPlayer0Hand().getGroupings().get(ttGameState.MAX_NUM_GROUPS-1).size());
        assertEquals(aceGroup,ttGameState.getPlayer0Hand().getGroupings().get(ttGameState.MAX_NUM_GROUPS-1));

        //last card in player 0 hand should not be in a group
        assertFalse(ttGameState.isCardInGroup(p0Hand.get(3)));
        assertTrue(ttGameState.isCardInGroup(p0Hand.get(0)));
        assertTrue(ttGameState.isCardInGroup(p0Hand.get(1)));
        assertTrue(ttGameState.isCardInGroup(p0Hand.get(2)));

        //should be able to discard the last card
        assertTrue(ttGameState.playerDiscard(p0Hand.get(3)));

        //the group of aces should be a valid set and invalid run
        assertFalse(ttGameState.getPlayer0Hand().checkIfRun(ttGameState.getPlayer0Hand().getGroupings().get(ttGameState.MAX_NUM_GROUPS-1)));
        assertTrue(ttGameState.getPlayer0Hand().checkIfSet(ttGameState.getPlayer0Hand().getGroupings().get(ttGameState.MAX_NUM_GROUPS-1)));

        //player should be able to go out
        assertTrue(ttGameState.canPlayerGoOut());
    }

    @Test
    public void goOut() throws Exception{
        //create new GameState
        TTGameState ttGameState = new TTGameState();

        //need to populate player 0's hand with cards that can be placed in a group
        //this would not normally happen in a game
        ArrayList<Card> p0Hand = ttGameState.getPlayer0Hand().getHand();
        ArrayList<Card> testDeck = ttGameState.getDeck();
        for(int i=0; i<3; i++){
            //remove each card in player 0's hand and place back in deck
            testDeck.add(p0Hand.get(0));
            p0Hand.remove(0);
        }

        //look for three Aces in deck and place in player 0's hand
        int count = 0;
        //do it this way so no ConcurrentModificationError
        for(Card c : testDeck){
            if((c.getCardRank() == 1) && (count<3)){
                p0Hand.add(c);
                count++;
            }
        }
        //unlikely that we won't find 3 Aces in deck, but possible
        for(Card a : p0Hand){
            testDeck.remove(a);
        }

        //now player 0 has 3 aces at the "start" of the game and draws card
        ArrayList<Card> aceGroup = new ArrayList<>(p0Hand);
        ttGameState.getPlayer0Hand().createGrouping(aceGroup);
        assertTrue(ttGameState.playerDrawDiscard());

        assertTrue(ttGameState.canPlayerGoOut());
        Card goOutCard = p0Hand.get(3);
        ttGameState.goOut();
        assertEquals(goOutCard,ttGameState.getDiscardPile().get(ttGameState.getDiscardPile().size()-1));
        assertEquals(3,p0Hand.size());
        assertTrue(ttGameState.isPlayer0GoneOut());
        assertFalse(ttGameState.isPlayer1GoneOut());
        assertEquals(1,ttGameState.getPlayerTurn());


    }

    @Test
    public void isCardInGroup() {
        TTGameState ttGameState = new TTGameState();
    }

    @Test
    public void isCardInHand() {
        TTGameState ttGameState = new TTGameState();
    }
}