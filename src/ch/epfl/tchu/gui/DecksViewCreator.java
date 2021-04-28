package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ch.epfl.tchu.gui.ActionHandlers.DrawCardHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawTicketsHandler;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.event.MouseEvent;


abstract class DecksViewCreator {
    private final static int CARD_CONTOUR_WIDTH = 60;
    private final static int CARD_CONTOUR_HEIGHT = 90;
    private final static int CARD_FILLED_WIDTH = 40;
    private final static int CARD_FILLED_HEIGHT = 70;
    private final static int CARD_IMAGE_WIDTH = 40;
    private final static int CARD_IMAGE_HEIGHT = 70;
    private final static int BUTTON_SCALE_WIDTH = 50;
    private final static int BUTTON_SCALE_HEIGHT = 5;
    private final static String NULL_COLOR_CSS_CLASS = "NEUTRAL";
    /**
     * return the hand view
     * @param obsGS
     * @return
     */
    public static HBox createHandView(ObservableGameState obsGS){
        HBox handView = new HBox();
        handView.getStylesheets().addAll("decks.css","colors.css");
        ListView<Ticket> billets = new ListView<>();
        billets.setId("tickets");
        HBox hand = new HBox();
        hand.setId("hand-pane");
        for (Card card: Card.ALL){
            ReadOnlyIntegerProperty amount = obsGS.getNumberOfCardInHand().get(card);

            StackPane general = new StackPane();
            general.getStyleClass().add("card");
            //setting visible or not depending on the int from above
            general.visibleProperty().bind(Bindings.greaterThan(amount, 0));
            //little question of colors -> specific case with locomotives
            if (card.color()!=null)
                general.getStyleClass().add(card.color().name());
            else
                general.getStyleClass().add("NEUTRAL");
            //drawing the card
            Rectangle contour = new Rectangle(CARD_CONTOUR_WIDTH,CARD_CONTOUR_HEIGHT);
            contour.getStyleClass().add("outside");
            Rectangle interior = new Rectangle(CARD_FILLED_WIDTH,CARD_FILLED_HEIGHT);
            interior.getStyleClass().addAll("filled","inside");
            Rectangle image = new Rectangle(CARD_IMAGE_WIDTH,CARD_IMAGE_HEIGHT);
            image.getStyleClass().add("train-image");
            //drawing the counter
            Text counter = new Text();
            counter.getStyleClass().add("count");
            //seetting visibility and amount to show
            counter.textProperty().bind(Bindings.convert(amount));
            counter.visibleProperty().bind(Bindings.greaterThan(amount,1));
            //adding all these nodes as the "general" children
            general.getChildren().addAll(contour,interior,image,counter);
            //adding the ganaeral node as a child to the hand
            hand.getChildren().add(general);
        }
        handView.getChildren().addAll(billets,hand);
        return handView;
    };

    /**
     * returns the view on the card deck
     * @param obsGS
     * @param drawTicketH
     * @param drawCardH
     * @return
     */
    public static VBox createCardsView(ObservableGameState obsGS
            ,ReadOnlyObjectProperty<DrawTicketsHandler> drawTicketH
            ,ReadOnlyObjectProperty<DrawCardHandler> drawCardH){
        VBox cardView = new VBox();
        cardView.getStylesheets().addAll("decks.css","colors.css");
        cardView.setId("card-pane");

        /**
         *  disabledProperty TO BE ADDED!!
         */
        Button ticketDeckButt = new Button();
        ticketDeckButt.getStyleClass().add("gauged");
        //calling the drawTicket handler when button is pressed
        ticketDeckButt.setOnAction(e -> drawTicketH.get().onDrawTickets());

        Button cardDeckButt = new Button();
        cardDeckButt.getStyleClass().add("gauged");
        //calling the drawCard handler when button is pressed
        cardDeckButt.setOnAction(e -> drawCardH.get().onDrawCard(Constants.DECK_SLOT));

        Group cards   = new Group();
        Group tickets = new Group();
        Rectangle cardBack   = new Rectangle(BUTTON_SCALE_WIDTH,BUTTON_SCALE_HEIGHT);
        Rectangle ticketBack = new Rectangle(BUTTON_SCALE_WIDTH,BUTTON_SCALE_HEIGHT);
        Rectangle cardFore   = new Rectangle(BUTTON_SCALE_WIDTH,BUTTON_SCALE_HEIGHT);
        Rectangle ticketFore = new Rectangle(BUTTON_SCALE_WIDTH,BUTTON_SCALE_HEIGHT);

        //drawing percentage of decks fullness
        cardFore.widthProperty().bind(
                obsGS.cardDeckCapacityProperty().multiply(50).divide(100));
        ticketFore.widthProperty().bind(
                obsGS.ticketDeckCapacityProperty().multiply(50).divide(100));

        cards.getChildren().addAll(cardBack,cardFore);
        tickets.getChildren().addAll(ticketBack,ticketFore);
        //finalising the buttons
        cardDeckButt.setGraphic(cards);
        ticketDeckButt.setGraphic(tickets);

        for (ReadOnlyObjectProperty<Card> cp : obsGS.getVisibleCards()){
            StackPane general = new StackPane();
            general.getStyleClass().add("card");
            if (cp.get() != Card.LOCOMOTIVE)
                general.getStyleClass().add(cp.get().color().name());
            else
                general.getStyleClass().add("NEUTRAL");

            Rectangle contour = new Rectangle(CARD_CONTOUR_WIDTH,CARD_CONTOUR_HEIGHT);
            contour.getStyleClass().add("outside");
            Rectangle interior = new Rectangle(CARD_FILLED_WIDTH,CARD_FILLED_HEIGHT);
            interior.getStyleClass().addAll("filled","inside");
            Rectangle image = new Rectangle(CARD_IMAGE_WIDTH,CARD_IMAGE_HEIGHT);
            image.getStyleClass().add("train-image");

            general.getChildren().addAll(contour,interior,image);
            cardView.getChildren().add(general);
            //adding a listener on the color of the card by changing the color class if something changes
            cp.addListener(new ChangeListener<Card>() {
                @Override
                public void changed(ObservableValue<? extends Card> observable, Card oldValue, Card newValue) {
                    String oldColor = oldValue != Card.LOCOMOTIVE ? oldValue.color().name() : NULL_COLOR_CSS_CLASS;
                    general.getStyleClass().remove(oldColor);
                    String newColor = newValue != Card.LOCOMOTIVE ? newValue.color().name() : NULL_COLOR_CSS_CLASS;
                    general.getStyleClass().add(newColor);
                }
            });
        }
        cardView.getChildren().addAll(ticketDeckButt,cardDeckButt);
        return cardView;
    }

}
