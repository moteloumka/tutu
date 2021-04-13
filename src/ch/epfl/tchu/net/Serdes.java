package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;

public final class Serdes {
   private Serdes(){};

    private final static String basicSeparator = ",";
    private final static String betterSeparator = ";";
    private final static String ogSeparator = ":";

   //à finir les deux premiers!!
   public static Serde<Integer> INTEGER;
   public static Serde<String> STRING;
   public static Serde<PlayerId> PLAYER_ID =
           Serde.oneOf(PlayerId.ALL);
   public static Serde<Player.TurnKind> TURN_KIND =
           Serde.oneOf(Player.TurnKind.ALL);
   public static Serde<Card> CARD =
           Serde.oneOf(Card.ALL);
   public static Serde<Route> ROUTE =
           Serde.oneOf(ChMap.routes());
   public static Serde<Ticket> TICKET =
           Serde.oneOf(ChMap.tickets());
   //-----------Lists-------------------

   public static Serde<List<String>> LIST_STRINGS =
           Serde.listOf(STRING, basicSeparator);
   public static Serde<List<Card>> LIST_CARDS =
           Serde.listOf(CARD, basicSeparator);
   public static Serde<List<Route>> LIST_ROUTES =
           Serde.listOf(ROUTE, basicSeparator);
   public static Serde<SortedBag<Card>> BAG_CARDS =
           Serde.bagOf(CARD, basicSeparator);
   public static Serde<SortedBag<Ticket>> BAG_TICKETS =
           Serde.bagOf(TICKET, basicSeparator);
   public static Serde<List<SortedBag<Card>>> LIST_BAGS_TICKETS =
           Serde.listOf(BAG_CARDS, betterSeparator);

   //--------COMPOSED STUFF--------------

    //this stuff isn't finished (not at all)

   public static Serde<PublicCardState> PUB_CARD_STATE;
   public static Serde<PublicPlayerState> PUB_PLAYER_CARD_STATE;
   public static Serde<PlayerState> PLAYER_STATE;
   public static Serde<PublicGameState> PUB_GAME_STATE;
}

