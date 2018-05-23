/**
 * Module      : PRO
 * File        : ViewController.java
 * Date        : 31.03.2018
 * <p>
 * Goal : This class implements a simple GUI logger
 * between the GUI and the user work, each event for each object from the GUI
 * contains a method that will manage the action.
 * <p>
 * <p>
 * Remarks :
 *
 * @author Siu Aurélien
 * @version 1.0
 */


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


    /**
     * @param console graphic label where the text will be printed
     * @param circle circle form that will take the same color of the text
     *
     * This is the contructor of the GuiLogger
     */
    public GuiLogger(Label console, Circle circle) {
        this.console = console;
        this.circle = circle;
    }

    /**
     * @param valueOf String value to set in the label
     *
     * This is the method to print text in the GuiLogger with colorInfo
     */
    public void printInfo(String valueOf) {
        Platform.runLater(() -> {
            circle.setFill(colorInfo);
            console.setTextFill(colorInfo);
            console.setText(valueOf);
        });
    }

    /**
     * @param valueOf String value to set in the label
     *
     * This is the method to print text in the GuiLogger with colorError
     */
    public void printError(String valueOf) {
        Platform.runLater(() -> {
            circle.setFill(colorError);
            console.setTextFill(colorError);
            console.setText(valueOf);
        });
    }

}
