import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

// for loading images and data files with flexible paths and caching
public final class HealiversePaths {
    private static final String ASSET_ROOT = "assets/images";
    private static final String LEGACY_IMAGE_ROOT = "images";
    private static final String DATA_ROOT = "data";
    private static final int VISIBLE_ALPHA_THRESHOLD = 8;
    private static final Map<String, int[]> VISIBLE_BOUNDS_CACHE = new HashMap<>();
    private static final Map<String, ImageIcon> SCALED_ICON_CACHE = new HashMap<>();

    private HealiversePaths() {
    }

    public static File dataFile(String fileName) {
        for (File file : dataCandidates(fileName)) {
            if (file.exists()) {
                return file;
            }
        }

        return writableDataFile(fileName);
    }

    public static File writableDataFile(String fileName) {
        File dataDir = preferredDataDirectory();
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        return new File(dataDir, fileName);
    }

    public static ImageIcon loadPixelIcon(String fileName, int maxWidth, int maxHeight) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }
        return loadImageIcon("pixel/" + aliasPixelFile(fileName), maxWidth, maxHeight);
    }

    public static ImageIcon loadImageIcon(String imagePath, int maxWidth, int maxHeight) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return null;
        }

        String normalized = aliasImagePath(imagePath.replace('\\', '/'));
        String withoutLegacyPrefix = normalized.startsWith(LEGACY_IMAGE_ROOT + "/")
                ? normalized.substring((LEGACY_IMAGE_ROOT + "/").length())
                : normalized;
        String withoutAssetPrefix = withoutLegacyPrefix.startsWith(ASSET_ROOT + "/")
                ? withoutLegacyPrefix.substring((ASSET_ROOT + "/").length())
                : withoutLegacyPrefix;

        for (String candidate : imageCandidates(normalized, withoutAssetPrefix)) {
            File file = new File(candidate);
            if (file.exists()) {
                String key = "file:" + file.getAbsolutePath();
                return loadAndScaleIcon(key, new ImageIcon(file.getPath()), maxWidth, maxHeight);
            }
        }

        URL resource = HealiversePaths.class.getResource("/" + withoutAssetPrefix);
        if (resource == null) {
            resource = HealiversePaths.class.getResource("/" + normalized);
        }
        if (resource != null) {
            return loadAndScaleIcon("resource:" + resource.toExternalForm(),
                    new ImageIcon(resource), maxWidth, maxHeight);
        }

        return null;
    }

    private static Set<String> imageCandidates(String normalized, String withoutAssetPrefix) {
        Set<String> candidates = new LinkedHashSet<>();
        addRelativeCandidates(candidates, ASSET_ROOT + "/" + withoutAssetPrefix);
        addRelativeCandidates(candidates, normalized);
        addRelativeCandidates(candidates, LEGACY_IMAGE_ROOT + "/" + withoutAssetPrefix);
        return candidates;
    }

    private static void addRelativeCandidates(Set<String> candidates, String relativePath) {
        candidates.add(relativePath);
        candidates.add("../" + relativePath);
        candidates.add("../../" + relativePath);
    }

    private static File[] dataCandidates(String fileName) {
        return new File[] {
                new File(DATA_ROOT, fileName),
                new File(".." + File.separator + DATA_ROOT, fileName),
                new File(fileName),
                new File(".." + File.separator + fileName)
        };
    }

    private static File preferredDataDirectory() {
        File rootData = new File(DATA_ROOT);
        if (rootData.exists()) {
            return rootData;
        }

        File parentData = new File(".." + File.separator + DATA_ROOT);
        if (parentData.exists()) {
            return parentData;
        }

        return rootData;
    }

    private static String aliasImagePath(String path) {
        if (path.startsWith("pixel/")) {
            return "pixel/" + aliasPixelFile(path.substring("pixel/".length()));
        }
        return path;
    }

    private static String aliasPixelFile(String fileName) {
        String name = fileName.trim();
        String lower = name.toLowerCase();
        if (lower.equals("healiverse logo.png") || lower.equals("healiverse title.png")) {
            return "Healiverse.png";
        }
        if (lower.equals("leaf.png") || lower.equals("heart green.png") || lower.equals("health wings.png")
                || lower.equals("heart wings.png")) {
            return "Green Heart Floating.png";
        }
        if (lower.equals("crescent moon.png") || lower.equals("potion.png")
                || lower.equals("sparkle blue.png") || lower.equals("sparkle pink.png")
                || lower.equals("sparkle yellow.png") || lower.equals("sparkle purple.png")) {
            return "Cloud Sparkle.png";
        }
        if (lower.equals("health.png") || lower.equals("coin.png")) {
            return "XP Coin.png";
        }
        if (lower.equals("learning explorer badge.png")) {
            return "Learn Button.png";
        }
        if (lower.equals("growth tracker badge.png")) {
            return "Green Heart Floating.png";
        }
        if (lower.equals("calm time badge.png")) {
            return "Calm Time Button.png";
        }
        if (lower.equals("heart badge.png")) {
            return "Heart.png";
        }
        if (lower.equals("quiz star badge.png")) {
            return "Star Badge.png";
        }
        if (lower.equals("top score badge.png") || lower.equals("wellness warrior badge.png")) {
            return "Star Trophy.png";
        }
        return name;
    }

    private static ImageIcon loadAndScaleIcon(String key, ImageIcon icon, int maxWidth, int maxHeight) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
        }
        if (maxWidth <= 0 || maxHeight <= 0) {
            return null;
        }

        String scaledKey = key + "|" + maxWidth + "x" + maxHeight;
        ImageIcon cachedIcon = SCALED_ICON_CACHE.get(scaledKey);
        if (cachedIcon != null) {
            return cachedIcon;
        }

        BufferedImage source = toBufferedImage(icon);
        int[] visibleBounds = VISIBLE_BOUNDS_CACHE.get(key);
        if (visibleBounds == null) {
            visibleBounds = findVisibleBounds(source);
            VISIBLE_BOUNDS_CACHE.put(key, visibleBounds);
        }
        BufferedImage artwork = cropToBounds(source, visibleBounds);

        double ratio = Math.min(maxWidth / (double) artwork.getWidth(),
                maxHeight / (double) artwork.getHeight());
        int width = Math.max(1, (int) Math.round(artwork.getWidth() * ratio));
        int height = Math.max(1, (int) Math.round(artwork.getHeight() * ratio));
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(artwork, 0, 0, width, height, null);
        g.dispose();
        ImageIcon scaledIcon = new ImageIcon(scaled);
        SCALED_ICON_CACHE.put(scaledKey, scaledIcon);
        return scaledIcon;
    }

    private static BufferedImage toBufferedImage(ImageIcon icon) {
        Image source = icon.getImage();
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return image;
    }

    private static int[] findVisibleBounds(BufferedImage image) {
        int minX = image.getWidth();
        int minY = image.getHeight();
        int maxX = -1;
        int maxY = -1;

        int[] row = new int[image.getWidth()];
        for (int y = 0; y < image.getHeight(); y++) {
            image.getRGB(0, y, image.getWidth(), 1, row, 0, image.getWidth());
            for (int x = 0; x < row.length; x++) {
                int alpha = (row[x] >>> 24) & 0xFF;
                if (alpha > VISIBLE_ALPHA_THRESHOLD) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            return new int[] { 0, 0, image.getWidth(), image.getHeight() };
        }
        if (minX == 0 && minY == 0 && maxX == image.getWidth() - 1 && maxY == image.getHeight() - 1) {
            return new int[] { 0, 0, image.getWidth(), image.getHeight() };
        }
        return new int[] { minX, minY, maxX - minX + 1, maxY - minY + 1 };
    }

    private static BufferedImage cropToBounds(BufferedImage image, int[] bounds) {
        if (bounds[0] == 0 && bounds[1] == 0
                && bounds[2] == image.getWidth() && bounds[3] == image.getHeight()) {
            return image;
        }
        return image.getSubimage(bounds[0], bounds[1], bounds[2], bounds[3]);
    }
}
