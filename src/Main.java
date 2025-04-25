import javax.swing.*;
import java.awt.*;

public class Main {

    private JFrame frame;
    private GUI gameGUI;
    private GameController gameController;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default for better native appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Warning: Could not set system look and feel.");
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
        frame.setSize(1200, 800); // Initial size
        frame.setMinimumSize(new Dimension(1000, 700)); // Set a minimum size
        frame.setResizable(true); // **** ALLOW RESIZING ****
        frame.setLocationRelativeTo(null);

        // --- Game Initialization Sequence ---

        // 1. Show the startup dialog to get player info, trail, month
        StartupDialog startupDialog = new StartupDialog(frame, gameController);
        startupDialog.setVisible(true); // Blocks until closed

        // Check if essential info was set (e.g., if user closed dialog early)
        if (gameController.getPlayer() == null || gameController.getTrail() == null || gameController.getTime() == null) {
            System.err.println("Game setup incomplete. Exiting.");
            System.exit(1);
        }


        // 2. Initialize the game logic now that settings are chosen
        gameController.startNewGame(); // Initializes internal state

        // 3. Show the market dialog for initial purchases
        // Need to ensure the frame is visible *before* showing modal dialogs based on it
        // A bit of a workaround: make frame visible briefly, then hide, show dialogs, then show finally.
        // Or, pass null as owner for initial dialogs if frame isn't ready. Let's try passing frame.
        // frame.setVisible(true); // Make frame visible so dialogs have an owner

        Market market = new Market(gameController.getPlayer(), gameController.getInventory());
        JDialog marketDialog = new JDialog(frame, "General Store - Initial Supplies", true);
        marketDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Ensure it closes properly
        marketDialog.add(market.createMarketPanel());
        marketDialog.pack();
        marketDialog.setSize(800, 600); // Set size after packing
        marketDialog.setLocationRelativeTo(frame);
        marketDialog.setVisible(true); // Blocks until closed

        // 4. Show the Fort Kearny journey intro dialog
        FortKearnyDialog fortKearnyDialog = new FortKearnyDialog(frame, gameController);
        fortKearnyDialog.setVisible(true); // Blocks until closed

        // 5. Simulate the journey to Fort Kearny (this will show the summary dialog)
        gameController.journeyToFortKearny();

        // 6. Make sure the main GUI is updated *before* showing the frame
        // The journeyToFortKearny method now calls notifyGameStateChanged after its summary dialog closes.

        // 7. Finally, show the main game frame
        frame.setVisible(true);
    }
}