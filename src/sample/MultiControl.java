package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.Vector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;

import static java.lang.Thread.sleep;

public class MultiControl implements Initializable {
    @FXML
    private Rectangle Do,Re,Mi,Fa,So,La,Ti,HiDo;	//피아노 건반 도,레,미,파,솔,라,시,도
    @FXML
    private Shape RecButton, StopButton, PlayButton;	//녹음버튼, 정지버튼, 재생버튼
    @FXML
    private Rectangle Do1,Re1,Mi1,Fa1,So1,La1,Ti1,HiDo1;	//피아노 건반 도,레,미,파,솔,라,시,도
    @FXML
    private Rectangle btnA, btnS, btnD, btnF, btnJ, btnK, btnL, btnSemiCol;	//키보드 버튼 모양
    @FXML
    private Label textA, textS, textD, textF, textJ, textK, textL, textSemiCol;	//키보드 버튼 글씨
    @FXML
    private Slider Volumn;	//볼륨조절용 슬라이더
    @FXML
    private ProgressBar proBar;	//볼륨조절용 프로그레스바
    @FXML
    private ImageView MusicSheetButton,Metronom,MuteButton,homeButton;	//악보보기버튼 메트로놈버튼, 음소거버튼
    @FXML
    private Rectangle SchoolBell,AirPlane,Baduki;	//음악자동재생 버튼(학교종, 비행기, 바둑이

    Vector<Integer> rec;	//연주를 저장하기위한 백터
    boolean recordable=false;	//연주를 저장중인지 여부를 판단하기 위한 boolean변수
    PianoPlay pp;	//피아노 소리를 재생하기 위한 객체
    Thread th;	//스레드
    MultiClient client;

    public MultiControl() {
        pp=new PianoPlay();
        rec=new Vector();

    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        client=new MultiClient(Do1, Re1,Mi1,Fa1,So1,La1,Ti1,HiDo1);
        client.startClient("35.200.64.88",4000);
        //볼륨조절용
        Volumn.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // TODO Auto-generated method stub
                proBar.setProgress(newValue.doubleValue() / 100.0);	//슬라이더와 프로그래스바를 바인딩
                pp.controlvolume(newValue.doubleValue());	//소리크기를 조절
            }
        });
    }

    public void BackToTheHome(MouseEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
        Scene scene=new Scene(root);
        Main.getInstance().getStage().setScene(scene);
    }
    public void BackButtonEntered(MouseEvent e) throws FileNotFoundException {
        homeButton.setImage(new Image(new FileInputStream("src\\sample\\backbutton_push.png")));
    }
    public void BackButtonExited(MouseEvent e) throws FileNotFoundException {
        homeButton.setImage(new Image(new FileInputStream("src\\sample\\backbutton.png")));
    }

//--------------------------------------------------------------------------------------------------------------------

    private BpmWithSound bws;	//메트로놈 Bpm을 조절하는 객체
    private Timer jobScheduler;	//잡스케줄러
    private boolean bpmSW=false;	//메트로놈이 실행중인지 여부를 판단하는 boolean 변수 true=on false=off
    //메트로놈을 재생하는 메소드
    public void PlayMetronom(MouseEvent e) throws FileNotFoundException {
        if(!bpmSW){
            Metronom.setImage(new Image(new FileInputStream("src\\sample\\left_blue_button_push.png")));		//버튼이 눌린 것처럼 보이기위해 눌린 버튼모양 이미지로 변경
            TextInputDialog dialog = new TextInputDialog("60");	//bpm을 입력받기위한 대화상자
            int bpm;	//입력받은 bpm을 저장할 변수
            dialog.setTitle("메트로놈 BPM");
            dialog.setHeaderText("BPM설정값을 입력해주세요");
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                bpm=Integer.parseInt(result.get());	//입력받은 숫자를 변수에다가 저장
                bws=new BpmWithSound();	//bpm을 계산하는 객체생성
                bws.setBpmTime(bpm);	//bpm을 설정
                jobScheduler= new Timer();	//타이머객체생성
                jobScheduler.scheduleAtFixedRate(bws, 0, bws.getBpmTime());	//타이머에 설정된 bpm을 세팅
                bpmSW=true;	//메트로놈 작동 스위치 on
            }
        }else {	//메트로놈이 작동중인경우
            jobScheduler.cancel();	//잡스케줄러 종료
            bpmSW=false;// 스위치 off
            Metronom.setImage(new Image(new FileInputStream("src\\sample\\left_blue_button.png")));	//버튼이 눌리기전의 상태로 버튼모양이미지 변경
        }
    }

