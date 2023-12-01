package employeeMana;

import com.fasterxml.jackson.databind.ObjectMapper;
import ManagerCheck.ManagerCheck;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class represents an Employee List UI component, which displays a list of employees
 * and provides buttons for adding, deleting, modifying employee information, and viewing personal details.
 */
public class EmployeeList extends VBox {

    private TableView<Employee> employeeList; // Table view for displaying employees
    public static ObservableList<Employee> data; // Observable list for storing employee data
    public static ObservableList<String> employeeFirstNameList;

    /**
     * Constructor for the EmployeeList class.
     * Initializes the UI components and sets up the employee table with columns and buttons.
     */
    public EmployeeList(String COOKIES) {
        // Setting up the table
        employeeList = new TableView<>();
        employeeList.prefWidthProperty().bind(this.widthProperty());
        data = FXCollections.observableArrayList();
        employeeFirstNameList = FXCollections.observableArrayList();

        // Adding column names
        TableColumn<Employee, String> firstNameCol = new TableColumn<>("Employee First Name");
        // Bind the cell value factory to employee's first name property
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        firstNameCol.prefWidthProperty().bind(employeeList.widthProperty().multiply(0.15)); // 15% width

        TableColumn<Employee, String> lastNameCol = new TableColumn<>("Employee Last Name");
        // Bind the cell value factory to employee's last name property
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        lastNameCol.prefWidthProperty().bind(employeeList.widthProperty().multiply(0.15)); // 15% width

        TableColumn<Employee, String> employeeIdCol = new TableColumn<>("Employee ID");
        // Bind the cell value factory to employee's ID property
        employeeIdCol.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty());
        employeeIdCol.prefWidthProperty().bind(employeeList.widthProperty().multiply(0.1)); // 10% width

        TableColumn<Employee, String> eMailCol = new TableColumn<>("Email");
        // Bind the cell value factory to employee's email property
        eMailCol.setCellValueFactory(cellData -> cellData.getValue().eMailProperty());
        eMailCol.prefWidthProperty().bind(employeeList.widthProperty().multiply(0.3)); // 30% width

        TableColumn<Employee, String> cellNumberCol = new TableColumn<>("Cell Phone");
        // Bind the cell value factory to employee's cell phone property
        cellNumberCol.setCellValueFactory(cellData -> cellData.getValue().cellProperty());
        cellNumberCol.prefWidthProperty().bind(employeeList.widthProperty().multiply(0.3)); // 30% width

        // Add columns to the table
        employeeList.getColumns().addAll(firstNameCol, lastNameCol, employeeIdCol, eMailCol, cellNumberCol);
        employeeList.setItems(data);

        // Create buttons and set their action handlers
        Button addItem = new Button("Add");
        addItem.setOnAction(actionEvent -> addEmployeeInfo(COOKIES));

        Button deleteItem = new Button("Delete");
        deleteItem.setOnAction(actionEvent -> deleteEmployee(COOKIES));

        Button modifyItem = new Button("Modify");
        modifyItem.setOnAction(actionEvent -> modifyEmployeeInfo(COOKIES));

        Button employeeInfo = new Button("Personal Details");
        employeeInfo.setOnAction(actionEvent -> openEmployeeInfo());

