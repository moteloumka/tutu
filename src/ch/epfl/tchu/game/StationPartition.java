package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class StationPartition implements StationConnectivity {

    private final List<Integer> references;

    /**
     * Private constructor that should only be called from the builder
     *
     * @param ref reference Array of ints
     */
    private StationPartition(List<Integer> ref) {
        this.references = List.copyOf(ref);
    }

    /**
     * Verifies if the two stations are in the same partition ( are connected)
     * (reference the same station in Array references)
     *
     * @param s1 first station
     * @param s2 second station
     * @return true if connected or are the same station false otherwise
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        if (s1.id() >= references.size() || s2.id() >= references.size())
            return s1.equals(s2);
        return references.get(s1.id()).equals(references.get(s2.id()));
    }

    public final static class Builder {
        //future Array of references
        private final int[] buildTab;

        /**
         * public StationPartition.Builder builder
         *
         * @param stationCount has to be a non negative int,
         *                     represents the number of Station id's to be considered upon construction
         */
        public Builder(int stationCount) {
            Preconditions.checkArgument(stationCount >= 0);
            this.buildTab = new int[stationCount];
            for (int i = 0; i < stationCount; ++i) {
                //each Station represents itself in the beginning
                this.buildTab[i] = i;
            }
        }

        /**
         * connects two stations by changing the index of the second one in the reference Array
         * by the number referenced by the other station
         *
         * @param station1 the one, whose reference will be taken into account
         * @param station2 the one, whose reference will be changed
         * @return same instance of the builder but with the Array modified
         */
        public Builder connect(Station station1, Station station2) {
            flatten(this.buildTab[station1.id()], this.buildTab[station2.id()], station2.id());
            return this;
        }

        /**
         * creates a new instance of StationPartition, passing the Array buildTab as the reference tab
         *
         * @return new instance of StationPartition
         */
        public StationPartition build() {
            List<Integer> ref = new ArrayList<>();
            for (int i = 0; i < this.buildTab.length; ++i) {
                //making sure that different partitions
                //will all have the same representative
                ref.add(representative(i));
            }
            return new StationPartition(ref);
        }

        /**
         * method that finds the "real" reference of a station
         *
         * @param rep the station id
         * @return its reference in the Array
         */
        private int representative(int rep) {
            if (this.buildTab[rep] == rep)
                return rep;
            return representative(this.buildTab[rep]);
        }

        private void flatten(int newPointer, int oldPointer, int willChangeId) {
            if (this.buildTab[willChangeId] != newPointer) {
                for(int i =0; i<this.buildTab.length; ++i){
                    if(this.buildTab[i]==oldPointer && i != oldPointer)
                        this.buildTab[i] = newPointer;
                }
                if(this.buildTab[oldPointer]==oldPointer)
                    this.buildTab[oldPointer] = newPointer;
                flatten(newPointer, this.buildTab[oldPointer],oldPointer);
            }
        }
    }
}