//----------------------------------------------------------------------------------------------------

    //연주를 녹음하는 메소드
    public void RecPlay(MouseEvent e) {
        rec.clear();	//백터안의 내용을 초기화
        recordable=true;	//녹음여부를 확인하는 boolean변수의 값을  true로 변경
        new btnControl(RecButton).start();	//버튼이 눌렸다가 떼지는 이펙트를 위한 스레드
    }

    //정지버튼 녹음, 자동연주를 하는 스레드를 interrupt해서 연주를 정지
    public void StopRec(MouseEvent e) {
        new btnControl(StopButton).start();	//버튼이 눌렸다가 올라오는 이펙트를 위한 스레드
        recordable=false;	//녹음여부를 확인하는 boolean변수를 false로 변경
        if(th != null && th.isAlive()){	//스레드가 null이 아니고 살아있다면
            th.interrupt();	//스레드를 인터럽트
        }
    }

    //녹음된 연주를 재생
    public void PlayRec(MouseEvent e) {
        new btnControl(PlayButton).start();	//버튼이 눌렸다가 떼지는 이펙트를 위한 스레드
        th=new RecPlay(rec);	//자동연주를 위한 스레드를 생성하여 연주가 기록된 백터를 전달
        th.start();
    }

//----------------------------------------------------------------------------------------------------

    //키보드로 피아노를 연주하기 위한 이벤트
    public void InputKeyPressed(KeyEvent e) {
        KeyCode key=e.getCode();	//입력받은 키를 확인

        if(key==KeyCode.A) {	//도
            if(recordable)		//녹음기능이 작동중이라면
                rec.add(1);		//벡터에 1을 저장
            pp.playSound(1);	//소리를 재생
            client.send(MultiClient.myId +" 1 ");
            Thread th=new PianoColorThread(Do,btnA,textA);	//피아노 건반이 눌리고 키보드 버튼모양이 눌리는 이펙트를 위한 스레드
            th.start();
        }else if(key==KeyCode.S) {	//레
            if(recordable)
                rec.add(2);
            pp.playSound(2);
            client.send(MultiClient.myId +" 2 ");
            Thread th=new PianoColorThread(Re,btnS,textS);
            th.start();
        }else if(key==KeyCode.D) {	//미
            if(recordable)
                rec.add(3);
            pp.playSound(3);
            client.send(MultiClient.myId +" 3 ");
            Thread th=new PianoColorThread(Mi,btnD,textD);
            th.start();
        }else if(key==KeyCode.F) {	//파
            if(recordable)
                rec.add(4);
            pp.playSound(4);
            client.send(MultiClient.myId +" 4 ");
            Thread th=new PianoColorThread(Fa,btnF,textF);
            th.start();
        }else if(key==KeyCode.J) {	//솔
            if(recordable)
                rec.add(5);
            pp.playSound(5);
            client.send(MultiClient.myId +" 5 ");
            Thread th=new PianoColorThread(So,btnJ,textJ);
            th.start();
        }else if(key==KeyCode.K) {	//라
            if(recordable)
                rec.add(6);
            pp.playSound(6);
            client.send(MultiClient.myId +" 6 ");
            Thread th=new PianoColorThread(La,btnK,textK);
            th.start();
        }else if(key==KeyCode.L) {	//시
            if(recordable)
                rec.add(7);
            pp.playSound(7);
            client.send(MultiClient.myId +" 7 ");
            Thread th=new PianoColorThread(Ti,btnL,textL);
            th.start();
        }else if(key==KeyCode.SEMICOLON) {	//도
            if(recordable)
                rec.add(8);
            pp.playSound(8);
            client.send(MultiClient.myId +" 8 ");
            Thread th=new PianoColorThread(HiDo,btnSemiCol,textSemiCol);
            th.start();
        }

    }

