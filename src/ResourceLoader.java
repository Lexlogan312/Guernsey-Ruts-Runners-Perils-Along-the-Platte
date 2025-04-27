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
    
    // We'll try multiple base paths to ensure resources are found
    private static final String[] RESOURCE_BASE_PATHS = {
        "/",            // Root of JAR or classpath
        "/resources/",  // Original path - for IDE compatibility
        ""              // Relative path - for both JAR and IDE
    };
    
    /**
     * Loads an image using multiple fallback strategies.
     * 
     * @param imagePath Path to the image, relative to the resources folder (e.g., "images/Wagon Icon.png")
     * @return ImageIcon or null if loading fails
     */
    public static ImageIcon loadImage(String imagePath) {
        ImageIcon icon = null;
        String fullPath = "resources/" + imagePath;
        
        // Debug information
        System.out.println("Attempting to load image: " + imagePath);
        
        // Try all resource base paths first (for JAR)
        for (String basePath : RESOURCE_BASE_PATHS) {
            String resourcePath = basePath + imagePath;
            try {
                URL resourceUrl = ResourceLoader.class.getResource(resourcePath);
                if (resourceUrl != null) {
                    icon = new ImageIcon(resourceUrl);
                    if (icon.getIconWidth() > 0) {
                        System.out.println("Image loaded as classpath resource: " + resourcePath);
                        return icon;
                    }
                } else {
                    System.out.println("Resource URL is null for path: " + resourcePath);
                }
            } catch (Exception e) {
                System.err.println("Failed to load image as resource from " + resourcePath + ": " + e.getMessage());
            }
        }
        
        // Also try direct paths (works when running from IDE)
        try {
            File file = new File(fullPath);
            if (file.exists()) {
                icon = new ImageIcon(fullPath);
                if (icon.getIconWidth() > 0) {
                    System.out.println("Image loaded from file path: " + fullPath);
                    return icon;
                }
            } else {
                System.out.println("File does not exist: " + fullPath);
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
            } else {
                System.out.println("Absolute file does not exist: " + absolutePath);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image from absolute path: " + e.getMessage());
        }
        
        // Lastly, try with just the image path directly
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                icon = new ImageIcon(imagePath);
                if (icon.getIconWidth() > 0) {
                    System.out.println("Image loaded from direct path: " + imagePath);
                    return icon;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load image from direct path: " + e.getMessage());
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
        // Debug information
        System.out.println("Attempting to load resource stream: " + resourcePath);
        
        // Try all resource base paths first (for JAR)
        InputStream is = null;
        for (String basePath : RESOURCE_BASE_PATHS) {
            String fullResourcePath = basePath + resourcePath;
            try {
                is = ResourceLoader.class.getResourceAsStream(fullResourcePath);
                if (is != null) {
                    System.out.println("Resource stream opened from classpath: " + fullResourcePath);
                    return is;
                } else {
                    System.out.println("Resource stream not found at: " + fullResourcePath);
                }
            } catch (Exception e) {
                System.err.println("Error checking resource at " + fullResourcePath + ": " + e.getMessage());
            }
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
            } else {
                System.out.println("File does not exist: resources/" + resourcePath);
            }
        } catch (Exception e) {
            System.err.println("Failed to load resource from file path: " + e.getMessage());
        }
        
        // Try direct path as a last resort
        try {
            File file = new File(resourcePath);
            if (file.exists()) {
                is = file.toURI().toURL().openStream();
                if (is != null) {
                    System.out.println("Resource loaded from direct path: " + file.getPath());
                    return is;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load resource from direct path: " + e.getMessage());
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
            // Debug information
            System.out.println("Attempting to load font: " + fontPath);
            
            Font font = null;
            
            // Try loading from resource stream
            InputStream is = getResourceAsStream(fontPath);
            if (is != null) {
                try {
                    // Determine if it's a TrueType font or OpenType font
                    int fontFormat = Font.TRUETYPE_FONT;
                    if (fontPath.toLowerCase().endsWith(".otf")) {
                        fontFormat = Font.TRUETYPE_FONT; // OpenType is handled by TRUETYPE_FONT in Java
                    }
                    
                    font = Font.createFont(fontFormat, is);
                    is.close();
                    if (font != null) {
                        System.out.println("Successfully loaded font: " + fontPath);
                        return font;
                    }
                } catch (Exception e) {
                    System.err.println("Failed to create font from stream: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // If we couldn't load the font, return null
            return null;
        } catch (Exception e) {
            System.err.println("Failed to load font: " + e.getMessage());
            e.printStackTrace();
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
        // Try all potential paths
        for (String basePath : RESOURCE_BASE_PATHS) {
            if (ResourceLoader.class.getResource(basePath + resourcePath) != null) {
                return true;
            }
        }
        
        // Try as file
        if (new File("resources/" + resourcePath).exists() || new File(resourcePath).exists()) {
            return true;
        }
        
        return false;
    }
} 