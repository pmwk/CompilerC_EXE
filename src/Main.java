package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Configuration.init();

        Pane mainView = FXMLLoader.load(getClass().getResource("/src/MainView.fxml"));
        Scene scene = new Scene(mainView, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
