package jovic.dragan.pj2.aerospace;

import jovic.dragan.pj2.Interfaces.Military;
import jovic.dragan.pj2.logger.GenericLogger;
import jovic.dragan.pj2.radar.RadarExporter;
import jovic.dragan.pj2.util.Pair;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class AerospaceUpdatingRunnable implements Runnable {

    private Map<Integer, Map<Integer, Queue<AerospaceObject>>> map;
    private Aerospace aerospace;
    private RadarExporter exporter;

    AerospaceUpdatingRunnable(Map<Integer, Map<Integer, Queue<AerospaceObject>>> map, Aerospace aerospace) {
        this.map = map;
        this.aerospace = aerospace;
        //RadarPreferences radarPreferences = RadarPreferences.load();

        exporter = new RadarExporter(map);
        exporter.start();
    }

    private boolean isInsideOfMap(int x, int y, int width, int height) {
        return x <= width && y <= height && x >= 0 && y >= 0;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            long start = System.currentTimeMillis();
            int mapWidth = aerospace.getPreferences().getFieldWidth(), mapHeight = aerospace.getPreferences().getFieldHeight();
            var mapsIter = map.values().iterator();
            while (mapsIter.hasNext()) {
                var subMap = mapsIter.next();
                var subMapIter = subMap.values().iterator();
                while (subMapIter.hasNext()) {
                    var list = subMapIter.next();
                    var listIter = list.iterator();
                    while (listIter.hasNext()) {
                        AerospaceObject ao = listIter.next();
                        int oldX = ao.getX(), oldY = ao.getY();
                        Pair<Integer, Integer> nextPosition = ao.getNextPosition();
                        if ((ao instanceof MilitaryAircraft) && !((MilitaryAircraft) ao).isForeign() && ((MilitaryAircraft) ao).getFollowing() != null) {
                            MilitaryAircraft aoMil = (MilitaryAircraft) ao;
                            int x1 = aoMil.getFollowing().getX(), y1 = aoMil.getFollowing().getY();
                            if (Math.sqrt((x1 - oldX) * (x1 - oldX) + (y1 - oldY) * (y1 - oldY)) < 1.1) {
                                System.out.println("Domaca letjelica unistava stranu!");
                                map.values().forEach(yMap -> yMap.values().forEach(q -> q.removeIf(qo -> qo.getId() == ((MilitaryAircraft) ao).getFollowing().getId())));
                                ((MilitaryAircraft) ao).setFollowing(null);
                            }
                        }
                        if (!isInsideOfMap(nextPosition.getFirst(), nextPosition.getSecond(), mapWidth, mapHeight)) {
                            listIter.remove();
                        } else if (oldX != nextPosition.getFirst() || oldY != nextPosition.getSecond()) {
                            //ao.setSkip(true);
                            ao.setX(nextPosition.getFirst());
                            ao.setY(nextPosition.getSecond());
                            listIter.remove();
                            if (!map.containsKey(ao.getX()))
                                map.put(ao.getX(), new ConcurrentHashMap<>());
                            if (!map.get(ao.getX()).containsKey(ao.getY()))
                                map.get(ao.getX()).put(ao.getY(), new ConcurrentLinkedDeque<>());
                            map.get(ao.getX()).get(ao.getY()).add(ao);
                        }
                    }
                }
            }
            //map.values().parallelStream().forEach((yMap) -> yMap.values().forEach(q -> q.forEach(ao -> ao.setSkip(false))));
            if (!aerospace.isFlightAllowed() && map.values().parallelStream().allMatch(yMap -> yMap.values().stream().allMatch(q -> q.stream().allMatch(
                    ao -> !(ao instanceof Military) || !((MilitaryAircraft) ao).isForeign()))))
                aerospace.allowFlight();
            long end = System.currentTimeMillis();
            try {
                Thread.sleep(aerospace.getPreferences().getSimulatorUpdatePeriod());
            } catch (InterruptedException ex) {
                GenericLogger.log(this.getClass(), ex);
            }
        }
    }
}
