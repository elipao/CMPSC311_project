import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StartFrame extends JFrame implements ActionListener {
    private JTextField nameBox = new JTextField();
    private JLabel welcomeMessage = new JLabel("Welcome to our Chat Application. Please enter your name to begin chatting!");
    private JLabel errorMessage = new JLabel(" "); 

    public StartFrame() {
        SwingUtilities.invokeLater(() -> {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(500, 150);
            setLayout(new BorderLayout());
            JButton submitButton = new JButton("Submit");

            submitButton.addActionListener(this);

            nameBox.setColumns(10);

            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            centerPanel.add(welcomeMessage);

            JPanel nameBoxPanel = new JPanel(new BorderLayout());
            nameBoxPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            nameBoxPanel.add(nameBox);

            add(centerPanel, BorderLayout.NORTH);
            add(nameBoxPanel, BorderLayout.CENTER);
            add(submitButton, BorderLayout.SOUTH);
            add(errorMessage, BorderLayout.WEST);

            setLocationRelativeTo(null);

            setVisible(true);
        });
    }

    public void actionPerformed(ActionEvent e) {
        String enteredName = nameBox.getText().trim();

        if (isValidName(enteredName)) {
            System.out.println("Valid name: " + enteredName);
            errorMessage.setText(" "); 
            //add here logic to switch slides 
        } else {
            System.out.println("Invalid name: " + enteredName);
            errorMessage.setText("Invalid entry, try again"); 
            errorMessage.setForeground(Color.RED); 
        }
    }

    private boolean isValidName(String name) {
        return !name.isEmpty() && name.matches("^[a-zA-Z]+$");
    }
}
