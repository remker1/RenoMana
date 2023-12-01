// import com.sun.net.httpserver.Authenticator;
//import javafx.geometry.Insets;
//import javafx.scene.control.CheckBox;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.Button;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//
//import javax.mail.Message;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.awt.*;
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.nio.charset.StandardCharsets;
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//public class Report extends VBox {
//
//    private String COOKIES.COOKIES;
//    public Report(String COOKIES.COOKIES) {
//
//        this.COOKIES.COOKIES = COOKIES.COOKIES;
//        // Set padding and spacing for the VBox layout
//        this.setPadding(new Insets(15, 15, 15, 15));
//        this.setSpacing(10);
//
//        // Input Frequency Box
//        HBox frequencyBox = new HBox();
//        frequencyBox.setSpacing(10); // Set spacing between elements in HBox
//        Label frequencyLabel = new Label("Send Report Frequency: ");
//        ComboBox<String> timeFrameDropdown = new ComboBox<>();
//        timeFrameDropdown.getItems().addAll("Daily", "Monthly", "Yearly");
//        frequencyBox.getChildren().addAll(frequencyLabel, timeFrameDropdown);
//
//        // Checkboxes
//        CheckBox inventoryCheckbox = new CheckBox("Include Inventory List");
//        CheckBox deadlineCheckbox = new CheckBox("Include Project Deadlines");
//
//        // Send Report Button
//        Button sendReportButton = new Button("Send Report");
//        sendReportButton.setOnAction(e -> sendReport());
//
//        // Adding components to the VBox layout
//        this.getChildren().addAll(frequencyBox, inventoryCheckbox, deadlineCheckbox, sendReportButton);
//    }
//
//    private String fetchInventoryData() {
//        // TODO: Fetch inventory data from database
//        return null;
//    }
//
//    private String fetchProjectData(String COOKIES.COOKIES) throws IOException, InterruptedException {
//        String msg = "{\"cookie\":\"" + COOKIES.COOKIES + "\"}";
//
//        HttpClient httpClient = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://127.0.0.1:5001/getProjectsData'"))
//                .timeout(Duration.ofMinutes(2))
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//        return response.body(); // a JSON string with project data
//    }
//
//    private String createReport(String projectData) {
//        return "Report Content:\n " + projectData;
//    }
//
//    private void sendReport() {
//        try {
//            List<String> emails = getEmailAddresses(COOKIES.COOKIES);
//
//            String projectData = fetchProjectData(COOKIES.COOKIES);
//            String report = createReport(projectData);
//
//            // For each email, send the report
//            for(String email : emails) {
//                sendEmail(email, report);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private List<String> getEmailAddresses(String COOKIES.COOKIES) throws IOException, InterruptedException {
//        List<String> emailList = new ArrayList<>();
//        String employeeDataJson = getEmployeeData(COOKIES.COOKIES);
//
//        // Parse Employee data to get email
//        String[] employees = employeeDataJson.split("\\},\\{");
//        for (String employee : employees) {
//            int emailStart = employee.indexOf("\"email\":\"") + 9;
//            int emailEnd = employee.indexOf("\"", emailStart);
//            if (emailStart != -1 && emailEnd != -1) {
//                String email = employee.substring(emailStart, emailEnd);
//                emailList.add(email);
//            }
//        }
//
//        return emailList;
//    }
//
//
//    private String getEmployeeData(String COOKIES.COOKIES) throws IOException, InterruptedException {
//        String msg = "{\"cookie\":\"" + COOKIES.COOKIES + "\"}";
//
//        HttpClient httpClient = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://127.0.0.1:5001/getEmployeeData"))
//                .timeout(Duration.ofMinutes(2))
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        return response.body(); // Returns JSON string with employee data
//    }
//
//    private void sendEmail(String email, String reportContent) {
//        try {
//            String from = "your-email@example.com"; // Your email
//            String host = "smtp.example.com"; // SMTP server
//
//            // Get system properties
//            Properties properties = System.getProperties();
//
//            // Setup mail server
//            properties.setProperty("mail.smtp.host", host);
//
//            // Get the default Session object
//            Session session = Session.getDefaultInstance(properties);
//
//            // Create a default MimeMessage object
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header
//            message.setFrom(new InternetAddress(from));
//
//            // Set To: header field of the header
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
//
//            // Set Subject: header field
//            message.setSubject("Your Report");
//
//            // Now set the actual message
//            message.setText(reportContent);
//
//            // Send message
//            Transport.send(message);
//            System.out.println("Sent message successfully....");
//        } catch (Exception mex) {
//            mex.printStackTrace();
//        }
//    }
//}