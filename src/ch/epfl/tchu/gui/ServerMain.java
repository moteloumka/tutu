package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
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
/**
 *  @author Nikolay (314355)
 *  @author Gullien (316143)
 */

/**
 * similar in it's usage to ClientMain, this class only serves to create connection
 * this time the main method is ran on the host's computer
 */
public class ServerMain extends Application {
    private final static int SERVER_NUMBER = Constants.GEN_PORT_NUM;
    private final List<String> names;
    //public static void main(String[] args) { launch(args);}
    /**
     * We added a constructor to this class
     * to be able to create a game when the program is already running
     * @param names the names of the players
     */
    public ServerMain(List<String> names){
        this.names = names;
    }
    @Override
    public void start(Stage primaryStage) {

        Preconditions.checkArgument(names.size() == Constants.PLAYERS_COUNT
                , "this game is supposed to have exactly "
                        +Constants.PLAYERS_COUNT+ " names for the same amount of players");

        Map<PlayerId, String> playerNames = Map.of(PLAYER_1, names.get(0), PLAYER_2,names.get(1));
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        try{
            ServerSocket s0 = new ServerSocket(SERVER_NUMBER);
            Socket s = s0.accept();
            //as soon as the host receives information that a client has connected,
            //the two players are created and the game starts in a new thread
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
