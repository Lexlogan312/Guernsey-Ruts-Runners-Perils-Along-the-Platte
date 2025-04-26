import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.io.File;

/**
 * Utility class for loading resources (images, fonts, etc.) in a way that works
 * both when running from an IDE and when packaged in a JAR file.
 */
public class ResourceLoader {
    
    private static final String RESOURCE_BASE_PATH = "/resources/";

    /**
     * Loads an image using multiple fallback strategies.
     * 
     * @param imagePath Path to the image, relative to the resources folder (e.g., "images/Wagon Icon.png")
     * @return ImageIcon or null if loading fails
     */
    public static ImageIcon loadImage(String imagePath) {
        ImageIcon icon = null;
        String fullPath = "resources/" + imagePath;
        String resourcePath = RESOURCE_BASE_PATH + imagePath;
        
        // Try loading as a resource from the classpath (works in JAR)
        try {
            URL resourceUrl = ResourceLoader.class.getResource(resourcePath);
            if (resourceUrl != null) {
                icon = new ImageIcon(resourceUrl);
                if (icon.getIconWidth() > 0) {
                    System.out.println("Image loaded as classpath resource: " + resourcePath);
                    return icon;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load image as resource: " + e.getMessage());
        }
        
        // Try direct file path (works when running from IDE)
        try {
            File file = new File(fullPath);
            if (file.exists()) {
                icon = new ImageIcon(fullPath);
                if (icon.getIconWidth() > 0) {
                    System.out.println("Image loaded from file path: " + fullPath);
                    return icon;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load image from file path: " + e.getMessage());
        }
        
        // Try with absolute path
        try {
            String absolutePath = System.getProperty("user.dir") + "/" + fullPath;
            File file = new File(absolutePath);
            if (file.exists()) {
                icon = new ImageIcon(absolutePath);
                if (icon.getIconWidth() > 0) {
                    System.out.println("Image loaded from absolute path: " + absolutePath);
                    return icon;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load image from absolute path: " + e.getMessage());
        }
        
        // If all methods failed, return null
        System.err.println("All image loading methods failed for: " + imagePath);
        return null;
    }
    
    /**
     * Gets an input stream for a resource file.
     * 
     * @param resourcePath Path to the resource, relative to the resources folder (e.g., "fonts/Kirsty Rg.otf")
     * @return InputStream or null if the resource cannot be found
     */
    public static InputStream getResourceAsStream(String resourcePath) {
        String fullResourcePath = RESOURCE_BASE_PATH + resourcePath;
        
        // Try to get resource from classpath (works in JAR)
        InputStream is = ResourceLoader.class.getResourceAsStream(fullResourcePath);
        if (is != null) {
            System.out.println("Resource stream opened: " + fullResourcePath);
            return is;
        }
        
        // Try as direct file path (works in IDE)
        try {
            File file = new File("resources/" + resourcePath);
            if (file.exists()) {
                is = file.toURI().toURL().openStream();
                if (is != null) {
                    System.out.println("Resource loaded from file path: " + file.getPath());
                    return is;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load resource from file path: " + e.getMessage());
        }
        
        System.err.println("Resource not found: " + resourcePath);
        return null;
    }
    
    /**
     * Loads a font using multiple fallback strategies.
     * 
     * @param fontPath Path to the font file, relative to the resources folder (e.g., "fonts/Kirsty Rg.otf")
     * @return Font object or null if loading fails
     */
    public static Font loadFont(String fontPath) {
        try {
            Font font = null;
            
            // Try loading from resource stream
            InputStream is = getResourceAsStream(fontPath);
            if (is != null) {
                try {
                    font = Font.createFont(Font.TRUETYPE_FONT, is);
                    is.close();
                    return font;
                } catch (Exception e) {
                    System.err.println("Failed to create font from stream: " + e.getMessage());
                }
            }
            
            // If we couldn't load the font, return null
            return null;
        } catch (Exception e) {
            System.err.println("Failed to load font: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Checks if a resource exists.
     * 
     * @param resourcePath Path to check, relative to the resources folder
     * @return true if resource exists, false otherwise
     */
    public static boolean resourceExists(String resourcePath) {
        // Try as classpath resource
        if (ResourceLoader.class.getResource(RESOURCE_BASE_PATH + resourcePath) != null) {
            return true;
        }
        
        // Try as file
        if (new File("resources/" + resourcePath).exists()) {
            return true;
        }
        
        return false;
    }
} 