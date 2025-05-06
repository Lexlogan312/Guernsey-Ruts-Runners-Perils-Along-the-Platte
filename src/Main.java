/**
 * Main Class of the Perils Along the Platte Game
 * Entry point for the game application that initializes and starts the game.
 * 
 * The Main class handles:
 * - Application startup and configuration
 * - GUI initialization and setup
 * - Game resource loading
 * - Game state initialization
 * - Startup sequence management
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Main.java
 */

import javax.swing.*;
import java.awt.*;

public class Main {

    /**
     * Main entry point for the application.
     * Sets up the Swing environment and creates the main game instance.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use system's native look and feel for better integration
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Warning: Could not set system look and feel.");
            }

            new Main();
        });
    }

    /**
     * Constructor for the Main class.
     * Initializes the game application by calling initializeApp().
     */
    public Main() {
        initializeApp();
    }

    /**
     * Initializes the game application by:
     * 1. Creating the main window
     * 2. Loading custom fonts
     * 3. Setting up the game controller
     * 4. Creating the GUI
     * 5. Showing the startup sequence of dialogs
     * 6. Starting the game
     * 
     * Startup sequence:
     * 1. StartupDialog: Get player information and game settings
     * 2. Market: Allow player to purchase initial supplies
     * 3. FortKearnyDialog: Show journey introduction
     * 4. Journey: Simulate initial journey to Fort Kearny
     * 5. Main Game: Display the main game window
     * 
     * @throws RuntimeException if game setup is incomplete
     */
    private void initializeApp() {
        // Create and configure the main window
        JFrame frame = new JFrame("Perils Along the Platte");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize game resources and components
        FontManager.loadCustomFonts();
        GameController gameController = new GameController();
        GUI gameGUI = new GUI(gameController);
        frame.getContentPane().add(gameGUI);

        // Configure window properties
        frame.setSize(1200, 800);
        frame.setMinimumSize(new Dimension(1000, 700));
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        // Game initialization sequence
        // 1. Get player information and game settings
        StartupDialog startupDialog = new StartupDialog(frame, gameController);
        startupDialog.setVisible(true);

        // Validate that essential game settings were configured
        if (gameController.getPlayer() == null || gameController.getTrail() == null || gameController.getTime() == null) {
            System.err.println("Game setup incomplete. Exiting.");
            System.exit(1);
        }

        // 2. Initialize game state with player choices
        gameController.startNewGame();

        // 3. Show market for initial supplies
        Market market = new Market(gameController.getPlayer(), gameController.getInventory());
        JDialog marketDialog = new JDialog(frame, "Market", true);
        marketDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        marketDialog.add(market.createMarketPanel());
        marketDialog.pack();
        marketDialog.setSize(800, 600);
        marketDialog.setLocationRelativeTo(frame);
        marketDialog.setVisible(true);

        // 4. Show journey introduction
        FortKearnyDialog fortKearnyDialog = new FortKearnyDialog(frame, gameController);
        fortKearnyDialog.setVisible(true);

        // 5. Simulate initial journey to Fort Kearny
        gameController.journeyToFortKearny();

        // 6. Display the main game window
        frame.setVisible(true);
    }
}