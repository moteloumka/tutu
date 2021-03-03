package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
class RouteTest {

    @Test
    void id() {
        Route r1 = ChMap.routes().get(0);
        Route r2 = ChMap.routes().get(10);
        assertEquals("AT1_STG_1",r1.id());
        assertEquals("BEL_LUG_2",r2.id());
    }

    @Test
    void station1() {
        Station station1 =  new Station(0,"TEST1");
        Station station2 =  new Station(0,"TEST2");

        String id1 = station1.name();
        String id2 = station2.name();

        Route route = new Route(id1,station1,station2,6, Route.Level.OVERGROUND,Color.BLACK);

        assertEquals(station1,route.station1());
    }

    @Test
    void station2() {
        Station station1 =  new Station(0,"TEST1");
        Station station2 =  new Station(0,"TEST2");

        String id1 = station1.name();
        String id2 = station2.name();

        Route route = new Route(id1,station1,station2,6, Route.Level.OVERGROUND,Color.BLACK);

        assertEquals(station2,route.station2());
    }

    @Test
    void length() {
        Station station1 =  new Station(0,"TEST1");
        Station station2 =  new Station(0,"TEST2");

        String id1 = station1.name();
        String id2 = station2.name();

        int length = 4;

        Route route = new Route(id1,station1,station2,length, Route.Level.OVERGROUND,Color.BLACK);

        assertEquals(length,route.length());
    }

    @Test
    void level() {

        Station station1 =  new Station(0,"TEST1");
        Station station2 =  new Station(0,"TEST2");

        String id1 = station1.name();
        String id2 = station2.name();

        int length = 4;

        Route route = new Route(id1,station1,station2,length, Route.Level.OVERGROUND,Color.BLACK);
        Route route1 = new Route(id1,station1,station2,length, Route.Level.UNDERGROUND,Color.BLACK);

        assertEquals(Route.Level.OVERGROUND,route.level());
        assertEquals(Route.Level.UNDERGROUND,route1.level());
    }

    @Test
    void color() {
        Station station1 =  new Station(0,"TEST1");
        Station station2 =  new Station(0,"TEST2");

        String id1 = station1.name();
        String id2 = station2.name();

        int length = 4;

        Route route = new Route(id1,station1,station2,length, Route.Level.OVERGROUND,Color.BLACK);
        Route route1 = new Route(id1,station1,station2,length, Route.Level.UNDERGROUND,Color.BLUE);

        assertEquals(Color.BLACK,route.color());
        assertEquals(Color.BLUE,route1.color());
    }

    @Test
    void stations() {
        Station station1 =  new Station(0,"TEST1");
        Station station2 =  new Station(1,"TEST2");

        String id1 = station1.name();
        String id2 = station2.name();

        int length = 4;

        Route route = new Route(id1,station1,station2,length, Route.Level.OVERGROUND,Color.BLACK);
        Route route1 = new Route(id1,station1,station2,length, Route.Level.UNDERGROUND,Color.BLUE);

        List stations = List.of(station1,station2);

        assertEquals(stations,route.stations());
    }

    @Test
    void stationOpposite() {
        Station station1 =  new Station(0,"TEST1");
        Station station2 =  new Station(1,"TEST2");

        String id1 = station1.name();
        String id2 = station2.name();

        int length = 4;

        Route route1 = new Route(id1,station1,station2,length, Route.Level.OVERGROUND,Color.BLACK);
        Route route2 = new Route(id1,station2,station1,length, Route.Level.UNDERGROUND,Color.BLUE);

        assertEquals(station2,route1.stationOpposite(station1));
        assertEquals(station1,route1.stationOpposite(station2));
    }

    @Test
    void possibleClaimCards() {
        Station station1 =  new Station(0,"TEST1");
        Station station2 =  new Station(1,"TEST2");

        String id1 = station1.name();
        String id2 = station2.name();

        int length = 4;

        Route route1 = new Route(id1,station1,station2,length, Route.Level.OVERGROUND,Color.BLACK);
        Route route2 = new Route(id1,station2,station1,length, Route.Level.UNDERGROUND,null);

        route1.possibleClaimCards();
        route2.possibleClaimCards();
    }

    @Test
    void additionalClaimCardsCount() {

        Station station1 =  new Station(0,"TEST1");
        Station station2 =  new Station(1,"TEST2");

        String id1 = station1.name();
        String id2 = station2.name();

        int length = 4;

        Route route1 = new Route(id1,station1,station2,length, Route.Level.OVERGROUND,Color.BLACK);
        Route route2 = new Route(id1,station2,station1,length, Route.Level.UNDERGROUND,null);

        SortedBag.Builder<Card> cardsOwnedBuilder = new SortedBag.Builder<>();
        cardsOwnedBuilder.add(2,Card.ORANGE);
        cardsOwnedBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> cardsOwned = cardsOwnedBuilder.build();

        SortedBag.Builder<Card> cardsDrawnBuilder = new SortedBag.Builder<>();
        cardsDrawnBuilder.add(Card.ORANGE);
        cardsDrawnBuilder.add(Card.RED);
        cardsDrawnBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> carsDrawn = cardsDrawnBuilder.build();

        int intReturn = route2.additionalClaimCardsCount(cardsOwned,carsDrawn);
        assertEquals(1,intReturn);

    }

    @Test
    void claimPoints() {
    }
}