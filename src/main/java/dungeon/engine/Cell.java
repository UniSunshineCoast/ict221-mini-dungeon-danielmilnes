package dungeon.engine;

import javafx.scene.layout.StackPane;

public class Cell extends StackPane {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String input) {
        content = input;
    }
}
