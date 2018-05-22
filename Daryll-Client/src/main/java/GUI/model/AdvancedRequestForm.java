package GUI.model;

import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdvancedRequestForm {

    private static int counter = 0;
    private int id;
    public static final List<String> BUILDING_NAMES = new ArrayList<String>(Arrays.asList("Cheseaux", "St-Roch"));
    private DatePicker beginDate = new DatePicker();
    private DatePicker endDate = new DatePicker();
    private TimeSpinner beginTime = new TimeSpinner();
    private TimeSpinner endTime = new TimeSpinner();

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

        beginDate.valueProperty().setValue(currentDateTime.toLocalDate());
        endDate.valueProperty().setValue(currentDateTime.toLocalDate());

        /*
        endDate.setMinWidth(130.0);
        beginTime.setMinWidth(85.0);
        endTime.setMinWidth(85.0);
        building.setMinWidth(110.0);
        floor.setMinWidth(60.0);
        classroom.setMinWidth(60.0);
        add.setMinWidth(60);
        remove.setMinWidth(60);*/

        beginDate.setPrefWidth(120.0);
        endDate.setPrefWidth(120.0);
        beginTime.setPrefWidth(85.0);
        endTime.setPrefWidth(85.0);
        building.setPrefWidth(105.0);
        floor.setPrefWidth(50.0);
        classroom.setPrefWidth(50.0);
        add.setPrefWidth(65.0);
        remove.setPrefWidth(65.0);

        gridPane.add(beginDate, 0, 0);
        gridPane.add(endDate, 1, 0);
        gridPane.add(beginTime, 2, 0);
        gridPane.add(endTime, 3, 0);
        gridPane.add(building, 4, 0);
        gridPane.add(floor, 5, 0);
        gridPane.add(classroom, 6, 0);
        gridPane.add(add, 7, 0);
        gridPane.add(remove, 8, 0);

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
        gridPane.getColumnConstraints().add(1, datePickerConstraints);
        gridPane.getColumnConstraints().add(2, hourConstraints);
        gridPane.getColumnConstraints().add(3, hourConstraints);
        gridPane.getColumnConstraints().add(4, buildingConstraints);
        gridPane.getColumnConstraints().add(5, textFieldConstraints);
        gridPane.getColumnConstraints().add(6, textFieldConstraints);
        gridPane.getColumnConstraints().add(7, buttonConstraints);
        gridPane.getColumnConstraints().add(8, buttonConstraints);

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

    public LocalDate getBeginDate(){
        return beginDate.getValue();
    }

    public LocalDate getEndDate(){
        return endDate.getValue();
    }

    public LocalTime getBeginTime(){
        return beginTime.getValue();
    }

    public LocalTime getEndTime(){
        return endTime.getValue();
    }

    public int getBuilding(){
        return 0; // TODO Get building label
    }

    public String getFloor(){
        return floor.getText();
    }

    public String getClassroomName(){
        return classroom.getText();
    }

}
