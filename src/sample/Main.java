package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    Stage stage;
    private static Main m;

    public Main(){
        m=this;
    }

    public static Main getInstance(){
        return m;
    }

    public Stage getStage(){
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.stage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setFullScreen(false);
        primaryStage.setTitle("PianoPlayer");
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
