import dungeon.engine.GameEngine;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.util.Scanner;

public class SaveWriterTest {
    @Test
    void testWriteSave() {
        // Create engine, set seed, reset game to have set layout, then create save file
        GameEngine engine = new GameEngine(10);
        engine.setSeed(12345);
        engine.playerInput("reset");
        engine.saveGame();

        // Check if save matches the game state
        File saveFile = new File("save.txt");
        try (Scanner input = new Scanner(saveFile)) {
            String text = "";
            while (input.hasNext()) {
                text = text.concat(input.nextLine());
            }
            assertEquals(
                    "running 1 4 4 0 10 100 3" +
                    ". . . G R . # . # T " +
                    ". # . . . # . . # # " +
                    ". # . . # # # . . . " +
                    "M . . . . # . R T G " +
                    ". . . . > . . T T . " +
                    ". H # # # . R . . . " +
                    ". G # . . . . . . H " +
                    ". . # # . . . . . M " +
                    ". # . . . T G . . . " +
                    "< @ G . . M . # # . ",
                    text);
        }
        catch (Exception e) {throw new RuntimeException(e);}
    }
}
