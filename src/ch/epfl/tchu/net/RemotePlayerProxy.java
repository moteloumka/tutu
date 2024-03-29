package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */

/**
 * an instance of RemotePlayerProxy exists on the gosting server/computer
 * and communicates with the instances of RemotePlayerClient on the clients commuters
 * it updates them on the current game situation and gets responses from them (if needed)
 * by sending encoded data (Serdes)/ by receiving the same Serdes
 * and decoding them back into instances of Objects defined in this project
 *
 * This class implements player to be able to be seen as an instance of a player by the game
 * the implementations are just the way Objects have to be encoded and sent
 * or received as strings and decoded back into objects
 */
public final class RemotePlayerProxy implements Player {

    public final static char SPACE_CHAR = ' ';
    private final Socket socket;

    /**
     * the constructor will be called upon launching the game in ServerMain by the host
     * @param socket the socket to which clients could then connect
     */
    public RemotePlayerProxy( Socket socket){
        this.socket = socket;
    }


    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        List<String> namesInOrder = new ArrayList<>();
        for(PlayerId p : PlayerId.ALL)
            namesInOrder.add(playerNames.get(p));
        List<String> message = List.of(MessageId.INIT_PLAYERS.name()
                ,Serdes.PLAYER_ID.serialize(ownId)
                ,Serdes.LIST_STRINGS.serialize(namesInOrder));
        send(message);
    }

    @Override
    public void receiveInfo(String info) {
        List<String> strings = List.of(MessageId.RECEIVE_INFO.name()
        ,Serdes.STRING.serialize(info));
        send(strings);
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        List<String> message = List.of(MessageId.UPDATE_STATE.name()
        ,Serdes.PUB_GAME_STATE.serialize(newState)
        ,Serdes.PLAYER_STATE.serialize(ownState));
        send(message);

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        List<String> strings = List.of(MessageId.SET_INITIAL_TICKETS.name()
        ,Serdes.BAG_TICKETS.serialize(tickets));
        send(strings);
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        send(List.of(MessageId.CHOOSE_INITIAL_TICKETS.name()));
        return Serdes.BAG_TICKETS.deserialize(receive());
    }

    @Override
    public TurnKind nextTurn() {
        send(List.of(MessageId.NEXT_TURN.name()));
        return Serdes.TURN_KIND.deserialize(receive());
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        send(List.of(MessageId.CHOOSE_TICKETS.name()
        ,Serdes.BAG_TICKETS.serialize(options)));
        return Serdes.BAG_TICKETS.deserialize(receive());
    }

    @Override
    public int drawSlot() {
        send(List.of(MessageId.DRAW_SLOT.name()));
        return Serdes.INTEGER.deserialize(receive());
    }

    @Override
    public Route claimedRoute() {
        send(List.of(MessageId.ROUTE.name()));
        return Serdes.ROUTE.deserialize(receive());
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        send(List.of(MessageId.CARDS.name()));
        return Serdes.BAG_CARDS.deserialize(receive());
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        send(List.of(MessageId.CHOOSE_ADDITIONAL_CARDS.name()
        ,Serdes.LIST_BAGS_CARDS.serialize(options)));
        return Serdes.BAG_CARDS.deserialize(receive());
    }

    //method used to send via socket
    private void send(List<String> strings) {
        try {
            BufferedWriter w =
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(),
                                    US_ASCII));
            String str = String.join(String.valueOf(SPACE_CHAR),strings);
            w.write(str);
            w.write('\n');
            w.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    //method used to receive via socket
    private String receive(){
        try{
            BufferedReader r =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream(),
                                    US_ASCII));
            return r.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

