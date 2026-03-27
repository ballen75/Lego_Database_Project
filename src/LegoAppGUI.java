import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;

public class LegoAppGUI extends Application {


    private LegoSetManager manager = new LegoSetManager(); //Object that handles CRUD operations
    private TableView<LegoSet> table = new TableView<>();

    private final String buttonColor = "#3498db"; // blue
    private final String buttonTextColor = "#ffffff"; // white

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Sets the title of my GUI window
        primaryStage.setTitle("Lego Set Manager");

        // Each table represents a property of the LegoSet to display
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

        // Create buttons for the CRUD actions.
        Button addButton = createButton("Add Set");
        Button loadButton = createButton("Upload File");
        Button removeButton = createButton("Remove Set");
        Button updateButton = createButton("Update Set");
        Button totalButton = createButton("Show Total Pieces");

        HBox buttonBox = new HBox(10, addButton, loadButton, removeButton, updateButton, totalButton);
        buttonBox.setPadding(new Insets(10));

        // --- Your existing VBox with table + buttons ---
        VBox layout = new VBox(10, table, buttonBox);
        layout.setPadding(new Insets(10));

// --- Create a BorderPane and set background image ---
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(layout);

// Loads the background image for the GUI
        Image bgImage = new Image("file:C:\\Users\\brisi\\OneDrive\\Documents\\Software Dev 1\\LegoDatabase\\src\\images\\LegoBackground1.jpg"); // relative path
        BackgroundSize bgSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,  // don't repeat
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bgSize);

        borderPane.setBackground(new Background(backgroundImage));


        Scene scene = new Scene(borderPane, 900, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Connect buttons to the methods
        addButton.setOnAction(e -> addLegoSetDialog());
        loadButton.setOnAction(e -> loadFromFile(primaryStage));
        removeButton.setOnAction(e -> removeLegoSet());
        updateButton.setOnAction(e -> updateLegoSetDialog());
        totalButton.setOnAction(e -> showTotalPieces());


    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + buttonColor + "; -fx-text-fill: " + buttonTextColor + ";" + "-fx-padding: 10 20 10 20;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5,0,0,1);");
        return btn;
    }
    //Dialog to add new Lego Set's.
    private void addLegoSetDialog() {
        Dialog<LegoSet> dialog = new Dialog<>();
        dialog.setTitle("Add Lego Set");

        // --- Form Inputs ---
        Label idLabel = new Label("Set ID:");
        TextField idField = new TextField();

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label piecesLabel = new Label("Pieces:");
        TextField piecesField = new TextField();

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();

        Label yearLabel = new Label("Year:");
        TextField yearField = new TextField();

        Label ageLabel = new Label("Recommended Age:");
        TextField ageField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(idLabel, 0, 0); grid.add(idField, 1, 0);
        grid.add(nameLabel, 0, 1); grid.add(nameField, 1, 1);
        grid.add(piecesLabel, 0, 2); grid.add(piecesField, 1, 2);
        grid.add(priceLabel, 0, 3); grid.add(priceField, 1, 3);
        grid.add(yearLabel, 0, 4); grid.add(yearField, 1, 4);
        grid.add(ageLabel, 0, 5); grid.add(ageField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        ButtonType addBtnType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addBtnType) {
                // --- Validation ---
                String id = idField.getText().trim();
                if (!id.matches("\\d+") || manager.findSet(id) != null) {
                    showAlert("Invalid or duplicate ID.");
                    return null;
                }

                String name = nameField.getText().trim();
                if (name.isEmpty()) { showAlert("Name cannot be empty."); return null; }

                int pieces; double price; int year; int age;
                try {
                    pieces = Integer.parseInt(piecesField.getText().trim());
                    if (pieces < 0 || pieces > 10000) { showAlert("Pieces must be 0–10000."); return null; }

                    price = Double.parseDouble(priceField.getText().trim());
                    if (price < 0 || price > 1000) { showAlert("Price must be 0–1000."); return null; }

                    year = Integer.parseInt(yearField.getText().trim());
                    if (year < 1950 || year > 2026) { showAlert("Year must be 1950–2026."); return null; }

                    age = Integer.parseInt(ageField.getText().trim());
                    if (age < 1 || age > 99) { showAlert("Recommended age must be 1–99."); return null; }

                } catch (NumberFormatException e) {
                    showAlert("Invalid number input.");
                    return null;
                }

                return new LegoSet(id, name, pieces, price, year, age);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(set -> {
            manager.addSet(set);
            table.getItems().add(set);
        });
    }
    //Load Lego Sets from a file
    private void loadFromFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Lego Set File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            int loaded = manager.loadSetsFromFile(file.getAbsolutePath());
            table.getItems().setAll(manager.listSets());
            showAlert(loaded + " Lego sets loaded.");
        }
    }
 // Remove selected Lego Set.
    private void removeLegoSet() {
        LegoSet selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            manager.deleteSet(selected.getLegoSetID());
            table.getItems().remove(selected);
        } else {
            showAlert("Select a Lego set to remove.");
        }
    }
 // Update selected Lego Set
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
                // validation
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

                selected.setsetName(name);
                selected.setPieceCount(pieces);
                selected.setPrice(price);
                selected.setReleaseYear(year);
                selected.setRecommendedAge(age);
                table.refresh();
            }
            return null;
        });

        dialog.showAndWait();
    }
 //Display the total number of lego pieces stored in local data.
    private void showTotalPieces() {
        int total = manager.countTotalPieces();
        showAlert("Total pieces across all sets: " + total);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}