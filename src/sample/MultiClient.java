package sample;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Rectangle;


public class MultiClient  {
    Socket socket;
    TextArea txtDisplay;
    Rectangle Do,Re,Mi,Fa,So,La,Ti,HiDo;
    public static String myId=Integer.toString(new Random().nextInt());

    public MultiClient(Rectangle Do, Rectangle Re, Rectangle Mi, Rectangle Fa, Rectangle So, Rectangle La, Rectangle Ti, Rectangle HiDo){
        this.Do=Do; this.Re=Re; this.Mi=Mi; this.Fa=Fa; this.So=So; this.La=La; this.Ti=Ti; this.HiDo=HiDo;
    }

    public void startClient(String ipAddress, int port) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    socket = new Socket(ipAddress, port);
                    // socket.connect(new InetSocketAddress(ipAddress, port));
                    receive();
                } catch (Exception e) {
                    // TODO: handle exception
                    if(!socket.isClosed()) stopClient();
                    System.out.println("[Server connection failure!!]");
                    Platform.exit();
                }
            }
        };
        thread.start();
    }

    public void stopClient() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void receive() {
        PianoPlay pp = new PianoPlay();
        while(true) {
            try {
                InputStream in = socket.getInputStream();
                byte[] buf = new byte[100];
                int len = in.read(buf);
                if(len == -1) throw new IOException();

                String msg = new String(buf, 0, len, "UTF-8");
                String[] split=msg.split(" ");
                if(!myId.equals(split[0])) {
                    int num = Integer.parseInt(split[1]);
                    switch (num) {
                        case 1:
                            new MultiPianoColor(Do).start();
                            pp.playSound(1);
                            break;
                        case 2:
                            new MultiPianoColor(Re).start();
                            pp.playSound(2);
                            break;
                        case 3:
                            new MultiPianoColor(Mi).start();
                            pp.playSound(3);
                            break;
                        case 4:
                            new MultiPianoColor(Fa).start();
                            pp.playSound(4);
                            break;
                        case 5:
                            new MultiPianoColor(So).start();
                            pp.playSound(5);
                            break;
                        case 6:
                            new MultiPianoColor(La).start();
                            pp.playSound(6);
                            break;
                        case 7:
                            new MultiPianoColor(Ti).start();
                            pp.playSound(7);
                            break;
                        case 8:
                            new MultiPianoColor(HiDo).start();
                            pp.playSound(8);
                            break;
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                stopClient();
                break;
            }
        }
    }
    public void send(String msg) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    OutputStream out = socket.getOutputStream();
                    byte buf[] = msg.getBytes("UTF-8");
                    out.write(buf);
                    out.flush();
                } catch (Exception e) {
                    // TODO: handle exception
                    stopClient();
                }
            }
        };
        thread.start();
    }


}