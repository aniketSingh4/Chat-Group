import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class MyServer {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Creating GUI for Server
    private JLabel heading = new JLabel("Server End.");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    
    public MyServer(){
        try {
            server = new ServerSocket(7777);
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            startReading();
            //startWriting();
            createGUI();
            handleEvents();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){
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

// create a gui 
private   void createGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Client Messenger End");
        frame.setSize(600,700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        heading.setFont(font);
        messageInput.setFont(font);
        messageArea.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

//        ImageIcon image = new ImageIcon("C:\\Users\\Aniket Singh\\Downloads\\Data Analysis\\ChatGroup\\chat.png");
//        heading.setIcon(image);

        frame.setLayout(new BorderLayout());
        frame.add(heading,BorderLayout.NORTH);
        //JScrollPane js = new JScrollPane(messageArea);
       // String JScrollPane;
        frame.add(messageArea,BorderLayout.CENTER);
        frame.add(messageInput,BorderLayout.SOUTH);
    }
    public void startReading(){

        Runnable r1 = ()->{
            System.out.println("Start Reading...");

            while (true) {
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client Terminated the chat.");
                        socket.close();
                        break;
                    }
                    System.out.println("Client : " + msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r1).start();
    }
    //Start Writing
    public void startWriting(){
        System.out.println("Start Writing...");
        Runnable r2 = ()->{
            while (true){
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();//forcefully write
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("Server End Start ");
        //System.out.println("Waiting for Client End for connection!");
        new MyClient();
    }
}