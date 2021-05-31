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

    private final String hostName;
    private final int portNum;
    //public static void main(String[] args) { launch(args); }

    /**
     * We added a constructor to this class
     * to be able to create a game when the program is already running
//     * @param address the IP address
//     * @param portNumber the port number
     */
    public ClientMain(String address, int portNumber){
        this.hostName = address;
        this.portNum = portNumber;
    }
    //start basically connects to the socket created by the server and then just stays in the loop of tun method
    @Override
    public void start(Stage primaryStage) {
        GraphicalPlayerAdapter playerAdapter = new GraphicalPlayerAdapter();
        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(playerAdapter,hostName,portNum);
        new Thread(remotePlayerClient::run).start();
    }
}
