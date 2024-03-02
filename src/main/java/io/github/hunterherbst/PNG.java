package io.github.hunterherbst;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class to create PNG objects and manipulate individual pixels in the image before saving to a file.
 */
public class PNG {

    private int width;
    private int height;
    private Pixel[][] pixels;

    public PNG(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new Pixel[width][height];

        // Initialize all pixels to black
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x][y] = new Pixel(0, 0, 0, 1);
            }
        }
    }

    public void setPixel(int x, int y, Pixel p) {
        pixels[x][y] = p;
    }

    public Pixel getPixel(int x, int y) {
        return pixels[x][y];
    }

    public float[][] getRedChannel() {
        float[][] red = new float[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                red[x][y] = pixels[x][y].getR();
            }
        }
        return red;
    }

    public float[][] getGreenChannel() {
        float[][] green = new float[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                green[x][y] = pixels[x][y].getG();
            }
        }
        return green;
    }

    public float[][] getBlueChannel() {
        float[][] blue = new float[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                blue[x][y] = pixels[x][y].getB();
            }
        }
        return blue;
    }

    public float[][] getAlphaChannel() {
        float[][] alpha = new float[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                alpha[x][y] = pixels[x][y].getA();
            }
        }
        return alpha;
    }

    public void setPixels(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    public Pixel[][] getPixels() {
        return pixels;
    }

    public void setGreyscale(float[][] data) {
        this.setRedChannel(data);
        this.setGreenChannel(data);
        this.setBlueChannel(data);
    }

    public void setRedChannel(float[][] red) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x][y].setR(red[x][y]);
            }
        }
    }

    public void setGreenChannel(float[][] green) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x][y].setG(green[x][y]);
            }
        }
    }

    public void setBlueChannel(float[][] blue) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x][y].setB(blue[x][y]);
            }
        }
    }

    public void setAlphaChannel(float[][] alpha) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x][y].setA(alpha[x][y]);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void save(String filename) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                img.setRGB(x, y, pixels[x][y].getRGBA());
            }
        }
        try {
            ImageIO.write(img, "png", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage toBufferedImage() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                img.setRGB(x, y, pixels[x][y].getRGBA());
            }
        }
        return img;
    }


}