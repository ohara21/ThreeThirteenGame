package edu.up.threethirteengame.TTGame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import androidx.core.widget.TextViewCompat;

import static org.junit.Assert.*;

public class TTComputerPlayerSmartTest {

    @Test
    public void findIndex() {

    }

    @Test
    public void checkForSet() {
        TTGameState ttGameState = new TTGameState();
        TTComputerPlayerSmart ttComp = new TTComputerPlayerSmart("Comp");

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
        //therefore the test may fail every so often
        for(Card a : p0Hand){
            assertNotEquals(null,a);
            assertEquals(1,a.getCardRank());
            testDeck.remove(a);
        }
        assertEquals(3,p0Hand.size());
        assertEquals(45,ttGameState.getDeck().size());

        //now player 0 is ready to draw from the deck and check for a set
        ttGameState.playerDrawDeck();
        System.out.println("**checkForSet() test");
        for(Card c : ttGameState.currentPlayerHand().getHand()){
            System.out.print(" "+c.getCardRank()+c.getCardSuit());
        }
        System.out.print("\n");
        ArrayList<ArrayList<Card>> set = new ArrayList<>();
        set = ttComp.checkForSet(ttGameState.currentPlayerHand());
        assertTrue(!set.isEmpty());

        for(ArrayList<Card> group : set){
            System.out.print("Group found in set: ");
            for(Card c : group){
                System.out.print(" "+c.getCardRank()+c.getCardSuit());
            }
        }
        System.out.print("\n");
        System.out.println("**End of checkForSet()\n\n\n");
    }

    @Test
    public void checkForRun(){
        TTGameState ttGameState = new TTGameState();
        TTComputerPlayerSmart ttComp = new TTComputerPlayerSmart("Comp");

        //we are just testing the hand so we don't care about the integrity of the entire gamestate
        System.out.println("**checkForRun() test");
        ttGameState.currentPlayerHand().getHand().clear();
        Card card1 = new Card(1, 's',5);
        Card card2 = new Card(1, 's', 7);
        Card card3 = new Card(1, 's', 6);
        Card card4 = new Card(1, 'c',2);
        Card card5 = new Card(1, 'd', 10);
        Card card6 = new Card(1, 'h', 3);
        Card card7 = new Card(1, 'h',4);
        Card card8 = new Card(1, 'h', 5);
        Card card9 = new Card(1, 's', 8);
        ttGameState.currentPlayerHand().getHand().add(card1);
        ttGameState.currentPlayerHand().getHand().add(card2);
        ttGameState.currentPlayerHand().getHand().add(card3);
        ttGameState.currentPlayerHand().getHand().add(card4);
        ttGameState.currentPlayerHand().getHand().add(card5);
        ttGameState.currentPlayerHand().getHand().add(card6);
        ttGameState.currentPlayerHand().getHand().add(card7);
        ttGameState.currentPlayerHand().getHand().add(card8);
        ttGameState.currentPlayerHand().getHand().add(card9);

        assertEquals(9,ttGameState.currentPlayerHand().getHand().size());

        System.out.println("current player hand: ");
        for(Card c : ttGameState.currentPlayerHand().getHand()){
            System.out.print(" "+c.getCardRank()+c.getCardSuit());
        }
        System.out.println();

        ArrayList<ArrayList<Card>> run = new ArrayList<>();
        run = ttComp.checkForRun(ttGameState.currentPlayerHand());

        System.out.println("Runs consists of:");
        for(ArrayList<Card> group : run){
            System.out.print("Group contains:");
            for(Card c : group){
                System.out.print(" "+c.getCardRank()+c.getCardSuit());
            }
            System.out.println();
        }

        System.out.println("**End of checkForRun() test\n");
    }

