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
        Trail longestTrail = new Trail();
        longestTrail = longestTrail.longest(List.of(r1));
        assertEquals(t1, longestTrail);
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