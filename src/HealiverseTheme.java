/**
 * Class/Interface: HealiverseTheme
 * Creator: Zeti Nur Aimar binti Ali
 * Tester: G04/SE Group 14
 * Description: Central theme configuration for consistent Healiverse UI styling.
 */

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public final class HealiverseTheme {
    // DIMENSIONS
    public static final int PHONE_WIDTH = 390;
    public static final int PHONE_HEIGHT = 720;
    public static final int PHONE_INSET = 10;
    public static final int CONTENT_WIDTH = 340;
    public static final int MODULE_WIDTH = 350;
    public static final int MODULE_HEIGHT = 588;
    public static final int LOGIN_CARD_WIDTH = 350;
    public static final int LOGIN_FIELD_WIDTH = 290;
    public static final int BUTTON_HEIGHT = 42;
    public static final int COMPACT_BUTTON_HEIGHT = 38;

    // COLOR PALETTE - clean SDG 3 mobile app theme
    // Primary Colors
    public static final Color PASTEL_PINK = new Color(0xF4B6C8);
    public static final Color LAVENDER = new Color(0xD8D2F0);
    public static final Color MINT = new Color(0xA8DDB5);
    public static final Color BABY_BLUE = new Color(0xB7DCE8);
    public static final Color SOFT_YELLOW = new Color(0xF4D57E);
    
    // Neutral Colors
    public static final Color CREAM = new Color(0xF7FAF8);
    public static final Color WHITE = Color.WHITE;
    public static final Color SURFACE = new Color(0xFFFFFF);
    
    // Text Colors
    public static final Color DARK_PURPLE = new Color(0x24313A);
    public static final Color MUTED_PURPLE = new Color(0x5F6B75);
    
    // UI Elements
    public static final Color LINE = new Color(0xD8E1DE);
    public static final Color PIXEL_PURPLE = new Color(0x7BAA8B);
    public static final Color PIXEL_SHADOW = new Color(0x6F8F80);
    public static final Color HEADER_PINK = new Color(0xEFA7BE);
    public static final Color HEADER_GREEN = new Color(0xCFE9D6);
    public static final Color SKY_TOP = new Color(0xEAF6F0);
    public static final Color SKY_BOTTOM = new Color(0xF7FAF8);

    // FONTS
    private HealiverseTheme() {
        // Prevent instantiation
    }

    /**
     * Title font (bold, monospaced) for main headings.
     * @param size Font size in points
     * @return Font configured for titles (24-32pt recommended)
     */
    public static Font titleFont(int size) {
        return new Font("Monospaced", Font.BOLD, size);
    }

    /**
     * Button font (bold, sans-serif) for interactive elements.
     * @param size Font size in points
     * @return Font configured for buttons (14-18pt recommended)
     */
    public static Font buttonFont(int size) {
        return new Font("SansSerif", Font.BOLD, size);
    }

    /**
     * Body text font (plain, sans-serif) for content text.
     * @param size Font size in points
     * @return Font configured for body text (12-14pt recommended)
     */
    public static Font bodyFont(int size) {
        return new Font("SansSerif", Font.PLAIN, size);
    }

    public static Border pixelBorder(Color accent, int padding) {
        return new CompoundBorder(
                new LineBorder(accent, 1),
                new EmptyBorder(padding, padding, padding, padding));
    }

    public static Border thinPixelBorder(Color accent, int padding) {
        return new CompoundBorder(
                new LineBorder(accent, 1),
                new EmptyBorder(padding, padding, padding, padding));
    }

    public static Border cardBorder(Color accent, int padding) {
        return cardBorder(accent, padding, padding);
    }

    public static Border cardBorder(Color accent, int verticalPadding, int horizontalPadding) {
        return new CompoundBorder(
                new LineBorder(accent, 1),
                new EmptyBorder(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding));
    }

    public static Border inputBorder() {
        return new CompoundBorder(
                new LineBorder(LINE, 1, true),
                new EmptyBorder(7, 10, 7, 10));
    }

    public static void stylePixelButton(AbstractButton button, Color fill) {
        button.setFont(buttonFont(13));
        button.setForeground(DARK_PURPLE);
        button.setBackground(fill);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
                new LineBorder(new Color(0x8FBBA0), 1),
                new EmptyBorder(8, 12, 8, 12)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void setFixedSize(JComponent component, int width, int height) {
        Dimension size = new Dimension(width, height);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        component.setMaximumSize(size);
    }

    public static void applyPhoneSize(JComponent component) {
        component.setPreferredSize(new Dimension(PHONE_WIDTH, PHONE_HEIGHT));
    }
}
