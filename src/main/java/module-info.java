module io.game.hillclimbracing {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens io.game.hillclimbracing.app to javafx.fxml;
    opens io.game.hillclimbracing.ui to javafx.fxml;
    exports io.game.hillclimbracing.app;
}