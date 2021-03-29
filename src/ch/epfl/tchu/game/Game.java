package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import javax.swing.text.html.parser.Entity;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class Game {
    private Game(){}
    public void play(Map<PlayerId, Player> players
            , Map<PlayerId, String> playerNames
            , SortedBag<Ticket> tickets, Random rng){

        Preconditions.checkArgument(players.size()==PlayerId.COUNT
                ,"players map isn't size " +PlayerId.COUNT+ " in Game play method");
        Preconditions.checkArgument(playerNames.size() ==  PlayerId.COUNT
                ,"playerNames isn't size "+PlayerId.COUNT+" in Game play method" );

        //initialization of the whole game

        //initializing the game state
        GameState gameState = GameState .initial(tickets, rng);
        //giving players names (maybe should call before game initialization)
        players.forEach((k,v) -> v.initPlayers(k,playerNames));


        Info infoOnFirstPlayer = new Info(playerNames.get(
                gameState.currentPlayerId()));

        //communicating to each player the name of the first one
        //players.forEach((k,v) -> v.receiveInfo(infoOnFirstPlayer.willPlayFirst()));
        tell(infoOnFirstPlayer.willPlayFirst(),players);

        //distributing (5) tickets to each player
        for(Map.Entry<PlayerId,Player> m : players.entrySet()){
            m.getValue()
                    .setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            //renewing the game state each time the tickets are distributed
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }
        //updating gui so players know which ticket to pick
        //given their initially distributed cards
        update(gameState, players);
        //each player chooses his tickets
        for(Map.Entry<PlayerId,Player> m : players.entrySet()){
            gameState = gameState.withInitiallyChosenTickets(
                             m.getKey()
                            ,m.getValue().chooseInitialTickets());
        }


        //the number of tickets chosen by each player is communicated to all the players
        for(Map.Entry<PlayerId,Player> m : players.entrySet()){
            Info info = new Info(playerNames
                    .get(m.getKey()));
            m.getValue()
                    .receiveInfo(info.keptTickets(
                    m.getValue().chooseInitialTickets().size()
            ));
        }

        //the game is played here
        //gets out of the cycle only if lastTurnBegins() of GameState returns true
        //(in the end of one's turn)

        do {

            PlayerId currentPlayerId = gameState.currentPlayerId();
            Player currentPlayer = players.get(currentPlayerId);
            //choosing the kind of turn
            Player.TurnKind turnKind = currentPlayer.nextTurn();
            Info info = new Info(playerNames.get(currentPlayerId));

            tell(info.canPlay(),players);



            switch (turnKind){

                case DRAW_TICKETS:

                    //creation of 2 sorted bags : first with tickets offered to the player, second with the chosen ones
                    SortedBag<Ticket> optionalTickets  =
                            gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    tell(info.drewTickets(optionalTickets.size()) ,players);

                    SortedBag<Ticket> chosenNewTickets =
                            currentPlayer.chooseTickets(optionalTickets);
                    tell(info.keptTickets(chosenNewTickets.size()) ,players);
                    //renewing the game state, communicating what has been chosen and trowing away the untouched cards
                    gameState = gameState.withChosenAdditionalTickets(optionalTickets,chosenNewTickets);
                    break;

                case DRAW_CARDS:

                    //we repeat the same  process of drawing cards (2) times
                    for (int i = 0; i<Constants.CARDS_DRAWN_PER_TOUR;++i){

                        int slot = currentPlayer.drawSlot();
                        //good idea to check this every time  we draw a card
                        gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                        if (slot == Constants.DECK_SLOT) {
                            gameState = gameState.withBlindlyDrawnCard();
                            tell(info.drewBlindCard(), players);
                        } else{
                            tell(info.drewVisibleCard(gameState
                                    .cardState()
                                    .faceUpCard(slot))
                                    ,players);
                            gameState = gameState.withDrawnFaceUpCard(slot);
                        }
                        update(gameState, players);
                    }
                    break;

                case CLAIM_ROUTE:

                    //find out what we're trying to claim
                    Route routeToClaim = currentPlayer.claimedRoute();
                    //find out with which cards the player is willing to claim the route
                    SortedBag<Card> initialCards = currentPlayer.initialClaimCards();
                    //checks if claiming the route is possible
                    if (gameState.currentPlayerState().canClaimRoute(routeToClaim)){

                        if(routeToClaim.level()== Route.Level.UNDERGROUND){

                            tell(info.attemptsTunnelClaim(routeToClaim, initialCards), players);
                            SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
                            //getting the (3) first cards
                            for (int i = 0; i<Constants.ADDITIONAL_TUNNEL_CARDS;++i){
                                gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                                builder.add(gameState.topCard());
                                gameState = gameState.withoutTopCard();
                            }
                            SortedBag<Card> drawnCards = builder.build();
                            //finding out how  many additional cards we need
                            int addCardCount = routeToClaim.additionalClaimCardsCount(initialCards,drawnCards);
                            tell(info.drewAdditionalCards(drawnCards, addCardCount), players);
                            if (addCardCount!=0){
                                //constructing  a list of combinations of additional cards the player can give
                                List<SortedBag<Card>> options = gameState.currentPlayerState()
                                        .possibleAdditionalCards(addCardCount
                                                ,initialCards
                                                ,drawnCards);
                                /**
                                 * not sure if we need to do this, there may be a method somewhere
                                 */
                                boolean canGetTunnel = false;
                                for (SortedBag<Card> cards: options){
                                    if (gameState.currentPlayerState().cards().contains(cards))
                                        canGetTunnel = true;
                                }
                                if (canGetTunnel){
                                    //this is also quite sus ngl
                                    SortedBag<Card> finAddCards = currentPlayer
                                            .chooseAdditionalCards(options)
                                            .union(initialCards);
                                    //when the player doesn't want to/can't claim route. This is mega sus
                                    if (initialCards.equals(finAddCards)) { tell(info.didNotClaimRoute(routeToClaim), players); }
                                    else {
                                        tell(info.claimedRoute(routeToClaim, finAddCards), players);
                                        gameState = gameState.withClaimedRoute(routeToClaim, finAddCards);
                                    }
                                }
                                else {
                                    tell(info.didNotClaimRoute(routeToClaim), players);
                                }
                            }
                        } else {
                            tell(info.claimedRoute(routeToClaim, initialCards), players);
                            gameState = gameState.withClaimedRoute(routeToClaim, initialCards);
                        }
                    }
                    break;
            }

            //announces the last turn begins
            if (gameState.lastPlayer() == null && gameState.lastTurnBegins()){
                tell(info.lastTurnBegins(gameState.currentPlayerState().carCount()), players);
            }

            if(gameState.lastPlayer() == null || gameState.lastPlayer() != currentPlayerId) {
                //updating the gui so that player knows what turn to have knowing the state of the
                //game at the end of the turn
                update(gameState, players);
                gameState.forNextTurn();
            }
        }while ( gameState.currentPlayerId() != gameState.lastPlayer() );


        //here comes the endgame , thanos reference
        //we count points the points and announce winner and loser

        //updating the gui one last time
        update(gameState, players);

        EnumMap<PlayerId, Integer> pointMap = new EnumMap<>(PlayerId.class);
        //finding out what is the length of longest trail (can happen for more than 1 player)
        //also puts the keys in pointMap
        int maxLength = 0;
        for(Map.Entry<PlayerId,Player> m : players.entrySet()) {
            int playerLongestLength = Trail.longest(gameState.playerState(m.getKey()).routes()).length();
            if (playerLongestLength > maxLength){
                maxLength = playerLongestLength;
            }
            pointMap.put(m.getKey(), gameState.playerState(m.getKey()).finalPoints());
        }

        //receiving info on longest trail bonus for each player that has the longest trail
        //and adding bonus points if necessary
        for(Map.Entry<PlayerId,Player> m : players.entrySet()) {
            Trail longestTrail = Trail.longest(gameState.playerState(m.getKey()).routes());
            if (longestTrail.length() == maxLength){
                Info info = new Info(playerNames.get(m.getKey()));
                //announcing to everyone who got the bonus
                tell(info.getsLongestTrailBonus(longestTrail), players);
                //adding the bonus points to the players who got the bonus
                pointMap.replace(m.getKey(),
                        pointMap.get(m.getKey())+Constants.LONGEST_TRAIL_BONUS_POINTS);
            }
        }

        int maxPoints = Collections.max(pointMap.values());
        int minPoints = Collections.min(pointMap.values());
        List<PlayerId> potentialWinners = List.of();

        for(Map.Entry<PlayerId,Player> m : players.entrySet()) {
            if (pointMap.get(m.getKey())==maxPoints){
                potentialWinners.add(m.getKey());}
        }

        if (potentialWinners.size()==1) {
            Info info = new Info(playerNames.get(potentialWinners.get(0)));
            //announcing the winner
            tell(info.won(maxPoints, minPoints), players);
        }
        else {
            //announcing the draw
            Info.draw(new ArrayList<>(playerNames.values()), maxPoints);
        }







    }




    private void tell(String string, Map<PlayerId,Player> players){
        players.forEach((k,v) -> v.receiveInfo(string));
    }

    private void update(GameState gameState, Map<PlayerId,Player> players){
        players.forEach((k,v) -> v.updateState(gameState, gameState.playerState(k)));
    }
}
