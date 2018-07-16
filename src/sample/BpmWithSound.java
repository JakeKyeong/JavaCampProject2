package sample;
import javafx.scene.media.AudioClip;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

//메트로놈을 재생하기위한 BPM을 설정
class BpmWithSound extends TimerTask
{
    final int BPM = 60000;// bpm의 기준
    private int inputBpm;
    private int bpmTime;
    final AudioClip Tick = new AudioClip(getClass().getResource("Tick.mp3").toString());

    public BpmWithSound()
    {
    }

    public void setBpmTime(int bpmTime) {
        this.inputBpm = bpmTime;
        this.bpmSpeed();
    }

    public int getBpmTime() {
        return bpmTime;
    }


    void bpmSpeed() // bpm 속도 조절(계산)
    {
        this.bpmTime = this.BPM / this.inputBpm;
    }


    @Override
    public void run() // 소리를 출력하는 스레드
    {
        Tick.play();
    }
}

