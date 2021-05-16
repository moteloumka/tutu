package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        String hostName = getParameters().getRaw().get(0);
        int portNum = Integer.parseInt(getParameters().getRaw().get(1));

        GraphicalPlayerAdapter playerAdapter = new GraphicalPlayerAdapter("side kick");
        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(playerAdapter,hostName,portNum);
        new Thread(remotePlayerClient::run).start();
    }
}
