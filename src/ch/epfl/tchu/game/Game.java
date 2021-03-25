package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.List;
import java.util.Map;
import java.util.Random;

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
        GameState gameState = GameState.initial(tickets, rng);
        //giving players names (maybe should call before game initialization)
        players.forEach((k,v) -> v.initPlayers(k,playerNames));

        Info infoOnFirstPlayer = new Info(playerNames.get(
                gameState.currentPlayerId()));

        //communicating to each player the name of the first one
        players.forEach((k,v) -> v.receiveInfo(infoOnFirstPlayer.willPlayFirst()));

        //distributing (5) tickets to each player
        for(Map.Entry<PlayerId,Player> m : players.entrySet()){
            m.getValue()
                    .setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            //renewing the game state each time the tickets are distributed
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }
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

                            SortedBag.Builder<Card> builder  = new SortedBag.Builder<>();
                            //getting the (3) first cards
                            for (int i = 0; i<Constants.ADDITIONAL_TUNNEL_CARDS;++i){
                                gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                                builder.add(gameState.topCard());
                                gameState = gameState.withoutTopCard();
                            }
                            SortedBag<Card> drawnCards = builder.build();
                            //finding out how  many additional cards we need
                            int addCardCount = routeToClaim.additionalClaimCardsCount(initialCards,drawnCards);
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
                                    gameState = gameState.withClaimedRoute(routeToClaim,finAddCards);
                                }
                            }
                        } else
                            gameState = gameState.withClaimedRoute(routeToClaim,initialCards);
                    }
                    break;
            }

        }while (!gameState.lastTurnBegins());




    }


    private void tell(String sting, Map<PlayerId,Player> players){
        players.forEach((k,v) -> v.receiveInfo(sting));
    }
}
