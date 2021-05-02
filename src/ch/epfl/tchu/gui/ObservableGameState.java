package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.management.loading.ClassLoaderRepository;
import java.util.*;
import java.util.stream.Collectors;

public final class ObservableGameState {
    private PublicGameState pubGameState;
    private PlayerState playerState;
    private final PlayerId playerId;

    //PublicGameState proprieties
    private final IntegerProperty cardDeckCapacity = new SimpleIntegerProperty(0);
    private final IntegerProperty ticketDeckCapacity = new SimpleIntegerProperty(0);
    private final List<ObjectProperty<Card>> visibleCards = new ArrayList<>();
    //I decided to go with a map, better for manipulations
    private final Map<Route,ObjectProperty<PlayerId>> routesOwners = new HashMap<>();

    //PublicPlayerState proprieties (of each player)
    private final Map<PlayerId,IntegerProperty> ticketsInHandCount = new EnumMap<>(PlayerId.class);
    private final Map<PlayerId,IntegerProperty> cardsInHandCount = new EnumMap<>(PlayerId.class);
    private final Map<PlayerId,IntegerProperty> wagonCount = new EnumMap<>(PlayerId.class);
    private final Map<PlayerId,IntegerProperty> constructionPoints = new EnumMap<>(PlayerId.class);

    //PlayerState properties
    private final ObservableList<Ticket> ticketsInHand = FXCollections.observableArrayList();
    private final List<ObjectProperty<Ticket>> ticketsInHandProp =
            new ArrayList<>(List.of(new SimpleObjectProperty<>(null)));
    private final Map<Card,IntegerProperty> numberOfCardInHand = new EnumMap<>(Card.class);
    private final Map<Route,BooleanProperty> canGetRoadMap = new HashMap<>();

    private final List<List<Route>> neighbors;


    /**
     * creates a new instance of ObservableGameState that has all the properties set to 0/null/false by default
     * this instance represents the information that will then be transferred to the screen seen by "playerId"
     * @param playerId the player who's information will be stocked in this instance
     */
    public ObservableGameState(PlayerId playerId){
        this.playerId = playerId;
        this.pubGameState = null;
        this.playerState = null;

        for (int ignored : Constants.FACE_UP_CARD_SLOTS)
            visibleCards.add(new SimpleObjectProperty<>(null));

        List<List<Route>> neighborBuilder = new ArrayList<>();
        for (Route route: ChMap.routes()){
            routesOwners.put(route,new SimpleObjectProperty<>(null));
            canGetRoadMap.put(route,new SimpleBooleanProperty(false));
            //creating the list of neighbor roads
            for (Route route1 :ChMap.routes()){
                if(route != route1
                && route.station1() == route1.station1()
                && route.station2() == route1.station2()
                && neighborBuilder.stream()
                .noneMatch(routes -> routes.contains(route) && routes.contains(route1)))
                    neighborBuilder.add(List.of(route,route1));
            }
        }
        neighbors = List.copyOf(neighborBuilder);

        for (PlayerId pId : PlayerId.ALL){
            ticketsInHandCount.put(pId, new SimpleIntegerProperty(0));
            cardsInHandCount.put(pId, new SimpleIntegerProperty(0));
            wagonCount.put(pId, new SimpleIntegerProperty(0));
            constructionPoints.put(pId, new SimpleIntegerProperty(0));
        }

        for (Card card: Card.ALL)
            numberOfCardInHand.put(card,new SimpleIntegerProperty(0));


    }

