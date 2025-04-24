import javax.swing.*;

public class Main {
    
    private JFrame frame;
    private GUI gameGUI;
    private GameController gameController;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new Main();
        });
    }
    
    public Main() {
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
        gameGUI = new GUI(gameController);
        frame.getContentPane().add(gameGUI);
        
        // Set frame size and center on screen
        frame.setSize(1200, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        // Initialize game
        gameController.startNewGame();
        
        // Show the startup dialog first (blocks until closed)
        StartupDialog startupDialog = new StartupDialog(frame, gameController);
        startupDialog.setVisible(true);
        
        // After startup dialog is closed, visit the market (blocks until closed)
        gameController.visitMarket();
        
        // Show the Fort Kearny journey dialog
        FortKearnyDialog fortKearnyDialog = new FortKearnyDialog(frame, gameController);
        fortKearnyDialog.setVisible(true);
        
        // Simulate journey to Fort Kearny (first main landmark)
        gameController.journeyToFortKearny();
        
        // Make sure the UI is updated to show Fort Kearny as the current location
        gameController.updateGameState();
        // Additional update to ensure the map refreshes
        gameGUI.updateGameState();
        
        // Show journey summary dialog
        TravelSummaryDialog summaryDialog = new TravelSummaryDialog(
            frame, 
            gameController.getJourneyEvents(),
            gameController.getTime().getTotalDays(),
            gameController.getMap().getDistanceTraveled()
        );
        summaryDialog.setVisible(true);
        
        // Finally, show the main game frame
        frame.setVisible(true);
    }
} 