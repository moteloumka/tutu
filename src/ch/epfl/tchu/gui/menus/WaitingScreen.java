package ch.epfl.tchu.gui.menus;

import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.gui.ActionHandlers;
import ch.epfl.tchu.net.ShowMyIpAddress;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;

public class WaitingScreen {

    public static void hostScreenCreator(EntryCreator entryCreator ,ActionHandlers.CloseLoadingScreen closeHandler, Stage gameParent, Stage loadingStage) {
        Image image = ImageGetter.getImage("/Users/kola/IdeaProjects/tCHu/res/java.gif");
        List<String> info = List.of("Waiting for players to connect..."
                    ,"Tell other players your address : "+ getAddress()
                    ,"Tell other players your port number : "+getPortNum());
        buildBasicScreen(info,image,gameParent,closeHandler, loadingStage);
    }
    public static void clientWaitingScreen(EntryCreator entryCreator , ActionHandlers.CloseLoadingScreen closeHandler,Stage gameParent, Stage loadingStage){
        Image image = ImageGetter.getImage("/Users/kola/IdeaProjects/tCHu/res/search.gif");
        List<String> info = List.of("Connecting to server..."
                ,"Currently connecting to server : "+entryCreator.getAddress()
                ,"Port number : "+entryCreator.getPortNum());
        buildBasicScreen(info,image,gameParent,closeHandler, loadingStage);
    }
    private static void buildBasicScreen(List<String> info, Image image, Stage gameParent, ActionHandlers.CloseLoadingScreen closeHandler, Stage loadingStage){
        Stage stage = loadingStage;
        VBox pane = new VBox();
        pane.getStylesheets().add("daGame.css");
        pane.setId("connectionScreen");

        for(String str : info){
            Text text = new Text(str);
            pane.getChildren().add(text);
        }

        if (image != null){
            //Creating the image view
            ImageView imageView = new ImageView();
            //Setting image to the image view
            imageView.setImage(image);
            //Setting the image view parameters
            imageView.setX(10);
            imageView.setY(10);
            imageView.setFitHeight(70);
            imageView.setFitWidth(70);
            pane.getChildren().add(imageView);
        }

        Button back = new Button("Back");
        back.setOnAction( e -> {
            closeHandler.onCloseLoadingScreen();
            ChoiceScreen.gameTypeWindow(gameParent);
            stage.close();
        });
        back.setPrefWidth(100);

        pane.getChildren().add(back);
        Scene scene = new Scene(pane);
        stage.resizableProperty().set(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(gameParent);
        stage.setScene(scene);
        stage.show();
    }

    private static String getAddress(){
        try {
            return ShowMyIpAddress.showIP();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Problem getting your IP, pls try something else";
    }
    private static int getPortNum(){
        return Constants.GEN_PORT_NUM;
    }
}
