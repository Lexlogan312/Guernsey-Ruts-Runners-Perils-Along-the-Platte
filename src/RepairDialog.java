import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RepairDialog extends JDialog {
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown

    private final Inventory inventory;
    private final GameController gameController;

    private JButton repairButton;
    private JButton cancelButton;

    public RepairDialog(String partName, GameController gameController, Inventory inventory) {
        super((Frame) null, "Repair Option", true);

        this.gameController = gameController;
        this.inventory = inventory;

        setSize(400, 200);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Header
        JLabel headerLabel = new JLabel("Repair Broken Part?");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 20));
        headerLabel.setForeground(HEADER_COLOR);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Message Panel
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(PANEL_COLOR);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel messageLabel = new JLabel("The " + partName + " is broken. Would you like to repair it?");
        messageLabel.setForeground(TEXT_COLOR);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messagePanel.add(messageLabel);
        add(messagePanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);

        repairButton = new JButton("Repair");
        repairButton.setBackground(ACCENT_COLOR);
        repairButton.setForeground(Color.WHITE);
        repairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.repairPart(partName);
                gameController.restoreSpeed();
                dispose();
            }
        });

        cancelButton = new JButton("Don't Repair");
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.reduceSpeed(.2);
                dispose();
            }
        });

        buttonPanel.add(repairButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showDialog() {
        setVisible(true);
    }
}
