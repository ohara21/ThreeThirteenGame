package edu.up.threethirteengame.TTGame;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class HandTest {

    @Test
    public void addToHand() {
    }

    @Test
    public void setHand() {
    }

    @Test
    public void getHand() {
    }

    @Test
    public void getCard() {
    }

    @Test
    public void getSize() {
    }

    @Test
    public void getGroupings() {
    }

    @Test
    public void sortByRank() {
    }

    @Test
    public void sortBySuit() {
        ArrayList<Card> hand = new ArrayList<>();
        ArrayList<Card> sorted = new ArrayList<>();
        Card c1 = new Card(1, 'c', 3);
        Card c2 = new Card(1, 'c', 4);
        Card c3 = new Card(1, 'h', 3);
        Card c4 = new Card(1, 'h',4);
        Card c5 = new Card(1, 'h',5);

        Hand userHand = new Hand();

        hand.add(c1);
        hand.add(c3);
        hand.add(c5);
        hand.add(c2);
        hand.add(c4);
        userHand.setHand(hand);
        hand = userHand.sortBySuit(userHand.getHand());
        for(Card c : hand){
            System.out.println(c.getCardSuit() + Integer.toString(c.getCardRank()));
        }

        sorted.add(c1);
        sorted.add(c2);
        sorted.add(c3);
        sorted.add(c4);
        sorted.add(c5);

        assertEquals(sorted, hand);
    }

    @Test
    public void checkIfSet() {
    }

    @Test
    public void checkIfRun() {
    }

    @Test
    public void checkHand() {
    }

    @Test
    public void createGrouping() {
    }

    @Test
    public void removeGrouping() {
    }
}