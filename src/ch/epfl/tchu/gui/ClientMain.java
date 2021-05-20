package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;
/**
 *  @author Nikolay (314355)
 *  @author Gullien (316143)
 */

/**
 * this class manages the client part of communication, similar to ServerMain
 */
public class ClientMain extends Application {
    public static void main(String[] args) { launch(args); }

    //start basically connects to the socket created by the server and then just stays in the loop of tun method
    @Override
    public void start(Stage primaryStage) {
        String hostName = getParameters().getRaw().get(0);
        int portNum = Integer.parseInt(getParameters().getRaw().get(1));

        GraphicalPlayerAdapter playerAdapter = new GraphicalPlayerAdapter();
        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(playerAdapter,hostName,portNum);
        new Thread(remotePlayerClient::run).start();
    }
}
