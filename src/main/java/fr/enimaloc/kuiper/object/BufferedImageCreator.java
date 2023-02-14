/*
 * BufferedImageCreator
 *
 * 0.0.1
 *
 * 11/02/2023
 */
package fr.enimaloc.kuiper.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 */
public class BufferedImageCreator {
    public static final int DEFAULT_TYPE = BufferedImage.TYPE_INT_RGB;
    public static final Logger LOGGER = LoggerFactory.getLogger(BufferedImageCreator.class);

    private BufferedImage img;

    private BufferedImageCreator(int width, int height, int type) {
        this.img = new BufferedImage(width, height, type);
    }

    public BufferedImageCreator fill(Color color) {
        return fill(color.getRGB());
    }

    public BufferedImageCreator fill(int color) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Filling image with color {}", color);
        }
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                set(x, y, color);
            }
        }
        return this;
    }

    public BufferedImageCreator set(int x, int y, Color color) {
        return set(x, y, color.getRGB());
    }

    public BufferedImageCreator set(int x, int y, int color) {
        if (LOGGER.isTraceEnabled()) {
            String prefix = "";
            if (Thread.currentThread().getStackTrace()[2].getClassName().equals(BufferedImageCreator.class.getName())) {
                prefix = "["+ Thread.currentThread().getStackTrace()[2].getMethodName() + "] ";
            }
            LOGGER.trace("{}Setting pixel ({}, {}) to color {}", prefix, x, y, color);
        }
        if (x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight()) {
            throw new IllegalArgumentException("x or y is out of bounds");
        }
        img.setRGB(x, y, color);
        return this;
    }

    public BufferedImageCreator square(int x, int y, int width, int height, Color color) {
        return square(x, y, width, height, color.getRGB());
    }

    public BufferedImageCreator square(int x, int y, int width, int height, int color) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Drawing square ({}, {}) with width {} and height {} with color {}", x, y, width, height, color);
        }
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                set(i, j, color);
            }
        }
        return this;
    }

    public BufferedImageCreator circle(int x, int y, int radius, Color color) {
        return circle(x, y, radius, color.getRGB());
    }

    public BufferedImageCreator circle(int x, int y, int radius, int color) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Drawing circle ({}, {}) with radius {} with color {}", x, y, radius, color);
        }
        for (int i = x - radius; i < x + radius; i++) {
            for (int j = y - radius; j < y + radius; j++) {
                if (Math.sqrt(Math.pow(i - x, 2) + Math.pow(j - y, 2)) <= radius) {
                    set(i, j, color);
                }
            }
        }
        return this;
    }

    public BufferedImageCreator line(int x1, int y1, int x2, int y2, Color color) {
        return line(x1, y1, x2, y2, color.getRGB());
    }

    public BufferedImageCreator line(int x1, int y1, int x2, int y2, int color) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Drawing line ({}, {}) to ({}, {}) with color {}", x1, y1, x2, y2, color);
        }
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;
        while (true) {
            set(x1, y1, color);
            if (x1 == x2 && y1 == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x1 = x1 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y1 = y1 + sy;
            }
        }
        return this;
    }

    public BufferedImageCreator text(int x, int y, String text, Color color, Font font) {
        return text(x, y, text, color.getRGB(), font);
    }

    public BufferedImageCreator text(int x, int y, String text, int color, Font font) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Drawing text \"{}\" at ({}, {}) with color {} and font {}", text, x, y, color, font);
        }
        Graphics2D g = img.createGraphics();
        g.setFont(font);
        g.setColor(new Color(color));
        g.drawString(text, x, y);
        g.dispose();
        return this;
    }

    public BufferedImageCreator text(int x, int y, int angle, String text, Color color, Font font) {
        return text(x, y, angle, text, color.getRGB(), font);
    }

    public BufferedImageCreator text(int x, int y, int angle, String text, int color, Font font) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Drawing text \"{}\" at ({}, {}) with color {} and font {}", text, x, y, color, font);
        }
        Graphics2D g = img.createGraphics();
        g.setFont(font);
        g.setColor(new Color(color));
        g.rotate(Math.toRadians(angle), x, y);
        g.drawString(text, x, y);
        g.dispose();
        return this;
    }

    public BufferedImageCreator image(int x, int y, BufferedImageCreator image) {
        return image(x, y, image.create());
    }

    public BufferedImageCreator image(int x, int y, Path path) throws IOException {
        return image(x, y, ImageIO.read(path.toFile()));
    }

    public BufferedImageCreator image(int x, int y, URL url) throws IOException {
        return image(x, y, ImageIO.read(url));
    }

    public BufferedImageCreator image(int x, int y, BufferedImage image) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Drawing image at ({}, {})", x, y);
        }
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                set(x + i, y + j, image.getRGB(i, j));
            }
        }
        return this;
    }

    public BufferedImageCreator image(int x, int y, BufferedImageCreator image, int width, int height) {
        return image(x, y, image.create().getSubimage(0, 0, width, height));
    }

    public BufferedImageCreator image(int x, int y, Path path, int width, int height, int hints) throws IOException {
        return image(x, y, (BufferedImage) ImageIO.read(path.toFile()).getScaledInstance(width, height, hints));
    }

    public BufferedImageCreator image(int x, int y, URL url, int width, int height, int hints) throws IOException {
        return image(x, y, (BufferedImage) ImageIO.read(url).getScaledInstance(width, height, hints));
    }

    public BufferedImageCreator image(int x, int y, BufferedImage image, int width, int height, int hints) {
        return image(x, y, (BufferedImage) image.getScaledInstance(width, height, hints));
    }

    public BufferedImageCreator resize(int width, int height) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Resizing image to {}x{}", width, height);
        }
        BufferedImage old = img;
        img = new BufferedImage(width, height, old.getType());
        int x, y;
        int ww = old.getWidth();
        int hh = old.getHeight();
        int[] ys = new int[height];
        for (y = 0; y < height; y++)
            ys[y] = y * hh / height;
        for (x = 0; x < width; x++) {
            int newX = x * ww / width;
            for (y = 0; y < height; y++) {
                int col = old.getRGB(newX, ys[y]);
                set(x, y, col);
            }
        }
        return this;
    }

    public BufferedImageCreator asType(int type) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Converting image to type {}", type);
        }
        BufferedImage old = img;
        img = new BufferedImage(old.getWidth(), old.getHeight(), type);
        for (int x = 0; x < old.getWidth(); x++) {
            for (int y = 0; y < old.getHeight(); y++) {
                set(x, y, old.getRGB(x, y));
            }
        }
        return this;
    }

    public BufferedImage create() {
        return img;
    }

    public static BufferedImageCreator fromRandom(int width, int height) {
        return fromRandom(width, height, DEFAULT_TYPE);
    }

    public static BufferedImageCreator fromRandom(int width, int height, int type) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating random image with width {} and height {} and type {}", width, height, type);
        }
        BufferedImageCreator creator = new BufferedImageCreator(width, height, type);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                creator.set(x, y, ThreadLocalRandom.current().nextInt(0, 0xFFFFFF));
            }
        }
        return creator;
    }

    public static BufferedImageCreator fromColor(int width, int height, Color color) {
        return fromColor(width, height, DEFAULT_TYPE, color);
    }

    public static BufferedImageCreator fromColor(int width, int height, int color) {
        return fromColor(width, height, DEFAULT_TYPE, color);
    }

    public static BufferedImageCreator fromColor(int width, int height, int type, Color color) {
        return fromColor(width, height, type, color.getRGB());
    }

    public static BufferedImageCreator fromColor(int width, int height, int type, int color) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating image ({}, {}) in type {} and color {}", width, height, type, color);
        }
        return new BufferedImageCreator(width, height, type).fill(color);
    }

    public static BufferedImageCreator fromImage(BufferedImage image, int type) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating image from image");
        }
        return new BufferedImageCreator(image.getWidth(), image.getHeight(), type).image(0, 0, image);
    }

    public static BufferedImageCreator fromImage(BufferedImage image) {
        return fromImage(image, image.getType());
    }

    public static BufferedImageCreator fromImage(Path path, int type) throws IOException {
        return fromImage(ImageIO.read(path.toFile()), type);
    }

    public static BufferedImageCreator fromImage(Path path) throws IOException {
        return fromImage(ImageIO.read(path.toFile()));
    }

    public static BufferedImageCreator fromImage(URL url, int type) throws IOException {
        return fromImage(ImageIO.read(url), type);
    }

    public static BufferedImageCreator fromImage(URL url) throws IOException {
        return fromImage(ImageIO.read(url));
    }

    public static BufferedImageCreator fromNothing(int width, int height) {
        return fromNothing(width, height, DEFAULT_TYPE);
    }

    public static BufferedImageCreator fromNothing(int width, int height, int type) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating empty image ({}, {}) in type {}", width, height, type);
        }
        return new BufferedImageCreator(width, height, type);
    }
}
