package dungeon.engine;

import javafx.scene.text.Text;
import java.util.Arrays;
import java.util.Random;

public class GameEngine {

    private Cell[][] map;
    private Tile[][] grid;

    /**
     * Runs the game.
     *
     * @param size the width and height.
     */
    public GameEngine(int size) {

        // The grid is the logical view of the game. The map is the JavaFX view of the game.
        // The grid is a 2D array of Tile objects which each have a "type" and "content".
        // "Type" is player, floor, wall, monster, etc. "Content" is what to display for that tile (@, ., #, M).

        // Create grid
        grid = new Tile[size][size];
        // Fill with floor tiles
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = new FloorTile();
            }
        }
        //place("player", 1); // Place player on grid


        // CODE FOR GUI
        /*
        // Create cell panes
        map = new Cell[size][size];
        // Initial cell setup
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Cell cell = new Cell();
                map[x][y] = cell;
            }
        }
        // Draw map
        drawMap();
         */
    }

    /**
     * Sets the content of each cell in the GUI.
     */
    public void drawMap() {
        int size = getSize();
        for (int x = 0; x < size; x++) {    // Loop through cell grid, add text field to each cell
            for (int y = 0; y < size; y++) {
                Tile tile = grid[x][y];
                Cell cell = map[x][y];
                Text text = new Text(tile.getContent());
                cell.getChildren().add(text);
                cell.setStyle(tile.getFXStyle());
                map[x][y] = cell;
            }
        }
    }

    /**
     * Places a tile of type (tile), (count) number of times
     *
     * @param tile  The tile to be placed
     * @param count The number of this tile to be placed
     */
    public void place(String tile, int count) {
        for (int counter = 0; counter < count; counter++) {
            Random r = new Random();
            boolean occupied = true;
            int x = 0; int y = 0;
            // Find unoccupied tile
            while (occupied) {
                x = r.nextInt(10);
                y = r.nextInt(10);
                if (grid[x][y].getType().equals("floor")) {occupied = false;}
                System.out.println("DEBUG: Attempting to place " + tile + " on " + x + ", " + y +
                        " - currently occupied by " + grid[x][y].getType());
            }
            System.out.println("DEBUG: Placing at " + x + ", " + y);
            if (tile == "player") {
                grid[x][y] = new PlayerTile();
            }
        }
    }

    /**
     * The size of the grid.
     *
     * @return this is both the width and the height.
     */
    public int getSize() {
        return grid.length;
    }

    /**
     * The text map of the current game.
     *
     * @return the map, which is a 2D tile array.
     */
    public Tile[][] getGrid() {
        return grid;
    }

    /**
     * The JavaFX map of the current game.
     *
     * @return the map, which is a 2D cell array.
     */
    public Cell[][] getMap() {
        return map;
    }

    /**
     * Plays a text-based game
     */
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        System.out.printf("The size of map is %d * %d\n", engine.getSize(), engine.getSize());
    }
}