//----------------------------------------------------------------------------------------------------

    //음악을 자동연주하기 위한 메소드
    public void PlaySchoolBell(MouseEvent event) {	//학교종
        new PlayBtnControl(SchoolBell).start();
        int[] sing= new int[] {5,5,6,6,5,5,3,0,5,5,3,3,2,0,5,5,6,6,5,5,3,0,5,3,2,3,1};	//연주순서가 담긴 배열 생성
        th=new AutoPlay(sing);	//연주 자동재생을 위한 스레드 생성
        th.start();
    }
    public void PlayBaduki(MouseEvent event) {	//바둑이
        new PlayBtnControl(Baduki).start();
        int[] sing= new int[] {1,3,3,3,3,3,0,2,4,4,4,4,4,0,3,5,5,5,3,0,2,4,3,2,1,0,8,8,5,5,6,8,7,6,5,4,3,2,0,8,8,5,5,6,8,7,6,5,0,1,3,3,3,3,3,0,2,4,4,4,4,4,0,3,5,5,5,3,0,2,4,3,2,1};
        th=new AutoPlay(sing);
        th.start();
    }
    public void PlayAirPlane(MouseEvent e) {	//비행기
        new PlayBtnControl(AirPlane).start();
        int[] sing= new int[] {3,2,1,2,3,3,3,0,2,2,2,3,5,5,0,3,2,1,2,3,3,3,0,2,2,3,2,1};
        th=new AutoPlay(sing);
        th.start();
    }

//-----------------------------------------------------------------------------------------------------

    //피아노 건반을 클릭하여 소리를 재생하기 위한 이벤트 메소드
    public void DoMouseClicked(MouseEvent e) {	//도
        pp.playSound(1);	//소리재생
        Thread th=new PianoColorThread(Do,btnA,textA);	//건반과 키보드 모양이 눌리는 것 처럼 보이기 위한 스레드
        th.start();
    }
    public void ReMouseClicked(MouseEvent e) {	//레
        pp.playSound(2);
        Thread th=new PianoColorThread(Re,btnS,textS);
        th.start();
    }
    public void MiMouseClicked(MouseEvent e) {	//미
        pp.playSound(3);
        Thread th=new PianoColorThread(Mi,btnD,textD);
        th.start();
    }
    public void FaMouseClicked(MouseEvent e) {	//파
        pp.playSound(4);
        Thread th=new PianoColorThread(Fa,btnF,textF);
        th.start();
    }
    public void SoMouseClicked(MouseEvent e) {	//솔
        pp.playSound(5);
        Thread th=new PianoColorThread(So,btnJ,textJ);
        th.start();
    }
    public void LaMouseClicked(MouseEvent e) {	//라
        pp.playSound(6);
        Thread th=new PianoColorThread(La,btnK,textK);
        th.start();
    }
    public void TiMouseClicked(MouseEvent e) {	//시
        pp.playSound(7);
        Thread th=new PianoColorThread(Ti,btnL,textL);
        th.start();
    }
    public void HiDoMouseClicked(MouseEvent e) {	//도
        pp.playSound(8);
        Thread th=new PianoColorThread(HiDo,btnSemiCol,textSemiCol);
        th.start();
    }
    //-----------------------------------------------------------------------------------------
