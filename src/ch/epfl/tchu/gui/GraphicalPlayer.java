package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import static ch.epfl.tchu.gui.MapViewCreator.createMapView;
import static ch.epfl.tchu.gui.DecksViewCreator.*;
import static ch.epfl.tchu.gui.InfoViewCreator.*;
import static javafx.application.Platform.isFxApplicationThread;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.epfl.tchu.gui.ActionHandlers.*;


import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
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
import javafx.util.StringConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
/**
 *  @author Nikolay (314355)
 *  @author Gullien (316143)
 */

/**
 * together with the class GraphicalPlayerAdapter (who's attribute this class is)
 * they end up implementing the interface Player
 * thus making it possible to communicate with the user
 * this class communicates the changes made by the user to the game and manages the "showing" part of the gui
 * also this class manages the creation of new windows, asking the user to make certain choices during the game
 */
public class GraphicalPlayer {
    private final Stage window;
    private final static String WINDOW_NAME_TEMPLATE = "tCHu — %s";
    private final static int INFO_TAB_SIZE = 5;
    private final ObservableGameState observableGameState;
    private final ObservableList<Text> infoList = FXCollections.observableList(new ArrayList<>());
    private final ObjectProperty<ClaimRouteHandler> claimRouteH = new SimpleObjectProperty<>(null);
    private final ObjectProperty<DrawTicketsHandler> drawTicketsH = new SimpleObjectProperty<>(null);
    private final ObjectProperty<DrawCardHandler> drawCardH = new SimpleObjectProperty<>(null);

