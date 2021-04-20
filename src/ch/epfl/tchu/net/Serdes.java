package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public final class Serdes {
   private Serdes(){};

    public final static char basicSeparator = ',';
    public final static char betterSeparator = ';';
    public final static char ogSeparator = ':';

   public final static Serde<Integer> INTEGER =
           new Serde<Integer>() {
              @Override
              public String serialize(Integer obj) {
                 return String.valueOf(obj);
              }

              @Override
              public Integer deserialize(String cipher) {
                 return Integer.valueOf(cipher);
              }
           };

   public final static Serde<String> STRING =
           new Serde<String>() {
              @Override
              public String serialize(String obj) {
                 return Base64.getEncoder().encodeToString(obj.getBytes(StandardCharsets.UTF_8));
              }

              @Override
              public String deserialize(String cipher) {
                  return new String(Base64.getDecoder()
                          .decode(cipher.getBytes(StandardCharsets.UTF_8)),
                          StandardCharsets.UTF_8);
              }
           };

   public final static Serde<PlayerId> PLAYER_ID =
           Serde.oneOf(PlayerId.ALL);
   public final static Serde<Player.TurnKind> TURN_KIND =
           Serde.oneOf(Player.TurnKind.ALL);
   public final static Serde<Card> CARD =
           Serde.oneOf(Card.ALL);
   public final static Serde<Route> ROUTE =
           Serde.oneOf(ChMap.routes());
   public final static Serde<Ticket> TICKET =
           Serde.oneOf(ChMap.tickets());
   //-----------Lists-------------------

   public final static Serde<List<String>> LIST_STRINGS =
           Serde.listOf(STRING, basicSeparator);
   public final static Serde<List<Card>> LIST_CARDS =
           Serde.listOf(CARD, basicSeparator);
   public final static Serde<List<Route>> LIST_ROUTES =
           Serde.listOf(ROUTE, basicSeparator);
   public final static Serde<SortedBag<Card>> BAG_CARDS =
           Serde.bagOf(CARD, basicSeparator);
   public final static Serde<SortedBag<Ticket>> BAG_TICKETS =
           Serde.bagOf(TICKET, basicSeparator);
   public final static Serde<List<SortedBag<Card>>> LIST_BAGS_CARDS =
           Serde.listOf(BAG_CARDS, betterSeparator);

   //--------COMPOSED STUFF--------------

    //java sux so we gotta define all this manually instead of creating a cheeky method(((
    //every time we're defining a unique way to encode/decode an object
    
   public final static Serde<PublicCardState> PUB_CARD_STATE =
           new Serde<PublicCardState>() {
              @Override
              public String serialize(PublicCardState obj) {
                 List<String> strings = List.of(LIST_CARDS.serialize(obj.faceUpCards())
                         , INTEGER.serialize(obj.deckSize())
                         , INTEGER.serialize(obj.discardsSize()));
                 return String.join(String.valueOf(betterSeparator),strings);
              }

              @Override
              public PublicCardState deserialize(String cipher) {
                 String[] strings = strTab(betterSeparator,cipher);
                 List<Card> cards = LIST_CARDS.deserialize(strings[0]);
                 int deckSize = INTEGER.deserialize(strings[1]);
                 int discardSize = INTEGER.deserialize(strings[2]);
                 return new PublicCardState(cards,deckSize,discardSize);
              }
           };

   public final static Serde<PublicPlayerState> PUB_PLAYER_STATE =
           new Serde<PublicPlayerState>() {
              @Override
              public String serialize(PublicPlayerState obj) {
                 List<String> strings = List.of(INTEGER.serialize(obj.ticketCount())
                         ,INTEGER.serialize(obj.cardCount())
                         ,LIST_ROUTES.serialize(obj.routes()));
                 return String.join(String.valueOf(betterSeparator),strings);
              }

              @Override
              public PublicPlayerState deserialize(String cipher) {
                 String[] strings = strTab(betterSeparator,cipher);
                 int ticketCount = INTEGER.deserialize(strings[0]);
                 int cardCount   = INTEGER.deserialize(strings[1]);
                 List<Route> routes = LIST_ROUTES.deserialize(strings[2]);
                 return new PublicPlayerState(ticketCount,cardCount,routes);
              }
           };
   public final static Serde<PlayerState> PLAYER_STATE =
           new Serde<PlayerState>() {
              @Override
              public String serialize(PlayerState obj) {
                 List<String> strings = List.of(BAG_TICKETS.serialize(obj.tickets())
                         ,BAG_CARDS.serialize(obj.cards())
                         ,LIST_ROUTES.serialize(obj.routes()));
                 return String.join(String.valueOf(betterSeparator),strings);
              }

              @Override
              public PlayerState deserialize(String cipher) {
                 String[] strings = strTab(betterSeparator,cipher);
                 SortedBag<Ticket> tickets = BAG_TICKETS.deserialize(strings[0]);
                 SortedBag<Card> cards = BAG_CARDS.deserialize(strings[1]);
                 List<Route> routes = LIST_ROUTES.deserialize(strings[2]);
                 return new PlayerState(tickets,cards,routes);
              }
           };

   public final static Serde<PublicGameState> PUB_GAME_STATE =
           new Serde<PublicGameState>() {
              @Override
              public String serialize(PublicGameState obj) {
                 List<String> strings = List.of( INTEGER.serialize(obj.ticketsCount())
                         ,PUB_CARD_STATE.serialize(obj.cardState())
                         ,PLAYER_ID.serialize(obj.currentPlayerId())
                         ,PUB_PLAYER_STATE.serialize(obj.playerState(PlayerId.PLAYER_1))
                         ,PUB_PLAYER_STATE.serialize(obj.playerState(PlayerId.PLAYER_2))
                         ,PLAYER_ID.serialize(obj.lastPlayer()) );
                 return String.join(String.valueOf(ogSeparator),strings);
              }

              @Override
              public PublicGameState deserialize(String cipher) {
                 String[] strings = strTab(ogSeparator,cipher);
                 int ticketsCount = INTEGER.deserialize(strings[0]);
                 PublicCardState pubCardState = PUB_CARD_STATE.deserialize(strings[1]);
                 PlayerId currentPlayer = PLAYER_ID.deserialize(strings[2]);

                 PublicPlayerState pubPlayer1 = PUB_PLAYER_STATE.deserialize(strings[3]);
                 PublicPlayerState pubPlayer2 = PUB_PLAYER_STATE.deserialize(strings[4]);
                 Map<PlayerId,PublicPlayerState> playerStateMap = new EnumMap<>(PlayerId.class);
                 playerStateMap.put(PlayerId.PLAYER_1,pubPlayer1);
                 playerStateMap.put(PlayerId.PLAYER_2,pubPlayer2);

                 PlayerId lastPlayer = PLAYER_ID.deserialize(strings[5]);
                 return new PublicGameState(ticketsCount
                         ,pubCardState
                         ,currentPlayer
                         ,playerStateMap
                         ,lastPlayer);
              }
           };

   private static String[] strTab(char sep,String str){
     return str.split(Pattern.quote(String.valueOf(sep)),-1);
   }
}


