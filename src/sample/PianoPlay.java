package sample;

import javafx.scene.media.AudioClip;

//피아노 소리를 재생하기 위한 객체
public class PianoPlay {
	final AudioClip note1 = new AudioClip(getClass().getResource("sound1.mp3").toString());	//도
	final AudioClip note2 = new AudioClip(getClass().getResource("sound2.mp3").toString());	//레
	final AudioClip note3 = new AudioClip(getClass().getResource("sound3.mp3").toString());	//미
	final AudioClip note4 = new AudioClip(getClass().getResource("sound4.mp3").toString());	//파
	final AudioClip note5 = new AudioClip(getClass().getResource("sound5.mp3").toString());	//솔
	final AudioClip note6 = new AudioClip(getClass().getResource("sound6.mp3").toString());	//라
	final AudioClip note7 = new AudioClip(getClass().getResource("sound7.mp3").toString());	//시
	final AudioClip note8 = new AudioClip(getClass().getResource("sound8.mp3").toString());	//도
	
//소리를 재생 1부터 8까지 숫자를 입력받아서 1부터 도,레,미,파,솔,라,시,도
	public void playSound(int number) {
		switch(number) {
		case 1:	//도
			note1.play();
			break;
		case 2:	//레
			note2.play();
			break;
		case 3:	//미
			note3.play();
			break;
		case 4:	//파
			note4.play();
			break;
		case 5:	//솔
			note5.play();
			break;
		case 6:	//라
			note6.play();
			break;
		case 7:	//시
			note7.play();
			break;
		case 8:	//도
			note8.play();
			break;
		}
	}

	//볼륨 소리를 조절하는 메소드
	public void controlvolume(Double number) { // 피아노 볼륨 조절
		note1.setVolume(number);
		note2.setVolume(number);
		note3.setVolume(number);
		note4.setVolume(number);
		note5.setVolume(number);
		note6.setVolume(number);
		note7.setVolume(number);
		note8.setVolume(number);
	}
	//음소거 처리를 위한 메소드
	public void muteVolume() {
		note1.setVolume(0);
		note2.setVolume(0);
		note3.setVolume(0);
		note4.setVolume(0);
		note5.setVolume(0);
		note6.setVolume(0);
		note7.setVolume(0);
		note8.setVolume(0);
	}
}

