package dungeon.engine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameEngine {

    private Tile[][] grid;
    private int level = 1;
    private int ladderX;
    private int ladderY;
    private String gameState;
    private int score = 0;
    private int hp = 10;
    private int movesLeft = 100;
    Random r = new Random();
    private ArrayList<String> messageLog = new ArrayList<>();
    private ArrayList<HighScore> highScores = new ArrayList<>();

    /**
     * Initialises the game.
     * @param size the width and height.
     */
    public GameEngine(int size) {
        gameState = "starting";

        // The grid is the logical view of the game.
        // It is a 2D array of Tile objects which each have a "type" and "content".
        // "Type" is player, floor, wall, monster, etc. "Content" is what to display for that tile (@, ., #, M).
        // They also have an fxStyle which I'll get to later TODO

        // Create grid
        grid = new Tile[size][size];
        // Fill with floor tiles
        clearGrid();
        // Build level 1
        buildLevel();

        gameState = "running";
    }


    /**
     * Places a tile of type (tile), (count) number of times
     *
     * @param tile  The tile to be placed
     * @param count The number of this tile to be placed
     */
    public void place(String tile, int count) {
        for (int counter = 0; counter < count; counter++) {
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
            // Wall tile: idk what to do with these yet, place them randomly
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
     * Receives and processes input from the TextUI/GUI controller.
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
        switch (input) {
            // For player movement: return (ignore command) if out of bounds, otherwise determine destination
            // For save, load, reset: just execute it
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
            case "reset": // Reset the game
                gameState = "starting";
                hp = 10;
                score = 0;
                movesLeft = 100;
                level = 1;
                clearGrid();
                buildLevel();
                messageLog.clear();
                gameState = "running";
                return;
            default:
                System.out.println("Invalid input.");
                return;
        }
        // Don't allow player to move into walls
        if (grid[destinationY][destinationX].getType().equals("wall")) {return;}

        // Move player
        moveTo(destinationX, destinationY);

        // Check if player lost game
        if (movesLeft == 0 || hp <= 0) {
            gameState = "lost";
            addToMessageLog("You lose!");
        }
    }

    /**
     * Moves the player tile from its current position to the destination, and handles logic for landing on a tile.
     * Assumes that the move has already been validated.
     * @param x Destination x (row)
     * @param y Destination y (column)
     */
    public void moveTo(int x, int y) {
        movesLeft -= 1;
        switch (grid[y][x].getType()) {
            case "gold":
                score += 2;
                addToMessageLog("You pick up a gold piece.");
                break;
            case "healthPotion":
                hp += 4;
                if (hp > 10) {hp = 10;}
                addToMessageLog("You drink a health potion.");
                break;
            case "trap":
                hp -= 2;
                addToMessageLog("You trigger a trap!");
                break;
            case "meleeMutant":
                hp -= 2;
                score += 2;
                addToMessageLog("You defeat a melee monster!");
                break;
            case "rangedMutant":
                score += 2;
                addToMessageLog("You defeat a ranged monster!");
                break;
            case "ladder": // Advance level or end game
                if (level == 1) {
                    level = 2;
                    clearGrid();
                    buildLevel();
                    addToMessageLog("You advance to the second level!");
                }
                else {
                    addToMessageLog("You escape the dungeon!");
                    gameState = "won";
                    addHighScore(score, LocalDateTime.now());
                    logHighScores();
                }
        }
        // check for ranged enemies in attack range, and roll 50% chance to hit
        // 3 if statements - will not cause index out of bounds exception, message for hit or miss
        if (x >= 2) {if (grid[y][x-2].getType().equals("rangedMutant")) {
                if (r.nextBoolean()) {hp -= 2; addToMessageLog("A ranged monster shoots you!");}
                else {addToMessageLog("A ranged monster shoots at you and misses!");}}}
        if (x <= 7) {if (grid[y][x+2].getType().equals("rangedMutant")) {
            if (r.nextBoolean()) {hp -= 2; addToMessageLog("A ranged monster shoots you!");}
            else {addToMessageLog("A ranged monster shoots at you and misses!");}}}
        if (y >= 2) {if (grid[y-2][x].getType().equals("rangedMutant")) {
            if (r.nextBoolean()) {hp -= 2; addToMessageLog("A ranged monster shoots you!");}
            else {addToMessageLog("A ranged monster shoots at you and misses!");}}}
        if (y <= 7) {if (grid[y+2][x].getType().equals("rangedMutant")) {
            if (r.nextBoolean()) {hp -= 2; addToMessageLog("A ranged monster shoots you!");}
            else {addToMessageLog("A ranged monster shoots at you and misses!");}}}

        // Update grid
        grid[getPlayerY()][getPlayerX()] = new FloorTile(); // Set player coordinates to empty tile
        place("entry", 1);  // Replace the entry tile in case the player tile overwrote it
        grid[y][x] = new PlayerTile();
    }

    /**
     * Fills the grid with floor tiles.
     */
    public void clearGrid() {
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                grid[x][y] = new FloorTile();
            }
        }
    }

    /**
     * Places all the objects in the level.
     */
    public void buildLevel() {
        place("entry", 1);
        place("wall", 20);
        place("player", 1);
        place("ladder", 1);
        place("trap", 5);
        place("gold", 5);
        place("meleeMutant", 3);
        place("rangedMutant", 1);
        place("healthPotion", 2);
    }

    /**
     * Add a string to the message log (ArrayList variable messageLog).
     * Max length 10, removes oldest message to add new one.
     * @param s The string to add
     */
    public void addToMessageLog(String s) {
        // Add message to end of log if it is less than 10 messages long
        if (messageLog.size() < 10) {messageLog.add(s);}
        // Otherwise remove the first message and add the new one to the end
        else {
            messageLog.removeFirst();
            messageLog.add(s);
        }
    }

    /**
     * Checks if a score should be added to the high score list, and adds it if so.
     * HighScore is a class to store both a score and timestamp.
     * @param score The score to be added
     * @param timestamp The timestamp of the score.
     */
    public void addHighScore(int score, LocalDateTime timestamp) {
        HighScore newScore = new HighScore(score, timestamp);
        // If list less than 5 long, add score to it
        if (highScores.size() < 5) {highScores.add(newScore);}
        // Otherwise, if it is greater than 5th place, add remove 5th place and add the new score
        else {
            if (newScore.score > highScores.getLast().score) {
                highScores.removeLast();
                highScores.add(newScore);
            }
        }
        // Finally, sort the list
        Collections.sort(highScores);
    }

    private void logHighScores() {
        addToMessageLog("HIGH SCORES");
        for (int i = 0; i < highScores.size(); i++) {
            HighScore hs = highScores.get(i);
            String year = String.valueOf(hs.timestamp.getYear());
            String month = String.valueOf(hs.timestamp.getMonthValue());
            if (month.length() == 1) {month = "0" + month;}
            String day = String.valueOf(hs.timestamp.getDayOfMonth());
            if (day.length() == 1) {day = "0" + day;}
            String hour = String.valueOf(hs.timestamp.getHour());
            if (hour.length() == 1) {hour = "0" + hour;}
            String minute = String.valueOf(hs.timestamp.getMinute());
            if (minute.length() == 1) {minute = "0" + minute;}
            String second = String.valueOf(hs.timestamp.getSecond());
            if (second.length() == 1) {second = "0" + second;}
            String message = (i+1) + ".\t" + hs.score + "\t" + year + "-" + month + "-" + day + " " +
                    hour + ":" + minute + ":" + second;
            addToMessageLog(message);
        }
    }

    /**
     * @return Player's x position (0-9).
     */
    public int getPlayerX() {
        Tile[][] grid = getGrid();
        int xCounter;
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

    /**
     * @return Player's y position (0-9).
     */
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
     * @return The game state ("starting", "running", "won", or "lost").
     */
    public String getGameState() {
        return gameState;
    }

    /**
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * @return The player's HP.
     */
    public int getHP() {
        return hp;
    }

    /**
     * @return The player's moves left.
     */
    public int getMovesLeft() {
        return movesLeft;
    }

    /**
     * @return The message log.
     */
    public ArrayList<String> getMessageLog() {
        return messageLog;
    }

    /**
     * Plays a text-based game (obsolete, run TextUI.java)
     */
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        System.out.printf("The size of map is %d * %d\n", engine.getSize(), engine.getSize());
    }
}