//음소거 기능
    private boolean mute=false;	//음소거가 작동중인지 여부를 판단하는 boolean변수
    public void MuteSound(MouseEvent e) throws FileNotFoundException {
        if(!mute){
            MuteButton.setImage(new Image(new FileInputStream("src\\sample\\mutebtn_push.png")));	//음소거가 눌린모양의 버튼
            pp.muteVolume();	//볼륨을 0으로
            mute=true;
        }else {
            MuteButton.setImage(new Image(new FileInputStream("src\\sample\\mutebtn.png")));		//음소거가 해제된 모양의 버튼
            pp.controlvolume(Volumn.getValue());	//현재 볼륨 슬라이더의 값을 가져와서 볼륨을 설정
            mute=false;
        }
    }

//---------------------------------------------------------------------------------------------------

    //음악 자동 재생을 위한 스레드
    class AutoPlay extends Thread{
        int[] song;	//음악 재생 정보가 담긴 배열

        public AutoPlay(int[] sing) {
            song=sing;
        }

        public void run() {
            Thread th;
            for(int i=0;i<song.length;i++) {
                if (song[i] > 0 && song[i] < 9) {
                    pp.playSound(song[i]);	//배열에 저장된 숫자에 해당하는 소리를 재생 1-8 도-높은도
                    switch (song[i]){
                        case 1:	//도
                            th = new PianoColorThread(Do,btnA,textA);	//피아노 건반과 키보드가 눌려보이는 이펙트를 위한 스레드
                            th.start();
                            break;
                        case 2:	//레
                            th = new PianoColorThread(Re,btnS,textS);
                            th.start();
                            break;
                        case 3:	//미
                            th = new PianoColorThread(Mi,btnD,textD);
                            th.start();
                            break;
                        case 4:	//파
                            th = new PianoColorThread(Fa,btnF,textF);
                            th.start();
                            break;
                        case 5:	//솔
                            th = new PianoColorThread(So,btnJ,textJ);
                            th.start();
                            break;
                        case 6:	//라
                            th = new PianoColorThread(La,btnK,textK);
                            th.start();
                            break;
                        case 7:	//시
                            th = new PianoColorThread(Ti,btnL,textL);
                            th.start();
                            break;
                        case 8:	//높은도
                            th = new PianoColorThread(HiDo,btnSemiCol,textSemiCol);
                            th.start();
                            break;
                    }
                }
                try {
                    sleep(400);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    return;
                }
            }
        }
    }

//-------------------------------------------------------------------------------------------------------------------

    //기록된 연주를 재생하는 스레드
    class RecPlay extends Thread{
        Vector<Integer> song;	//연주가 저장되어있는 벡터
        public RecPlay(Vector<Integer> sing) {
            song=sing;
        }
        public void run() {
            Thread th;
            for(int i=0;i<song.size();i++) {
                if (song.get(i) > 0 && song.get(i) < 9) {	//백터 안의 값이 1-8일때
                    pp.playSound(song.get(i));	//백터 i번째 인덱스의 값에 있는 숫자에 맞는 소리를 재생 1-8 도-높은도
                    switch (song.get(i)){
                        case 1:	//도
                            th = new PianoColorThread(Do,btnA,textA);	//피아노건반과 키보드가 눌렸다 때지는 이펙트를 위한 스레드
                            th.start();
                            break;
                        case 2:	//레
                            th = new PianoColorThread(Re,btnS,textS);
                            th.start();
                            break;
                        case 3:	//미
                            th = new PianoColorThread(Mi,btnD,textD);
                            th.start();
                            break;
                        case 4:	//파
                            th = new PianoColorThread(Fa,btnF,textF);
                            th.start();
                            break;
                        case 5:	//솔
                            th = new PianoColorThread(So,btnJ,textJ);
                            th.start();
                            break;
                        case 6:	//라
                            th = new PianoColorThread(La,btnK,textK);
                            th.start();
                            break;
                        case 7:	//시
                            th = new PianoColorThread(Ti,btnL,textL);
                            th.start();
                            break;
                        case 8:	//도
                            th = new PianoColorThread(HiDo,btnSemiCol,textSemiCol);
                            th.start();
                            break;
                    }
                }
                try {
                    sleep(400);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    return;
                }
            }
        }
    }


}
