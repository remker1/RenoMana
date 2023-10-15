import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Calendar extends Application {

    private BorderPane rootLayout;
    LocalDate today = LocalDate.now();


    @Override
    public void start(Stage primaryStage) {
        rootLayout = new BorderPane();
        Scene scene = new Scene(rootLayout, 800, 600);

        ComboBox<String> viewSelector = new ComboBox<>();
        viewSelector.getItems().addAll("Day", "Week", "Month", "Year");
        viewSelector.setValue("Month");
        viewSelector.setOnAction(event -> switchView(viewSelector.getValue()));

        rootLayout.setTop(viewSelector);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Calendar App");
        primaryStage.show();

        switchView("Month");
    }

    private void switchView(String view) {
        switch (view) {
            case "Day":
                setupDayView(today);
                break;
            case "Week":
                setupWeekView();
                break;
            case "Month":
                setupMonthView();
                break;
            case "Year":
                setupYearView();
                break;
        }
    }

    private void setupMonthView() {
        GridPane monthGrid = new GridPane();

        // current month
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();

        // calculate the starting cell for the 1st of the month
        int startCell = firstDayOfWeek.getValue() % 7;

        int dayOfMonth = 1;
        LocalDate dateBeingSetup = firstDayOfMonth;

        for (int i = 0; i < 6; i++) { // for up to 6 weeks in a month
            for (int j = startCell; j < 7; j++) { // 7 days in a week

                if (dayOfMonth > firstDayOfMonth.lengthOfMonth()) {
                    break;
                }

                Button dayButton = new Button(String.valueOf(dayOfMonth));
                dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                int finalDayOfMonth = dayOfMonth;
                LocalDate finalDateBeingSetup = dateBeingSetup;
                dayButton.setOnAction(e -> {
                    System.out.println("Clicked on: " + finalDateBeingSetup.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                });

                monthGrid.add(dayButton, j, i);
                dayOfMonth++;
                dateBeingSetup = dateBeingSetup.plusDays(1);
            }
            startCell = 0; // reset start cell for the next row/week
        }

        rootLayout.setCenter(monthGrid);
    }

    private void setupWeekView() {
        GridPane weekGrid = new GridPane();

        // current week
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.minusDays(currentDate.getDayOfWeek().getValue());

        for (int i = 0; i < 7; i++) { // 7 days in a week
            LocalDate dayInWeek = startOfWeek.plusDays(i);

            Button dayButton = new Button(dayInWeek.getDayOfMonth() + "\n" + dayInWeek.format(DateTimeFormatter.ofPattern("EEE")));
            dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            dayButton.setOnAction(e -> {
                System.out.println("Clicked on: " + dayInWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            });

            weekGrid.add(dayButton, i, 0);
        }

        rootLayout.setCenter(weekGrid);
    }

    private void setupYearView() {
        GridPane yearGrid = new GridPane();

        LocalDate currentDate = LocalDate.now().withDayOfYear(1); // start of the year

        for (int month = 0; month < 12; month++) {
            GridPane monthGrid = new GridPane();
            int daysInMonth = currentDate.lengthOfMonth();
            int dayCount = 1;

            for (int week = 0; week < 6; week++) { // max 6 rows for weeks
                for (int day = 0; day < 7 && dayCount <= daysInMonth; day++) {
                    LocalDate dayDate = currentDate.withDayOfMonth(dayCount);

                    if (dayDate.getDayOfWeek().getValue() % 7 == day) { //
                        Button dayButton = new Button(String.valueOf(dayCount));
                        dayButton.setOnAction(e -> {
                            System.out.println("Clicked on: " + dayDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        });

                        monthGrid.add(dayButton, day, week);
                        dayCount++;
                    }
                }
            }

            yearGrid.add(monthGrid, month % 4, month / 4); // 4x3 layout

            currentDate = currentDate.plusMonths(1);
        }

        rootLayout.setCenter(yearGrid);
    }

    private Button createHourButton(int hour) {
        String hourText = String.format("%02d:00 - %02d:59", hour, hour);
        Button hourButton = new Button(hourText);
        hourButton.setMaxWidth(Double.MAX_VALUE);  // to take full width

        hourButton.setOnAction(e -> {
            System.out.println("Selected hour: " + hour);
        });

        return hourButton;
    }

    private void setupDayView(LocalDate selectedDate) {
        VBox dayVBox = new VBox(5); // 5 is the spacing between items

        // display the selected date at the top
        Label dateLabel = new Label(selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd, EEEE")));
        dayVBox.getChildren().add(dateLabel);

        for (int hour = 0; hour < 24; hour++) {
            dayVBox.getChildren().add(createHourButton(hour));
        }

        rootLayout.setCenter(dayVBox);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
