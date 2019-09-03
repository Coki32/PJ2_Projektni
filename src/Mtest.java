import java.util.HashMap;

public class Mtest {

    static final int MAX = 10;

    public static void main(String[] args) {
        HashMap<Integer, HashMap<Integer, Better>> mapa = new HashMap<>();
        mapa.put(5, new HashMap<>());
        mapa.get(5).put(3, new Better(0));
        HashMap<Integer, HashMap<Integer, Better>> copy = new HashMap<>(mapa);
        copy.get(5).get(3).inc();
        System.out.println(mapa);
        System.out.println(copy);
    }

    static class Better {
        public int value;

        public Better(int value) {
            this.value = value;
        }

        public void inc() {
            value++;
        }

        public String toString() {
            return "" + value;
        }
    }
}
