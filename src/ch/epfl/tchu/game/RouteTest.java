package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

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

        Route route = new Route(id1,station1,station2)
        Route r1 = ChMap.routes().get(0);
        Route r2 = ChMap.routes().get(10);

    }

    @Test
    void station2() {
    }

    @Test
    void length() {
    }

    @Test
    void level() {
    }

    @Test
    void color() {
    }

    @Test
    void stations() {
    }

    @Test
    void stationOpposite() {
    }

    @Test
    void possibleClaimCards() {
    }

    @Test
    void additionalClaimCardsCount() {
    }

    @Test
    void claimPoints() {
    }
}