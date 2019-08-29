package jovic.dragan.pj2.util;

import java.util.Map;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;
    private static final Map<Direction, Direction> OPPOSITES_MAP =
            Map.of(UP, DOWN, DOWN, UP, LEFT, RIGHT, RIGHT, LEFT);
    private static final Map<Integer, Direction> INTEGER_SIDE_MAP =
            Map.of(0, UP, 1, DOWN, 2, LEFT, 3, RIGHT);

    public Direction opposite() {
        return OPPOSITES_MAP.get(this);
    }

    public static Direction fromInt(int i) {
        return INTEGER_SIDE_MAP.get(i);
    }

    public Vector2D getDirectionVector() {
        switch (this) {
            case UP:
                return new Vector2D(0, 1);
            case DOWN:
                return new Vector2D(0, -1);
            case LEFT:
                return new Vector2D(-1, 0);
            case RIGHT:
                return new Vector2D(1, 0);
            default:
                throw new IllegalArgumentException("bukvalno nije moguce!");
        }
    }
}
