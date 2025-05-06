/**
 * FontManager Class of the Perils Along the Platte Game
 * Manages custom western-themed fonts for the game interface.
 * Handles font loading, registration, and provides access to different font styles and sizes.
 * Uses the Kirsty font family (regular and bold variants) with fallback to system fonts
 * if custom fonts cannot be loaded.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file FontManager.java
 */

import java.awt.*;

public class FontManager {
    //Regular western font for general text display
    public static Font WESTERN_FONT;
    
    //Bold western font for emphasis and headers.
    public static Font WESTERN_FONT_BOLD;
    
    //Large bold western font for titles and major headings.
    public static Font WESTERN_FONT_TITLE;
    
    /**
     * Loads and initializes the custom western fonts.
     * Attempts to load both regular and bold variants of the Kirsty font family.
     * If font loading fails, falls back to system Serif font.
     * Registers the loaded fonts with the graphics environment for system-wide availability.
     * 
     * @throws Exception if the regular font fails to load
     */
    public static void loadCustomFonts() {
        try {
            // Load base and bold font variants from resource files
            Font baseFont = ResourceLoader.loadFont("fonts/Kirsty Rg.otf");
            Font boldFont = ResourceLoader.loadFont("fonts/Kirsty Bd.otf");
            
            if (baseFont == null) {
                throw new Exception("Could not load regular font using ResourceLoader");
            }
            
            if (boldFont == null) {
                System.err.println("Could not load bold font using ResourceLoader. Using regular font with bold style.");
                boldFont = baseFont.deriveFont(Font.BOLD);
            }
            
            // Initialize font instances with different sizes for various UI elements
            WESTERN_FONT = baseFont.deriveFont(Font.PLAIN, 14f);
            WESTERN_FONT_BOLD = boldFont.deriveFont(Font.BOLD, 14f);
            WESTERN_FONT_TITLE = boldFont.deriveFont(Font.BOLD, 32f);
            
            // Register fonts for system-wide availability
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);
            ge.registerFont(boldFont);
            
        } catch (Exception e) {
            // Fallback to system fonts if custom fonts fail to load
            System.err.println("Failed to load custom font: " + e.getMessage());
            WESTERN_FONT = new Font("Serif", Font.PLAIN, 14);
            WESTERN_FONT_BOLD = new Font("Serif", Font.BOLD, 14);
            WESTERN_FONT_TITLE = new Font("Serif", Font.BOLD, 32);
        }
    }
    
    /**
     * Gets a scaled version of the regular western font.
     * Creates a new Font object with the specified size while maintaining
     * the original font's style and family. This method is useful for
     * creating consistent text styling across different UI elements.
     * 
     * @param size The desired font size in points
     * @return A new Font object with the specified size
     */
    public static Font getWesternFont(float size) {
        return WESTERN_FONT.deriveFont(size);
    }
    
    /**
     * Gets a scaled version of the bold western font.
     * Creates a new Font object with the specified size while maintaining
     * the original font's bold style and family. This method is useful for
     * creating consistent emphasis text across different UI elements.
     * 
     * @param size The desired font size in points
     * @return A new Font object with the specified size
     */
    public static Font getBoldWesternFont(float size) {
        return WESTERN_FONT_BOLD.deriveFont(size);
    }
} 