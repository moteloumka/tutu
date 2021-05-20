package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.gui.ActionHandlers.ChooseCardsHandler;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  @author Nikolay (314355)
 *  @author Gullien (316143)
 */

class MapViewCreator {
    //constants for positioning shapes
    private final static int RECTANGLE_WIDTH = 36;
    private final static int RECTANGLE_HEIGHT = 12;
    private final static int CIRCLE_RADIUS = 3;
    private final static int RELATIVE_Y_POSITION = 6;
    private final static int RELATIVE_X_POSITION_1 = 12;
    private final static int RELATIVE_X_POSITION_2 = 24;

    private MapViewCreator(){}

    @FunctionalInterface
    interface CardChooser {
        /**
         * when a player hase to chose one set of cards from a few possible options
         * @param options the possible options
         * @param handler instance of ActionHandlers.ChooseCardsHandler
         *                functional interface that will handle the decision made by the player
         */
        void chooseCards(List<SortedBag<Card>> options,
                         ChooseCardsHandler handler);
    }

    /**
     * creates a new instance of Pane which will have the map image a background,
     * contain all the routes in the game as rectangles
     * (with PlayerId specific color and 2 circles if route is owned by the PlayerId)
     * @param obsGS the instance of ObservableGameState that will communicate any changes
     *              to the visual representation of the map
     * @param claimRouteH handler called upon when a player claims a route
     * @param cardChooser handler called upon when a player chooses a card
     * @return new instance of Pane as described above
     */
    public static Pane createMapView(ObservableGameState obsGS
            , ObjectProperty<ClaimRouteHandler> claimRouteH
            , CardChooser cardChooser){
        Pane carte = new Pane();
        carte.getStylesheets().addAll("map.css","colors.css");
        ImageView imageView = new ImageView();
        carte.getChildren().add(imageView);
        //creating each road
        for (Route route: ChMap.routes()){
            //create route group and set basic params like color/level
            Group routeGroup = setRouteParams(route);
            //creating each piece of a road (case on map)
            for (int i=1; i<= route.length();++i){
                Group caseOnMap = caseOnMap(route,i);
                routeGroup.getChildren().add(caseOnMap);
            }
            //adding a listener on who owns the route
            obsGS.owner(route).addListener((observable, oldValue, newValue) ->
                    routeGroup.getStyleClass().add(newValue.name()));
            //making sure the player can't click on a road when they can't get possession of it
            routeGroup.disableProperty().bind(
                    claimRouteH.isNull().or(obsGS.claimable(route).not()));
            //launches the protocol of getting a new route
            routeGroup.setOnMouseClicked( event -> {

                List<SortedBag<Card>> possibleClaimCards = obsGS.possibleClaimCards(route);
                //if the player has only one combination of possible cards, there's no need to elaborate
                if (possibleClaimCards.size() == 1)
                    claimRouteH.get().onClaimRoute(route, possibleClaimCards.get(0));
                //bunch of handlers show the possibilities on screen and handle the choice made by the player
                else {
                    ChooseCardsHandler chooseCardsH =
                            chosenCards -> claimRouteH.get().onClaimRoute(route, chosenCards);
                    cardChooser.chooseCards(possibleClaimCards, chooseCardsH);
                }
            });
            //adding the route with everything connected to the highest parent -> the Pane
            carte.getChildren().add(routeGroup);
        }
        return carte;
    }

    private static Group caseOnMap(Route route, int i){
        Group caseOnMap = new Group();
        caseOnMap.setId(route.id()+"_"+i);
        //creating the voie (whatever that is)
        Rectangle voie = new Rectangle(RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
        voie.getStyleClass().addAll("track","filled");
        //creating the wagon
        Group wagon = new Group();
        wagon.getStyleClass().add("car");
        Rectangle wagonRect = new Rectangle(RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
        wagonRect.getStyleClass().add("filled");
        Circle wagonCircle1 = new Circle(RELATIVE_X_POSITION_1,RELATIVE_Y_POSITION,CIRCLE_RADIUS);
        Circle wagonCircle2 = new Circle(RELATIVE_X_POSITION_2,RELATIVE_Y_POSITION,CIRCLE_RADIUS);
        wagon.getChildren().addAll(wagonRect,wagonCircle1,wagonCircle2);
        //connecting everything together
        caseOnMap.getChildren().addAll(voie,wagon);
        return caseOnMap;
    }

    private static Group setRouteParams(Route route){
        Group routeGroup = new Group();
        routeGroup.setId(route.id());
        routeGroup.getStyleClass().addAll("route");

        if (route.level() == Route.Level.UNDERGROUND )
            routeGroup.getStyleClass().addAll("UNDERGROUND");

        if (route.color() != null)
            routeGroup.getStyleClass().add(route.color().name());
        else
            routeGroup.getStyleClass().add("NEUTRAL");
        return routeGroup;
    }
}