        TextField searchField = new TextField();
        searchField.setPromptText("Search Here...");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> {
            String searchText = searchField.getText();
            searchHelper(searchText);
        });

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setPadding(new Insets(10));

        Button refreshEmployeeTable = new Button("Refresh EmployeeList");
        refreshEmployeeTable.setOnAction(actionEvent -> {
            try {
                loadEmployeeList(getEmployeeData(COOKIES));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            loadEmployeeList(getEmployeeData(COOKIES));
        }catch (Exception e){
            System.out.println(e);
        }

        HBox optButton;
        if (ManagerCheck.isManager()){
            optButton = new HBox(10, addItem, deleteItem, modifyItem, employeeInfo, refreshEmployeeTable);
        } else {
            optButton = new HBox(10, employeeInfo, refreshEmployeeTable);
        }

        // Create a horizontal box to hold the buttons
        optButton.setPadding(new Insets(10, 0, 10, 0)); // top, right, bottom, left padding

        // Set vertical grow for the table and add it along with the buttons to the VBox
        VBox.setVgrow(employeeList, Priority.ALWAYS);
        this.getChildren().addAll(searchBox, employeeList, optButton);
    }

    /**
     * Searches for an employee with the specified first name in the 'data' list.
     *
     * @param toFind The first name of the employee to search for.
     * @return The index of the first occurrence of the employee with the specified first name,
     * or -1 if no such employee is found.
     */
    public static int employeeSearch(String toFind) {
        // Initialize index variable to keep track of the current position in the list.
        int idx = 0;

        // Iterate through the 'data' list to search for the employee.
        for (Employee employee : data) {
            // Check if the first name of the current employee matches the specified first name.
            if (Objects.equals(employee.getEmployeeFirstName().toLowerCase(), toFind.toLowerCase())) {
                // If a match is found, return the index.
                return idx;
            } else if (Objects.equals(employee.getEmployeeLastName().toLowerCase(), toFind.toLowerCase())) {
                return idx;
            } else if (Objects.equals(employee.getEmployeeID(), toFind)) {
                return idx;
            } else if (Objects.equals(employee.getEMail().toLowerCase(), toFind.toLowerCase())) {
                return idx;
            } else if (Objects.equals(employee.getCell(),formatCell(toFind))) {
                return idx;
            }else if (Objects.equals(employee.getUsername().toLowerCase(),toFind.toLowerCase())){
                return idx;
            }
            // Increment the index for the next iteration.
            idx++;
        }
        return -1;
    }

    public void searchHelper(String toFind) {
        int idx = employeeSearch(toFind);
        if (idx >= 0 && idx < data.size()) {
            employeeList.getSelectionModel().select(idx);
            employeeList.scrollTo(idx);
        } else {
            showAlert("Search Result", "No employee was found. You searched: " + toFind);
        }
    }

    /**
     * Generates a username based on the first two characters of the first name,
     * the first character of the last name, and a random three-digit number.
     *
     * @param fname The first name used for generating the username.
     * @param lname The last name used for generating the username.
     * @return      The generated username.
     */
    private String generateUsername(String fname, String lname) {
        // Create a StringBuffer to build the username
        StringBuffer result = new StringBuffer();

        // Append the first two characters of the first name (converted to lowercase)
        result.append(fname.substring(0, 2).toLowerCase());

        // Append the first character of the last name (converted to lowercase)
        result.append(lname.substring(0, 1).toLowerCase());

        // Append a random three-digit number
        result.append(new Random().nextInt(900) + 100);

        // Convert the StringBuffer to a String and return the result
        return result.toString();
    }

    /**
     * Formats the given employee cell phone number to a specific format (e.g., (XXX) XXX-XXXX).
     *
     * @param employeeCell The employee's cell phone number.
     * @return The formatted cell phone number as a string.
     */
    public static String formatCell(String employeeCell) {
        // Create a StringBuffer with the employee cell phone number
        StringBuffer result = new StringBuffer(employeeCell);

        // Insert parentheses, space, and dash at specific positions in the cell phone number
        result.insert(0, "(");
        result.insert(4, ") ");
        result.insert(9, "-");

        // Return the formatted cell phone number as a string
        return result.toString();
    }
    /**
     * Shows an alert dialog with the specified title and content text.
     *
     * @param title   The title of the alert dialog.
     * @param content The content text of the alert dialog.
     */
    private void showAlert(String title, String content) {
        Alert invalidNumAlert = new Alert(Alert.AlertType.ERROR);
        invalidNumAlert.setTitle(title);
        invalidNumAlert.setHeaderText(null);
        invalidNumAlert.setContentText(content);
        invalidNumAlert.showAndWait();
    }

    /**
     * Opens a new window displaying the personal details of the selected employee.
     * If no employee is selected, shows a warning message.
     */
    private void openEmployeeInfo() {
        // Get the selected employee from the table view
        Employee selectedEmployee = employeeList.getSelectionModel().getSelectedItem();

        // Check if an employee is selected
        if (selectedEmployee == null) {
            // If no employee is selected, show a warning message
            showAlert("Error!", "No employee is selected! Please select an employee from the list to check details.");
            return;
        }

        // If an employee is selected, open a new window displaying their details
        new EmployeeInfo(selectedEmployee).start(new Stage());
    }


    /**
     * Prompts the user to enter new employee information and adds the employee to the employee list.
     */
    private void addEmployeeInfo(String COOKIES) {
        // Generate a unique employee ID for the new employee
        int employeeID = employeeList.getItems().size() + 1;
        String stringid = String.valueOf(employeeID);

        // Prompt the user for first name
        TextInputDialog firstNameInput = new TextInputDialog();
        firstNameInput.setTitle("Add New First Name");
        firstNameInput.setHeaderText("Enter First Name");
        String firstName = firstNameInput.showAndWait().orElse("");

        // Prompt the user for last name
        TextInputDialog lastNameInput = new TextInputDialog();
        lastNameInput.setTitle("Add New Last Name");
        lastNameInput.setHeaderText("Enter Last Name");
        String lastName = lastNameInput.showAndWait().orElse("");

        for (Employee employee: data) {
            if (Objects.equals(employee.getEmployeeFirstName().toLowerCase(), firstName.toLowerCase()) &&
                    Objects.equals(employee.getEmployeeLastName().toLowerCase(), lastName.toLowerCase())) {
                showAlert("Duplicate Error", "Employee with the first and last name already exist!");
                return;
            }
        }

        // Prompt the user for cell phone number
        String newEmployeeCell;
        try {
            TextInputDialog cellInput = new TextInputDialog("0");
            cellInput.setHeaderText("Enter 10 digit American Phone Number");
            newEmployeeCell = cellInput.showAndWait().orElse("Invalid Input!");

            // Validate the cell phone number format
            if (newEmployeeCell.length() != 10) {
                throw new RuntimeException();
            } else if (isDuplicate(formatCell(newEmployeeCell), "cell")) {
                showAlert("Duplication Error", "Employee with the cell number already exist!");
                return;
            }
        } catch (RuntimeException e) {
            // Show an error message if the input is not a valid 10-digit number
            showAlert("Invalid Input!", "Please enter a valid 10 digit Canadian Cell Number");
            return;
        }
        // Format the cell phone number
        String employeeCell = formatCell(newEmployeeCell);

        // Prompt the user for email address
        String employeeEMail;
        try {
            TextInputDialog emailInput = new TextInputDialog("0");
            emailInput.setHeaderText("Enter Valid Email Address");
            employeeEMail = emailInput.showAndWait().orElse("");

            // Validate the email address format
            if (!employeeEMail.contains("@") || employeeEMail.length() <= 4) {
                throw new RuntimeException();
            } else if (isDuplicate(employeeEMail, "email")) {
            showAlert("Duplication Error", "Employee with the email already exist!");
            return;
        }
        } catch (RuntimeException e) {
            // Show an error message if the input is not a valid email address
            showAlert("Invalid Input!", "Please enter a valid Email address");
            return;
        }

        // Prompt the user for employee title
        TextInputDialog titleInput = new TextInputDialog();
        titleInput.setTitle("Title Assignment");
        titleInput.setHeaderText("Add title to this employee");
        String employeeTitle = titleInput.showAndWait().orElse("");

        String username = generateUsername(firstName,lastName);
        String password = "pass1234";

        String msg = "{" +
                "\"id\":\"" + stringid + "\"," +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"email\":\"" + employeeEMail + "\"," +
                "\"cellNumber\":\"" + employeeCell + "\"," +
                "\"fname\":\"" + firstName + "\"," +
                "\"lname\":\"" + lastName + "\"," +
                "\"title\":\"" + employeeTitle + "\"" +
                "}";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/addEmployeeData"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println("[ADD EMPLOYEE]: " + request.toString());
        try{
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("[ADD EMPLOYEE]: " +response.body());
        }catch (Exception e){
            System.out.println(e);
        }

        if (isDuplicate(firstName + " " + lastName, "name")) {
            showAlert("Duplicate Error", "Employee with the first and last name already exist!");
            return;
        } else {
            // Add the new employee to the data list and refresh the table view
            employeeFirstNameList.add(firstName);
            try {
                loadEmployeeList(getEmployeeData(COOKIES));
            }catch(Exception e){
                showAlert("Error!",e.toString());
            }

        }
    }

    /**
     * Allows the user to modify the information of a selected employee.
     */
    private void modifyEmployeeInfo(String COOKIE) {
        // Get the selected employee from the table view
        Employee selectedEmployee = employeeList.getSelectionModel().getSelectedItem();


        // Check if an employee is selected
        if (selectedEmployee == null) {
            // If no employee is selected, show a warning message
            showAlert("Error!", "No employee is selected! Please select an employee from the list to modify.");
            return;
        }

        AtomicReference<String> modUsername = new AtomicReference<>(selectedEmployee.getUsername());
        AtomicReference<String> modFirstName = new AtomicReference<>(selectedEmployee.getEmployeeFirstName());
        AtomicReference<String> modLastName = new AtomicReference<>(selectedEmployee.getEmployeeLastName());
        AtomicReference<String> modCellNumber = new AtomicReference<>(selectedEmployee.getCell());
        AtomicReference<String> modEMail = new AtomicReference<>(selectedEmployee.getEMail());
        AtomicReference<String> modTitle = new AtomicReference<>(selectedEmployee.getTitle());


        // Create a new stage for modification options
        Stage modifyStage = new Stage();
        TilePane modifyTile = new TilePane();

        // Label for prompting the user
        Label promptmsg = new Label("Select information to modify:");

        // Checkboxes for different fields
        CheckBox firstNameMod = new CheckBox("First Name");
        CheckBox lastNameMod = new CheckBox("Last Name");
        CheckBox cellNumberMod = new CheckBox("Cell Number");
        CheckBox eMailMod = new CheckBox("Email");
        CheckBox titleMod = new CheckBox("Job Title");

        // Button for confirming modifications
        Button doneButton = new Button("Done");
        doneButton.setOnAction(event -> {
            // Check which fields are selected for modification
            if (firstNameMod.isSelected()) {
                TextInputDialog firstNameInput = new TextInputDialog(selectedEmployee.getEmployeeFirstName());
                firstNameInput.setTitle("Modify First Name");
                firstNameInput.setHeaderText("Enter new First Name");
                String newFirstName = firstNameInput.showAndWait().orElse("");

                if (isDuplicate(newFirstName + " " + selectedEmployee.getEmployeeLastName(), "name")) {
                    showAlert("Duplicate Error", "Employee with the first and last name already exist!");
                }
                modFirstName.set(newFirstName);
                employeeFirstNameList.remove(selectedEmployee.getEmployeeFirstName());
                employeeFirstNameList.add(newFirstName);
            }

            if (lastNameMod.isSelected()) {
                TextInputDialog lastNameInput = new TextInputDialog(selectedEmployee.getEmployeeLastName());
                lastNameInput.setTitle("Modify Last Name");
                lastNameInput.setHeaderText("Enter new Last Name");
                String newLastName = lastNameInput.showAndWait().orElse("");

                if (isDuplicate(selectedEmployee.getEmployeeFirstName() + " " + newLastName, "name")) {
                    showAlert("Duplicate Error", "Employee with the first and last name already exist!");
                    return;
                }
                modLastName.set(newLastName);

            }

            if (cellNumberMod.isSelected()) {
                String newEmployeeCell;
                try {
                    TextInputDialog cellInput = new TextInputDialog();
                    cellInput.setHeaderText("Enter 10 digit American Phone Number");
                    newEmployeeCell = cellInput.showAndWait().orElse("Invalid Input!");

                    // Validate the cell phone number format
                    if (newEmployeeCell.length() != 10) {
                        throw new RuntimeException();
                    } else if (isDuplicate(formatCell(newEmployeeCell), "cell")) {
                        showAlert("Duplication Error", "Employee with the cell number already exist!");
                        return;
                    }
                    modCellNumber.set(formatCell(newEmployeeCell));
                } catch (RuntimeException e) {
                    showAlert("Error!", "Invalid Input! Please enter a valid 10 digit Canadian Cell Number");
                    return;
                }
            }

            if (eMailMod.isSelected()) {
                TextInputDialog emailInput = new TextInputDialog(selectedEmployee.getEMail());
                emailInput.setHeaderText("Enter Valid Email Address");
                String newEmployeeEmail = emailInput.showAndWait().orElse("");

                // Validate the email address format
                if (!newEmployeeEmail.contains("@")||newEmployeeEmail.length()<=4) {
                    showAlert("Error!", "Invalid Input! Please enter a valid Email address");
                    return;
                } else if (isDuplicate(newEmployeeEmail, "email")) {
                    showAlert("Duplication Error", "Employee with the email already exist!");
                    return;
                }
                modEMail.set(newEmployeeEmail);
            }

            if (titleMod.isSelected()) {
                TextInputDialog titleInput = new TextInputDialog(selectedEmployee.getTitle());
                titleInput.setTitle("Modify Title");
                titleInput.setHeaderText("Enter new Title");
                String newEmployeeTitle = titleInput.showAndWait().orElse("");
                modTitle.set(newEmployeeTitle);
            }

            String msg = "{" +
                    "\"username\":\"" + modUsername.get() + "\"," +
                    "\"email\":\"" + modEMail.get() + "\"," +
                    "\"cellNumber\":\"" + modCellNumber.get() + "\"," +
                    "\"fname\":\"" + modFirstName.get() + "\"," +
                    "\"lname\":\"" + modLastName.get() + "\"," +
                    "\"title\":\"" + modTitle.get() + "\"" +
                    "}";
            // Refresh the table view and close the modification window
            System.out.println("[MOD EMPLOYEE]: " + msg);

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:5001/modEmployeeData"))
                    .timeout(Duration.ofMinutes(2))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                    .build();

            System.out.println("[MOD EMPLOYEE]: " + request.toString());
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            }catch (Exception e){
                System.out.println(e);
            }

            try{
                loadEmployeeList(getEmployeeData(COOKIE));
            }catch (Exception e){
                showAlert("Error!",e.toString());
            }

            modifyStage.close();
        });

        // Add UI elements to the modification stage
        modifyTile.getChildren().addAll(promptmsg,firstNameMod, lastNameMod, cellNumberMod, eMailMod, titleMod, doneButton);

        Scene modifyScene = new Scene(modifyTile, 200, 200);
        modifyStage.setTitle("Modify Options");
        modifyStage.setScene(modifyScene);
        modifyStage.show();
    }

    /**
     * Deletes the selected employee from the employee list.
     */
    private void deleteEmployee(String COOKIE) {
        // Get the selected employee from the table view
        Employee selectedEmployee = employeeList.getSelectionModel().getSelectedItem();

        // Check if an employee is selected
        if (selectedEmployee == null) {
            // If no employee is selected, show a warning message
            showAlert("Error!", "No employee is selected! Please select an employee from the list to delete.");
            return;
        }

        // Remove the selected employee from the data list

        String msg = "{" +
                "\"deleteUsername\":\"" + selectedEmployee.getUsername() + "\"" +
                "}";
        System.out.println("[EMPLOYEE DELETE]: " + msg);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/deleteEmployeeData"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println("[EMPLOYEE DELETE]: " + request.toString());
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }catch (Exception e){
            System.out.println(e);
        }

        employeeFirstNameList.remove(selectedEmployee.getEmployeeFirstName());
        try{
            loadEmployeeList(getEmployeeData(COOKIE));
        }catch (Exception e){
            showAlert("Error!",e.toString());
        }

        // Refresh the table view to reflect the changes
    }

    private String getEmployeeData(String COOKIES) throws IOException, InterruptedException {
        String msg = "{" +
                "\"cookie\":\"" + COOKIES + "\"" +
                "}";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/getEmployeesData"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println("[LOAD EMPLOYEE]: " + request.toString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

    private void loadEmployeeList(String responsBody){
        data.clear();
        employeeFirstNameList.clear();
        try {
            ObjectMapper mapper = new ObjectMapper();

            Employees employees = mapper.readValue(responsBody, Employees.class);
            this.data.addAll(employees.getEmployees());
        } catch (Exception e){
            System.out.println(e);
        }

        for (Employee employee: data){
            employeeFirstNameList.add(employee.getEmployeeFirstName());
        }

        employeeList.refresh();
    }

    public static String parseJson(String string, String target) {
        // Find the index of the cookie key
        int startIndex = string.indexOf("\"" + target + "\"");

        // Check if the key is found
        if (startIndex != -1) {
            // Move the index to the start of the value
            startIndex = string.indexOf(":", startIndex) + 1;

            // Find the end of the value (up to the next comma or the end of the JSON object)
            int endIndex = string.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = string.indexOf("}", startIndex);
            }

            String value = string.substring(startIndex, endIndex).trim().replace("\"", "");

            return value;

        } else {
            System.out.println(target + " not found in the response");
            return null;
        }
    }

    /**
     * Checks whether a given input for a specific attribute type (cell, email, name, or username)
     * already exists in the list of Employee objects.
     *
     * @param input         The input to check for duplicates.
     * @param attributeType The type of attribute to check (cell, email, name, or username).
     * @return              True if a duplicate is found, false otherwise.
     */
    private boolean isDuplicate(String input, String attributeType) {
        // Convert the input to lowercase for case-insensitive comparison
        input = input.toLowerCase();

        // Iterate through each employee in the list of Employee objects (data)
        for (Employee employee : data) {
            // Check based on the attribute type
            switch (attributeType) {
                case "cell":
                    // If the input matches any employee's cell, return true (duplicate found)
                    if (employee.getCell().equals(input)) {
                        return true;
                    }
                    break;
                case "email":
                    // If the input matches any employee's email (case-insensitive), return true (duplicate found)
                    if (employee.getEMail().toLowerCase().equals(input)) {
                        return true;
                    }
                    break;
                case "name":
                    // If the input matches any employee's first and last name (case-insensitive), return true (duplicate found)
                    if (employee.getEmployeeFirstName().toLowerCase().equals(input.split(" ")[0]) &&
                            employee.getEmployeeLastName().toLowerCase().equals(input.split(" ")[1])) {
                        return true;
                    }
                    break;
                case "username":
                    // If the input matches any employee's username (case-insensitive), return true (duplicate found)
                    if (employee.getUsername().toLowerCase().equals(input)) {
                        return true;
                    }
                    break;
            }
        }
        // If no match is found, return false (no duplicate)
        return false;
    }

}