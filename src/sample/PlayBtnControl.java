package sample;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//음악 자동연주 버튼(보라색버튼) 클릭시 버튼이 눌리는 것처럼 보이기 위한 메소드
public class PlayBtnControl extends Thread  {
        Rectangle obj;

    public PlayBtnControl(Rectangle obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Platform.runLater(() -> {
                obj.setLayoutY(obj.getLayoutY() + 5);   //버튼의 위치를 아래로
                Color c = Color.web("#5e2d9e");
                obj.setFill(c); //어두운 보라색으로 변경
            });
            try {
                Thread.sleep(100);  //0.1초뒤에

            } catch (Exception e) {
                return;
            }finally {
                Platform.runLater(() -> {
                    obj.setLayoutY(obj.getLayoutY() - 5);   //버튼을 다시 위로
                        Color c = Color.web("#9747ff");
                        obj.setFill(c); //버튼색을 원래대로
                });
            }
        }}
