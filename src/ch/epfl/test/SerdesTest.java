package ch.epfl.test;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.Serde;
import ch.epfl.tchu.net.Serdes;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SerdesTest {

    @Test
    void intEncodesCorrectly(){
        Serde<Integer> intSerde = Serdes.INTEGER;
        for(Integer i=0; i<1000;++i){
            assertEquals(String.valueOf(i),intSerde.serialize(i));
        }
    }

    @Test
    void intDecodesCorrectly(){
        Serde<Integer> intSerde = Serdes.INTEGER;
        for(Integer i=0; i<1000;++i){
            assertEquals(i,intSerde.deserialize(String.valueOf(i)));
        }
    }

    @Test
    void stringEncodesCorrectly(){
        Serde<String> stringSerde = Serdes.STRING;
        String test = "imposter do be lookin sus";
        String cipher = Base64.getEncoder().encodeToString(test.getBytes(StandardCharsets.UTF_8));
        assertEquals( cipher, stringSerde.serialize(test));
    }

    @Test
    void stringDecodesCorrectly(){
        Serde<String> stringSerde = Serdes.STRING;
        List <String> cipherList = List.of("aW1wb3N0ZXI=", "aXM=", "c3Vz");
        assertEquals("imposter", stringSerde.deserialize(cipherList.get(0)));
        assertEquals("is", stringSerde.deserialize(cipherList.get(1)));
        assertEquals("sus", stringSerde.deserialize(cipherList.get(2)));
    }

    @Test
    void stringEncodesAndDecodesCorrectly(){
        Serde<String> stringSerde = Serdes.STRING;
        String test = "amog us amog us amog us amog us";
        assertEquals(test , stringSerde.deserialize(stringSerde.serialize(test)));
    }

    @Test
    void emptyStringEncodesAndDecodesCorrectly(){
        Serde<String> stringSerde = Serdes.STRING;
        String emptyString = "";
        assertEquals(emptyString, stringSerde.deserialize(stringSerde.serialize(emptyString)));
    }

    @Test
    void playerIdEncodesCorrectly(){
        Serde<PlayerId> playerId = Serdes.PLAYER_ID;
        for(PlayerId id : PlayerId.ALL){
            assertEquals(String.valueOf(id.ordinal()),playerId.serialize(id));
        }
        assertEquals("",playerId.serialize(null));
    }

    @Test
    void playerIdDecodesCorrectly(){
        Serde<PlayerId> playerId = Serdes.PLAYER_ID;
        for(PlayerId id : PlayerId.ALL)
            assertEquals(id,playerId.deserialize(String.valueOf(id.ordinal())));
        assertNull(playerId.deserialize(""));
    }

    @Test
    void turnKindEncodesCorrectly(){
        Serde<Player.TurnKind> turnSerde = Serdes.TURN_KIND;
        for(Player.TurnKind turnKind : Player.TurnKind.ALL)
            assertEquals(String.valueOf(turnKind.ordinal()),turnSerde.serialize(turnKind));
    }

    @Test
    void turnKindDecodesCorrectly() {
        Serde<Player.TurnKind> turnSerde = Serdes.TURN_KIND;
        for(Player.TurnKind turnKind : Player.TurnKind.ALL)
            assertEquals(turnKind,turnSerde.deserialize(String.valueOf(turnKind.ordinal())));
    }

    @Test
    void routeEncodesCorrectly() {
        Serde<Route> routeSerde = Serdes.ROUTE;
        List<Route> routes = ChMap.routes();
        for(Route route : routes)
            assertEquals(String.valueOf(routes.indexOf(route)),routeSerde.serialize(route) );
    }

    @Test
    void routeDecodesCorrectly() {
        Serde<Route> routeSerde = Serdes.ROUTE;
        List<Route> routes = ChMap.routes();
        for (Route route : routes)
            assertEquals(route, routeSerde.deserialize(String.valueOf(routes.indexOf(route))));
    }


    @Test
    void listOfCardsEncodesCorrectly(){
        Serde<List<Card>> cadListSerde = Serdes.LIST_CARDS;
        Serde<Card> ticketSerde = Serdes.CARD;
        List<Card> tickets = List.of(Card.BLUE,Card.GREEN,Card.RED,Card.BLACK,Card.BLACK,Card.LOCOMOTIVE);
        StringBuilder sb = new StringBuilder();
        for(Card ticket : tickets){
            sb.append(ticketSerde.serialize(ticket));
            if(tickets.indexOf(ticket)<tickets.size()-1)
                sb.append(Serdes.basicSeparator);
        }
        assertEquals(sb.toString(),cadListSerde.serialize(tickets));
        assertEquals("",cadListSerde.serialize(List.of()));
    }

    @Test
    void listOfCardsDecodesCorrectly(){
        Serde<List<Card>> cadListSerde = Serdes.LIST_CARDS;
        Serde<Card> cardSerde = Serdes.CARD;
        List<Card> cards = List.of(Card.RED,Card.BLUE, Card.BLUE, Card.ORANGE, Card.LOCOMOTIVE, Card.WHITE);
        String name = cadListSerde.serialize(cards);
        List<Card> newCards = cadListSerde.deserialize(name);
        for (int i=0 ; i<cards.size();++i){
            assertEquals(cards.get(i),newCards.get(i));
        }
    }

    @Test
    void sortedBagOfCardsEncodesCorrectly(){
        Serde<SortedBag<Card>> sbCardsSerde = Serdes.BAG_CARDS;
        Serde<Card> cardSerde = Serdes.CARD;
        SortedBag<Card> sbCards = Constants.ALL_CARDS;
        StringBuilder sb = new StringBuilder();
        for (int i=0 ; i<sbCards.size()-1;++i)
            sb
                    .append(cardSerde.serialize(sbCards.get(i)))
                    .append(Serdes.basicSeparator);
        sb.append(cardSerde.serialize(sbCards.get(sbCards.size()-1)));
        assertEquals(sb.toString(),sbCardsSerde.serialize(sbCards));
    }

    @Test
    void sortedBagOfCardsDecodesCorrectly(){
        Serde<SortedBag<Card>> sbCardsSerde = Serdes.BAG_CARDS;
        SortedBag<Card> sbCards = Constants.ALL_CARDS;
        String cipher = sbCardsSerde.serialize(sbCards);
        SortedBag<Card> newSbCards = sbCardsSerde.deserialize(cipher);
        for(int i=0; i<sbCards.size();++i){
            assertEquals(sbCards.get(i),newSbCards.get(i));
        }
    }

}