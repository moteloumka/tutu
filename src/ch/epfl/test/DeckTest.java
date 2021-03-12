package ch.epfl.test;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Deck;
import org.junit.jupiter.api.Test;

import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
class DeckTest {

    Card card1 = Card.BLACK;
    Card card2 = Card.RED;
    Card card3 = Card.GREEN;


    @Test
    void of() {
        SortedBag.Builder <Card> builder = new SortedBag.Builder<>();
        builder.add(card1);
        builder.add(10,card2);
        builder.add(10,card3);
        Deck.of(builder.build(),new Random());
    }

    @Test
    void size() {
        SortedBag.Builder <Card> builder = new SortedBag.Builder<>();
        builder.add(card1);
        builder.add(10,card2);
        builder.add(10,card3);
        Deck<Card> ofCards = Deck.of(builder.build(),new Random());
        assertEquals(21,ofCards.size());
    }

    @Test
    void isEmpty() {
        SortedBag.Builder <Card> builder = new SortedBag.Builder<>();
        builder.add(card1);
        builder.add(10,card2);
        builder.add(10,card3);
        Deck<Card> ofCards = Deck.of(builder.build(),new Random());
        assertFalse(ofCards.isEmpty());
        SortedBag.Builder <Card> builder2 = new SortedBag.Builder<>();
        Deck<Card> ofCards2 = Deck.of(builder2.build(),new Random());
        assertTrue(ofCards2.isEmpty());
    }

    @Test
    void topCard() {
        SortedBag.Builder <Card> builder0 = new SortedBag.Builder<>();
        builder0.add(card1);
        Deck<Card> ofCards = Deck.of(builder0.build(),new Random());
        assertEquals(card1,ofCards.topCard());

        SortedBag.Builder <Card> builder = new SortedBag.Builder<>();
        builder.add(card1);
        builder.add(card2);

        Deck<Card> ofCards1 = Deck.of(builder.build(),new Random());
    }

    @Test
    void withoutTopCard() {
        SortedBag.Builder <Card> builder = new SortedBag.Builder<>();
        builder.add(card1);
        builder.add(card2);

    }

    @Test
    void topCards() {
    }

    @Test
    void withoutTopCards() {
    }
}