import java.awt.*;
import java.io.File;
import java.io.InputStream;

public class FontManager {
    public static Font WESTERN_FONT;
    public static Font WESTERN_FONT_BOLD;
    public static Font WESTERN_FONT_TITLE;
    
    /**
     * Loads the custom western font in different sizes
     */
    public static void loadCustomFonts() {
        try {
            // Try multiple methods to load the font
            Font baseFont = null;
            Exception lastException = null;
            
            // Try method 1: direct file path
            try {
                File fontFile = new File("resources/fonts/Kirsty Rg.otf");
                if (fontFile.exists()) {
                    baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                    System.out.println("Font loaded using direct file path");
                }
            } catch (Exception e) {
                lastException = e;
                System.err.println("Failed to load font using direct file path: " + e.getMessage());
            }
            
            // Try method 2: absolute path
            if (baseFont == null) {
                try {
                    String fontPath = System.getProperty("user.dir") + "/resources/fonts/Kirsty Rg.otf";
                    File fontFile = new File(fontPath);
                    if (fontFile.exists()) {
                        baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                        System.out.println("Font loaded using absolute path: " + fontPath);
                    }
                } catch (Exception e) {
                    lastException = e;
                    System.err.println("Failed to load font using absolute path: " + e.getMessage());
                }
            }
            
            // Try method 3: resource stream (original method)
            if (baseFont == null) {
                try {
                    InputStream is = FontManager.class.getResourceAsStream("/resources/fonts/Kirsty Rg.otf");
                    if (is != null) {
                        baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
                        System.out.println("Font loaded using resource stream");
                    }
                } catch (Exception e) {
                    lastException = e;
                    System.err.println("Failed to load font using resource stream: " + e.getMessage());
                }
            }
            
            // If all methods failed, throw the last exception
            if (baseFont == null) {
                throw lastException != null ? lastException : 
                    new Exception("Could not find font file using any method");
            }
            
            // Create different sizes and styles
            WESTERN_FONT = baseFont.deriveFont(Font.PLAIN, 14f);
            WESTERN_FONT_BOLD = baseFont.deriveFont(Font.BOLD, 14f);
            WESTERN_FONT_TITLE = baseFont.deriveFont(Font.BOLD, 32f);
            
            // Register with the Graphics Environment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);
            
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