    /**
     * creates a new instance of this class assigned to a particular player
     * @param playerId the owner of this gui
     * @param playerNames names of all the players
     */
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

        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.show();
    }

    /**
     * updates the components of observableGameState
     * @param pubGS the new PublicGameState to be passed on
     * @param playerState the new PlayerState to be passed on
     */
    public void setState(PublicGameState pubGS, PlayerState playerState){
        assert isFxApplicationThread();
        observableGameState.setState(pubGS,playerState);
    }

    /**
     * this method is called when a new message has to appear n the info column
     * @param message new line to show
     */
    public void receiveInfo(String message){
        assert isFxApplicationThread();
        //making sure only (5) messages are shown at a time
        if (infoList.size() >= INFO_TAB_SIZE)
            infoList.remove(0);
        infoList.add(new Text(message));
    }

    /**
     * called upon the beginning of a players turn,
     * the point of this method is to :
     * 1) assigning the handlers passed as parameters to the handler properties of this particular instance
     * so they can used in other methods of the class.
     * 2) slightly modify the handlers making it impossible to do certain actions after doing an action
     * (for example after picking a ticket or claiming a road, all handlers are set to null,
     * thus making it impossible for the player to do any actions before their turn restarts )
     * @param drawTicketsH the handler that will manage the ticket drawing process (implemented in the adapter class)
     * @param drawCardH the handler that will manage the card drawing (implemented in the adapter class)
     * @param claimRouteH the handler that will manage the claiming process (implemented in the adapter class)
     */
    public void startTurn(DrawTicketsHandler drawTicketsH
            , DrawCardHandler drawCardH
            , ClaimRouteHandler claimRouteH){
        assert isFxApplicationThread();

        DrawCardHandler finalDrawCardH = slot -> {
            drawCardH.onDrawCard(slot);
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

    /**
     * called when player chooses tickets
     * creates a window that asks which tickets the player wants to keep,
     * communicates to the program what tickets were picked by the player
     * @param options tickets to chose from
     * @param chooseTicketsH handles the choice of the tickets
     */
    public void chooseTickets(SortedBag<Ticket> options,ChooseTicketsHandler chooseTicketsH){
        assert isFxApplicationThread();
        Preconditions.checkArgument(options.size()==3||options.size()==5
                ,"the amount of tickets to chose from is incorrect");
        String indication = "choose your tickets, kind sir";
        ObservableList<Ticket> obsTickets = FXCollections.observableArrayList(options.toList());

        DefaultWindowBuilder<Ticket> newWindow = new DefaultWindowBuilder<>(indication
                ,true
                , obsTickets
                ,null, options.size()-2, false);
        newWindow.button.setOnAction(e->{
            newWindow.defaultWindow.hide();
            newWindow.options.getSelectionModel().getSelectedItems();
            List<Ticket> fuckingTickets = new ArrayList<>(newWindow.options.getSelectionModel().getSelectedItems());
            SortedBag<Ticket> fuckingBag = SortedBag.of(fuckingTickets);
            chooseTicketsH.onChooseTickets(fuckingBag);
        });
        newWindow.defaultWindow.setScene(newWindow.scene);
        newWindow.defaultWindow.show();
    }

    /**
     * called when the player has already drawn a card
     * and the only thing they can do is thus pulling another card
     * @param drawCardH the handler that will manage the card drawing process (implemented in the adapter class)
     */
    public void drawCard(DrawCardHandler drawCardH){
        assert isFxApplicationThread();

        DrawCardHandler finalDrawCardH = slot -> {
            drawCardH.onDrawCard(slot);
            setHandlersToNull();
        };
        if (observableGameState.canDrawCards())
            this.drawCardH.set(finalDrawCardH);
        else
            this.drawCardH.set(null);
        this.drawTicketsH.set(null);
        this.claimRouteH.set(null);
    }

    /**
     * called when player chooses the cards they want to claim a road with
     * creates a window that asks which cards the player wants to use,
     * communicates to the program what cards were picked by the player
     * @param options the different sets of cards they can use
     * @param chooseCardsH action handler
     */
    public void chooseClaimCards(List<SortedBag<Card>> options, ChooseCardsHandler chooseCardsH){
        assert isFxApplicationThread();

        CardBagStringConverter converter = new CardBagStringConverter();
        ObservableList<SortedBag<Card>> observableOptions = FXCollections.observableArrayList(options);
        String indication = "Choose which cars you wish to claim the route with";

        DefaultWindowBuilder<SortedBag<Card>> newWindow = new DefaultWindowBuilder<>(indication
                ,false
                ,observableOptions
                ,converter ,0,false);
        newWindow.button.setOnAction(e->{
            newWindow.defaultWindow.hide();
            SortedBag<Card> choice = newWindow.options.getSelectionModel().getSelectedItem();
            chooseCardsH.onChooseCards(choice);
        });
        newWindow.defaultWindow.setScene(newWindow.scene);
        newWindow.defaultWindow.show();
    }

    /**
     * same as chooseClaimCards but called upon when additional cards are necessary
     * @param options the different sets of cards they can use
     * @param chooseCardsH action handler
     */
    public void chooseAdditionalCards(List<SortedBag<Card>> options, ChooseCardsHandler chooseCardsH){
        assert isFxApplicationThread();

        CardBagStringConverter converter = new CardBagStringConverter();
        ObservableList<SortedBag<Card>> observableOptions = FXCollections.observableArrayList(options);
        String indication = "Choose the additional cards or press the button choose to abandon ";

        DefaultWindowBuilder<SortedBag<Card>> newWindow = new DefaultWindowBuilder<>(indication
                ,false
                ,observableOptions
                ,converter ,0,true);
        newWindow.button.setOnAction(e->{
            SortedBag<Card> choice;
            newWindow.defaultWindow.hide();

            if(newWindow.options.getSelectionModel().getSelectedItem() != null)
                choice = newWindow.options.getSelectionModel().getSelectedItem();
            //case when nothing got selected -> player doesn't want to get road
            else choice = SortedBag.of();
            chooseCardsH.onChooseCards(choice);
        });
        newWindow.defaultWindow.setScene(newWindow.scene);
        newWindow.defaultWindow.show();
    }

    private void setHandlersToNull(){
        this.drawCardH.set(null);
        this.drawTicketsH.set(null);
        this.claimRouteH.set(null);
    }



    /**
     * is it really worth the hustle?
     * this class imbriqué creates an instance of a "tipical" window used in this class
     * since certain aspects do change, depending which method the window is used for,
     * we will need access to some of the windows attributes, thus making it impossible
     * to create such a generic window in a method and obliging us to use this solution.
     * @param <T> Type of objects to chose from, they will be shown as a list
     */
   private class  DefaultWindowBuilder <T>{

        private final Button button;
        private final Stage defaultWindow;
        private final ListView<T> options;
        private final Scene scene;

        private DefaultWindowBuilder(String indication
                , boolean multipleChoice
                , ObservableList<T> elements
                , StringConverter<T> converter
                , int minObjSelected
                , boolean closableByCross){

            this.defaultWindow = new Stage(StageStyle.UTILITY);
            defaultWindow.initModality(Modality.WINDOW_MODAL);
            defaultWindow.initOwner(GraphicalPlayer.this.window);
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

            this.button = new Button("Choose");
            //disables button if the necessary amount of stuff wasn't selected
            if (minObjSelected > 0)
                button.disableProperty()
                        .bind(
                                Bindings.lessThan(Bindings.size(options.getSelectionModel().getSelectedItems())
                                        , minObjSelected));
            //action on button left to define later on
            if (!closableByCross)
                defaultWindow.setOnCloseRequest(Event::consume);

            content.getChildren().add(textFlow);
            content.getChildren().add(options);
            content.getChildren().add(this.button);

            this.scene = new Scene(content);
            scene.getStylesheets().add("chooser.css");

        }
   }

        private static class CardBagStringConverter extends StringConverter<SortedBag<Card>> {
            @Override
            public String toString(SortedBag<Card> bag) {
                return Info.cardNumerator(bag);
            }

            @Override
            public SortedBag<Card> fromString(String string) {
                throw new UnsupportedOperationException();
            }

        }
}
