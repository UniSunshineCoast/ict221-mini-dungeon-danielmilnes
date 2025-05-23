package dungeon.engine;

import java.time.LocalDateTime;

public class HighScore implements Comparable<HighScore> {
    int score;
    LocalDateTime timestamp;

    public HighScore(int s, LocalDateTime t) {
        score = s;
        timestamp = t;
    }

    public int compareTo(HighScore other) {
        return this.score - other.score;
    }
}
