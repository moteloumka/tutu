package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Color;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Station;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfoTest {

    @Test
    void claimedRoute() {

        String id1 = "AAA";

        Station station1 = new Station(1, "imposter");
        Station station2 = new Station(2, "is sus");
        Card card1 = Card.of(Color.GREEN);
        Card card2 = Card.of(Color.BLACK);
        Card card3 = Card.of(Color.BLACK);
        Card card4 = Card.of(Color.YELLOW);
        SortedBag.Builder<Card> sbBuilder = new SortedBag.Builder<>();
        sbBuilder.add(card1);
        sbBuilder.add(card2);
        sbBuilder.add(card3);
        sbBuilder.add(card4);
        Info info = new Info("Someone");

        SortedBag sortedBag = sbBuilder.build();

        Route r1 = new Route(id1, station1, station2, 2, Route.Level.OVERGROUND, null);
        System.out.println();

        assertEquals("Someone a pris possession de la route imposter -" +
                " is sus au moyen de 2 noires, 1 verte et 1 jaune.",
                info.claimedRoute(r1, sortedBag));



    }

    @Test
    void attemptsTunnelClaim() {
    }

    @Test
    void drewAdditionalCards() {
    }
}