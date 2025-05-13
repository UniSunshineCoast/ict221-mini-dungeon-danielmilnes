package dungeon.engine;

import javafx.scene.text.Text;
import java.util.Objects;
import java.util.Random;

public class GameEngine {

    /**
     * An example board to store the current game state.
     *
     * Note: depending on your game, you might want to change this from 'int' to String or something?
     */
    private Cell[][] map;
    private String[][] grid;

    /**
     * Creates a square game board.
     *
     * @param size the width and height.
     */
    public GameEngine(int size) {

        // Create grid which cells will represent
        grid = new String[size][size];
        // Fill with empty tiles
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = ".";
            }
        }
        place("@", 1); // Place player


        // Create cell panes
        map = new Cell[size][size];

        // Initial cell setup
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Cell cell = new Cell();
                cell.setContent(grid[x][y]);
                Text text = new Text(cell.getContent());
                cell.getChildren().add(text);
                map[x][y] = cell;
            }
        }

        refreshMap();
    }

    public void refreshMap() {
        int size = getSize();

        for (int x = 0; x < size; x++) {    // Loop through cell grid, add text field to each cell
            for (int y = 0; y < size; y++) {
                Cell cell = map[x][y];
                Text text = new Text(cell.getContent());
                cell.getChildren().add(text);
                cell.setStyle(cell.getFXStyle());
                map[x][y] = cell;
            }
        }
    }

    /**
     * Places a tile (string) in a grid (string table), x number of times
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
                if (Objects.equals(grid[x][y], ".")) {occupied = false;}
            }
            grid[x][y] = tile;
        }
    }

    /**
     * The size of the grid.
     *
     * @return this is both the width and the height.
     */
    public int getSize() {
        return map.length;
    }

    /**
     * The map of the current game.
     *
     * @return the map, which is a 2d array.
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
