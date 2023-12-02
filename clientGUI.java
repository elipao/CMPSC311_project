import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class clientGUI extends JFrame {

    private JTextArea messageArea;
    private JTextField messageInput;
    private PrintWriter out;
    private BufferedReader in;

    public clientGUI() {
        initComponents();
        connectToServer();
    }

    // set up components
    private void initComponents() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        messageArea = new JTextArea();
        messageArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(messageArea);

        messageInput = new JTextField();
        // messageInput will call sendMessage upon user interaction (enter key pushed)
        messageInput.addActionListener(e -> sendMessage());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(messageInput, BorderLayout.SOUTH);
    }

    private void connectToServer() {
        try {
            // connects to server (./server 7777)
            Socket socket = new Socket("localhost", 7777);

            // write to server socket 
            out = new PrintWriter(socket.getOutputStream(), true);

            // read from server socket
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // now the connections are established, readMessage() will constantly 
            // read messages from the server socket 
            readMessage();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unable to connect to the server.");
            System.exit(1);
        }
    }

    private void sendMessage() {
        String message = messageInput.getText();
        // send user's message to server socket 
        out.println(message);
        messageInput.setText("");
    }

    private void readMessage() {
        // create new thread that checks for messages until the message == null (server cuts off)
        new Thread(() -> {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;  
                    }
                    SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new clientGUI().setVisible(true));
    }
}
