package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.StringsFr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 * a Route links two stations, can be controlled by a player
 */
public final class Route {
    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;

    /**
     * enum Level: the two states a road can have
     */
    public enum Level{
        UNDERGROUND,OVERGROUND;
    }

    /**
     *
     * @param id the route name, unique for each road
     * @param station1 station of departure
     * @param station2 station of arrival
     * @param length length of the road (integer n 1<=n<=6)
     * @param level specification if the route passes through a tunnel or is overground
     * @param color color of wagons that can be used to privatize the route
     *              (if color null is used, any color can then be used get the route)
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        Preconditions.checkArgument(!station1.equals(station2) &&
                !(length<Constants.MIN_ROUTE_LENGTH || length>Constants.MAX_ROUTE_LENGTH));
        this.id = Objects.requireNonNull(id,"route id can't be null");
        this.station1 = Objects.requireNonNull(station1,"station 1 can't be null when constructing a route");
        this.station2 = Objects.requireNonNull(station2,"station 2 can't be null when constructing a route");
        this.length = length;
        this.level = Objects.requireNonNull(level,"level can't be null when constructing a route");
        this.color = color;
    }

    /**
     *
     * @return the name of a route
     */
    public String id() {
        return id;
    }

    /**
     *
     * @return the departure station of a route
     */
    public Station station1() {
        return station1;
    }

    /**
     *
     * @return the destination station of a route
     */
    public Station station2() {
        return station2;
    }

    /**
     *
     * @return the length of a route
     */
    public int length() {
        return length;
    }

    /**
     *
     * @return is a route underground or overground
     */
    public Level level() {
        return level;
    }

    /**
     *
     * @return color of cards that have to be used to own a route
     */
    public Color color() {
        return color;
    }

    /**
     *
     * @return list with two elements: first is the departure station, the second is the destination station
     */
    public List<Station> stations(){
       return List.of(this.station1,this.station2);
    }

    /**
     *
     * @param station a station that is on one of the two sides of the route
     * @return the station that is on the opposite side of the rout from the  one, given as the parameter
     */
    public Station stationOpposite(Station station){
        Preconditions.checkArgument(station==this.station1||station==this.station2);
        return station == station1 ? this.station2 : this.station1;
    }

    public Route makeRouteOpposite (Route route){
        return new Route(this.id, this.station2, this.station1, this.length, this.level, this.color);
    }

    /**
     * @return list of all possible ways to obtain the given route
     *
     * the list is given  in a specific order,
     * starting from only cards with colors (in their order: BLACK, VIOLET,...,WHITE),
     * then adding one locomotive in the end each step.
     * last list to be presented, consists of locomotives only
     */
    public List<SortedBag<Card>> possibleClaimCards(){
        List<SortedBag<Card>> list = new ArrayList<>();


        if(this.level==Level.UNDERGROUND){
            if(this.color == null){
                for(int i=0;i<this.length;++i){
                    //creating all combinations  by going through all the colors
                    for(Color color: Color.values()){
                        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
                        builder.add(this.length-i,Card.of(color));
                        //adding locomotives as missing cards
                        builder.add(i,Card.LOCOMOTIVE);
                        SortedBag<Card> card = builder.build();
                        //adding the new combinations to the list
                        list.add(card);
                    }
                }
            } else {
                for (int i = 0; i<this.length; i++) {
                    SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
                    builder.add(this.length-i,Card.of(this.color));
                    builder.add(i,Card.LOCOMOTIVE);
                    SortedBag<Card> card = builder.build();
                    list.add(card);
                }
            }
            list.add(SortedBag.of(this.length,Card.LOCOMOTIVE));
        }else{
            if (this.color == null){
                for( Color  color: Color.values()){
                    SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
                    builder.add(this.length,Card.of(color));
                    SortedBag<Card> card = builder.build();
                    list.add(card);
                }
            }else{
                SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
                builder.add(this.length,Card.of(this.color));
                SortedBag<Card> card = builder.build();
                list.add(card);
            }
        }

        //adds the line of locomotives in the end (is done in both situations and avoids a new for iteration)

        return list;
    }

    /**
     *
     * @param claimCards cards presented by the player
     * @param drawnCards cards drawn from the deck (3)
     * @return the number of cards the player has to add to obtain the tunnel
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards){
        int count = 0;
        List<Color> color = new ArrayList<>();
        //we don't really have anything that checks the different cards provided in claimCards (?)
        Preconditions.checkArgument(this.level==Level.UNDERGROUND
                && drawnCards.size()==Constants.ADDITIONAL_TUNNEL_CARDS);

        for (int i = 0; i < claimCards.size(); i++) {
            //find if at least one CAR Card was used (not locomotive)
            if(claimCards.get(i) != null){
                //the only way we have found to save the color, since it's an enum
                color.add(claimCards.get(i).color());
            }
        }
        for (int i = 0; i < drawnCards.size(); i++) {
            //add a card to the counter if it's the same color as the one found before (color) or if it's a locomotive
            //if (color) is a locomotive (null), it's still counted once
            if( drawnCards.get(i).color() == null || (!color.isEmpty() && drawnCards.get(i).color().equals(color.get(0))) ){
                ++count;
            }
        }
        return count;
    }

    /**
     * @return number of points obtained by a player for obtaining the route
     */
    public int claimPoints(){
        List<Integer> points = Constants.ROUTE_CLAIM_POINTS;
        //no need in precondition check bc there is one in the class constructor
        //shall we start from 1 instead of 0?
        for (int i = 0; i < points.size(); i++) {
            if (this.length==i){
                return points.get(i);
            }
        }
        //not sure if this is a good decision, technically the method isn't ever supposed to get here
        //but without a return in the end the program wouldn't compile
        return 0;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (this.id.equals(((Route) obj).id)) return true;
        else return false;
    }

    @Override
    public String toString() {
        return station1.toString() + StringsFr.EN_DASH_SEPARATOR + station2.toString();
    }
}
