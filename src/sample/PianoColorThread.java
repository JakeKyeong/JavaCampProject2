package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//피아노 건반과 키보드 모양 버튼이 눌렸다가 떼지는 이펙트를 위한 스레드
class PianoColorThread extends Thread {
	Rectangle gunban, keyboard;	//건반과 키보드모양
	Label keyString;	//키보드 위의 글씨

	public PianoColorThread(Rectangle gunban, Rectangle keyboard, Label keyString) {
		this.gunban = gunban;
		this.keyboard = keyboard;
		this.keyString = keyString;
	}

	@Override
	public void run() {

		Platform.runLater(() -> {
			Color c = Color.web("#d0d0d0");
			gunban.setFill(c);	//건반색변경
			keyboard.setFill(c);	//키보드 색변경
			keyboard.setLayoutY(keyboard.getLayoutY() + 6);	//키보드 모양을 아래로 내림
			keyString.setLayoutY(keyString.getLayoutY() + 9);	//키보드 위의 글씨도 같이 아래로 내림
		});

		try {
			sleep(100);
		} catch (Exception e) {
			return;
		}finally {
			Platform.runLater(() -> {
				Color c = Color.web("#fff");
				gunban.setFill(c);	//건반색을 원래대로
				keyboard.setFill(c);	//키보드 색을 원래대로
				keyboard.setLayoutY(keyboard.getLayoutY() - 6);	//키보드위치를 원래대로
				keyString.setLayoutY(keyString.getLayoutY() - 9);

			});
		}
	}
}
