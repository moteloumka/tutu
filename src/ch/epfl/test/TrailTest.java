package ch.epfl.test;

import ch.epfl.tchu.game.Color;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Station;
import ch.epfl.tchu.game.Trail;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrailTest {

    Station station1 =  new Station(1,"Station1");
    Station station2 =  new Station(2,"Station2");
    Station station3 =  new Station(3,"Station3");
    Station station4 =  new Station(4,"Station4");
    Station station5 =  new Station(5,"Station5");
    //Stations created for unconnected route
    Station station69 = new Station(6, "Station69");
    Station station420 = new Station(7, "Station420");

    String id1 = "AAA";
    String id2 = "BBB";
    String id3 = "CCC";
    String id4 = "DDD";
    String id5 = "EEE";
    String id6 = "FFF";

    Route route1 = new Route(id1,station1,station2,1, Route.Level.OVERGROUND, Color.BLACK);
    Route route2 = new Route(id2,station2,station3,6, Route.Level.UNDERGROUND,null);
    Route route3 = new Route(id3,station3,station4,1, Route.Level.UNDERGROUND,null);
    Route route4 = new Route(id4,station4,station5,6, Route.Level.UNDERGROUND,null);
    Route route2Reversed = new Route(id5, station3, station2, 6, Route.Level.UNDERGROUND, null);
    Route route5 = new Route(id6, station69, station420, 3, Route.Level.UNDERGROUND, null);


    //Trail trail1 = new Trail(route1);

    /**
    @Test
    void longestTrailForSingleRouteTrail() {
        assertEquals(trail1, Trail.longest(List.of(route1)));
    }
     */

    @Test
    void longestTrailForMultipleDoubledRouteTrail() {
        assertEquals(Trail.longest(List.of(route1, route2, route3, route4)),
              Trail.longest(List.of(route1, route1,  route2, route2, route3, route4)));
    }

    @Test
    void longestTrailForReversedTrail() {
        Trail trail1 = Trail.longest(List.of(route1, route2, route3, route4));
        Trail trail2 = Trail.longest(List.of(route1,route2,route2Reversed, route3, route4));
        System.out.println(trail1);
        System.out.println(trail2);
        assertNotEquals(trail1, trail2);
    }

    @Test
    void longestTrailForUnconnectedTrail(){
        assertEquals(Trail.longest(List.of(route1,route2,route3,route4)),
                Trail.longest(List.of(route1, route2, route3, route4, route5)));
    }


    @Test
    void length() {
        assertEquals(14, Trail.longest(List.of(route1,route2,route3,route4)).length());
    }

    @Test
    void emptyLength() {
        assertEquals( 0, Trail.longest(List.of()).length());
    }

    @Test
    void station1() {
        assertEquals(station1, Trail.longest(List.of(route1)).station1());
    }

    @Test
    void station2() {
        assertEquals(station420, Trail.longest(List.of(route5)).station2());
    }

    @Test
    void singleRouteTestToString() {
        assertEquals("Station1 - Station2 (1)", Trail.longest(List.of(route1)).toString());
    }


    /**
    @Test
    void checkRouteAdder(){
        Trail added = Trail.addRoute(trail1,route2);
        System.out.println(added);
        added = Trail.addRoute(added,route3);
        System.out.println(added);
    }
    */
}