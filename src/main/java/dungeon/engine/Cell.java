package dungeon.engine;

import javafx.scene.layout.StackPane;

/**
 * A Cell is a JavaFX Pane representing one grid tile. Its 'content' field contains the String that should be
 * displayed on it, although setting the Content does not
 */
public class Cell extends StackPane {
    private String content;
    private String fxStyle;

    public String getContent() {
        return content;
    }

    public void setContent(String input) {
        content = input;
    }

    public String getFXStyle() {
        return fxStyle;
    }
}
