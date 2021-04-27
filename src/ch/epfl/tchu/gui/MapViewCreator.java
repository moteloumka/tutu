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
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;



import java.util.List;


class MapViewCreator {
    //constants for positioning shapes
    private final static int RECTANGLE_WIDTH = 36;
    private final static int RECTANGLE_HEIGHT = 12;
    private final static int CIRCLE_RADIUS = 3;
    private final static int RELATIVE_Y_POSITION = 6;
    private final static int RELATIVE_X_POSITION_1 = 12;
    private final static int RELATIVE_X_POSITION_2 = 24;

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ChooseCardsHandler handler);
    }

    /**
     * I'm not exactly sure about the way I define paths here,
     * that's something we gotta check
     * @param obsGS
     * @param claimRouteH
     * @param cardChooser
     */
    public void createMapView(ObservableGameState obsGS
            , ObjectProperty<ClaimRouteHandler> claimRouteH
            , CardChooser cardChooser ){
        Pane carte = new Pane();
        carte.getStylesheets().addAll("/res/styles/map.css","/res/styles/colors.css");
        ImageView imageView = new ImageView();
        //Image image = new Image("/res/images/map.png");
        //imageView.setImage(image);
        carte.getChildren().add(imageView);
        //creating each road
        for (Route route: ChMap.routes()){
            Group routeGroup = new Group();
            routeGroup.setId(route.id());
            routeGroup.getStyleClass().addAll("route","UNDERGROUND","NEUTRAL");
            //creating each piece of a road (case)
            for (int i=1; i<= route.length();++i){
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
                routeGroup.getChildren().add(caseOnMap);
            }
            //adding the route with everything connected to the highest parent -> the Pane
            carte.getChildren().add(routeGroup);
        }
        for (ReadOnlyObjectProperty<PlayerId> op : obsGS.getRoutesOwners()){
            op.addListener(new ChangeListener<PlayerId>() {
                @Override
                public void changed(ObservableValue<? extends PlayerId> observable, PlayerId oldValue, PlayerId newValue) {
                    //+1 bc fond is added first, obsOG, as this method keep the order of the routes
                    //as they are received from ChMap.routes()
                    //maybe gonna change everything to maps idk
                    int index = 1+obsGS.getRoutesOwners().indexOf(op);
                    carte.getChildren()
                            .get(index)
                            .getStyleClass()
                            .add(newValue.name());
                }
            });
        }
        //doesn't look too good, perhaps maps are the way to go
        List<Route> routes = ChMap.routes();
        for(int i=1;i<carte.getChildren().size();++i){
            Route route = routes.get(i-1);
            carte.getChildren().get(i).disableProperty().bind(
                    claimRouteH.isNull().or(obsGS.claimable(route).not()));

//            if(obsGS.possibleClaimCards(route).size()==1)
//                System.out.println("aaaa");
//            ChooseCardsHandler chooseCardsH;
//            if (obsGS.possibleClaimCards(route).size()>1)
//                 chooseCardsH =
//                        chosenCards -> claimRouteH.onClaimRoute(route, chosenCards);
//            cardChooser.chooseCards(obsGS.possibleClaimCards(route), chooseCardsH);
        }
    }
}
