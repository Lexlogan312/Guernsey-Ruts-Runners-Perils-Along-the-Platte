import java.awt.*;

public class FontManager {
    public static Font WESTERN_FONT;
    public static Font WESTERN_FONT_BOLD;
    public static Font WESTERN_FONT_TITLE;
    
    /**
     * Loads the custom western font in different sizes
     */
    public static void loadCustomFonts() {
        try {
            // Use the ResourceLoader to load the fonts
            Font baseFont = ResourceLoader.loadFont("fonts/Kirsty Rg.otf");
            Font boldFont = ResourceLoader.loadFont("fonts/Kirsty Bd.otf");
            
            if (baseFont == null) {
                throw new Exception("Could not load regular font using ResourceLoader");
            }
            
            if (boldFont == null) {
                System.err.println("Could not load bold font using ResourceLoader. Using regular font with bold style.");
                boldFont = baseFont.deriveFont(Font.BOLD);
            }
            
            // Create different sizes and styles
            WESTERN_FONT = baseFont.deriveFont(Font.PLAIN, 14f);
            WESTERN_FONT_BOLD = boldFont.deriveFont(Font.BOLD, 14f);
            WESTERN_FONT_TITLE = boldFont.deriveFont(Font.BOLD, 32f);
            
            // Register with the Graphics Environment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);
            ge.registerFont(boldFont);
            
        } catch (Exception e) {
            // Fallback to a similar looking font if custom font fails to load
            System.err.println("Failed to load custom font: " + e.getMessage());
            WESTERN_FONT = new Font("Serif", Font.PLAIN, 14);
            WESTERN_FONT_BOLD = new Font("Serif", Font.BOLD, 14);
            WESTERN_FONT_TITLE = new Font("Serif", Font.BOLD, 32);
        }
    }
    
    /**
     * Gets western font at a custom size
     */
    public static Font getWesternFont(float size) {
        return WESTERN_FONT.deriveFont(size);
    }
    
    /**
     * Gets bold western font at a custom size
     */
    public static Font getBoldWesternFont(float size) {
        return WESTERN_FONT_BOLD.deriveFont(size);
    }
} 