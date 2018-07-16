package sample;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

//녹음버튼, 정지버튼, 재생버튼이 눌렸을때 눌렸다가 떼지는 모습을 보여주기 위한 스레드
public class btnControl extends Thread  {
    Shape obj;  
    //Shape 객체로 업케스팅된 버튼을 저장(녹음버튼=Circle, 정지버튼 재생버튼=Rectangle Shape 객체는 모든 도형의 부모클래스)

    btnControl(Shape obj) {
        this.obj = obj;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        Platform.runLater(() -> {
            obj.setLayoutY(obj.getLayoutY() + 5);   //객체의 Y좌표를 +5하여 내려간것처럼 보이게 한다.
            if (obj instanceof Circle) {    //녹음버튼일경우 객체가 원일경우
                Color c = Color.web("#AF3333");
                obj.setFill(c);
            } else if (obj instanceof Rectangle) {  //객체가 네모일경우
                if (obj.getId().equals("StopButton")) { //객체 ID가 정지버튼이면
                    Color c = Color.web("#161616");
                    obj.setFill(c);
                } else {    //재생버튼
                    Color c = Color.web("#5a2b97");
                    obj.setFill(c);
                }
            } else {
                Color c = Color.web("#1d6f25");
                obj.setFill(c);
            }

        });
        try {
            Thread.sleep(100);

        } catch (Exception e) {
            return;
        }finally {
            Platform.runLater(() -> {
                obj.setLayoutY(obj.getLayoutY() - 5);   //버튼이 다시 올라온것처럼보이기 위해 Y좌표를 -5
                if (obj instanceof Circle) {
                    Color c = Color.web("#F34c4c");
                    obj.setFill(c);
                } else if (obj instanceof Rectangle) {
                    if (obj.getId().equals("StopButton")) {
                        Color c = Color.web("#333333");
                        obj.setFill(c);
                    } else {
                        Color c = Color.web("#9044f3");
                        obj.setFill(c);
                    }
                } else {
                    Color c = Color.web("#26b328");
                    obj.setFill(c);
                }
            });
        }
    }
}
