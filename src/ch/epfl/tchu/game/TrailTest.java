package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrailTest {

    Station station1 =  new Station(1,"TEST1");
    Station station2 =  new Station(2,"TEST2");
    Station station3 =  new Station(3,"TEST3");
    Station station4 =  new Station(4,"TEST4");

    String id1 = "AAA";
    String id2 = "BBB";
    String id3 = "CCC";

    Route route1 = new Route(id1,station1,station2,6, Route.Level.OVERGROUND,Color.BLACK);
    Route route2 = new Route(id2,station2,station3,1, Route.Level.UNDERGROUND,null);
    Route route3 = new Route(id3,station3,station4,1, Route.Level.UNDERGROUND,null);

    Trail trail1 = new Trail(route1);
    Trail trail2 = new Trail(route2);
    Trail trail3 = new Trail(route3);

    @Test
    void longestTrailForSingleRouteTrail() {

        List<Route> routes = List.of(route1,route2,route3);
        System.out.println(Trail.longest(routes).getRoutes().get(0));
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

    @Test
    void checkRouteAdder(){
        Trail added = Trail.addRoute(trail1,route2);
        System.out.println(added);
        added = Trail.addRoute(added,route3);
        System.out.println(added);
    }

    @Test
    void checkMerge(){

        Trail merged1 = Trail.merge(trail1,trail2);
        System.out.println(merged1);
        Trail merged2= Trail.merge(merged1,trail3);
        System.out.println(merged2);

    }
}