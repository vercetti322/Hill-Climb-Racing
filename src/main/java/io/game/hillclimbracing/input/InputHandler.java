package io.game.hillclimbracing.input;

import lombok.Data;
import lombok.NonNull;

@Data
public class InputHandler {

    private static final InputHandler INSTANCE = new InputHandler();

    @NonNull
    private boolean leftKeyPressed;

    @NonNull
    private boolean rightKeyPressed;

    private InputHandler() {}

    public static InputHandler getInstance() {
        return INSTANCE;
    }
}
