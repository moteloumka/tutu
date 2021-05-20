package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;


import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import java.util.Map;

import static ch.epfl.tchu.gui.StringsFr.PLAYER_STATS;
/**
 *  @author Nikolay (314355)
 *  @author Gullien (316143)
 */

class InfoViewCreator {
    private static final int CIRCLE_RADIUS = 5;
    private InfoViewCreator(){}
    /**
     * called upon creation of a the gui of a particular player n the class GraphicalPlayer
     * creates the info bar found on the left of the users interface
     * @param playerId the id of thee player who's gui is being created
     * @param playerNames names of the players in the game
     * @param obsGS information will be taken from here to update info state
     * @param infos list of information that will be updated in the GraphicalPlayer,
     *              yet this list will be updated with the method bind
     * @return new instance of VBox that has all the needed information shown
     */
    public static VBox createInfoView(PlayerId playerId
            , Map<PlayerId,String> playerNames
            , ObservableGameState obsGS
            , ObservableList<Text> infos){
        VBox mainColumn = new VBox();
        mainColumn.getStylesheets().addAll("info.css","colors.css");
        VBox playerStats = new VBox();
        playerStats.setId("player-stats");

        TextFlow thisPlayerStats = individualPlayerStatsCreator(playerId,playerNames,obsGS);
        TextFlow otherPlayerStats= individualPlayerStatsCreator(playerId.next(),playerNames,obsGS);
        playerStats.getChildren().addAll(thisPlayerStats,otherPlayerStats);

        Separator separator = new Separator();

        TextFlow messages = new TextFlow();
        messages.setId("game-info");
        Bindings.bindContent(messages.getChildren(),infos);

        mainColumn.getChildren().addAll(playerStats,separator,messages);
        return mainColumn;
    }

    private static TextFlow individualPlayerStatsCreator(PlayerId playerId
            ,Map<PlayerId,String> playerNames
            ,ObservableGameState obsGS ){
        TextFlow individualStat = new TextFlow();
        individualStat.getStyleClass().add(playerId.name());
        Circle circle = new Circle(CIRCLE_RADIUS);
        circle.getStyleClass().add("filled");
        Text text = new Text();
        text.textProperty().bind(Bindings.format(PLAYER_STATS
                ,playerNames.get(playerId)
                ,obsGS.getTicketsInHandCount(playerId)
                ,obsGS.getCardsInHandCount(playerId)
                ,obsGS.getWagonCount(playerId)
                ,obsGS.getConstructionPoints(playerId)));
        individualStat.getChildren().addAll(circle,text);
        return individualStat;
    }
}

