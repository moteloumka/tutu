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
    }
    private final static Route getRoute(String id) {
        Preconditions.checkArgument(routeIDs.contains(id),
                "given id doesn't belong to the route list");
        for (Route route : routeFull) {
            if (id.equals(route.id())) {
                return route;
            }
        }
        return null;
    }//used to get a route from the ID


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

        PlayerState pps = new PlayerState(tickets, cards, routes);

        assertEquals(5, pps.ticketPoints());
    }

     @Test
     void finalPointsWithOneTicket (){
         SortedBag.Builder<Ticket>  ticketBuilder = new SortedBag.Builder<>();
         ticketBuilder.add(ticketFull.get(0)); //Basel - Berne (5)
         SortedBag<Ticket> tickets = ticketBuilder.build();

         SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();
         cardBuilder.add(2, Card.of(Color.ORANGE));
         cardBuilder.add(3, Card.of(Color.GREEN));

         SortedBag<Card> cards = cardBuilder.build();

         List<Route> routes = List.of(
                 getRoute("BAL_OLT_1"), //length 2
                 getRoute("LUC_OLT_1"), //length 3
                 getRoute("BER_LUC_1"), //length 4
                 getRoute("GEN_LAU_1")  //length 4
         );

         PlayerState pps = new PlayerState(tickets, cards, routes);

         assertEquals(25, pps.finalPoints());
   }

   @Test
    void ticketPointsWithMultipleTickets() {
       SortedBag.Builder<Ticket>  ticketBuilder = new SortedBag.Builder<>();
       ticketBuilder.add(ticketFull.get(0)); //Basel - Berne (5)
       ticketBuilder.add(ticketFull.get(1)); //Basel - Brigue (10)
       ticketBuilder.add(ticketFull.get(2)); //Basel - St-Gall (8)
       SortedBag<Ticket> tickets = ticketBuilder.build();

       SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();
       cardBuilder.add(2, Card.of(Color.ORANGE));
       cardBuilder.add(3, Card.of(Color.GREEN));

       SortedBag<Card> cards = cardBuilder.build();

       List<Route> routes = List.of(
               getRoute("BAL_OLT_1"),
               getRoute("LUC_OLT_1"),
               getRoute("BER_LUC_1"),
               getRoute("OLT_ZUR_1"),
               getRoute("STG_ZUR_1"),
               getRoute("BER_INT_1")
       );

       PlayerState pps = new PlayerState(tickets, cards, routes);

       assertEquals(3, pps.ticketPoints());
   }

   @Test
    void finalPointWithMultipleTickets (){
       SortedBag.Builder<Ticket>  ticketBuilder = new SortedBag.Builder<>();
       ticketBuilder.add(ticketFull.get(0)); //Basel - Berne (5)
       ticketBuilder.add(ticketFull.get(1)); //Basel - Brigue (10)
       ticketBuilder.add(ticketFull.get(2)); //Basel - St-Gall (8)
       SortedBag<Ticket> tickets = ticketBuilder.build();

       SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();
       cardBuilder.add(2, Card.of(Color.ORANGE));
       cardBuilder.add(3, Card.of(Color.GREEN));

       SortedBag<Card> cards = cardBuilder.build();

       List<Route> routes = List.of(
               getRoute("BAL_OLT_1"), //length 2
               getRoute("LUC_OLT_1"), //length 3
               getRoute("BER_LUC_1"), //length 4
               getRoute("OLT_ZUR_1"), //length 3
               getRoute("STG_ZUR_1"), //length 4
               getRoute("BER_INT_1")  //length 3
       );

       PlayerState pps = new PlayerState(tickets, cards, routes);

       assertEquals(31, pps.finalPoints());
   }

  @Test
  void ticketPointWithNothing(){
        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        SortedBag<Ticket> tickets = ticketBuilder.build();

      SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();
      SortedBag<Card> cards = cardBuilder.build();

      List<Route> routes = List.of();

        PlayerState pps = new PlayerState(tickets, cards, routes);
        assertEquals(0, pps.ticketPoints());
  }

  @Test
    void initialWithWrongNumberOfCard(){
      SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();
      cardBuilder.add(3, Card.of(Color.BLACK));
      SortedBag<Card> cards = cardBuilder.build();

      assertThrows(IllegalArgumentException.class, () -> {
          PlayerState.initial(cards);
      });

  }


  @Test
    void canClaimRouteWithNotEnoughCars () {
      SortedBag.Builder<Ticket>  ticketBuilder = new SortedBag.Builder<>();
      SortedBag<Ticket> tickets = ticketBuilder.build();

      SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();
      SortedBag<Card> cards = cardBuilder.build();

      List<Route> routes = List.copyOf(routeFull);

      PlayerState pps = new PlayerState(tickets, cards, routes);

      assertFalse(pps.canClaimRoute(routeFull.get(0)));
  }


  @Test
  void canClaimRouteWithEnoughCars () {
      SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
      ticketBuilder.add(ticketFull.get(0));
      SortedBag<Ticket> tickets = ticketBuilder.build();

        SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();
        cardBuilder.add(4, Card.of(Color.ORANGE));
        SortedBag<Card> cards = cardBuilder.build();

        PlayerState pps = PlayerState.initial(cards);
        pps.withAddedTickets(tickets);

      assertTrue(pps.canClaimRoute(routeFull.get(0)));
  }



}