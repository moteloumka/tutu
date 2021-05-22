package ch.epfl.tchu.gui.menus;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import javafx.util.Duration;

public class WelcomeScreen extends Application {
    private BorderPane layout;
    @Override
    public void start(Stage primaryStage) throws IOException {


        layout = new BorderPane();
        layout.getStylesheets().setAll("daGame.css");
        layout.getStyleClass().add("blueback");

        //DarkMode switch
        ToggleButton lightMode = new ToggleButton();
        ToggleButton darkMode = new ToggleButton();
        ToggleGroup darkGroup = new ToggleGroup();
        darkGroup.getToggles().addAll(lightMode,darkMode);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(lightMode,darkMode);
        //layout.setTop(hBox);


        //list stuff
        List<String> list = List.of("Host Game","Join Game");
        ObservableList<String> obsList =  FXCollections.observableArrayList(list);
        ListView<String> strings = new ListView<>(obsList);
        VBox vbox = new VBox();
        vbox.getChildren().add(strings);

        //creating the image objects
        Image image = ImageGetter.getImage("/Users/kola/Desktop/vaporTrain.gif");
        Image title = ImageGetter.getImage("/Users/kola/Desktop/wordartNoGame.png");


        //Creating the image view for the wagon
        ImageView imageView = new ImageView();
        //Setting image to the image view
        imageView.setImage(image);
        //Setting the image view parameters
        //imageView.setX(10);
        //imageView.setY(10);
        imageView.fitWidthProperty().bind(layout.widthProperty());
        imageView.fitHeightProperty().bind(layout.heightProperty());

        ImageView imageViewTitle = new ImageView();
        //Setting image to the image view
        imageViewTitle.setImage(title);
        //Setting the image view parameters
        imageViewTitle.setX(10);
        imageViewTitle.setY(10);
        imageViewTitle.setFitHeight(350);
        imageViewTitle.setFitWidth(500);

        imageView.setPreserveRatio(true);

        Text text = new Text("press DA button");
        //adding them nodes
        StackPane images = new StackPane();
        images.getChildren().addAll(imageView,imageViewTitle,text);
        StackPane.setAlignment(text, Pos.BOTTOM_CENTER);
        text.setStyle("-fx-font-size: 30;");
        text.setFill(Color.WHITE);

        layout.setOnMouseClicked(event -> {
            fadeOut(1,text);
            ChoiceScreen.gameTypeWindow(primaryStage);
        });

        layout.setCenter(images);
        //layout.setStyle("-fx-background-image: url(bigMan.jpg) ");
        Scene window = new Scene(layout);

        primaryStage.setTitle("DaGameLessGo");
        primaryStage.setScene(window);
        primaryStage.show();

        //fading in the title
        fadeIn(5,imageViewTitle);
        //fading in the indication
        fadeIn(10,text);
    }

    private void fadeIn(int sec,Node node){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(sec),node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }
    private void fadeOut(int sec,Node node){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(sec),node);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }

}
