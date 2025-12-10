package io.game.hillclimbracing.model;

import io.game.hillclimbracing.input.InputHandler;
import io.game.hillclimbracing.utils.Constants;
import lombok.Data;
import lombok.NonNull;

import static java.lang.Math.clamp;

@Data
public class CarBody {

    private static CarBody INSTANCE;

    public static CarBody init(Vector position, Vector velocity, InputHandler input) {
        if (INSTANCE == null) {
            INSTANCE = new CarBody(position, velocity, input);
        }
        return INSTANCE;
    }

    private CarBody(Vector position, Vector velocity, InputHandler input) {
        this.position = position;
        this.velocity = velocity;
        this.input = InputHandler.getInstance();
    }

    @NonNull
    private Vector position;

    @NonNull
    private final Vector screen = Constants.CAR_SCREEN_POS;

    @NonNull
    private Vector velocity;

    private double orientation;

    @NonNull
    private InputHandler input;

    private CarBody() {}

    public void updatePosition() {
        position.setX(position.getX() + velocity.getX());
        position.setY(position.getY() + velocity.getY());
    }

    public void updateVelocity() {
        // update x velocity based on keyboard input & orientation of car body (slope effect)
        double keyFactor = input.isRightKeyPressed() ? Constants.CAR_KEY_FACTOR : -Constants.CAR_KEY_FACTOR;
        double slopeFactor = Math.sin(Constants.HILL_ORIENTATION_TEMPORARY) * Constants.CAR_SLOPE_FACTOR;
        double newXVelocity = velocity.getX() + keyFactor + slopeFactor;
        velocity.setX(clamp(newXVelocity, -Constants.CAR_MAX_SPEED_X, Constants.CAR_MAX_SPEED_X));

        // update y velocity based on gravity
        double newYVelocity = velocity.getY() + Constants.GRAVITY;
        velocity.setY(clamp(newYVelocity, -Constants.CAR_MAX_SPEED_Y, Constants.CAR_MAX_SPEED_Y));
    }
}
