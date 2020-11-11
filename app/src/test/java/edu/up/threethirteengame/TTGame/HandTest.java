package edu.up.threethirteengame.TTGame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class HandTest {

    @Test
    public void addToHand() {
        Hand actualHand = new Hand();
        Card c1 = new Card(1, 'h', 3);
        actualHand.addToHand(c1);

        assertEquals(actualHand.getCard(0), c1);
        assertEquals(actualHand.getSize(), 1);

    }

    @Test
    public void setHand() {
        ArrayList<Card> hand = new ArrayList<>();
        Hand actualHand = new Hand();

        hand.add(new Card(1, 'd', 3));
        hand.add(new Card(1, 'h', 12));
        hand.add(new Card(1, 's', 5));

        actualHand.setHand(hand);

        assertEquals(actualHand.getSize(), 3);
        assertEquals(actualHand.getHand(), hand);
    }

    @Test
    public void getHand() {
        ArrayList<Card> hand = new ArrayList<>();
        Hand actualHand = new Hand();

        hand.add(new Card(1, 'd', 3));
        hand.add(new Card(1, 'h', 12));
        hand.add(new Card(1, 's', 5));

        actualHand.setHand(hand);

        assertEquals(actualHand.getSize(), 3);
        assertEquals(actualHand.getHand(), hand);


    }

    @Test
    public void getCard() {
        Hand actualHand = new Hand();
        ArrayList<Card> expectedHand = new ArrayList<>();
        Card c1 = new Card(1, 'd', 10);
        expectedHand.add(c1);
        actualHand.addToHand(c1);

        assertEquals(actualHand.getHand(), expectedHand);
    }

    @Test
    public void getSize() {
        Hand actualHand = new Hand();
        ArrayList<Card> expectedHand = new ArrayList<>();
        expectedHand.add(new Card(1, 'c', 3));
        expectedHand.add(new Card(1, 'd', 5));
        expectedHand.add(new Card(1, 's', 1));
        expectedHand.add(new Card(1, 'c', 9));
        expectedHand.add(new Card(1, 'h', 11));
        actualHand.setHand(expectedHand);

        assertEquals(actualHand.getSize(), expectedHand.size());

    }

    @Test
    public void getGroupings() {
        Hand hand1 = new Hand();
        ArrayList<Card> group1 = new ArrayList<>();
        hand1.createGrouping(group1);
    }

    @Test
    public void sortByRank() {
        ArrayList<Card> hand = new ArrayList<>();
        ArrayList<Card> sorted = new ArrayList<>();
        Card c1 = new Card(1, 'c', 1);
        Card c2 = new Card(1, 'c', 2);
        Card c3 = new Card(1, 'h', 3);
        Card c4 = new Card(1, 'h',5);
        Card c5 = new Card(1, 'h',8);

        Hand userHand = new Hand();

        hand.add(c1);
        hand.add(c3);
        hand.add(c5);
        hand.add(c4);
        hand.add(c2);
        userHand.setHand(hand);
        hand = userHand.sortByRank(userHand.getHand());

        sorted.add(c1);
        sorted.add(c2);
        sorted.add(c3);
        sorted.add(c4);
        sorted.add(c5);

        assertEquals(sorted, hand);
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

        assertEquals(hand.get(0).getCardSuit(), 'c');
        assertEquals(hand.get(1).getCardSuit(), 'c');
        assertEquals(hand.get(2).getCardSuit(), 'h');
        assertEquals(hand.get(3).getCardSuit(), 'h');
        assertEquals(hand.get(4).getCardSuit(), 'h');

    }

    @Test
    public void checkIfSet() {
        Hand hand = new Hand();
        ArrayList<Card> group1 = new ArrayList<>();
        ArrayList<Card> group2 = new ArrayList<>();
        ArrayList<Card> group3 = new ArrayList<>();
        ArrayList<Card> group4 = new ArrayList<>();

        group2.add(new Card(1, 'd', 7));
        group2.add(new Card(1, 's', 5));
        group2.add(new Card(1, 'c', 1));

        group3.add(new Card(1, 'd', 2));
        group3.add(new Card(1, 'd', 3));
        group3.add(new Card(1, 'd', 1));

        group4.add(new Card(1, 'd', 6));
        group4.add(new Card(1, 's', 6));
        group4.add(new Card(1, 'c', 6));

        assertFalse(hand.checkIfSet(group1));
        assertFalse(hand.checkIfSet(group2));
        assertFalse(hand.checkIfSet(group3));
        assertTrue(hand.checkIfSet(group4));


    }

    @Test
    public void checkIfRun() {
        Hand hand = new Hand();
        ArrayList<Card> group1 = new ArrayList<>();
        ArrayList<Card> group2 = new ArrayList<>();
        ArrayList<Card> group3 = new ArrayList<>();
        ArrayList<Card> group4 = new ArrayList<>();

        group2.add(new Card(1, 'd', 7));
        group2.add(new Card(1, 's', 5));
        group2.add(new Card(1, 'c', 1));

        group3.add(new Card(1, 'd', 1));
        group3.add(new Card(1, 'd', 2));
        group3.add(new Card(1, 'd', 3));

        group4.add(new Card(1, 'd', 6));
        group4.add(new Card(1, 's', 6));
        group4.add(new Card(1, 'c', 6));

        assertFalse(hand.checkIfRun(group1));
        assertFalse(hand.checkIfRun(group2));
        assertTrue(hand.checkIfRun(group3));
        assertFalse(hand.checkIfRun(group4));

    }

    @Test
    public void checkGroup() {
        Hand hand = new Hand();
        ArrayList<Card> group1 = new ArrayList<>();

        group1.add(new Card(1,'d',7));
        group1.add(new Card(1,'d',6));
        group1.add(new Card(1,'d',5));

        int[] check1 = hand.checkGroup(group1);
        int[] actual = {1,1};
        for(int x : actual){
            System.out.print(actual[x] + " ");
        }
        System.out.println();
        for(int y : check1){
            System.out.print(check1[y] + " ");
        }

        assertArrayEquals(check1, actual);


    }


    @Test
    public void createGrouping() {
    }

    @Test
    public void removeGrouping() {
    }
}