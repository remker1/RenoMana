package ProjectMana;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class Project extends VBox {

    public static TableView<ProjectItem> ProjectTable;
    public static ObservableList<ProjectItem> data;

    public Project() throws IOException, InterruptedException {
        // Setting up the table
        ProjectTable = new TableView<>();
        ProjectTable.prefWidthProperty().bind(this.widthProperty());
        data = FXCollections.observableArrayList();

        // Adding column names
        TableColumn<ProjectItem, String> ProjectCol = new TableColumn<>("Project");
        ProjectCol.setCellValueFactory(cellData -> cellData.getValue().Name());
        ProjectCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.3)); // 40% width

        TableColumn<ProjectItem, String> EmailCol = new TableColumn<>("Email");
        EmailCol.setCellValueFactory(cellData -> cellData.getValue().Email());
        EmailCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.20));

        TableColumn<ProjectItem, String> ContactCol = new TableColumn<>("Contact");
        ContactCol.setCellValueFactory(cellData -> cellData.getValue().Contact());
        ContactCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.20));

        TableColumn<ProjectItem, String> CompanyCol = new TableColumn<>("Company");
        CompanyCol.setCellValueFactory(cellData -> cellData.getValue().Company());
        CompanyCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.20));

        TableColumn<ProjectItem, String> Start_dateCol = new TableColumn<>("Start_date");
        Start_dateCol.setCellValueFactory(cellData -> cellData.getValue().Start_date());
        Start_dateCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.20));

        TableColumn<ProjectItem, String> End_dateCol = new TableColumn<>("End_date");
        End_dateCol.setCellValueFactory(cellData -> cellData.getValue().End_date());
        End_dateCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.3)); // 40% width

        TableColumn<ProjectItem, String> DescriptionCol = new TableColumn<>("Description");
        DescriptionCol.setCellValueFactory(cellData -> cellData.getValue().Description());
        DescriptionCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.3)); // 40% width

        TableColumn<ProjectItem, String> InquiryCol = new TableColumn<>("Inquiry");
        InquiryCol.setCellValueFactory(cellData -> cellData.getValue().Inquiry());
        InquiryCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.3)); // 40% width


        ProjectTable.getColumns().addAll(ProjectCol,
                                         EmailCol,
                                         ContactCol,
                                         CompanyCol,
                                         Start_dateCol,
                                         End_dateCol,
                                         DescriptionCol);
        ProjectTable.setItems(data);


        // Setting up the button options for things user can do in this tab
        Button refreshReviews = new Button("Refresh");

        VBox.setVgrow(ProjectTable, Priority.ALWAYS);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/submitRequest"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String[] responseBody = response.body().split("(?<=},)");


    }}
