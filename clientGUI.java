import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;

public class clientGUI extends JFrame {

    private JTextArea messageArea;
    private JTextField messageInput;
    private PrintWriter out;
    private BufferedReader in;
    private String userName; 
    private String themeChoice;
    private Color textColor = Color.black; 
    private Color backgroundColor = Color.white; 
    private Color midColor = new Color(173, 179, 192);
    private Color scrollColor = new Color(169, 189, 235);; 
    
    public clientGUI() {
        // start up instance of GetName to get name and get theme choice 
        GetName getName = new GetName(this);
        getName.setVisible(true);
        userName = getName.getUserName(); 
        themeChoice = getName.getSelectedTheme(); 

        // begin initiazation sequence of clientGUI 
        this.initComponents();
        this.connectToServer();
        this.readMessage();
        this.out.println(getTime() +"\t ~ "+ userName + " connected ~"); 
    }

    private void setTheme(){
        // initialize any custom colors for themed color palettes
        Color lightBlue = new Color(222, 225, 253); //custom rgb colors
        Color darkBlue = new Color(11, 25, 145); 
        Color lightGrey = new Color(232, 232, 232); 
        Color darkGrey = new Color(59, 70, 82); 
        Color babyBlue = new Color(171, 210, 255); 

        // switch colors based on chosen theme
        switch(themeChoice){
            case "Default":
                break; 
            case "NightMode": 
                textColor = lightGrey; 
                backgroundColor = Color.black; 
                midColor = darkGrey; 
                scrollColor = new Color(135, 149, 179); 
                break; 
            case "Blue":
                textColor = lightBlue; 
                backgroundColor = darkBlue; 
                midColor = babyBlue; 
                scrollColor = new Color(35, 175, 245); 
                break; 
        }
    }

    // formats the time from military to regular form
    private String getTime(){
        LocalTime currentTime = LocalTime.now();
        String midday = "AM"; 
        String minute = "" + currentTime.getMinute(); 
        int hour = currentTime.getHour()%12; 

        if(hour == 0){ // when military time is 0000 or 2400
            hour = 12; 
        }
        if(currentTime.getHour() > 11){
            midday = "PM"; 
        }
        if(currentTime.getMinute() < 10){ 
            minute = "0" + currentTime.getMinute(); 
        }
        return(hour + ":" + minute + " " + midday); 
    }

    // set up components + add cosmetics / make it pretty
    private void initComponents() {

        this.setTheme(); 

        Font sansSerif = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
        Font sansSerifI = new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 13); // italicized bold version

        setTitle("Chat Service");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // FONTS 
        JLabel userLabel = new JLabel(("Current User: " + userName));
        userLabel.setFont(sansSerifI); 
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(sansSerif);
        messageInput = new JTextField();
        messageInput.setFont(sansSerif); 
        // messageInput will call sendMessage upon user interaction (enter key pushed)
        messageInput.addActionListener(e -> sendMessage());

        // COLORS
        userLabel.setForeground(backgroundColor);
        userLabel.setBackground(midColor);
        userLabel.setOpaque(true);
        messageArea.setForeground(textColor); 
        messageArea.setBackground(backgroundColor);
        messageInput.setForeground(textColor); 
        messageInput.setBackground(backgroundColor);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override      // scrollbar cosmetics
            protected void configureScrollBarColors() {
                thumbColor = scrollColor; // scroll color 
                trackColor = midColor; //  color track
            }
        });
     
        // stack the legos
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(userLabel, BorderLayout.NORTH); 
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(messageInput, BorderLayout.SOUTH);
        
        this.setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void connectToServer() {
        try {
   
            Socket socket = new Socket("localhost", 18); 

            // write to server socket 
            this.out = new PrintWriter(socket.getOutputStream(), true);

            // read from server socket
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unable to connect to the server.");
            System.exit(1);
        }
    }

    private void sendMessage() {
        
        String message = messageInput.getText();
        // send user's message to server socket with timestamp
        this.out.println(getTime() + "\t" +userName + ": " + message);
        messageInput.setText("");
    }

    private void readMessage() {
        // create new thread that checks for messages until the message == null (null indicates the server closed)
        new Thread(() -> {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;  
                    }
                    // update messageArea with the message
                    SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public static void main(String[] args) {
        // runs on an EDT (event dispatch thread) to prevent collision issues / concurrency
        SwingUtilities.invokeLater(() -> new clientGUI().setVisible(true));
    }
     
}
