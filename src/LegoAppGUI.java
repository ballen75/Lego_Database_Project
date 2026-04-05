import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.*;
import java.sql.Connection;

public class LegoAppGUI extends Application {

    private TableView<LegoSet> table = new TableView<>();
    private Connection conn; // DATABASE CONNECTION

    private final String buttonColor = "#3498db";
    private final String buttonTextColor = "#ffffff";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Lego Set Manager");

        TableColumn<LegoSet, String> idCol = new TableColumn<>("Set ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("legoSetID"));

        TableColumn<LegoSet, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("setName"));

        TableColumn<LegoSet, Integer> piecesCol = new TableColumn<>("Total Pieces");
        piecesCol.setCellValueFactory(new PropertyValueFactory<>("pieceCount"));

        TableColumn<LegoSet, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<LegoSet, Integer> yearCol = new TableColumn<>("Year Released");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

        TableColumn<LegoSet, Integer> ageCol = new TableColumn<>("Recommended Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("recommendedAge"));

        table.getColumns().addAll(idCol, nameCol, piecesCol, priceCol, yearCol, ageCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Buttons
        Button connectButton = createButton("Connect DB");
        Button addButton = createButton("Add Set");
        Button loadFileButton = createButton("Load Sets from File");
        Button removeButton = createButton("Remove Set");
        Button updateButton = createButton("Update Set");
        Button totalButton = createButton("Show Total Pieces");

        HBox buttonBox = new HBox(10, connectButton, addButton, loadFileButton, removeButton, updateButton, totalButton);
        buttonBox.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, buttonBox);
        layout.setPadding(new Insets(10));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(layout);

        // Background image
        Image bgImage = new Image(getClass().getResource("/images/LegoBackground.jpg").toExternalForm());
        BackgroundSize bgSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bgSize);

