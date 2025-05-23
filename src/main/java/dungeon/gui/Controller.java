package dungeon.gui;

import dungeon.engine.Cell;
import dungeon.engine.GameEngine;
import dungeon.engine.Tile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class Controller {
    @FXML private GridPane gridPane;
    @FXML private Button upButton;
    @FXML private Button downButton;
    @FXML private Button leftButton;
    @FXML private Button rightButton;
    @FXML private Label hpLabel;
    @FXML private Label scoreLabel;
    @FXML private Label movesLabel;
    @FXML private Label messageLogLabel;
    @FXML private Button resetButton;
    @FXML private Button saveButton;
    @FXML private Button loadButton;

    GameEngine engine;

    @FXML
    public void initialize() {
        // Create game engine
        engine = new GameEngine(10);

        // Bind event handlers
        upButton.setOnAction(e -> processInput("up"));
        downButton.setOnAction(e -> processInput("down"));
        leftButton.setOnAction(e -> processInput("left"));
        rightButton.setOnAction(e -> processInput("right"));
        resetButton.setOnAction(e -> processInput("reset"));
        saveButton.setOnAction(e -> processInput("save"));
        loadButton.setOnAction(e -> processInput("load"));

        // Add all cells to GUI
        updateGui();
    }

    private void processInput(String input) {
        // Player can press reset or load at any time
        if (input.equals("reset") || input.equals("load")) {
            engine.playerInput(input);
            updateGui();
            return;
        }
        // Ignore all other inputs if game state is not "running"
        if (engine.getGameState().equals("running")) {
            engine.playerInput(input);
            updateGui();
        }
    }

    private void updateGui() {
        // Update labels
        hpLabel.setText("HP: " + engine.getHP());
        scoreLabel.setText("Score: " + engine.getScore());
        movesLabel.setText("Moves remaining: " + engine.getMovesLeft());

        // Update message log
        String messages = "\n";
        ArrayList<String> messageLog = engine.getMessageLog();
        for (String s : messageLog) {
            messages = messages.concat(s);
            messages = messages.concat("\n");
        }
        messageLogLabel.setText(messages);

        // Clear old GUI grid pane
        gridPane.getChildren().clear();
        // Loop through grid, create Cell (StackPane) for each tile, add to gridPane
        int size = engine.getSize();
        for (int x = 0; x < size; x++) {    // Loop through cell grid, add text field to each cell
            for (int y = 0; y < size; y++) {
                Tile tile = engine.getGrid()[y][x];
                Cell cell = new Cell();
                Text text = new Text(tile.getContent());
                cell.getChildren().add(text);
                cell.setStyle(tile.getFXStyle());
                cell.setPrefWidth(15);
                cell.setPrefHeight(15);
                gridPane.add(cell, x, y);
            }
        }
    }
}
