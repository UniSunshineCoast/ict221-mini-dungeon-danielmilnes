package dungeon.gui;

import dungeon.engine.Cell;
import dungeon.engine.GameEngine;
import dungeon.engine.Tile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Controller {
    @FXML private GridPane gridPane;
    @FXML private Button upButton;
    @FXML private Button downButton;
    @FXML private Button leftButton;
    @FXML private Button rightButton;

    GameEngine engine;

    @FXML
    public void initialize() {
        // Create game engine
        engine = new GameEngine(10);

        // Bind event handlers
        // U, D, L, R buttons: send player input to engine then update GUI
        upButton.setOnAction(e -> {engine.playerInput("up"); updateGui();});
        downButton.setOnAction(e -> {engine.playerInput("down"); updateGui();});
        leftButton.setOnAction(e -> {engine.playerInput("left"); updateGui();});
        rightButton.setOnAction(e -> {engine.playerInput("right"); updateGui();});

        // Add all cells to GUI
        updateGui();
    }

    private void updateGui() {
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
                gridPane.add(cell, x, y);
            }
        }
        gridPane.setGridLinesVisible(false);
    }
}
