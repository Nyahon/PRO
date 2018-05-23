package GUI.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static utils.ClassroomsByFloor.BUILDING_MAP;
import static utils.ClassroomsByFloor.FLOORS_MAP;

public class AdvancedRequestForm {

    private static int counter = 0;
    private int id;
    public static final List<String> BUILDING_NAMES = new ArrayList<String>(Arrays.asList("Cheseaux", "St-Roch"));


    private DatePicker beginDate = new DatePicker();
    private DatePicker endDate = new DatePicker();
    private TimeSpinner beginTime = new TimeSpinner();
    private TimeSpinner endTime = new TimeSpinner(LocalTime.of(23,59));

    private ChoiceBox building = new ChoiceBox();

    private ChoiceBox floor = new ChoiceBox();
    private ChoiceBox classroom = new ChoiceBox();

    private Button add = new Button("ajouter");
    private Button remove = new Button("retirer");

    private GridPane gridPane = new GridPane();

    public AdvancedRequestForm() {

        building.getItems().addAll(BUILDING_NAMES);
        building.setValue(BUILDING_NAMES.get(0));


        List<String> defaultFloors = new ArrayList<>(BUILDING_MAP.get(0));
        defaultFloors.add(0, "Tous");
        floor.getItems().addAll(defaultFloors);
        floor.setValue(defaultFloors.get(0));



        building.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                List<String> floors = new ArrayList<>(BUILDING_MAP.get(number2));
                floors.add(0, "Tous");
                floor.getItems().clear();
                floor.getItems().addAll(floors);
                floor.setValue(floors.get(0));
            }
        });


        List<String> defaultClassrooms = new ArrayList<>();
        defaultClassrooms.add(0, "Tous");
        classroom.getItems().addAll(defaultClassrooms);
        classroom.setValue(defaultClassrooms.get(0));


        floor.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                System.out.println(number2);


                System.out.println(classroom.getItems().get(0));
                if( number2.intValue() > 0) {

                    List<String> classrooms = new ArrayList<>(FLOORS_MAP.get(floor.getItems().get((Integer) number2)));
                    classrooms.add(0, "Tous");
                    classroom.getItems().clear();
                    classroom.getItems().addAll(classrooms);
                    classroom.setValue(classrooms.get(0));
                }else{
                    List<String> classrooms = new ArrayList<>();
                    classrooms.add(0, "Tous");
                    classroom.getItems().clear();
                    classroom.getItems().addAll(classrooms);
                    classroom.setValue(classrooms.get(0));
                }

            }
        });





        id = counter++;

        LocalDateTime currentDateTime = LocalDateTime.now();

        beginDate.valueProperty().setValue(currentDateTime.toLocalDate());
        endDate.valueProperty().setValue(currentDateTime.toLocalDate());

        endTime.getValueFactory().setValue(LocalTime.of(23,59));
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
        building.setPrefWidth(100.0);
        floor.setPrefWidth(60.0);
        classroom.setPrefWidth(60.0);
        add.setPrefWidth(75.0);
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

        ColumnConstraints ChoiceBoxConstraints = new ColumnConstraints();
        ChoiceBoxConstraints.setPrefWidth(80.0);
        ChoiceBoxConstraints.setHalignment(HPos.CENTER);
        ChoiceBoxConstraints.setFillWidth(false);

        ColumnConstraints buttonConstraints = new ColumnConstraints();
        buttonConstraints.setPrefWidth(80.0);
        buttonConstraints.setHalignment(HPos.CENTER);


        gridPane.getColumnConstraints().add(0, datePickerConstraints);
        gridPane.getColumnConstraints().add(1, datePickerConstraints);
        gridPane.getColumnConstraints().add(2, hourConstraints);
        gridPane.getColumnConstraints().add(3, hourConstraints);
        gridPane.getColumnConstraints().add(4, buildingConstraints);
        gridPane.getColumnConstraints().add(5, ChoiceBoxConstraints);
        gridPane.getColumnConstraints().add(6, ChoiceBoxConstraints);
        gridPane.getColumnConstraints().add(7, buttonConstraints);
        gridPane.getColumnConstraints().add(8, buttonConstraints);

    }

    /**
     * @throws Exception
     * @return Launch the view for the room schedule.
     */
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
        return building.getSelectionModel().getSelectedIndex();
    }

    public String getFloorName(){
        return floor.getSelectionModel().getSelectedItem().toString();
    }

    public String getClassroomName(){
        return classroom.getSelectionModel().getSelectedItem().toString();
    }

}
