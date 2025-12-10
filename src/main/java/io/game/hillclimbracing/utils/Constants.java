package io.game.hillclimbracing.utils;

import io.game.hillclimbracing.model.Vector;

public class Constants {
    public static final double GRAVITY = 0.3;
    public static final Vector CAR_SCREEN_POS = new Vector(300.0, 400.0);
    public static final double CAR_MAX_SPEED_X = 20.0;
    public static final double CAR_MAX_SPEED_Y = 50.0 * GRAVITY;
    public static final double CAR_SLOPE_FACTOR = 1.5;
    public static final double CAR_KEY_FACTOR = 0.1;
    public static final double HILL_ORIENTATION_TEMPORARY = 3.0; // temporary
}
