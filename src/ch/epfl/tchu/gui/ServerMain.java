package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.*;
/**
 *  @author Nikolay (314355)
 *  @author Gullien (316143)
 */

/**
 * similar in it's usage to ClientMain, this class only serves to create connection
 * this time the main method is ran on the host's computer
 */
public class ServerMain extends Application {
    private final ObjectProperty<Socket> socketProp = new SimpleObjectProperty<>(null);
    private final  int SERVER_NUMBER = Constants.GEN_PORT_NUM;
    private final List<String> names;
    public static void main(String[] args) { launch(args);}
    /**
     * We added a constructor to this class
     * to be able to create a game when the program is already running
     //* @param names the names of the players
     */
    public ServerMain(List<String> names){this.names = names;}
    @Override
    public void start(Stage primaryStage) {

        Preconditions.checkArgument(names.size() == Constants.PLAYERS_COUNT
                , "this game is supposed to have exactly "
                        +Constants.PLAYERS_COUNT+ " names for the same amount of players");

        Map<PlayerId, String> playerNames = Map.of(PLAYER_1, names.get(0), PLAYER_2,names.get(1));
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());

        new Thread(()->{
            try {
                ServerSocket s0 = new ServerSocket(SERVER_NUMBER);
                Socket s = s0.accept();

                socketProp.set(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        //this is messy but the only way we managed to stay on the javaFX thread
        socketProp.addListener((observable, oldValue, newValue) -> {
            //as soon as the host receives information that a client has connected,
            //the two players are created and the game starts in a new thread
            GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
            RemotePlayerProxy remotePlayer = new RemotePlayerProxy(socketProp.get());
            //i guess we just sort of assume that player one is the server lad
            Map<PlayerId, Player> players = Map.of(PLAYER_1, graphicalPlayer, PLAYER_2, remotePlayer);
            new Thread(()-> Game.play(players,playerNames,tickets, new Random())).start();
        });
    }


}
