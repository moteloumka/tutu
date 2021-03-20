package ch.epfl.test;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationPartitionTest {

    //copies everything from ChMap
    private final static List<Station> stationFull = List.copyOf(ChMap.stations());
    private final static List<Route> routeFull = List.copyOf(ChMap.routes());
    private final static List<String> routeIDs = getRoutesIDs();
    private final static List<Ticket> ticketFull = List.copyOf(ChMap.tickets());

    //methods
    private final static List<String> getRoutesIDs() {
        List<String> routesIDs = new ArrayList<>();
        for (Route route : routeFull) {
            routesIDs.add(route.id());
        }
        return routesIDs;
    } //used to get a route from the ID
    private final static Route getRoute(String id) {
        Preconditions.checkArgument(routeIDs.contains(id),
                "given id doesn't belong to the route list");
        for (Route route : routeFull) {
            if (id.equals(route.id())) {
                return route;
            }
        }
        return null;
    }

    @Test
    void connected() {
        List<Station> stations = List.copyOf(stationFull);
        StationPartition.Builder builder = new StationPartition.Builder(stations.size());

        builder.connect(getRoute("BAL_OLT_1").station1(), getRoute("BAL_DE1_1").station2());

        StationPartition partition = builder.build();

        assertEquals(false, partition.connected(getRoute("BAL_OLT_1").station1(), getRoute("BAL_OLT_1").station2()));

    }
}