    /**
     * update the information after a change in the game
     * @param pubGS the mew public game state
     * @param playerState the new player state
     */
    public void setState(PublicGameState pubGS, PlayerState playerState){
        List<Route> routes = ChMap.routes();
        this.pubGameState = pubGS;
        this.playerState = playerState;

        cardDeckCapacity.set( 100 * pubGameState.cardState().deckSize()/Constants.TOTAL_CARDS_COUNT);
        ticketDeckCapacity.set( 100 * pubGameState.ticketsCount()/ChMap.tickets().size());

        for(int slot : Constants.FACE_UP_CARD_SLOTS){
            Card cardInSlot = pubGameState.cardState().faceUpCards().get(slot);
            ObjectProperty<Card> cardObjectProperty = visibleCards.get(slot);
            cardObjectProperty.set(cardInSlot);
        }

        for (PlayerId pID : PlayerId.ALL) {
            PublicPlayerState pubPS = pubGS.playerState(pID);

            ticketsInHandCount.get(pID).set(pubPS.ticketCount());
            cardsInHandCount.get(pID).set(pubPS.cardCount());
            wagonCount.get(pID).set(pubPS.carCount());
            constructionPoints.get(pID).set(pubPS.claimPoints());

            for (Route route : routes)
                if (pubPS.routes().contains(route))
                    routesOwners.get(route).set(pID);

            for (Card card : Card.ALL)
                numberOfCardInHand.get(card).set(playerState.cards().countOf(card));
        }
        //we dont throw away tickets until the end of the game -> we can only add them (?)
        for (Ticket ticket : playerState.tickets()){
            if (ticketsInHand.stream().noneMatch(t -> t.equals(ticket)))
                ticketsInHand.add(ticket);
        }

        for (Route route : routes){

            //verification if a neighbor route exists and if it's already taken
            //in which case, it won't be possible to get the original route in one's possession
            boolean neighborIsOwned = false;

            List<List<Route>> possibleOwned = neighbors.stream()
                    .filter(rts -> rts.contains(route)).collect(Collectors.toList());
            if (!possibleOwned.isEmpty()){
                //there's supposed to only be one
                List<Route> pair = possibleOwned.get(0);
                int index = pair.indexOf(route) == 0 ? 1 : 0;
                neighborIsOwned = owner(pair.get(index)).get() != null;
            }

            //updating all possible routes to claim by this player
            canGetRoadMap.get(route).set(
                    pubGS.currentPlayerId() == this.playerId
                            && owner(route).get() == null
                            && !neighborIsOwned
                            && playerState.canClaimRoute(route)
            );
        }
    }

    /**
     * @return percentage : view on (property) integer of the card deck fullness
     */
    public ReadOnlyIntegerProperty cardDeckCapacityProperty() {
        return cardDeckCapacity;
    }

    /**
     * @return percentage : view on (property) integer of the ticket deck fullness
     */
    public ReadOnlyIntegerProperty ticketDeckCapacityProperty() {
        return ticketDeckCapacity;
    }


    /**
     * @return (property) card on the visible slot
     * @param slot the slot
     */
    public ReadOnlyObjectProperty<Card> getVisibleCardProperty(int slot){
        Preconditions.checkArgument(Constants.FACE_UP_CARD_SLOTS.contains(slot)
                ," int slot has to be contained in FACE_UP_CARD_SLOTS");
        return visibleCards.get(slot);
    }

    /**
     * @return view on (property) PlayerId that owns the road
     * @param route the route in question
     */
    public ReadOnlyObjectProperty<PlayerId> owner(Route route){
        return routesOwners.get(route);
    }

    /**
     * @return view on (property) number of tickets in their hand
     * @param playerId the player in question
     */
    public ReadOnlyIntegerProperty getTicketsInHandCount(PlayerId playerId) {
        return ticketsInHandCount.get(playerId);
    }

    /**
     * @return view on the (property) amount of cards this player has in its hands
     */
    public ReadOnlyIntegerProperty getCardsInHandCount(PlayerId playerId) {
        return cardsInHandCount.get(playerId);
    }

    /**
     * @return view on the (property) amount of wagons this player has left
     * @param playerId the player in question
     */
    public ReadOnlyIntegerProperty getWagonCount(PlayerId playerId) {
        return wagonCount.get(playerId);
    }

    /**
     * @return view on  the (property) amount of points this player has got for constructing roads
     * (these are the only publicly available points before the end of the game)
     * @param playerId the plyer in question
     */
    public ReadOnlyIntegerProperty getConstructionPoints(PlayerId playerId) {
        return constructionPoints.get(playerId);
    }


    public ObservableList<Ticket> getTicketsInHand(){
        return this.ticketsInHand;
    }

    /**
     * @param card the card
     * @return view on the (property) amount of this card in this.playerId hand
     */
    public ReadOnlyIntegerProperty getNumberOfCardInHand(Card card){
        return numberOfCardInHand.get(card);
    }


    /**
     * @param route a route
     * @return view in boolean property, indicating if a route can be claimed by this.playerId
     */
    public ReadOnlyBooleanProperty claimable(Route route){
        return canGetRoadMap.get(route);
    }

    //same methods, new wrapping

    /**
     * indicates if this.playerId can draw cards at this moment
     * @return true if they can
     */
    public boolean canDrawCards(){
        return pubGameState.canDrawCards();
    }
    /**
     * indicates if this.playerId can draw tickets at this moment
     * @return true if they can
     */
    public boolean canDrawTickets(){
        return pubGameState.canDrawTickets();
    }

    /**
     * called when this.playerId wants to claim a route
     * @param route the route in question
     * @return the possible combinations of cards
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route){
        return playerState.possibleClaimCards(route);
    }

}
