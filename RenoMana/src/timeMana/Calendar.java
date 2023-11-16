package timeMana;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Calendar extends VBox {

    // main layout
    private final BorderPane root;
    LocalDate today = LocalDate.now();

    private final ObservableList<Project> allProjects;

    public Calendar(ObservableList<Project> allProjects) {

        this.allProjects = allProjects;

        root = new BorderPane();

        // dropdown list for selecting calendar view
        ComboBox<String> viewSelector = new ComboBox<>();
        viewSelector.getItems().addAll("Day", "Week", "Month", "Year");
        viewSelector.setValue("Month"); // default drop down view option will be month
        viewSelector.setOnAction(event -> switchView(viewSelector.getValue()));

        // add dropdown list to the top of the main layout
        root.setTop(viewSelector);

        // set view to month when application starts
        switchView("Month");
        this.getChildren().add(root);
    }

    /***
     * Method to switch calendar display based on selected view
     * @param view either day, week, month, or year
     */
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

    /***
     * Method to set up the calendar for monthly view
     */
    private void setupMonthView() {
        GridPane monthGrid = new GridPane();

        // current month and its first day
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();

        // calculate the starting cell for the 1st of the month
        int startCell = firstDayOfWeek.getValue() % 7;

        // variables to keep track of current day and date being set up
        int dayOfMonth = 1;
        LocalDate dateBeingSetup = firstDayOfMonth;

        // populate the calendar grid for the month
        for (int i = 0; i < 6; i++) { // for up to 6 weeks in a month
            for (int j = startCell; j < 7; j++) { // 7 days in a week

                // stop once all days of the month are added
                if (dayOfMonth > firstDayOfMonth.lengthOfMonth()) {
                    break;
                }

                // button for each day
                Button dayButton = new Button(String.valueOf(dayOfMonth));
                dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setHgrow(dayButton, Priority.ALWAYS);
                GridPane.setVgrow(dayButton, Priority.ALWAYS);

                // temporary function: display the date when button is clicked
                LocalDate finalDateBeingSetup = dateBeingSetup;
                dayButton.setOnAction(e -> {
                    System.out.println("Clicked on: " + finalDateBeingSetup.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    // other functionalities
                    LocalDate clickedDate = finalDateBeingSetup;
                    // filter projects whose due date is on or after the clicked date
                    List<Project> dueProjects = allProjects.stream()
                            .filter(project -> !LocalDate.parse(project.getTimeline(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).isBefore(clickedDate))
                            .collect(Collectors.toList());

                    // display the due projects (simple example using an alert)
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, formatProjectList(dueProjects), ButtonType.OK);
                    alert.setHeaderText("Projects due on or after " + clickedDate);
                    alert.showAndWait();
                });

                // add button to calendar grid
                monthGrid.add(dayButton, j, i);
                dayOfMonth++;
                dateBeingSetup = dateBeingSetup.plusDays(1);
            }
            startCell = 0; // reset start cell for the next row/week
        }

        // update main layout to display the month grid
        root.setCenter(monthGrid);
    }

    private String formatProjectList(List<Project> projects) {
        return projects.stream()
                .map(Project::getName)
                .collect(Collectors.joining("\n"));
    }

    /***
     * Sets up the calendar for weekly view
     */
    private void setupWeekView() {
        GridPane weekGrid = new GridPane();

        // start of the current week
        LocalDate currentDate = LocalDate.now();
        // to make sure the week starts on sunday
        int daysToSubtract = currentDate.getDayOfWeek() == DayOfWeek.SUNDAY ? 0 : currentDate.getDayOfWeek().getValue();
        LocalDate startOfWeek = currentDate.minusDays(daysToSubtract);

        // populate the calendar grid for the week
        for (int i = 0; i < 7; i++) { // 7 days in a week
            LocalDate dayInWeek = startOfWeek.plusDays(i);

            // create a button for each day with number and day of the week
            Button dayButton = new Button(dayInWeek.getDayOfMonth() + "\n" + dayInWeek.format(DateTimeFormatter.ofPattern("EEE")));
            dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            // temporary functionality: display the date when button is clicked
            dayButton.setOnAction(e -> {
                System.out.println("Clicked on: " + dayInWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                // other functionalities
                List<Project> dueProjects = allProjects.stream()
                        .filter(project -> !LocalDate.parse(project.getTimeline(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).isBefore(dayInWeek))
                        .collect(Collectors.toList());

                Alert alert = new Alert(Alert.AlertType.INFORMATION, formatProjectList(dueProjects), ButtonType.OK);
                alert.setHeaderText("Projects due on or after " + dayInWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                alert.showAndWait();
            });

            // add button to the grid
            weekGrid.add(dayButton, i, 0);
        }

        // update the main layout to display the week grid
        root.setCenter(weekGrid);
    }

    /***
     * Sets up the calendar for yearly view
     */
    private void setupYearView() {
        GridPane yearGrid = new GridPane();

        LocalDate currentDate = LocalDate.now().withDayOfYear(1); // start of the year

        // populate the calendar grid for the year
        for (int month = 0; month < 12; month++) {
            GridPane monthGrid = new GridPane();
            int daysInMonth = currentDate.lengthOfMonth();
            int dayCount = 1;

            // iterate through a potential maximum of 6 weeks for each month
            for (int week = 0; week < 6; week++) {
                for (int day = 0; day < 7 && dayCount <= daysInMonth; day++) {
                    LocalDate dayDate = currentDate.withDayOfMonth(dayCount);

                    // check if the day matches with the week day
                    if (dayDate.getDayOfWeek().getValue() % 7 == day) { //
                        // add to the month grid if it matches
                        Button dayButton = new Button(String.valueOf(dayCount));
                        // temporary functionality: display date clicked
                        dayButton.setOnAction(e -> {
                            System.out.println("Clicked on: " + dayDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                            // other functionalities
                            List<Project> dueProjects = allProjects.stream()
                                    .filter(project -> !LocalDate.parse(project.getTimeline(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).isBefore(dayDate))
                                    .collect(Collectors.toList());

                            Alert alert = new Alert(Alert.AlertType.INFORMATION, formatProjectList(dueProjects), ButtonType.OK);
                            alert.setHeaderText("Projects due on or after " + dayDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                            alert.showAndWait();
                        });

                        monthGrid.add(dayButton, day, week);
                        dayCount++;
                    }
                }
            }

            // add each month grid to te year grid
            yearGrid.add(monthGrid, month % 4, month / 4); // 4 months per row layout

            // move to the next month
            currentDate = currentDate.plusMonths(1);
        }

        // update the main layout to display the year grid
        root.setCenter(yearGrid);
    }

    // helper function to create a button representing an hour in the day view
    private Button createHourButton(int hour) {
        String hourText = String.format("%02d:00 - %02d:59", hour, hour);
        Button hourButton = new Button(hourText);
        hourButton.setMaxWidth(Double.MAX_VALUE);  // to take full width

        // temporary functionality: display the selected hour when button is clicked
        hourButton.setOnAction(e -> {
            System.out.println("Selected hour: " + hour);
            // other functionalities
        });

        return hourButton;
    }

    /***
     * Sets up the calendar for daily view
     * @param selectedDate whichever day is being viewed
     */
    private void setupDayView(LocalDate selectedDate) {
        VBox dayVBox = new VBox(5); // 5 is the spacing between items

        // display the selected date at the top
        Label dateLabel = new Label(selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd, EEEE")));
        dayVBox.getChildren().add(dateLabel);

        // create buttons for each hour of the day and add them to the day VBox
        for (int hour = 0; hour < 24; hour++) {
            dayVBox.getChildren().add(createHourButton(hour));
        }

        // update the main layout to display the day view
        root.setCenter(dayVBox);
    }
}
