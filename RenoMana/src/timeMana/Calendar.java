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

        Label monthNameLabel = new Label(firstDayOfMonth.format(DateTimeFormatter.ofPattern("MMMM")));
        monthGrid.add(monthNameLabel, 0, 0, 7, 1);

        // calculate the starting cell for the 1st of the month
        int startCell = firstDayOfWeek.getValue() % 7;

        // variables to keep track of current day and date being set up
        int dayOfMonth = 1;
        LocalDate dateBeingSetup = firstDayOfMonth;

        // populate the calendar grid for the month
        for (int i = 1; i <= 6; i++) {
            for (int j = startCell; j < 7; j++) {
                if (dayOfMonth > firstDayOfMonth.lengthOfMonth()) {
                    break;
                }

                final LocalDate finalDateBeingSetup = dateBeingSetup;

                Button dayButton = new Button(String.valueOf(dayOfMonth));
                dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setHgrow(dayButton, Priority.ALWAYS);
                GridPane.setVgrow(dayButton, Priority.ALWAYS);

                dayButton.setOnAction(e -> {
                    displayProjectsForDate(finalDateBeingSetup);
                });

                monthGrid.add(dayButton, j, i);
                dayOfMonth++;
                dateBeingSetup = dateBeingSetup.plusDays(1);
            }
            startCell = 0;
        }

        // update main layout to display the month grid
        root.setCenter(monthGrid);
    }

    /***
     * Sets up the calendar for weekly view
     */
    private void setupWeekView() {
        GridPane weekGrid = new GridPane();

        // start of the current week
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);

        // populate the calendar grid for the week
        for (int i = 0; i < 7; i++) {
            LocalDate dayInWeek = startOfWeek.plusDays(i);
            final LocalDate finalDateBeingSetup = dayInWeek;

            // create a button for each day with number and day of the week
            Button dayButton = new Button(dayInWeek.getDayOfMonth() + "\n" + dayInWeek.format(DateTimeFormatter.ofPattern("EEE")));
            dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            dayButton.setOnAction(e -> {
                displayProjectsForDate(finalDateBeingSetup);
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

        LocalDate currentDate = LocalDate.now().withDayOfYear(1);

        // populate the calendar grid for the year
        for (int month = 0; month < 12; month++) {
            GridPane monthGrid = new GridPane();
            Label monthNameLabel = new Label(currentDate.format(DateTimeFormatter.ofPattern("MMMM")));
            monthGrid.add(monthNameLabel, 0, 0, 7, 1);

            LocalDate firstDayOfMonth = currentDate;
            int daysInMonth = firstDayOfMonth.lengthOfMonth();
            int dayCount = 1;

            // iterate through a potential maximum of 6 weeks for each month
            for (int week = 1; week <= 6; week++) {
                for (int day = 0; day < 7 && dayCount <= daysInMonth; day++) {
                    LocalDate dayDate = firstDayOfMonth.withDayOfMonth(dayCount);

                    // check if the day matches with the week day
                    if (dayDate.getDayOfWeek().getValue() % 7 == day) {
                        final LocalDate finalDateBeingSetup = dayDate;
                        // add to the month grid if it matches
                        Button dayButton = new Button(String.valueOf(dayCount));
                        dayButton.setOnAction(e -> {
                            displayProjectsForDate(finalDateBeingSetup);
                        });

                        monthGrid.add(dayButton, day, week);
                        dayCount++;
                    }
                }
            }
            // add each month grid to te year grid
            yearGrid.add(monthGrid, month % 4, month / 4);
            // move to the next month
            currentDate = currentDate.plusMonths(1);
        }
        // update the main layout to display the year grid
        root.setCenter(yearGrid);
    }

    /***
     * Sets up the calendar for daily view
     * @param selectedDate whichever day is being viewed
     */
    private void setupDayView(LocalDate selectedDate) {
        VBox dayVBox = new VBox(5);

        Label dateLabel = new Label(selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd, EEEE")));
        dayVBox.getChildren().add(dateLabel);

        Button dayButton = new Button("Show Projects");
        dayButton.setMaxWidth(Double.MAX_VALUE);

        dayButton.setOnAction(e -> {
            displayProjectsForDate(selectedDate);
        });

        dayVBox.getChildren().add(dayButton);

        root.setCenter(dayVBox);
    }


    private void displayProjectsForDate(LocalDate date) {
        List<Project> dueProjects = allProjects.stream()
                .filter(project -> !LocalDate.parse(project.getTimeline(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).isBefore(date))
                .collect(Collectors.toList());

        // display the due projects (for example, using an alert)
        Alert alert = new Alert(Alert.AlertType.INFORMATION, formatProjectList(dueProjects), ButtonType.OK);
        alert.setHeaderText("Projects due on or after " + date);
        alert.showAndWait();
    }

    private String formatProjectList(List<Project> projects) {
        return projects.stream()
                .map(Project::getName)
                .collect(Collectors.joining("\n"));
    }

}
