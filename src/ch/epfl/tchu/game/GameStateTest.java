package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;
import ch.epfl.tchu.game.GameState;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private final static List<Ticket> ticketList = List.copyOf(ChMap.tickets());



    @Test
    void InitialWithCorrectCardDeckSizeNumber (){
        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        for (Ticket t: ticketList){ ticketBuilder.add(t); }
        SortedBag<Ticket> tickets = ticketBuilder.build();

        GameState gameState = GameState.initial(tickets, new Random());

        assertEquals(102, gameState.cardState().totalSize());
    }


    @Test
    void InitialWithShuffledDifferentDeck (){
        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        for (Ticket t: ticketList){ ticketBuilder.add(t); }
        SortedBag<Ticket> tickets = ticketBuilder.build();

        GameState gameState1 = GameState.initial(tickets, new Random());
        GameState gameState2 = GameState.initial(tickets, new Random());

        System.out.println(gameState1.topCard().color());
        System.out.println(gameState2.topCard().color());

        assertTrue(gameState1.topCard().equals(gameState1.topCard()));
        assertTrue(!gameState1.topCard().equals(gameState2.topCard()));
    }
}