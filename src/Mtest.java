import jovic.dragan.pj2.aerospace.handlers.InvasionHandler;
import jovic.dragan.pj2.util.Direction;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Mtest {

    static final int MAX = 10;

    static class Better {
        public boolean skip;
        public int value;

        public Better(int value) {
            this.value = value;
        }

        public String toString() {
            return "" + value;
        }
    }

    static void add(ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Better>> mapa, int x, int y, Better val) {
        if (!mapa.containsKey(x))
            mapa.put(x, new ConcurrentHashMap<>());
        if (!mapa.get(x).containsKey(y))
            mapa.get(x).put(y, val);
    }

    static void move(ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Better>> map, int ox, int oy, int nx, int ny) {
        if (!map.containsKey(nx))
            map.put(nx, new ConcurrentHashMap<>());
        Better obj = map.get(ox).get(oy);
        if (!obj.skip) {
            map.get(ox).remove(oy);
            Better toAdd = new Better(obj.value + 1);
            toAdd.skip = true;
            map.get(nx).put(ny, toAdd);
        }
    }

    public static void main(String[] args) {
        Random r = new Random();
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Better>> mapa = new ConcurrentHashMap<>();
        for (int i = 0; i < 5; i++)
            add(mapa, r.nextInt(100), r.nextInt(100), new Better(0));
        for (int i : mapa.keySet()) {
            for (int j : mapa.get(i).keySet()) {
                move(mapa, i, j, r.nextInt(100), r.nextInt(100));
            }
        }
        System.out.println(mapa);
        System.out.println(mapa.values().stream().allMatch(yMap -> yMap.values().stream().allMatch(i -> i.value == 1)));

    }
}
