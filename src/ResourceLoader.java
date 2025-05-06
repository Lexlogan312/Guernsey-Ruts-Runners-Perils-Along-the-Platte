/**
 * ResourceLoader Class of the Perils Along the Platte Game
 * A utility class for loading game resources such as images and fonts.
 * Implements multiple fallback strategies to ensure resources can be loaded
 * both when running from an IDE and when packaged in a JAR file.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file ResourceLoader.java
 */

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.io.File;

public class ResourceLoader {
    
    // Array of base paths to search for resources
    private static final String[] RESOURCE_BASE_PATHS = {
        "/",            // Root of JAR or classpath
        "/resources/",  // Original path - for IDE compatibility
        ""              // Relative path - for both JAR and IDE
    };
    
    /**
     * Loads an image using multiple fallback strategies.
     * Attempts to load the image from various locations in this order:
     * 1. Classpath resources (for JAR deployment)
     * 2. Direct file path (for IDE development)
     * 3. Absolute path
     * 4. Direct image path
     * 
     * @param imagePath Path to the image, relative to the resources folder
     *                  (e.g., "images/Wagon Icon.png")
     * @return ImageIcon object if loading succeeds, null if all loading attempts fail
     * @throws SecurityException If access to the file system is denied
     * @throws IllegalArgumentException If the image path is invalid
     */
    public static ImageIcon loadImage(String imagePath) {
        ImageIcon icon;
        String fullPath = "resources/" + imagePath;
        
        // Debug information for troubleshooting
        System.out.println("Attempting to load image: " + imagePath);
        
        // Try all resource base paths first (for JAR deployment)
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
        
        // Try direct paths (works when running from IDE)
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
            }
        } catch (Exception e) {
            System.err.println("Failed to load image from absolute path: " + e.getMessage());
        }
        
        // If all attempts fail, return null
        System.err.println("Failed to load image after all attempts: " + imagePath);
        return null;
    }
    
    /**
     * Gets an InputStream for a resource file.
     * Attempts to load the resource using multiple strategies:
     * 1. Classpath resources (for JAR deployment)
     * 2. Direct file path (for IDE development)
     * 3. Absolute path
     * 
     * This method is useful for loading any type of resource file
     * that needs to be read as a stream, such as configuration files
     * or text resources.
     * 
     * @param resourcePath Path to the resource, relative to the resources folder
     * @return InputStream for the resource if found, null if all loading attempts fail
     * @throws SecurityException If access to the file system is denied
     * @throws IllegalArgumentException If the resource path is invalid
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
     * Loads a font file using multiple fallback strategies.
     * Attempts to load the font from various locations in this order:
     * 1. Classpath resources (for JAR deployment)
     * 2. Direct file path (for IDE development)
     * 3. Absolute path
     * 
     * The method includes detailed logging to help diagnose loading issues
     * and provides multiple fallback options to ensure the font can be loaded
     * in different deployment scenarios.
     * 
     * @param fontPath Path to the font file, relative to the resources folder
     *                 (e.g., "fonts/Kirsty Rg.otf")
     * @return Font object if loading succeeds, null if all loading attempts fail
     * @throws SecurityException If access to the file system is denied
     * @throws IllegalArgumentException If the font path is invalid
     * @throws FontFormatException If the font file is corrupted or in an unsupported format
     */
    public static Font loadFont(String fontPath) {
        try {
            // Debug information
            System.out.println("Attempting to load font: " + fontPath);
            
            Font font;

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
} 