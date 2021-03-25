package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class GameState extends PublicGameState {

    private final Deck<Ticket> ticketDeck;
    private final CardState cardState;
    private final Map<PlayerId,PlayerState> playerStates;

    /**
     * OG constructor that takes every needed param individually
     * we only call it  it explicitly once (from initialise)
     * otherwise it' called from the second constructor of this class
     * @param cardState instance of cardState of this game - info linked to the deck of cards etc
     * @param currentPlayerId the current player (chosen one if first tour)
     * @param pubPlayerState the map needed to be passed as param to the PublicGameState constructor
     * @param lastPlayer null if first tour
     * @param tickets deck of tickets that are in the game and not in the players hands
     * @param playerStates full information on the player states
     */
    private GameState( CardState cardState,
                       PlayerId currentPlayerId,
                       Map<PlayerId, PublicPlayerState> pubPlayerState,
                       PlayerId lastPlayer,
                       Deck<Ticket> tickets,
                       Map<PlayerId,PlayerState> playerStates ) {

        super(  tickets.size(),
                (PublicCardState) cardState,
                currentPlayerId,
                pubPlayerState,
                lastPlayer);

        this.ticketDeck   = tickets;
        this.cardState    = cardState;
        this.playerStates = Map.copyOf(playerStates);
    }

    /**
     * Constructor used to create new instances from already existing GameStates but with modified attributes
     * Can't be called when initializing
     * @param cardState self
     * @param tickets explain
     * @param fullPlayerStates natory
     * @param gameState the instance to be "modified"
     */
    private GameState(CardState     cardState,
                      Deck<Ticket>  tickets,
                      Map<PlayerId,PlayerState> fullPlayerStates,
                      GameState     gameState){

        this(   cardState,
                gameState.currentPlayerId(),
                makePublic(fullPlayerStates),
                gameState.lastPlayer(),
                tickets,
                fullPlayerStates );
    }

    private GameState(PlayerId currentPlayerId,
                      PlayerId lastPlayerId,
                      GameState gameState){

        this(   gameState.cardState,
                currentPlayerId,
                makePublic(gameState.playerStates),
                lastPlayerId,
                gameState.ticketDeck,
                gameState.playerStates);
    }


    /**
     * GameState initializer : to be used in the beginning of the game (not more than  once)
     * - decides which player starts
     * - creates a shuffled deck with all the cards in the game
     * - distributes (4) cards to each player
     * - creates their game states
     * - creates a shuffled deck of tickets with the ones passed as parameter
     * @param tickets the tickets to be used in the game
     * @param rng instance of Random used to shuffle the cards/tickets
     * @return instance of GameState in it's initial state
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng){
        //creating  a new deck of  tickets with the ones passed as param
        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
        //creating a new deck with all the cards in it
        Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS,rng);
        //creating the enum map with player states (distributing them their first cards in the same time)
        Map<PlayerId,PlayerState> psMap = Map.copyOf(newPlayerStates(cardDeck));
        //creating a new deck from the card left overs
        cardDeck = cardDeck.withoutTopCards(PlayerId.COUNT * Constants.INITIAL_CARDS_COUNT );
        CardState cardState = CardState.of(cardDeck);

        //deciding who goes first
        PlayerId starter = whoGoesFirst(rng);

        return new GameState(cardState
                ,starter
                ,makePublic(psMap)
                //as I see it, it' the first tour -> the id if the lastPlayer doesn't exist yet
                ,null
                ,ticketDeck
                ,psMap);
    }

    /**
     * gives the full player state (not just public info)
     * @param playerId the player
     * @return full player's state
     */
    public PlayerState playerState(PlayerId playerId){
        return this.playerStates.get(playerId);
    }

    /**
     * gives full player state on the current player
     * @return PlayerState of current player
     */
    public PlayerState currentPlayerState(){
        return this.playerStates.get(super.currentPlayerId());
    }

    /**
     * @param count the amount of tickets to be picked
     * @return the first count tickets
     */
    public SortedBag<Ticket> topTickets(int count){
        check(count);
        return this.ticketDeck.topCards(count);
    }

    /**
     * @param count the amount of tickets to be removed from the new game state
     * @return same game state but without the top tickets (new instance though)
     */
    public GameState withoutTopTickets(int count){
        check(count);
        return new GameState(this.cardState,ticketDeck.withoutTopCards(count),this.playerStates,this);
    }

    /**
     *
     * @return the top card from the deck
     */
    public Card topCard(){
        Preconditions.checkArgument(!this.cardState.isDeckEmpty(),
                "deck do be empty these days");
        return this.cardState.topDeckCard();
    }

    /**
     *
     * @return same game state but without the top card in the  deck (new instance though)
     */
    public GameState withoutTopCard(){
        Preconditions.checkArgument(!this.cardState.isDeckEmpty(),
                "deck do be empty these days");
        CardState newCardState = this.cardState.withoutTopDeckCard();
        return new GameState(newCardState,this.ticketDeck,this.playerStates,this);
    }

    /**
     * @param discardedCards cards to be added to the  discarded cards
     * @return same game state but with the discarded cards added (new instance though)
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        CardState newCardState = this.cardState.withMoreDiscardedCards(discardedCards);
        return new GameState(newCardState,this.ticketDeck,this.playerStates,this);
    }

    /**
     * @param rng instance of random used to shuffle
     * @return same game state but with card deck recreated from discarded cards (new instance though)
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng){
        CardState cardStateVar = this.cardState;
        if(this.cardState.isDeckEmpty())
            cardStateVar = this.cardState.withDeckRecreatedFromDiscards(rng);

        return new GameState( cardStateVar,this.ticketDeck,this.playerStates,this);
    }

    /**
     *
     * @param playerId the player that will have tickets added
     * @param chosenTickets the  tickets  in question
     * @return same game state but with the one player state updated (tickets added) (new instance though)
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){
        PlayerState oPs = this.playerStates.get(playerId);
        Preconditions.checkArgument(oPs.tickets().isEmpty(),
                "the player shouldn't have tickets at the beginning");
        Preconditions.checkArgument(Constants.INITIAL_TICKETS_COUNT
                - chosenTickets.size()
                < Constants.DISCARDABLE_TICKETS_COUNT
        ,"cant discard more than (2) tickets");

        PlayerState nPs = new PlayerState(chosenTickets,oPs.cards(),oPs.routes());
        Map<PlayerId,PlayerState> newPsMap = newPlayerStates(nPs);

        return new GameState(this.cardState, this.ticketDeck, newPsMap,this);
    }

    /**
     * call when player chooses it's tickets
     * - creates new player state of the current  player by adding the chosen tickets
     * - creates new deck of tickets by deleting the tickets in drawnTickets from the deck of tickets
     * - since there is no pile of reusable tickets (like for the cards), they are simply deleted
     * @param drawnTickets the tickets the player chooses from
     * @param chosenTickets the tickets the player has chosen
     * @return new instance of GameState with modified attributes,concerning the ticket operation
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets,
                                                 SortedBag<Ticket> chosenTickets){

        Preconditions.checkArgument(this.canDrawTickets());
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets),
                "drawnTickets doesn't contain chosenTickets");

        PlayerState nPs = this.playerStates
                .get(this.currentPlayerId())
                .withAddedTickets(chosenTickets);

        Map<PlayerId,PlayerState> newPsMap = newPlayerStates(nPs);
        Deck<Ticket> newTickets = this.ticketDeck.withoutTopCards(drawnTickets.size());
        return new GameState(cardState,newTickets,newPsMap,this);
    }

    /**
     * call when player draws a face up card
     * - creates new player state of the current  player by adding the chosen card into it's possession
     * - creates new CardState by removing the face up card from the slot
     * and replacing it with a card from top of the deck
     * @param slot slot where the card is placed
     * @return new GameState that keeps track of these changes
     */
    public GameState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(this.canDrawCards()
                ,"can't draw cards");
        PlayerState nPs = this.currentPlayerState().
                withAddedCard(this.cardState.faceUpCard(slot));
        CardState newCardState = this.cardState.withDrawnFaceUpCard(slot);
        Map<PlayerId,PlayerState> newPsMap = newPlayerStates(nPs);
        return new GameState(newCardState,this.ticketDeck,newPsMap,this);
    }

    /**
     * call when player draws a face down card from the deck
     * - creates new player state of the current  player by adding the drawn card into it's possession
     * - creates new CardState by removing the face down card from the top of the deck
     * - replacing it with a card from top underneath
     * @return new GameState that keeps track of these changes
     */
    public GameState withBlindlyDrawnCard(){
        Preconditions.checkArgument(this.canDrawCards(),
                "can't draw cards");
        CardState newCardState = this.cardState.withoutTopDeckCard();
        PlayerState nPs = this.currentPlayerState()
                .withAddedCard(this.cardState.topDeckCard());

        Map<PlayerId,PlayerState> newPsMap = newPlayerStates(nPs);
        return new GameState( newCardState,this.ticketDeck, newPsMap,this);
    }

    /**
     * call when player claims route
     * - creates new player state of the current player by adding the claimed route into it's possession
     * - and taking away the cards he/she used to obtain the route
     * - creates new CardState by adding  the used cards (param cards) into the recycled cards
     * @param route the route the player has claimed
     * @param cards the cards used to claim the route
     * @return new GameState that keeps track of these changes
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){
        Preconditions.checkArgument(this.canDrawCards(),
                "can't draw cards");
        CardState newCardState = this.cardState.withMoreDiscardedCards(cards);
        PlayerState nPs = this.currentPlayerState().withClaimedRoute(route,cards);
        Map<PlayerId,PlayerState> newPsMap = newPlayerStates(nPs);
        return new GameState( newCardState,this.ticketDeck, newPsMap,this);
    }


    /**
     * !!HAS TO BE CALLED IN THE END OF THE TURN!!
     * indicates if it's time for the last turn
     * @return true if this player has less or equal to (2) wagons left
     */
    public boolean lastTurnBegins(){
        return  (this.playerStates.get(this.currentPlayerId())
                .ticketPoints()<=Constants.MIN_WAGONS_TO_ENDGAME);
    }

    /**
     * starts a new turn
     * - the new current player becomes the player next to the current current player
     * - if lastTurnBegins() returns true -> in addition  to what  the method usually does,
     * it will also put the current player as the last player
     * @return instance of GameState in the sate of the new turn
     */
    public GameState forNextTurn(){
        if(this.lastTurnBegins())
            return new GameState(this.currentPlayerId().next(),this.currentPlayerId(),this);
        return new GameState(this.currentPlayerId().next(),this.lastPlayer(),this);
    }



    /**
     * little method to avoid rewriting this code every time we create a new instance of GameState
     * @param psm the map which we want to convert
     * @return version of  the map that PublicGameState needs upon construction
     */
    private static Map<PlayerId,PublicPlayerState> makePublic(Map<PlayerId,PlayerState> psm) {
        Map<PlayerId,PublicPlayerState> pubMap = new EnumMap<>(PlayerId.class);
        psm.forEach(pubMap::put);
        //psm.forEach((k,v) -> pubMap.put(k, (PublicPlayerState) v));
        return pubMap;
    }

    /**
     * this is more of a little joke, really :)
     * @param count a number, u know
     */
    private void check(int count){
        Preconditions.checkArgument(count>0
                && count<this.ticketDeck.size(),
                " nauchis' pisat' normal'no");
    }


    private static PlayerId whoGoesFirst(Random rng){
        int turn = rng.nextInt(PlayerId.COUNT);
        //ok, we only have 2 players so i'm not going to complicate this
        return turn == 0 ? PlayerId.PLAYER_1 : PlayerId.PLAYER_2;
    }

    /**
     * initializer of the map playerId -> PlayerState
     * distributes the first (4) cards from the deck to the first player, next (4) to the second
     * creates player states and then the map
      * @param deck pre shuffled deck of cards
     * @return map playerId -> PlayerState (with no tickets)
     */
    private static EnumMap<PlayerId,PlayerState> newPlayerStates(Deck<Card> deck){
        int c = Constants.INITIAL_CARDS_COUNT;
        //first (4) cards go into the first pile
        SortedBag<Card> p1Cards = deck.topCards(c);
        //taking off (4) cards from the top of the deck, repeating last manipulation
        SortedBag<Card> p2Cards = deck.withoutTopCards(c).topCards(c);
        //creating the player states for the two players
        //SortedBag.of() used to show that no tickets are taken by these players yet
        List<Route> routeList1 = List.of();
        List<Route> routeList2 = List.of();
        PlayerState ps1 = new PlayerState(SortedBag.of(), p1Cards,routeList1);
        PlayerState ps2 = new PlayerState(SortedBag.of(), p2Cards,routeList2);
        //creating the map
        EnumMap<PlayerId,PlayerState> enumMap = new EnumMap<>(PlayerId.class);
        enumMap.put(PlayerId.PLAYER_1,ps1);
        enumMap.put(PlayerId.PLAYER_2,ps2);
        return enumMap;
    }
    private EnumMap<PlayerId,PlayerState> newPlayerStates(PlayerState nPs){
        PlayerId p = this.currentPlayerId();
        EnumMap<PlayerId,PlayerState> newPsMap = new EnumMap<>(PlayerId.class);
        newPsMap.put(p,nPs);
        newPsMap.put(p.next(), this.playerState(p.next()));
        return newPsMap;
    }
}
