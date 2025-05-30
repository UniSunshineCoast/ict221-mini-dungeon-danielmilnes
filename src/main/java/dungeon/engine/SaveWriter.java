package dungeon.engine;

import java.io.PrintWriter;
import java.io.File;

/**
 * Constructing a SaveWriter object will take the current state of a GameEngine and
 * write it to a text file called "save.txt".
 */
public class SaveWriter {

    public SaveWriter(GameEngine engine) {
        // Assign variables that will need to be stored
        String gameState = engine.getGameState();
        Tile[][] grid = engine.getGrid();
        int level = engine.getLevel();
        int ladderX = engine.getLadderX();
        int ladderY = engine.getLadderY();
        int score = engine.getScore();
        int hp = engine.getHP();
        int movesLeft = engine.getMovesLeft();
        int difficulty = engine.getDifficulty();

        try {
            // Create save file
            File saveFile = new File("save.txt");
            if (saveFile.createNewFile()) {
                System.out.println("Save file created.");
            }
            // Write to save file
            PrintWriter output = new PrintWriter(saveFile);
            // Write variables
            output.println(gameState + " " + level + " " + ladderX + " " + ladderY + " " +
                    score + " " + hp + " " + movesLeft + " " + difficulty);
            // Write grid
            for (Tile[] row : grid) {
                for (Tile tile : row) {
                    output.print(tile.getContent() + " ");
                }
                output.println();
            }
            System.out.println("Game saved.");
            output.close();
        }
        catch (Exception e) {System.out.println("Error writing save file.");}
    }
}
