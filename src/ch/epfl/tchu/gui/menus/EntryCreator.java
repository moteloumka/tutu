package ch.epfl.tchu.gui.menus;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.gui.ActionHandlers;
import ch.epfl.tchu.gui.ClientMain;
import ch.epfl.tchu.gui.ServerMain;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.stream.Collectors;


public class EntryCreator {

    private final PlayerType playerType;
    private final Stage stage;
    private final ObjectProperty<String> name1prop = new SimpleObjectProperty<>("");
    private final ObjectProperty<String> name2prop = new SimpleObjectProperty<>("");
    private final Button confirm1;
    private final Button confirm2;
    private final BorderPane pane;

    public EntryCreator(PlayerType playerType){

        this.playerType = playerType;
        this.stage = new Stage();
        pane = new BorderPane();

        this.confirm1 = new Button("confirm");
        this.confirm2 = new Button("confirm");
    }

    public void createNameEntryScreen(Stage ogParent, Stage gameParent){

        TextField name1 = new TextField();
        TextField name2 = new TextField();
        Text explanation;

        String mainButtonText;
        pane.getStyleClass().add("daGame.css");

        if (playerType.equals(PlayerType.HOST)){
            pane.getStyleClass().add("namePage");
            name1.setPromptText("enter name : player1");
            name2.setPromptText("enter name : player2");
            explanation = new Text("Please enter player names");
            mainButtonText = "Host";
        }
        else{
            pane.getStyleClass().add("ipPage");
            name1.setPromptText("enter ip address");
            name2.setPromptText("enter port number");
            explanation = new Text("Please enter connection settings");
            mainButtonText = "Join";
        }

        confirm1.setOnAction(event -> nameEntryHandler(name1,name1prop,confirm1));
        confirm2.setOnAction(event -> {
            if(playerType == PlayerType.CLIENT && !Preconditions.isInteger(name2.getText())){
                name2.getStyleClass().add("wrongText");
                name2.clear();
                name2.setPromptText("enter port NUMBER");

                event.consume();
            }
            else
                nameEntryHandler(name2,name2prop,confirm2);
        });

        HBox player1Line = new HBox();
        player1Line.getChildren().setAll(name1,confirm1);
        HBox player2Line = new HBox();
        player2Line.getChildren().setAll(name2,confirm2);

        Button masterConfirm = new Button(mainButtonText);
        masterConfirm.disableProperty().bind(Bindings.or(
                 Bindings.equal("",name2prop)
                ,Bindings.equal("",name1prop)));

        masterConfirm.onActionProperty().set(event -> {

            if (this.playerType == PlayerType.HOST)
                startDisGame(gameParent);
            else
                joinDisGame(gameParent);
            stage.close();
        });

        Button back = new Button("Back");
        back.setOnAction(event -> {
            stage.close();
            ChoiceScreen.gameTypeWindow(gameParent);
        });

        HBox utilButtons = new HBox();
        utilButtons.getChildren().addAll(masterConfirm, back);

        VBox daBox = new VBox();
        daBox.getChildren().addAll(explanation,player1Line,player2Line,utilButtons);

        pane.setCenter(daBox);
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("daGame.css");

        stage.setScene(scene);
        stage.resizableProperty().set(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(ogParent);
        stage.show();
    }

    public String getAddress(){
        return name1prop.get();
    }
    public int getPortNum(){
        return Integer.parseInt(name2prop.get());
    }
    public Map<PlayerId,String> getNames(){
        return Map.of(PlayerId.PLAYER_1,name1prop.get(),PlayerId.PLAYER_2,name2prop.get());
    }

    /**
     * important methods :
     * 1) Create an instance of server/client
     * 2) Start the game in a new thread
     * 3) create a window that will be shown while the connection is happening
     * 4) also we create a way to stop trying to connect by stopping the thread
     * @param gameParent still the first menu screen
     */
    private void startDisGame(Stage gameParent){
        ServerMain server = new ServerMain(new ArrayList<>(getNames().values()));
        Thread thread = new Thread(()->server.start(new Stage()));
        ActionHandlers.CloseLoadingScreen closeHandler = thread::stop;
        WaitingScreen.hostScreenCreator(this,closeHandler ,gameParent);

    }
    private void joinDisGame(Stage gameParent){
        ClientMain client = new ClientMain(getAddress(),getPortNum());
        Thread thread = new Thread(()->client.start(new Stage()));
        ActionHandlers.CloseLoadingScreen closeHandler = thread::stop;
        WaitingScreen.clientWaitingScreen(this ,closeHandler,gameParent);
    }

    /**
     * instead of rewriting this for each button that handles text entry
     */
    private void nameEntryHandler(TextField name, ObjectProperty<String> nameProp, Button button){
            nameProp.set(name.getText());
            if (!name.getText().equals("")){
                name.getStyleClass().removeAll("wrongText");
                name.getStyleClass().add("correctText");
                button.setDisable(true);
            }
            else
                name.getStyleClass().add("wrongText");
        }


}
