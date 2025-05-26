import dungeon.engine.GameEngine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameEngineTest {
    @Test
    void testGetSize() {
        GameEngine engine = new GameEngine(10);
        assertEquals(10, engine.getSize());
    }

    @Test
    void testPlayerPlacement() {
        GameEngine engine = new GameEngine(10);
        assertEquals(1, engine.getPlayerX());
        assertEquals(9, engine.getPlayerY());
    }

    @Test
    void testLoseGame() {
        // Create engine, set seed, reset game to have set layout
        GameEngine engine = new GameEngine(10);
        engine.setSeed(12345);
        engine.playerInput("reset");
        while (engine.getGameState().equals("running")) {
            // Move around until game is lost
            engine.playerInput("up");
            engine.playerInput("left");
            engine.playerInput("right");
            engine.playerInput("down");
        }
        assertEquals("lost", engine.getGameState());
        assertEquals(-1, engine.getScore());
    }

    @Test
    void testWinGame() {
        /*
        This effectively tests all the methods involved in running the game.
        If place(), moveTo(), etc. do not work properly then the end state of the game
        will not match the expected values here.
         */

        // Create engine, set seed, reset game to have set layout
        GameEngine engine = new GameEngine(10);
        engine.setSeed(12345);
        engine.playerInput("reset");
        // Make the following inputs:
        multipleInputs(engine, "right", 5);
        multipleInputs(engine, "up", 1);
        multipleInputs(engine, "right", 3);
        multipleInputs(engine, "up", 2);
        multipleInputs(engine, "left", 3);
        multipleInputs(engine, "up", 2);
        multipleInputs(engine, "left", 2);
        multipleInputs(engine, "up", 3);
        multipleInputs(engine, "right", 5);
        multipleInputs(engine, "down", 4);

        // Check all values
        assertEquals(2, engine.getHP());
        assertEquals(14, engine.getScore());
        assertEquals(70, engine.getMovesLeft());
        assertEquals("won", engine.getGameState());
    }

    /**
     * Multiple inputs - for testing a complete playthrough of the game
     */
    void multipleInputs(GameEngine engine, String input, int count) {
        for (int i = 0; i < count; i++) {
            engine.playerInput(input);
        }
    }
}