    @Test
    public void checkForSimilar(){
        TTGameState ttGameState = new TTGameState();
        TTComputerPlayerSmart ttComp = new TTComputerPlayerSmart("Comp");

        //we are just testing the hand so we don't care about the integrity of the entire gamestate
        System.out.println("**checkForSimilar() test");
        ttGameState.currentPlayerHand().getHand().clear();
        Card card1 = new Card(1, 's',5);
        Card card2 = new Card(1, 's', 7);
        Card card3 = new Card(1, 's', 6);
        Card card4 = new Card(1, 'c',2);
        Card card5 = new Card(1, 'd', 10);
        Card card6 = new Card(1, 'h', 3);
        Card card7 = new Card(1, 'h',4);
        Card card8 = new Card(1, 'h', 5);
        Card card9 = new Card(1, 'd', 5);
        Card card10 = new Card(1, 'c',5);
        Card card11 = new Card(1, 'c', 6);
        Card card12 = new Card(1, 'c', 7);
        ttGameState.currentPlayerHand().getHand().add(card1);
        ttGameState.currentPlayerHand().getHand().add(card2);
        ttGameState.currentPlayerHand().getHand().add(card3);
        ttGameState.currentPlayerHand().getHand().add(card4);
        ttGameState.currentPlayerHand().getHand().add(card5);
        ttGameState.currentPlayerHand().getHand().add(card6);
        ttGameState.currentPlayerHand().getHand().add(card7);
        ttGameState.currentPlayerHand().getHand().add(card8);
        ttGameState.currentPlayerHand().getHand().add(card9);
        ttGameState.currentPlayerHand().getHand().add(card10);
        ttGameState.currentPlayerHand().getHand().add(card11);
        ttGameState.currentPlayerHand().getHand().add(card12);

        assertEquals(12,ttGameState.currentPlayerHand().getHand().size());

        System.out.println("current player hand: ");
        for(Card c : ttGameState.currentPlayerHand().getHand()){
            System.out.print(" "+c.getCardRank()+c.getCardSuit());
        }
        System.out.println();

        ArrayList<ArrayList<Card>> run = new ArrayList<>();
        run = ttComp.checkForRun(ttGameState.currentPlayerHand());

        System.out.println("Runs consists of:");
        for(ArrayList<Card> group : run){
            for(Card c : group){
                System.out.print(" "+c.getCardRank()+c.getCardSuit());
            }
            if(!group.isEmpty()){
                System.out.println();
            }
        }

        ArrayList<ArrayList<Card>> set = new ArrayList<>();
        set = ttComp.checkForSet(ttGameState.currentPlayerHand());
        assertTrue(!set.isEmpty());

        System.out.println("Sets consists of: ");
        for(ArrayList<Card> group : set){
            for(Card c : group){
                System.out.print(" "+c.getCardRank()+c.getCardSuit());
            }
            if(!group.isEmpty()){
                System.out.println();
            }
        }

        ArrayList<ArrayList<Card>> allGroups = new ArrayList<>();
        allGroups.addAll(run);
        allGroups.addAll(set);
        System.out.println("All Groupings consists of: ");
        for(ArrayList<Card> group : allGroups){
            for(Card c : group){
                System.out.print(" "+c.getCardRank()+c.getCardSuit());
            }
            if(!group.isEmpty()){
                System.out.println();
            }
        }

        ArrayList<Card> similarCards = new ArrayList<>();
        similarCards = ttComp.checkForSimilar(allGroups);
        System.out.println("Similar cards found: ");
        for(Card c : similarCards){
            System.out.print(" "+c.getCardRank()+c.getCardSuit());
        }
        System.out.println();
        System.out.println("**End of checkForSimilar() test\n\n");
    }

    @Test
    public void optimizeHand(){
        TTGameState ttGameState = new TTGameState();
        TTComputerPlayerSmart ttComp = new TTComputerPlayerSmart("Comp");

        //we are just testing the hand so we don't care about the integrity of the entire gamestate
        System.out.println("**optimizeHand() test**");
        ttGameState.currentPlayerHand().getHand().clear();
        Card card1 = new Card(1, 's',5);
        Card card2 = new Card(1, 's', 7);
        Card card3 = new Card(1, 's', 6);
        Card card4 = new Card(1, 'c',2);
        Card card5 = new Card(1, 'd', 10);
        Card card6 = new Card(1, 'h', 3);
        Card card7 = new Card(1, 'h',4);
        Card card8 = new Card(1, 'h', 5);
        Card card9 = new Card(1, 'd', 5);
        Card card10 = new Card(1, 'c',5);
        Card card11 = new Card(1, 'c', 6);
        Card card12 = new Card(1, 'c', 7);
        ttGameState.currentPlayerHand().getHand().add(card1);
        ttGameState.currentPlayerHand().getHand().add(card2);
        ttGameState.currentPlayerHand().getHand().add(card3);
        ttGameState.currentPlayerHand().getHand().add(card4);
        ttGameState.currentPlayerHand().getHand().add(card5);
        ttGameState.currentPlayerHand().getHand().add(card6);
        ttGameState.currentPlayerHand().getHand().add(card7);
        ttGameState.currentPlayerHand().getHand().add(card8);
        ttGameState.currentPlayerHand().getHand().add(card9);
        ttGameState.currentPlayerHand().getHand().add(card10);
        ttGameState.currentPlayerHand().getHand().add(card11);
        ttGameState.currentPlayerHand().getHand().add(card12);

        assertEquals(12,ttGameState.currentPlayerHand().getHand().size());

        System.out.println("current player hand: ");
        for(Card c : ttGameState.currentPlayerHand().getHand()){
            System.out.print(" "+c.getCardRank()+c.getCardSuit());
        }
        System.out.println();

        ArrayList<Card> needArray = ttComp.optimizeHand(ttGameState);



        System.out.println();
        System.out.println("**End of optimizeHand() test**\n\n");
    }

}