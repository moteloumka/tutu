package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.US_ASCII;

public final class RemotePlayerProxy implements Player {
    private final Socket socket;
    public RemotePlayerProxy( Socket socket){
        this.socket = socket;
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        List<String> message = List.of(MessageId.INIT_PLAYERS.name()
                ,Serdes.PLAYER_ID.serialize(ownId)
                ,Serdes.LIST_STRINGS.serialize(new ArrayList<>(playerNames.values())));
        send(message);
    }

    @Override
    public void receiveInfo(String info) {


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


    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return null;
    }

    @Override
    public TurnKind nextTurn() {
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        return null;
    }

    @Override
    public int drawSlot() {
        return 0;
    }

    @Override
    public Route claimedRoute() {
        return null;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return null;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        return null;
    }

    private void send(List<String> strings) {
        try (BufferedWriter w =
                     new BufferedWriter(
                             new OutputStreamWriter(socket.getOutputStream(),
                                     US_ASCII))) {
            String str = String.join(" ",strings);
            w.write(str + '\n');
            //w.write('\n');
            w.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    private String receive(){
        try (BufferedReader r =
                     new BufferedReader(
                             new InputStreamReader(socket.getInputStream(),
                                     US_ASCII))){
            return r.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

