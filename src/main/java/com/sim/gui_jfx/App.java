package com.sim.gui_jfx;

import com.sim.config.WorldConfig;
import com.sim.controller.WorldController;
import com.sim.gui_adapters.ThreadScheduler;
import com.sim.world.World;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    static int CELL_SIZE = 4;

    @Override
    public void start(Stage stage) {

        stage.setTitle("Labworld");

        //World world = new World(200, 200);
        JFXWorldView worldView = new JFXWorldView();
        ThreadScheduler applicationThread = new JFXAppThread();
        WorldConfig worldConfig = new WorldConfig();
        World world = new World(worldConfig);
        WorldController controller = new WorldController(world, worldView, applicationThread);

        BorderPane root = new BorderPane();

        root.setRight(createSettingsPanel());
        root.setCenter(createWorldContainer(worldView));

        Scene scene = new Scene(root, 1000, 720);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();

        controller.start();
    }

    private Parent createWorldContainer(JFXWorldView view) {

        StackPane container = new StackPane();
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #202020;");

        container.getChildren().add(view.root());

        return container;
    }

    private Parent createSettingsPanel() {

        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setPrefWidth(280);
        panel.setStyle("-fx-background-color: #2b2b2b;");

        panel.getChildren().addAll(
                new Label("Simulation Settings"),
                new Slider(0, 1, 0.5),
                new Button("Reset"),
                new CheckBox("Show Temperature")
        );

        return panel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
