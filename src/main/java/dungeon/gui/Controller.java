package dungeon.gui;

import dungeon.engine.Cell;
import dungeon.engine.GameEngine;
import dungeon.engine.Tile;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    @FXML private Button helpButton;

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
        helpButton.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Help");
            a.setHeaderText("MiniDungeon Game");
            a.setContentText(
                    """
                    Welcome to MiniDungeon Game!
                    Get to the dungeon exit in 100 moves, scoring as many points as possible.
                    
                    Controls:
                    Use arrow buttons to move.
                    Help button displays this menu.
                    New Game button starts a new game.
                    Save button saves the game state to disk.
                    Load button loads the game state from disk.
                    
                    Tiles:
                    @\tThis is you.
                    <\tThe level entrance.
                    >\tThe level exit.
                    #\tA wall.
                    .\tThe floor.
                    G\tGold - worth 2 points!
                    H\tPotion - restores 2 HP (max 10).
                    M\tMutant monster - step on it to fight!
                     \tReduces HP, increases score by 2.
                    R\tRanged mutant monster - shoots you from 2 tiles away!
                     \tStep on it to fight, increases score by 2.
                    T\tTrap - avoid these! Reduces HP.
                    """);
            a.show();
        });

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
