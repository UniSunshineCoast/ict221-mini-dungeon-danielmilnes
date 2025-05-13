package dungeon.textui;

import dungeon.engine.Cell;
import dungeon.engine.GameEngine;

public class TextUI {

    GameEngine engine = new GameEngine(10);

    public void drawUI() {
        Cell[][] map = engine.getMap();
        for (Cell[] row : map) {
            for (Cell cell : row) {
                System.out.print(cell.getContent() + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        TextUI ui = new TextUI();
        ui.drawUI();
    }
}
