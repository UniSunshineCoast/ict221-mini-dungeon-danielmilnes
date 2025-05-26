package dungeon.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SaveReader {

    // Create variables the GameEngine will read
    String gameState;
    int level;
    int ladderX;
    int ladderY;
    int score;
    int hp;
    int movesLeft;
    int difficulty;
    ArrayList<String> tiles = new ArrayList<>();

    public SaveReader(String fileName) {
        // Create file object
        File saveFile = new File(fileName);

        try (Scanner input = new Scanner(saveFile)) {
            // Read variables
            gameState = input.next();
            level = input.nextInt();
            ladderX = input.nextInt();
            ladderY = input.nextInt();
            score = input.nextInt();
            hp = input.nextInt();
            movesLeft = input.nextInt();
            difficulty = input.nextInt();


            // Read grid
            while (input.hasNext()) {
                String tile = input.next();
                tiles.add(tile);
            }
            System.out.println("Save file read.");
        }
        catch (FileNotFoundException e) {System.out.println("No save file to read.");}
    }
}
