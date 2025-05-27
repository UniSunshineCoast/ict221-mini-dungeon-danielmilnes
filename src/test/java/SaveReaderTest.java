import dungeon.engine.GameEngine;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.PrintWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaveReaderTest {
    @Test
    void testReadSave() {
        GameEngine engine = new GameEngine(10);

        try {
            // Create save file to read
            File saveFileTest = new File("saveFileTest.txt");
            saveFileTest.createNewFile();

            // Write the test game state to it
            PrintWriter output = new PrintWriter(saveFileTest);
            output.print("""
                    running 1 4 4 0 10 100 3
                    . . . G R . # . # T\s
                    . # . . . # . . # #\s
                    . # . . # # # . . .\s
                    M . . . . # . R T G\s
                    . . . . > . . T T .\s
                    . H # # # . R . . .\s
                    . G # . . . . . . H\s
                    . . # # . . . . . M\s
                    . # . . . T G . . .\s
                    < @ G . . M . # # .\s
                    """);
            output.close();

            // Load the test save file
            engine.loadSave("saveFileTest.txt");
            // Move a few tiles over so we're sure the game state has loaded instead of it being a random one
            engine.playerInput("right");
            engine.playerInput("right");
            engine.playerInput("right");
            engine.playerInput("right");
            // Run tests
            assertEquals("running", engine.getGameState());
            assertEquals(4, engine.getScore());
            assertEquals(8, engine.getHP());
            assertEquals(96, engine.getMovesLeft());
            assertEquals(5, engine.getPlayerX());
            assertEquals(9, engine.getPlayerY());
        }
        catch (Exception e) {throw new RuntimeException(e);}
    }
}