package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageControl implements Initializable{
    @FXML
    ImageView SinglePlayButton, MultiPlayButton;
    Main main;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main=Main.getInstance();
    }

    public void SinglePlayMouseEntered() throws FileNotFoundException {
        SinglePlayButton.setImage(new Image(new FileInputStream("src\\sample\\singlebtn_push.png")));
    }
    public void SinglePlayMouseExited() throws FileNotFoundException {
        SinglePlayButton.setImage(new Image(new FileInputStream("src\\sample\\singlebtn.png")));
    }
    public void MultiPlayMouseEntered() throws FileNotFoundException {
        MultiPlayButton.setImage(new Image(new FileInputStream("src\\sample\\multibtn_push.png")));
    }
    public void MultiPlayMouseExited() throws FileNotFoundException {
        MultiPlayButton.setImage(new Image(new FileInputStream("src\\sample\\multibtn.png")));
    }
    public void SinglePlayClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("rootPiano.fxml"));
        Scene scene=new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        main.getStage().setScene(scene);
    }
    public void MultiPlayClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("multiPiano.fxml"));
        Scene scene=new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        main.getStage().setScene(scene);
    }
}
