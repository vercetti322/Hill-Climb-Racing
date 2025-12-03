package io.game.hillclimbracing.app;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameLauncher extends Application {
    @Override
    public void start(Stage stage) {
        System.out.println("Game Started!!!");
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Application.launch(GameLauncher.class, args);
    }
}
