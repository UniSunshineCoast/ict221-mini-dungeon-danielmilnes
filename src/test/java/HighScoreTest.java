import dungeon.engine.GameEngine;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

// The logic for high scores is in the GameEngine class - the HighScore class is just an information container.
// But to simplify things I'm putting the test for the HighScore class here.

public class HighScoreTest {
    @Test
    void testHighScores() {
        GameEngine engine = new GameEngine(10);
        engine.addHighScore(10, LocalDateTime.now());
        engine.addHighScore(20, LocalDateTime.now());
        engine.logHighScores();

        ArrayList<String> messageLog = engine.getMessageLog();
        assertEquals("HIGH SCORES", messageLog.getFirst());

        String highScore1 = messageLog.get(1).substring(0,5);
        assertEquals("1.\t20", highScore1);
        String highScore2 = messageLog.get(2).substring(0, 5);
        assertEquals("2.\t10", highScore2);
    }
}