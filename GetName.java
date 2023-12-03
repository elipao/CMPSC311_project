import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GetName extends JDialog {

    private JTextField nameField;
    private String userName;
    private JComboBox<String> themeComboBox;
    Font arialFont = new Font("Arial", Font.PLAIN, 16); // Font name, style, size
    

    // a child of the JFrame class, takes on the 3rd argument which is modal: true 
    // modal blocks input from other windows in the app until it's closed 
    public GetName(JFrame parent) {
        super(parent, "Welcome to our chatting services!", true);
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout()); 

        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new FlowLayout());

        // Create label, set text color to purple + font arial
        JLabel nameLabel = new JLabel("Enter your name: ");
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setFont(arialFont); 
        namePanel.add(nameLabel);

        // Create text field, set text color to purple + font arial
        nameField = new JTextField(10);
        nameField.setForeground(Color.BLACK);
        nameField.setFont(arialFont);
        namePanel.add(nameField);

        // definitely a better way to do this, like creating a label class... but albeit
        // there's only 2 
        JLabel comboLabel = new JLabel("Select Theme: ");
        comboLabel.setForeground(Color.BLACK);
        comboLabel.setFont(arialFont); 
        comboPanel.add(comboLabel);

        // Create a theme selection JComboBox
        String[] themes = {"Default", "NightMode", "Blue"};
        themeComboBox = new JComboBox<>(themes);
        themeComboBox.setForeground(Color.BLACK);
        themeComboBox.setFont(arialFont);
        comboPanel.add(themeComboBox);
   
        // Create button, set text color to purple + font arial
        JButton okButton = new JButton("OK");
        okButton.setForeground(Color.BLACK);
        okButton.setFont(arialFont);

        // add everything together to the main panel, again, probably not the best execution
        mainPanel.add(namePanel, BorderLayout.NORTH);
        mainPanel.add(comboPanel, BorderLayout.CENTER); // Adjust position to CENTER
        mainPanel.add(okButton, BorderLayout.SOUTH);
            
        // override actionPerformed when okButton is clicked by disposing the frame
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputName = nameField.getText().trim();

                if (isValidUserName(inputName)) {
                    userName = inputName;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(GetName.this,
                            "Please enter a valid user name.",
                            "Invalid User Name",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        add(mainPanel);
        pack(); // auto reformats components when window is resized 
        setLocationRelativeTo(null); // default is the center position of screen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private boolean isValidUserName(String name){
        return !name.isEmpty() && name.matches("^[a-zA-Z]+$"); 
    }

    public String getUserName() {
        return userName;
    }

    public String getSelectedTheme() {
        return (String) themeComboBox.getSelectedItem();
    }
}
