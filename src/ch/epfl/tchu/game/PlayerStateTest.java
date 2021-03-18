package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {

    //copies everything from ChMap
    private final static List<Station> stationFull = List.copyOf(ChMap.stations());
    private final static List<Route> routeFull = List.copyOf(ChMap.routes());
    private final static List<String> routeIDs = getRoutesIDs();
    private final static List<Ticket> ticketFull = List.copyOf(ChMap.tickets());

    //methods
    private final static List<String> getRoutesIDs() {
        List<String> routesIDs = new ArrayList<>();
        for (Route route : routeFull) {
            routesIDs.add(route.id());
        }
        return routesIDs;
    } //used to get a route from the ID
    private final static Route getRoute(String id) {
        Preconditions.checkArgument(routeIDs.contains(id),
                "given id doesn't belong to the route list");
        for (Route route : routeFull) {
            if (id.equals(route.id())) {
                return route;
            }
        }
        return null;
    }


    @Test
    void ticketPointsWithOneTicket() {
        SortedBag.Builder<Ticket>  ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ticketFull.get(0)); //Basel - Berne (5)
        SortedBag<Ticket> tickets = ticketBuilder.build();

        SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();
        cardBuilder.add(2, Card.of(Color.ORANGE));
        cardBuilder.add(3, Card.of(Color.GREEN));

        SortedBag<Card> cards = cardBuilder.build();

        List<Route> routes = List.of(
                getRoute("BAL_OLT_1"),
                getRoute("LUC_OLT_1"),
                getRoute("BER_LUC_1")
        );

        PlayerState pps1 = new PlayerState(tickets, cards, routes);

        System.out.println(pps1.ticketPoints());

        assertEquals(5, pps1.ticketPoints());

    }

}