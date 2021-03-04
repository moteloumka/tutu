package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrailTest {

    Station station1 =  new Station(1,"TEST1");
    Station station2 =  new Station(2,"TEST2");
    Station station3 =  new Station(3,"TEST3");
    Station station4 =  new Station(4,"TEST4");
    Station station5 =  new Station(5,"TEST5");

    String id1 = "AAA";
    String id2 = "BBB";
    String id3 = "CCC";
    String id4 = "DDD";

    Route route1 = new Route(id1,station1,station2,1, Route.Level.OVERGROUND,Color.BLACK);
    Route route2 = new Route(id2,station2,station3,6, Route.Level.UNDERGROUND,null);
    Route route3 = new Route(id3,station3,station4,1, Route.Level.UNDERGROUND,null);
    Route route4 = new Route(id4,station4,station5,6, Route.Level.UNDERGROUND,null);


    Trail trail1 = new Trail(route1);
    Trail trail2 = new Trail(route2);
    Trail trail3 = new Trail(route3);
    Trail trail4 = new Trail(route4);

    @Test
    void longestTrailForSingleRouteTrail() {

        List<Route> routes = List.of(route1,route2,route3,route4);
        System.out.println(Trail.longest(routes).getRoutes().get(1));
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

}