package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;


/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class StationPartition implements StationConnectivity{

    private final int[] references;

    /**
     * Private constructor that should only be called from the builder
     * @param ref reference Array of ints
     */
    private StationPartition(int[] ref){
        this.references = ref ;
    }

    /**
     * Verifies if the two stations are in the same partition ( are connected)
     * (reference the same station in Array references)
     * @param s1 first station
     * @param s2 second station
     * @return true if connected or are the same station false otherwise
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        if(s1.id() >= references.length || s2.id() >= references.length)
            return s1.equals(s2);
        return references[s1.id()] == references[s2.id()];
    }

    public final static class Builder{
        //future Array of references
        private int [] buildTab;

        /**
         * public StationPartition.Builder builder
         * @param stationCount has to be a non negative int,
         * represents the number of Station id's to be considered upon construction
         */
        public Builder(int stationCount){
            Preconditions.checkArgument(stationCount>=0);
            this.buildTab = new int[stationCount];
            for(int i=0; i<stationCount; ++i){
                //each Station represents it self in the beginning
                this.buildTab[i] = i;
            }
        }

        /**
         * connects two stations by changing the index of the second one in the reference Array
         * by the number referenced by the other station
         * @param station1 the one, who's reference will be taken into account
         * @param station2 the one, who's reference will be changed
         * @return same instance of the builder but with the Array modified
         */
        public Builder connect(Station station1, Station station2){
            this.buildTab[station2.id()]= representative(station1.id());
            return this;
        }

        /**
         * creates a new instance of StationPartition, passing the Array buildTab as the reference tab
         * @return new instance of StationPartition
         */
        public StationPartition build(){
            for(int i=0;i<this.buildTab.length;++i){
                //making sure that different partitions
                //will all have the same representative
                this.buildTab[i] = representative(i);
            }
            return new StationPartition(this.buildTab);
        }

        /**
         * method that finds the "real" reference of a station
         * @param rep the station id
         * @return it's reference int he Array
         */
        private int representative(int rep){
            int answer;
            do{
               answer = this.buildTab[rep];
            }while (answer!=this.buildTab[answer]);
            return answer;
        }


    }
}
