import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class clientGUI extends JFrame {

    private JTextArea messageArea;
    private JTextField messageInput;
    private PrintWriter out;
    private Scanner in;

    public clientGUI() {
        initComponents();
        connectToServer();
        receiveMessages();
    }

    private void initComponents() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        messageArea = new JTextArea();
        messageArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(messageArea);

        messageInput = new JTextField();
        messageInput.addActionListener(e -> sendMessage());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(messageInput, BorderLayout.SOUTH);
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 7777);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unable to connect to the server.");
            System.exit(1);
        }
    }

    private void receiveMessages() {
        new Thread(() -> {
            try {
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    SwingUtilities.invokeLater(() -> messageArea.append(line + "\n"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage() {
        String message = messageInput.getText();
        out.println(message);
        messageInput.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new clientGUI().setVisible(true));
    }
}
