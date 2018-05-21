package GUI.model;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdvancedRequestForm {

    private static int counter = 0;
    private int id;
    public static final List<String> BUILDING_NAMES = new ArrayList<String>(Arrays.asList("Cheseaux", "St-Roch"));
    private DatePicker date = new DatePicker();
    private TimeSpinner beginHour = new TimeSpinner();
    private TimeSpinner endHour = new TimeSpinner();

    private ChoiceBox building = new ChoiceBox();

    private TextField floor = new TextField();
    private TextField classroom = new TextField();

    private Button add = new Button("ajouter");
    private Button remove = new Button("retirer");

    private GridPane gridPane = new GridPane();

    public AdvancedRequestForm() {

        building.getItems().addAll(BUILDING_NAMES);
        building.setValue(BUILDING_NAMES.get(0));

        id = counter++;

        LocalDateTime currentDateTime = LocalDateTime.now();

        date.valueProperty().setValue(currentDateTime.toLocalDate());

        /*
        date.setMinWidth(130.0);
        beginHour.setMinWidth(85.0);
        endHour.setMinWidth(85.0);
        building.setMinWidth(110.0);
        floor.setMinWidth(60.0);
        classroom.setMinWidth(60.0);
        add.setMinWidth(60);
        remove.setMinWidth(60);*/

        date.setPrefWidth(120.0);
        beginHour.setPrefWidth(85.0);
        endHour.setPrefWidth(85.0);
        building.setPrefWidth(105.0);
        floor.setPrefWidth(50.0);
        classroom.setPrefWidth(50.0);
        add.setPrefWidth(65.0);
        remove.setPrefWidth(65.0);

        gridPane.add(date, 0, 0);
        gridPane.add(beginHour, 1, 0);
        gridPane.add(endHour, 2, 0);
        gridPane.add(building, 3, 0);
        gridPane.add(floor, 4, 0);
        gridPane.add(classroom, 5, 0);
        gridPane.add(add, 6, 0);
        gridPane.add(remove, 7, 0);

        ColumnConstraints datePickerConstraints = new ColumnConstraints();
        datePickerConstraints.setPrefWidth(130.0);
        datePickerConstraints.setHalignment(HPos.CENTER);

        ColumnConstraints hourConstraints = new ColumnConstraints();
        hourConstraints.setPrefWidth(100.0);
        hourConstraints.setHalignment(HPos.CENTER);

        ColumnConstraints endHourConstraints = new ColumnConstraints();
        endHourConstraints.setPrefWidth(100.0);

        ColumnConstraints buildingConstraints = new ColumnConstraints();
        buildingConstraints.setPrefWidth(115.0);
        buildingConstraints.setHalignment(HPos.CENTER);

        ColumnConstraints textFieldConstraints = new ColumnConstraints();
        textFieldConstraints.setPrefWidth(60.0);
        textFieldConstraints.setHalignment(HPos.CENTER);
        textFieldConstraints.setFillWidth(false);

        ColumnConstraints buttonConstraints = new ColumnConstraints();
        buttonConstraints.setPrefWidth(70.0);
        buttonConstraints.setHalignment(HPos.CENTER);


        gridPane.getColumnConstraints().add(0, datePickerConstraints);
        gridPane.getColumnConstraints().add(1, hourConstraints);
        gridPane.getColumnConstraints().add(2, hourConstraints);
        gridPane.getColumnConstraints().add(3, buildingConstraints);
        gridPane.getColumnConstraints().add(4, textFieldConstraints);
        gridPane.getColumnConstraints().add(5, textFieldConstraints);
        gridPane.getColumnConstraints().add(6, buttonConstraints);
        gridPane.getColumnConstraints().add(7, buttonConstraints);

    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public Button getAddButton() {
        return add;
    }

    public Button getRemoveButton() {
        return remove;
    }

}
