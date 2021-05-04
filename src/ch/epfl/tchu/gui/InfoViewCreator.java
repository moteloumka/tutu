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

class InfoViewCreator {
    private static final int CIRCLE_RADIUS = 5;
    private InfoViewCreator(){}
    public static VBox createInfoView(PlayerId playerId
            , Map<PlayerId,String> playerNames
            , ObservableGameState obsGS
            , ObservableList<Text> infos){
        VBox mainColumn = new VBox();
        mainColumn.getStylesheets().addAll("styles/info.css","styles/colors.css");
        VBox playerStats = new VBox();
        playerStats.getStyleClass().add("player-stats");

        TextFlow thisPlayerStats = individualPlayerStatsCreator(playerId,playerNames,obsGS);
        TextFlow otherPlayerStats= individualPlayerStatsCreator(playerId.next(),playerNames,obsGS);
        playerStats.getChildren().addAll(thisPlayerStats,otherPlayerStats);

        Separator separator = new Separator();

        TextFlow messages = new TextFlow();
        messages.getStyleClass().add("game-info");
        Bindings.bindContent(messages.getChildren(),infos);

        mainColumn.getChildren().addAll(playerStats,separator,messages);
        return mainColumn;
    }

    private static TextFlow individualPlayerStatsCreator(PlayerId playerId
            ,Map<PlayerId,String> playerNames
            ,ObservableGameState obsGS ){
        TextFlow individualStat = new TextFlow();
        individualStat.setId(playerId.name());
        Circle circle = new Circle(CIRCLE_RADIUS);
        circle.setId("filled");
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

