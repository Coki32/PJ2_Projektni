package jovic.dragan.pj2.util;

public class Pair<T1 extends Comparable, T2 extends Comparable> {

    T1 first;
    T2 second;

    public Pair(T1 first, T2 second){
        this.first = first;
        this.second = second;
    }

    /**
     * Performs equality check as first.compareTo(obj.first)==0 && second.compareTo(obj.second)==0
     *
     * @param obj Other object comparing to this one
     * @return True if this.first is equal to other.first and this.second is equal to other.second
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair<?, ?>))
            return false;
        return ((Pair<?, ?>) obj).first.compareTo(this.first) == 0 && ((Pair<?, ?>) obj).second.compareTo(this.second) == 0;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + "," + second.toString() + ")";
    }
}
