package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

public interface ActionHandlers {
    /**
     * the only method of this interface is called when the player gets new tickets
     */
    @FunctionalInterface
    public interface DrawTicketsHandler{
        void onDrawTickets();
    }
    /**
     * the only method of this interface is called when the player gets new cards
     */
    @FunctionalInterface
    public interface DrawCardHandler{
        void onDrawCard(int slot);

    }
    /**
     * the only method of this interface is called when the player tries getting a new route
     */
    @FunctionalInterface
    public interface ClaimRouteHandler{
        void onClaimRoute(Route route, SortedBag<Card> cards);
    }
    @FunctionalInterface
    public interface ChooseTicketsHandler{
        void onChooseTickets(SortedBag<Ticket> tickets);
    }
    @FunctionalInterface
    public interface ChooseCardsHandler{
        void onChooseCards(SortedBag<Card> cards);
    }
}
