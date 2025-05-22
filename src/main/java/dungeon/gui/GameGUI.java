package dungeon.gui;

import dungeon.engine.GameEngine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * GUI for the Maze Runner Game.
 *
 * NOTE: Do NOT run this class directly in IntelliJ - run 'RunGame' instead.
 */
public class GameGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load fxml
        BorderPane root = FXMLLoader.load(getClass().getResource("game_gui.fxml"));

        // Display scene
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.setTitle("MiniDungeon Game");
        primaryStage.show();
    }

    /** In IntelliJ, do NOT run this method.  Run 'RunGame.main()' instead. */
    public static void main(String[] args) {
        launch(args);
    }
}
