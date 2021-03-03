package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrailTest {

    @Test
    void longestTrailForSingleRouteTrail() {
        //from Austria to St-Gall, length 4
        Route r1 = ChMap.routes().get(0);
        Trail t1 = new Trail(r1.length(), r1.station1(), r1.station2(),
                List.of(r1.station1(),r1.station2()), List.of(r1));
        //Trail longestTrail = new Trail();
        //Trail longestTrail = Trail.longest(List.of(r1));
        //assertEquals(t1, longestTrail);

        Station station1 =  new Station(1,"TEST1");
        Station station2 =  new Station(2,"TEST2");
        Station station3 =  new Station(3,"TEST3");
        Station station4 =  new Station(4,"TEST4");

        String id1 = "AAA";
        String id2 = "BBB";
        String id3 = "CCC";

        Route route1 = new Route(id1,station1,station2,1, Route.Level.OVERGROUND,Color.BLACK);
        Route route2 = new Route(id2,station2,station3,2, Route.Level.UNDERGROUND,null);
        Route route3 = new Route(id3,station2,station3,2, Route.Level.UNDERGROUND,null);

        List<Route> routes = List.of(route1,route2,route3);
        Trail.longest(routes);
    }

    @Test
    void length() {
    }

    @Test
    void station1() {
    }

    @Test
    void station2() {
    }

    @Test
    void singleRouteTestToString() {
    }
}