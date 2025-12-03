module io.game.hillclimbracing {
    requires javafx.controls;
    requires javafx.fxml;


    opens io.game.hillclimbracing to javafx.fxml;
    exports io.game.hillclimbracing;
}