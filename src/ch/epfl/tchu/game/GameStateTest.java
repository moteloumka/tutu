package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;
import ch.epfl.tchu.game.GameState;

import javax.management.relation.RelationNotFoundException;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private final static List<Ticket> ticketList = List.copyOf(ChMap.tickets());
    private final static SortedBag<Ticket> ticketBag1 =
            new SortedBag.Builder<Ticket>().
                    add(ticketList.get(0))
                    .build();



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

        //test doesn't work 1 out of 8 times when they randomly are the same color
        assertTrue(gameState1.topCard().equals(gameState1.topCard()));
        assertTrue(!gameState1.topCard().equals(gameState2.topCard()));
    }

    @Test
    void withInitiallyChosenTicketsDoesNotWorkWithNonEmptyTicketList() {
        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ticketList.get(0));
        SortedBag<Ticket> tickets1 = ticketBuilder.build();

        ticketBuilder.add(ticketList.get(1));
        SortedBag<Ticket> tickets2 = ticketBuilder.build();

        GameState gameState1 = GameState.initial(tickets1, new Random());
        GameState gameState2 = gameState1.withChosenAdditionalTickets(tickets2,tickets1);

        System.out.println(tickets1.size());
        System.out.println(gameState1.currentPlayerState().tickets().size());
        System.out.println(gameState2.currentPlayerState().tickets().size());

        assertThrows(IllegalArgumentException.class, () -> {
            gameState2.withInitiallyChosenTickets(gameState2.currentPlayerId(), tickets1);
        });
    }


    @Test
    void NikMadeMeWriteThisAlsoIfTestPassesMeansWithDrawnFaceUpCardMethodWorks (){
        GameState gameState1 = GameState.initial(ticketBag1, new Random());

        for (int i = 0; i < 5; ++i){
            GameState gameState2 = gameState1.withDrawnFaceUpCard(i);

            //for each iteration checks that the player has the type of color card that he just drew
            assertTrue(gameState2.currentPlayerState().cards()
                    .contains(gameState1.cardState().faceUpCard(i)));

            //checks the size of the deck actually changed accordingly
            assertEquals(gameState1.cardState().deckSize()-1, gameState2.cardState().deckSize());
        }

    }


    @Test
    void SussySussyWithBlindlyDrawnCard (){
        GameState gameState1 = GameState.initial(ticketBag1, new Random());

        for (int i = 0; i < 10; ++i) {
            GameState gameState2 = gameState1.withBlindlyDrawnCard();

            //for each iteration checks that the player has the type of color card that he just drew BLINDLY
            assertTrue(gameState2.currentPlayerState().cards()
                    .contains(gameState1.topCard()));
        }
    }


    @Test
    void ChecksForNextTurnMethodsAndPlayerMapWorks(){
        GameState gameState1 = GameState.initial(ticketBag1, new Random());
        GameState gameState2 = gameState1.withDrawnFaceUpCard(3);
        GameState gameState3 = gameState2.forNextTurn();

        assertEquals(gameState2.currentPlayerState().cards().size(),
                gameState3.playerState(gameState3.lastPlayer()).cards().size());
    }
}