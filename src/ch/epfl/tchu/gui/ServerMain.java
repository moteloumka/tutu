package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.*;

public class ServerMain extends Application {
    public static void main(String[] args) { launch(args);}
    @Override
    public void start(Stage primaryStage) {
        List<String> names = getParameters().getRaw();
        Map<PlayerId, String> playerNames = Map.of(PLAYER_1, names.get(0), PLAYER_2,names.get(1));
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        try{
            ServerSocket s0 = new ServerSocket(5108);
            Socket s = s0.accept();
            GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
            RemotePlayerProxy remotePlayer = new RemotePlayerProxy(s);
            //i guess we just sort of assume that player one is the server lad
            Map<PlayerId, Player> players = Map.of(PLAYER_1, graphicalPlayer, PLAYER_2, remotePlayer);

            new Thread(()-> Game.play(players,playerNames,tickets, new Random())).start();

        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

}
