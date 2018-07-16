package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class MultiPianoColor extends Thread {
    Rectangle gunban;

    public MultiPianoColor(Rectangle gunban) {
        this.gunban = gunban;
    }

    @Override
    public void run() {

        Platform.runLater(() -> {
            Color c = Color.web("#d0d0d0");
            gunban.setFill(c);	//건반색변경
        });

        try {
            sleep(100);
        } catch (Exception e) {
            return;
        }finally {
            Platform.runLater(() -> {
                Color c = Color.web("#fff");
                gunban.setFill(c);	//건반색을 원래대로
            });
        }
    }
}
