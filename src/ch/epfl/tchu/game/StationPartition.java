package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class StationPartition implements StationConnectivity{

    private final List< List<Station> >partitions;
    //private final Set<Station> representatives;

    private StationPartition(List<Integer> list){
        List <List<Station>> partitions = new ArrayList<>();
        List<Station> partition = new ArrayList<>();
        this.partitions = partitions ;
        //this.representatives = representatives;
    }
    @Override
    public boolean connected(Station s1, Station s2) {
        return false;
    }

    public static class Builder{
        public Builder(int stationCount){
            Preconditions.checkArgument(stationCount>=0);

        }

    }
}
