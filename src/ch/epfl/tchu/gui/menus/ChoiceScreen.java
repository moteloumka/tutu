package ch.epfl.tchu.gui.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChoiceScreen {
    public static void gameTypeWindow(Stage parentWindow){
        Stage stage = new Stage();
        BorderPane pane = new BorderPane();

        Button host = new Button("Host Game");
        host.setOnAction(event -> {
            EntryCreator entryCreator = new EntryCreator(PlayerType.HOST);
            entryCreator.createNameEntryScreen(parentWindow, parentWindow);
            stage.close();
        });

        Button join = new Button("Join Game");
        join.setOnAction(event -> {
            EntryCreator entryCreator = new EntryCreator(PlayerType.CLIENT);
            entryCreator.createNameEntryScreen(parentWindow, parentWindow);
            stage.close();
        });

        VBox forButtons = new VBox();
        forButtons.getStyleClass().add("biggie");
        forButtons.setId("gameChoice");

        forButtons.getChildren().setAll(host,join);
        pane.setCenter(forButtons);

        Scene window = new Scene(pane);
        window.getStylesheets().add("daGame.css");
        stage.setScene(window);
        stage.resizableProperty().set(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentWindow);
        stage.show();



        stage.centerOnScreen();
    }
}
