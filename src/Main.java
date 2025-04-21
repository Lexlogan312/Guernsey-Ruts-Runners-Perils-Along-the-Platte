import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize the GUI application
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Start the application
            new MainApp();
        });
    }
}