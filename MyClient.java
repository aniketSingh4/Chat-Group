import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading = new JLabel("Client End.");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    public MyClient(){
        try {
             socket = new Socket("localhost",7777);
            System.out.println("Connection Done.");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            startReading();
//            startWriting();
            createGUI();
            handleEvents();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                System.out.println("Key Released" + keyEvent.getKeyCode());
                if(keyEvent.getKeyCode() == 10){
                    //System.out.println("You Pressed Enter.");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }
        });
    }

    private   void createGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Client Messenger End");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        heading.setFont(font);
        messageInput.setFont(font);
        messageArea.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

//        ImageIcon image = new ImageIcon("C:\\Users\\Aniket Singh\\Downloads\\Data Analysis\\ChatGroup\\chat.png");
//        heading.setIcon(image);

        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        //JScrollPane js = new JScrollPane(messageArea);
       // String JScrollPane;
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
    }
    public void startReading(){

        Runnable r1 = ()->{
            System.out.println("Start Reading...");

            while (true) {
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server Terminated the chat.");
                        JOptionPane .showMessageDialog(this,"Server terminated");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                   // System.out.println("Server : " + msg);
                    messageArea.append("Server : " + msg + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        System.out.println("Start Writing...");
        Runnable r2 = ()->{
            while (true){
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    if(content.equals("Exit")){
                        break;
                    }
                    out.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();
    }
    // initialize socket and input output streams
    public static void main(String[] args) {
        System.out.println("Trying to connection with Server.");
        new MyServer();
        //new MyFrame();
    }
}
