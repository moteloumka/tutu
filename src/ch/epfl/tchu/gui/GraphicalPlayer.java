package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import static ch.epfl.tchu.gui.MapViewCreator.createMapView;
import static ch.epfl.tchu.gui.DecksViewCreator.*;
import static ch.epfl.tchu.gui.InfoViewCreator.*;
import static javafx.application.Platform.isFxApplicationThread;

import ch.epfl.tchu.gui.ActionHandlers.*;


import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GraphicalPlayer {
    private final Stage window;
    private final static String WINDOW_NAME_TEMPLATE = "tCHu — %s";
    private final static int INFO_TAB_SIZE = 5;
    private final int TICKETS_MIN_CONSTANT_THINGY = 2;
    private final ObservableGameState observableGameState;
    private final ObservableList<Text> infoList = FXCollections.observableList(new ArrayList<>());
    private final ObjectProperty<ClaimRouteHandler> claimRouteH = new SimpleObjectProperty<>(null);
    private final ObjectProperty<DrawTicketsHandler> drawTicketsH = new SimpleObjectProperty<>(null);
    private final ObjectProperty<DrawCardHandler> drawCardH = new SimpleObjectProperty<>(null);

    public Scene scene;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId,String> playerNames){
        assert isFxApplicationThread();

        this.observableGameState = new ObservableGameState(playerId);
        this.window = new Stage();
        window.setTitle(String.format(WINDOW_NAME_TEMPLATE,playerNames.get(playerId)));

        Pane map = createMapView(observableGameState, claimRouteH, this::chooseClaimCards);
        VBox cardsView = createCardsView(observableGameState, drawTicketsH, drawCardH);
        HBox handView = createHandView(observableGameState);
        VBox infos = createInfoView(playerId, playerNames, observableGameState, infoList);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(map);
        borderPane.setRight(cardsView);
        borderPane.setBottom(handView);
        borderPane.setLeft(infos);

        scene = new Scene(borderPane);
        window.setScene(scene);
    }

    public void setState(PublicGameState pubGS, PlayerState playerState){
        assert isFxApplicationThread();
        observableGameState.setState(pubGS,playerState);
    }

    public void receiveInfo(String message){
        assert isFxApplicationThread();
        if (infoList.size() >= INFO_TAB_SIZE)
            infoList.remove(0);
        infoList.add(new Text(message));
    }

    public void startTurn(DrawTicketsHandler drawTicketsH
            , DrawCardHandler drawCardH
            , ClaimRouteHandler claimRouteH){
        assert isFxApplicationThread();

        DrawCardHandler finalDrawCardH = slot -> {
            drawCardH.onDrawCard(slot);
            System.out.println("ticket drawn");
            drawCard(drawCardH);
            this.drawTicketsH.set(null);
            this.claimRouteH.set(null);
        };
        if (observableGameState.canDrawCards())
            this.drawCardH.set(finalDrawCardH);
        else
            this.drawCardH.set(null);

        DrawTicketsHandler finalDrawTicketH = () -> {
            drawTicketsH.onDrawTickets();
            setHandlersToNull();
        };
        if(observableGameState.canDrawTickets())
            this.drawTicketsH.set(finalDrawTicketH);
        else
            this.drawTicketsH.set(null);

        ClaimRouteHandler finalClaimRouteH = (route, cards) -> {
            claimRouteH.onClaimRoute(route,cards);
            setHandlersToNull();
        };
        this.claimRouteH.set(finalClaimRouteH);
    }

    public void chooseTickets(SortedBag<Ticket> tickets,ChooseTicketsHandler chooseTicketsH){
        assert isFxApplicationThread();
        Preconditions.checkArgument(tickets.size()==3||tickets.size()==5
                ,"the amount of tickets to chose from is incorrect");
        String indication = "choose your tickets,retard";
        ObservableList<Ticket> obsTickets = FXCollections.observableArrayList(tickets.toList());

        DefaultWindowBuilder<Ticket> newWindow = new DefaultWindowBuilder<>(indication
                ,true,obsTickets
                ,null, tickets.size()-TICKETS_MIN_CONSTANT_THINGY, false);
        newWindow.button.setOnAction(e->{
            newWindow.defWindow.hide();
            newWindow.options.getSelectionModel().getSelectedItems();
            List<Ticket> fuckingTickets = new ArrayList<>(newWindow.options.getSelectionModel().getSelectedItems());
            SortedBag<Ticket> fuckingBag = SortedBag.of(fuckingTickets);
            chooseTicketsH.onChooseTickets(fuckingBag);
        });
        newWindow.defWindow.setScene(newWindow.scene);
    }

    /**
     * this is probably (surely) false for now
     * @param drawCardH
     */
    public void drawCard(DrawCardHandler drawCardH){
        assert isFxApplicationThread();
        System.out.println("in drawing methode");

        DrawCardHandler finalDrawCardH = slot -> {
            drawCardH.onDrawCard(slot);
            System.out.println("drew second card");
            setHandlersToNull();
        };
        if (observableGameState.canDrawCards())
            this.drawCardH.set(finalDrawCardH);
        else
            this.drawCardH.set(null);

        this.drawTicketsH.set(null);
        this.claimRouteH.set(null);
    }

    public void chooseClaimCards(List<SortedBag<Card>> options, ChooseCardsHandler chooseCardsH){
        assert isFxApplicationThread();
        //same code as in chooseAddCards idk if it's work caring about??
        Convertors.CardBagStringConverter converter = new Convertors.CardBagStringConverter();
        ObservableList<SortedBag<Card>> observableOptions = FXCollections.observableArrayList(options);
        DefaultWindowBuilder<SortedBag<Card>> newWindow = new DefaultWindowBuilder<>("sup"
                ,false
                ,observableOptions
                ,converter ,0,false);
        newWindow.button.setOnAction(e->{
            newWindow.defWindow.hide();
            SortedBag<Card> choice = newWindow.options.getSelectionModel().getSelectedItem();
            chooseCardsH.onChooseCards(choice);
        });
        newWindow.defWindow.setScene(newWindow.scene);
    }

    public void chooseAdditionalCards(List<SortedBag<Card>> options, ChooseCardsHandler chooseCardsH){
        assert isFxApplicationThread();

        Convertors.CardBagStringConverter converter = new Convertors.CardBagStringConverter();
        ObservableList<SortedBag<Card>> observableOptions = FXCollections.observableArrayList(options);

        DefaultWindowBuilder<SortedBag<Card>> newWindow = new DefaultWindowBuilder<>("sup"
                ,false
                ,observableOptions
                ,converter ,0,false);
        newWindow.button.setOnAction(e->{
            newWindow.defWindow.hide();
            SortedBag<Card> choice = newWindow.options.getSelectionModel().getSelectedItem();
            //idk can this thing be null?
            chooseCardsH.onChooseCards(choice);
        });
        newWindow.defWindow.setScene(newWindow.scene);
    }

    private void setHandlersToNull(){
        this.drawCardH.set(null);
        this.drawTicketsH.set(null);
        this.claimRouteH.set(null);
    }

    /**
     * is it really worth the hustle?
     * @param <T>
     */
   private class  DefaultWindowBuilder <T>{
        private final Button button;
        private final Stage defWindow;
        private final ListView<T> options;
        private final Scene scene;

        private DefaultWindowBuilder(String indication
                , boolean multipleChoice
                , ObservableList<T> elements
                , StringConverter<T> converter
                , int minObjSelected
                , boolean closableByCross){

            this.defWindow = new Stage(StageStyle.UTILITY);
            defWindow.initModality(Modality.WINDOW_MODAL);
            defWindow.initOwner(GraphicalPlayer.this.window);
            VBox content = new VBox();

            TextFlow textFlow = new TextFlow();
            Text text = new Text();
            text.textProperty().set(indication);
            textFlow.getChildren().add(text);

            this.options = new ListView<>(elements);
            if(converter != null)
                options.setCellFactory(v -> new TextFieldListCell<>(converter));
            if (multipleChoice)
                options.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            this.button = new Button();
            //disables button if the necessary amount of stuff wasn't selected
            if (minObjSelected > 0)
                button.disableProperty()
                        .bind(new SimpleBooleanProperty(
                                Bindings.size(options.getSelectionModel().getSelectedItems()).get() < minObjSelected));
            //action on button left to define later on
            if (closableByCross)
                defWindow.setOnCloseRequest(Event::consume);

            content.getChildren().add(textFlow);
            content.getChildren().add(options);
            content.getChildren().add(this.button);

            this.scene = new Scene(content);
        }
   }

    private static class Convertors {
        private static class CardBagStringConverter extends StringConverter<SortedBag<Card>> {
            @Override
            public String toString(SortedBag<Card> bag) {
                return Info.cardNumerator(bag);
            }

            @Override
            public SortedBag<Card> fromString(String string) {
                SortedBag.Builder<Card> bobTheBuilder = new SortedBag.Builder<Card>();
                String[] ciphers = string
                        .split(Pattern.quote(Info.COMA_SEPARATOR+" "), -1);
                //TODO FINISH DECODING PROPERLY
                return bobTheBuilder.build();
            }
        }

    }
}
