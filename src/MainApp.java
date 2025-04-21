import javax.swing.*;
import java.awt.*;

public class MainApp {
    
    private JFrame frame;
    private EnhancedGUI gameGUI;
    private GameController gameController;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new MainApp();
        });
    }
    
    public MainApp() {
        initializeApp();
    }
    
    private void initializeApp() {
        // Create frame
        frame = new JFrame("Perils Along the Platte");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Load custom font
        FontManager.loadCustomFonts();
        
        // Create game controller
        gameController = new GameController();
        
        // Create and add GUI component
        gameGUI = new EnhancedGUI(gameController);
        frame.getContentPane().add(gameGUI);
        
        // Set frame size and center on screen
        frame.setSize(1080, 720);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        // Initialize game
        gameController.startNewGame();
        
        // Show the startup dialog first (blocks until closed)
        StartupDialog startupDialog = new StartupDialog(frame, gameController);
        startupDialog.setVisible(true);
        
        // After startup dialog is closed, visit the market (blocks until closed)
        gameController.visitMarket();
        
        // Simulate journey to Fort Kearny (first main landmark)
        gameController.journeyToFortKearny();
        
        // Finally, show the main game frame
        frame.setVisible(true);
    }
} 