package dungeon.engine;

import java.time.LocalDateTime;

/**
 * HighScore is an information container for a high score value.
 * It stores both the score, and the time that score was achieved.
 */
public class HighScore implements Comparable<HighScore> {
    int score;
    LocalDateTime timestamp;

    public HighScore(int s, LocalDateTime t) {
        score = s;
        timestamp = t;
    }

    public int compareTo(HighScore other) {
        // Sort timestamps oldest to newest if scores equals
        if (this.score == other.score) {
            if (this.timestamp.isBefore(other.timestamp)) {return -1;}
            if (this.timestamp.isEqual(other.timestamp)) {return 0;}
            if (this.timestamp.isAfter(other.timestamp)) {return 1;}
        }
        // Sort scores highest to lowest
        return other.score - this.score;
    }
}
