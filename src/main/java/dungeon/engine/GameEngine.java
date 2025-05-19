package dungeon.engine;

import javafx.scene.text.Text;
import java.util.Random;

public class GameEngine {

    private Cell[][] map;
    private Tile[][] grid;
    private int level = 1;
    private int ladderX;
    private int ladderY;
    private String gameState;
    private int score = 0;
    private int hp = 10;
    private int movesLeft = 100;

    /**
     * Runs the game.
     *
     * @param size the width and height.
     */
    public GameEngine(int size) {
        gameState = "starting";

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

        // BUILD LEVEL 1: Place tiles on grid
        place("entry", 1);
        place("wall", 20);
        place("player", 1);
        place("ladder", 1);
        place("trap", 5);
        place("gold", 5);
        place("meleeMutant", 3);
        place("rangedMutant", 1);
        place("healthPotion", 2);

        gameState = "running";


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
                x = r.nextInt(getSize());
                y = r.nextInt(getSize());
                if (grid[x][y].getType().equals("floor")) {occupied = false;}
            }

            // Entry tile: bottom-left on level 1, same as level 1 ladder on level 2
            if (tile.equals("entry")) {
                if (level == 1) {grid[getSize()-1][0] = new EntryTile();}
                else {grid[ladderX][ladderY] = new EntryTile();}
            }

            // Ladder tile: place randomly, set ladderX and ladderY on level 1
            if (tile.equals("ladder")) {
                if (level == 1) {ladderX = x; ladderY = y;}
                grid[x][y] = new LadderTile();
            }

            // Player tile: spawn on entry tile
            if (tile.equals("player")) {
                if (level == 1) {grid[getSize()-1][1] = new PlayerTile();}
                if (level == 2) {grid[ladderX][ladderY] = new PlayerTile();}
            }
            // Wall tile: idk what to do with these yet
            if (tile.equals("wall")) {grid[x][y] = new WallTile();}
            // Tiles to be placed randomly
            if (tile.equals("trap")) {grid[x][y] = new TrapTile();}
            if (tile.equals("gold")) {grid[x][y] = new GoldTile();}
            if (tile.equals("meleeMutant"))  {grid[x][y] = new MeleeMutantTile();}
            if (tile.equals("rangedMutant")) {grid[x][y] = new RangedMutantTile();}
            if (tile.equals("healthPotion")) {grid[x][y] = new HealthPotionTile();}
        }
    }

    /**
     * Run when receiving input from the TextUI/GUI controller.
     * @param input The input. Only accepts "up", "down", "left" and "right".
     */
    public void playerInput(String input) {
        // X = ROW
        // Y = COLUMN
        // MUST BE REFERENCED WITH GRID[Y][X]
        int playerX = getPlayerX();
        int playerY = getPlayerY();
        int destinationX;
        int destinationY;
        switch (input) {    // Ignore command if out of bounds, otherwise determine destination
            case "up":
                if (playerY == 0) {return;}
                destinationX = playerX;
                destinationY = playerY - 1;
                break;
            case "down":
                if (playerY == getSize() - 1) {return;}
                destinationX = playerX;
                destinationY = playerY + 1;
                break;
            case "left":
                if (playerX == 0) {return;}
                destinationX = playerX - 1;
                destinationY = playerY;
                break;
            case "right":
                if (playerX == getSize() - 1) {return;}
                destinationX = playerX + 1;
                destinationY = playerY;
                break;
            default:
                System.out.println("Invalid input.");
                return;
        }
        // Don't allow player to move into walls
        if (grid[destinationY][destinationX].getType().equals("wall")) {return;}
        // DEBUG
        System.out.println("Player: " + playerX + ", " + playerY + " " + grid[playerY][playerX].getType());
        System.out.println("Destination: " + destinationX + ", " + destinationY + " " + grid[destinationY][destinationX].getType());

        // Move player
        moveTo(destinationX, destinationY);
    }

    /**
     * Moves the player tile from its current position to the destination. Assumes that the move
     * has already been validated.
     * @param x Destination x (row)
     * @param y Destination y (column)
     */
    public void moveTo(int x, int y) {
        movesLeft -= 1;
        switch (grid[y][x].getType()) {
            case "gold":
                score += 2;
            case "healthPotion":
                hp += 4;
                if (hp > 10) {hp = 10;}
            case "trap":
                hp -= 2;
            case "meleeMutant":
                hp -= 2;
                score += 2;
            case "rangedMutant":
                score += 2;
        }
        // check for ranged enemies
        /*
        if ((grid[y+2][x].getContent().equals("rangedMutant")) || (grid[y-2][x].getContent().equals("rangedMutant"))
         || (grid[y][x+2].getContent().equals("rangedMutant")) || (grid[y][x-2].getContent().equals("rangedMutant"))) {
            hp -= 2;
        }
        */

        // Update grid
        grid[getPlayerY()][getPlayerX()] = new FloorTile(); // Set player coordinates to empty tile
        place("entry", 1);  // Replace the entry tile in case the player tile overwrote it
        grid[y][x] = new PlayerTile();
    }

    public int getPlayerX() {
        Tile[][] grid = getGrid();
        int xCounter = 0;
        for (Tile[] row : grid) {
            xCounter = 0;
            for (Tile tile : row) {
                if (tile.getType().equals("player")) {
                    return xCounter;
                }
                xCounter++;
            }
        }
        return -1;
    }

    public int getPlayerY() {
        Tile[][] grid = getGrid();
        int yCounter = 0;
        for (Tile[] row : grid) {
            for (Tile tile : row) {
                if (tile.getType().equals("player")) {
                    return yCounter;
                }
            }
            yCounter++;
        }
        return -1;
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

    public String getGameState() {
        return gameState;
    }

    public int getScore() {
        return score;
    }

    public int getHP() {
        return hp;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    /**
     * Plays a text-based game (obsolete, run TextUI.java)
     */
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        System.out.printf("The size of map is %d * %d\n", engine.getSize(), engine.getSize());
    }
}
