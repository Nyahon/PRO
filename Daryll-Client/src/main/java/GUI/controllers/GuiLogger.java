package GUI.controllers;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class GuiLogger {
    private Label console;
    private Circle circle;

    private Paint colorInfo = Color.LIMEGREEN;
    private Paint colorError = Color.RED;

    public GuiLogger(Label console, Circle circle) {
        this.console = console;
        this.circle = circle;
    }

    public void printInfo(String valueOf) {
        Platform.runLater(() -> {
            circle.setFill(colorInfo);
            console.setTextFill(colorInfo);
            console.setText(valueOf);
        });
    }

    public void printError(String valueOf) {
        Platform.runLater(() -> {
            circle.setFill(colorError);
            console.setTextFill(colorError);
            console.setText(valueOf);
        });
    }

}
