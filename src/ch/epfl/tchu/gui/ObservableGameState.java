package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import javafx.beans.property.*;
import javafx.beans.value.ObservableObjectValue;

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
    private final List<ObjectProperty<Ticket>> ticketsInHand =
            new ArrayList<>(List.of(new SimpleObjectProperty<>(null)));
    private final Map<Card,IntegerProperty> numberOfCardInHand = new EnumMap<>(Card.class);
    private final Map<Route,BooleanProperty> canGetRoadMap = new HashMap<>();


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

        for (Route route: ChMap.routes()){
            routesOwners.put(route,new SimpleObjectProperty<>(null));
            canGetRoadMap.put(route,new SimpleBooleanProperty(false));
        }

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

            //we dont throw away tickets until the end of the game -> we can only add them (?)
            for (Ticket ticket : playerState.tickets()) {
                if (ticketsInHand.stream().map(ObservableObjectValue::get).noneMatch(t -> t == ticket))
                    ticketsInHand.add(new SimpleObjectProperty<>(ticket));
            }
            for (Card card : Card.ALL)
                numberOfCardInHand.get(card).set(playerState.cards().countOf(card));
        }

        for (Route route : routes){
            //verification if a neighbor route exists and if it's already taken
            //in which case, it won't be possible to get the original route in one's possession
            boolean neighborIsOwned = false;
            for (Route rte : routes)
                if ( routesOwners.get(rte) != null
                        && rte.station1()==route.station1()
                        && rte.station2()==route.station2()
                        && rte != route)
                    neighborIsOwned = true;

            //updating all possible routes to claim by this player
            canGetRoadMap.get(route).set(
                    pubGS.currentPlayerId() == this.playerId
                            && routesOwners.get(route) == null
                            && neighborIsOwned
                            && playerState.canClaimRoute(route));
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
     * @return list of views on (properties) cards on the visible slots : number of the slot -> index in the list
     */
    public List<ReadOnlyObjectProperty<Card>> getVisibleCards() {
        return visibleCards.stream().map(o->(ReadOnlyObjectProperty<Card>)o).collect(Collectors.toList());
    }

    /**
     * @return map of routes linked -> view on (property) PlayerId that owns the road
     * null if road isn't owned by anyone
     */
    public Map< Route ,ReadOnlyObjectProperty<PlayerId>> getRoutesOwners() {
        Map<Route,ReadOnlyObjectProperty<PlayerId>> javaCouldMakeThisEasier = new HashMap<>();
        routesOwners.forEach(javaCouldMakeThisEasier::put);
        return javaCouldMakeThisEasier;
    }

    /**
     * @return map of player IDs linked -> view on (property) number of tickets in their hand
     */
    public Map<PlayerId, ReadOnlyIntegerProperty> getTicketsInHandCount() {
        Map<PlayerId,ReadOnlyIntegerProperty> answer = new EnumMap<>(PlayerId.class);
        ticketsInHandCount.forEach(answer::put);
        return answer;
    }

    /**
     * @return map PlayerId -> view on the (property) amount of cards this player has in its hands
     */
    public Map<PlayerId, ReadOnlyIntegerProperty> getCardsInHandCount() {
        Map<PlayerId,ReadOnlyIntegerProperty> answer = new EnumMap<>(PlayerId.class);
        cardsInHandCount.forEach(answer::put);
        return answer;
    }

    /**
     * @return map PlayerId -> view on the (property) amount of wagons this player has left
     */
    public Map<PlayerId, ReadOnlyIntegerProperty> getWagonCount() {
        Map<PlayerId,ReadOnlyIntegerProperty> answer = new EnumMap<>(PlayerId.class);
        wagonCount.forEach(answer::put);
        return answer;
    }

    /**
     * @return map PlayerId -> view on  the (property) amount of points this player has got for constructing roads
     * (these are the only publicly available points before the end of the game)
     */
    public Map<PlayerId, ReadOnlyIntegerProperty> getConstructionPoints() {
        Map<PlayerId,ReadOnlyIntegerProperty> answer = new EnumMap<>(PlayerId.class);
        constructionPoints.forEach(answer::put);
        return answer;
    }

    /**
     * @return List of views on the (properties) tickets held in the hand
     * of the owner of this instance of ObservableGameState (this.playerId)
     */
    public List<ReadOnlyObjectProperty<Ticket>> getTicketsInHand() {
        return ticketsInHand.stream().map(o->(ReadOnlyObjectProperty<Ticket>)o).collect(Collectors.toList());
    }

    /**
     * @return map card -> view on the (property) amount of this card in this.playerId hand
     */
    public Map<Card, ReadOnlyIntegerProperty> getNumberOfCardInHand() {
        Map<Card,ReadOnlyIntegerProperty> answer = new EnumMap<>(Card.class);
        numberOfCardInHand.forEach(answer::put);
        return answer;

    }

    /**
     * @return map route -> view on (property) boolean,
     * indicating if this.playerId can currently get (try to get) this road
     */
    public Map<Route,ReadOnlyBooleanProperty> getCanGetRoadMap() {
        Map<Route,ReadOnlyBooleanProperty> oracleGottaGetTheirShitTogether = new HashMap<>();
        canGetRoadMap.forEach(oracleGottaGetTheirShitTogether::put);
        return oracleGottaGetTheirShitTogether;
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