        borderPane.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(borderPane, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Button Actions
        connectButton.setOnAction(e -> connectToDatabase(primaryStage));
        addButton.setOnAction(e -> addLegoSetDialog());
        loadFileButton.setOnAction(e -> loadSetsFromFile(primaryStage));
        removeButton.setOnAction(e -> removeLegoSet());
        updateButton.setOnAction(e -> updateLegoSetDialog());
        totalButton.setOnAction(e -> showTotalPieces());
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + buttonColor + "; -fx-text-fill: " + buttonTextColor + ";" +
                "-fx-padding: 10 20 10 20;" +
                "-fx-background-radius: 15;");
        return btn;
    }

    // Connection to Database
    private void connectToDatabase(Stage stage) {
        TextInputDialog dialog = new TextInputDialog("lego.db");
        dialog.setTitle("Connect to Database");
        dialog.setHeaderText("Enter database path:");

        dialog.showAndWait().ifPresent(path -> {
            conn = DatabaseManager.connect(path);
            if (conn != null) {
                DatabaseSetup.createTable(conn);
                refreshTable();
                showAlert("Connected!");
            } else {
                showAlert("Connection failed.");
            }
        });
    }

    // Add data manually to the Lego Database
    private void addLegoSetDialog() {
        if (conn == null) {
            showAlert("Connect to database first.");
            return;
        }

        Dialog<LegoSet> dialog = new Dialog<>();
        dialog.setTitle("Add Lego Set");

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField piecesField = new TextField();
        TextField priceField = new TextField();
        TextField yearField = new TextField();
        TextField ageField = new TextField();

        // --- Only allow digits for Set ID ---
        idField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getText().matches("[0-9]*")) {
                idField.setStyle(""); // reset style if valid
                return change;
            } else {
                idField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                return null; // reject non-digit input
            }
        }));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("ID:"), 0, 0); grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1); grid.add(nameField, 1, 1);
        grid.add(new Label("Pieces:"), 0, 2); grid.add(piecesField, 1, 2);
        grid.add(new Label("Price:"), 0, 3); grid.add(priceField, 1, 3);
        grid.add(new Label("Year:"), 0, 4); grid.add(yearField, 1, 4);
        grid.add(new Label("Age:"), 0, 5); grid.add(ageField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String piecesStr = piecesField.getText().trim();
                String priceStr = priceField.getText().trim();
                String yearStr = yearField.getText().trim();
                String ageStr = ageField.getText().trim();

                if (id.isEmpty() || name.isEmpty() || piecesStr.isEmpty() || priceStr.isEmpty()
                        || yearStr.isEmpty() || ageStr.isEmpty()) {
                    showAlert("All fields must be filled!");
                    return null;
                }

                try {
                    // Validate numeric fields
                    int pieces = Integer.parseInt(piecesStr);
                    if (pieces < 0 || pieces > 10000) { showAlert("Pieces must be 0–10000."); return null; }

                    double price = Double.parseDouble(priceStr);
                    if (price < 0 || price > 1000) { showAlert("Price must be 0–1000."); return null; }

                    int year = Integer.parseInt(yearStr);
                    if (year < 1950 || year > 2026) { showAlert("Year must be 1950–2026."); return null; }

                    int age = Integer.parseInt(ageStr);
                    if (age < 1 || age > 99) { showAlert("Age must be 1–99."); return null; }

                    // Check ID: numeric only (already enforced) + uniqueness
                    if (LegoDAO.exists(conn, id)) {
                        showAlert("ID already exists!");
                        return null;
                    }

                    return new LegoSet(id, name, pieces, price, year, age);

                } catch (NumberFormatException e) {
                    showAlert("Invalid number input.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(set -> {
            LegoDAO.addSet(conn, set);
            refreshTable();
        });
    }

    // Batch upload sets from a text file
    private void loadSetsFromFile(Stage stage) {
        if (conn == null) {
            showAlert("Connect to database first.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Lego Set File");
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("-");

                if (parts.length < 6) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                try {
                    String id = parts[0].trim();
                    String ageStr = parts[parts.length - 1].trim();
                    String yearStr = parts[parts.length - 2].trim();
                    String priceStr = parts[parts.length - 3].trim();
                    String piecesStr = parts[parts.length - 4].trim();

                    // Name is everything in between ID and pieces/price/year/age
                    StringBuilder nameBuilder = new StringBuilder();
                    for (int i = 1; i <= parts.length - 5; i++) {
                        if (i > 1) nameBuilder.append("-");
                        nameBuilder.append(parts[i].trim());
                    }
                    String name = nameBuilder.toString();

                    int pieces = Integer.parseInt(piecesStr);
                    double price = Double.parseDouble(priceStr);
                    int year = Integer.parseInt(yearStr);
                    int age = Integer.parseInt(ageStr);

                    LegoSet set = new LegoSet(id, name, pieces, price, year, age);

                    if (!LegoDAO.exists(conn, id)) {
                        LegoDAO.addSet(conn, set);
                        count++;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Skipping line with invalid number: " + line);
                }
            }

            refreshTable();
            showAlert(count + " Lego sets loaded from file!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error loading file.");
        }
    }

    // Delete lego set from the database
    private void removeLegoSet() {
        LegoSet selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            LegoDAO.deleteSet(conn, selected.getLegoSetID());
            refreshTable();
        } else {
            showAlert("Select a set.");
        }
    }

    // Update Lego Set
    private void updateLegoSetDialog() {
        LegoSet selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a Lego set to update.");
            return;
        }

        Dialog<LegoSet> dialog = new Dialog<>();
        dialog.setTitle("Update Lego Set");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField(selected.getSetName());

        Label piecesLabel = new Label("Pieces:");
        TextField piecesField = new TextField(String.valueOf(selected.getPieceCount()));

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField(String.valueOf(selected.getPrice()));

        Label yearLabel = new Label("Year:");
        TextField yearField = new TextField(String.valueOf(selected.getReleaseYear()));

        Label ageLabel = new Label("Recommended Age:");
        TextField ageField = new TextField(String.valueOf(selected.getRecommendedAge()));

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(nameLabel, 0, 0); grid.add(nameField, 1, 0);
        grid.add(piecesLabel, 0, 1); grid.add(piecesField, 1, 1);
        grid.add(priceLabel, 0, 2); grid.add(priceField, 1, 2);
        grid.add(yearLabel, 0, 3); grid.add(yearField, 1, 3);
        grid.add(ageLabel, 0, 4); grid.add(ageField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        ButtonType updateBtnType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateBtnType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateBtnType) {
                String name = nameField.getText().trim();
                if (name.isEmpty()) { showAlert("Name cannot be empty."); return null; }

                int pieces, year, age; double price;
                try {
                    pieces = Integer.parseInt(piecesField.getText().trim());
                    if (pieces < 0 || pieces > 10000) { showAlert("Pieces 0–10000."); return null; }

                    price = Double.parseDouble(priceField.getText().trim());
                    if (price < 0 || price > 1000) { showAlert("Price 0–1000."); return null; }

                    year = Integer.parseInt(yearField.getText().trim());
                    if (year < 1950 || year > 2026) { showAlert("Year 1950–2026."); return null; }

                    age = Integer.parseInt(ageField.getText().trim());
                    if (age < 1 || age > 99) { showAlert("Age 1–99."); return null; }

                } catch (NumberFormatException e) {
                    showAlert("Invalid number input."); return null;
                }

                // Update the object
                selected.setsetName(name);
                selected.setPieceCount(pieces);
                selected.setPrice(price);
                selected.setReleaseYear(year);
                selected.setRecommendedAge(age);

                // --- NEW: Update in the database ---
                LegoDAO.updateSet(conn, selected);  // conn is your active DB connection

                table.refresh(); // refresh the TableView to show updated values
            }
            return null;
        });

        dialog.showAndWait();
    }
    // Custom Method for total lego pieces in the database
    private void showTotalPieces() {
        int total = LegoDAO.getAllSets(conn)
                .stream()
                .mapToInt(LegoSet::getPieceCount)
                .sum();

        showAlert("Total Pieces: " + total);
    }

    // Refresh Table
    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(
                LegoDAO.getAllSets(conn)
        ));
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.show();
    }
}