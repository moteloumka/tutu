package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
/**
 *  @author Nikolay (314355)
 *  @author Gullien (316143)
 */

public interface ActionHandlers {
    /**
     * the only method of this interface is called when the player gets new tickets
     */
    @FunctionalInterface
     interface DrawTicketsHandler{
        /**
         * tells the program what to do when a ticket is being drawn
         */
        void onDrawTickets();
    }

    /**
     * the only method of this interface is called when the player gets new cards
     */
    @FunctionalInterface
     interface DrawCardHandler{
        /**
         * tells the program what to do when a visible card placed on slot "slot" is being taken
         * @param slot the placement of the card
         */
        void onDrawCard(int slot);

    }

    /**
     * the only method of this interface is called when the player tries getting a new route
     */
    @FunctionalInterface
     interface ClaimRouteHandler{
        /**
         * tells the program what to do when the "route" is being taken with "cards"
         * @param route the route that's being taken
         * @param cards the cards that are being used to do it
         */
        void onClaimRoute(Route route, SortedBag<Card> cards);
    }

    /**
     * this functional interface is called upon when a choice of tickets by a player has to be communicated to the program
     */
    @FunctionalInterface
     interface ChooseTicketsHandler{
        /**
         * handles the decision the player has made by choosing "tickets"
         * @param tickets the set of tickets the player wants to chose
         */
        void onChooseTickets(SortedBag<Ticket> tickets);
    }

    /**
     * this functional interface is called upon when a choice of cards by a player has to be communicated to the program
     */
    @FunctionalInterface
     interface ChooseCardsHandler{
        /**
         * handles the decision the player has made by choosing "cards"
         * @param cards the set of cards the player wants to chose
         */
        void onChooseCards(SortedBag<Card> cards);
    }

    @FunctionalInterface
    interface CloseLoadingScreen{
        void onCloseLoadingScreen();
    }
}
