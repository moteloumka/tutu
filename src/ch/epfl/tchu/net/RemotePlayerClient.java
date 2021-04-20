package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;


import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.US_ASCII;

public final class RemotePlayerClient {
    private final Player player;
    private final Socket socket;

    public RemotePlayerClient(Player player, String socketName, int port) throws IOException {
        this.player = player;
        this.socket = new Socket(socketName,port);
    }
    private void run(){
        try (BufferedReader r =
                     new BufferedReader(
                             new InputStreamReader(socket.getInputStream(),
                                     US_ASCII));
             BufferedWriter w =
                     new BufferedWriter(
                             new OutputStreamWriter(socket.getOutputStream(),
                                     US_ASCII))) {
            String[] ciphers = r.readLine()
                    .split(Pattern.quote(String.valueOf(RemotePlayerProxy.SPACE_CHAR)),-1);

            switch(MessageId.valueOf(ciphers[0])){

                case INIT_PLAYERS :
                    PlayerId playerId = Serdes.PLAYER_ID.deserialize(ciphers[1]);
                    List<String> names = Serdes.LIST_STRINGS.deserialize(ciphers[2]);
                    EnumMap<PlayerId,String> nameMap = new EnumMap<>(PlayerId.class);
                    //putting names in the enumMap assuming that they were well sorted in the List
                    for (int i=0; i<names.size(); i++)
                        nameMap.put(PlayerId.ALL.get(i),names.get(i));
                    player.initPlayers(playerId,nameMap);
                    break;

                case RECEIVE_INFO:
                    String str = Serdes.STRING.deserialize(ciphers[1]);
                    player.receiveInfo(str);
                    break;

                case UPDATE_STATE:
                    PublicGameState pubGameState = Serdes.PUB_GAME_STATE.deserialize(ciphers[1]);
                    PlayerState playerState = Serdes.PLAYER_STATE.deserialize(ciphers[2]);
                    player.updateState(pubGameState,playerState);
                    break;

                case SET_INITIAL_TICKETS:
                    SortedBag<Ticket> tickets = Serdes.BAG_TICKETS.deserialize(ciphers[1]);
                    player.setInitialTicketChoice(tickets);
                    break;

                case CHOOSE_INITIAL_TICKETS:
                    SortedBag<Ticket> ticketsChosen = player.chooseInitialTickets();
                    w.write(Serdes.BAG_TICKETS.serialize(ticketsChosen));
                    w.write('\n');
                    w.flush();
                    break;

                case NEXT_TURN:
                    Player.TurnKind turnKind = player.nextTurn();
                    w.write(Serdes.TURN_KIND.serialize(turnKind));
                    w.write('\n');
                    w.flush();
                    break;

                case CHOOSE_TICKETS:
                   SortedBag<Ticket> ticketsBag = player
                           .chooseTickets(Serdes.BAG_TICKETS
                                   .deserialize(ciphers[1]));
                   w.write(Serdes.BAG_TICKETS.serialize(ticketsBag));
                   w.write('\n');
                   w.flush();
                   break;

                case DRAW_SLOT:
                    int slot = player.drawSlot();
                    w.write(Serdes.INTEGER.serialize(slot));
                    w.write('\n');
                    w.flush();
                    break;

                case CLAIMED_ROUTE:
                    Route routeToClaim = player.claimedRoute();
                    w.write(Serdes.ROUTE.serialize(routeToClaim));
                    w.write('\n');
                    w.flush();
                    break;

                case INITIAL_CLAIM_CARDS:
                    SortedBag<Card> initialClaimCards = player.initialClaimCards();
                    w.write(Serdes.BAG_CARDS.serialize(initialClaimCards));
                    w.write('\n');
                    w.flush();
                    break;

                case CHOOSE_ADDITIONAL_CARDS:
                    List<SortedBag<Card>> options = Serdes.LIST_BAGS_CARDS.deserialize(ciphers[1]);
                    SortedBag<Card> additionalCards = player.chooseAdditionalCards(options);
                   w.write(Serdes.BAG_CARDS.serialize(additionalCards));
                    w.write('\n');
                    w.flush();
                    break;

            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
