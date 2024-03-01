package io.github.hunterherbst;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class PGM {

    private int width;
    private int height;
    private int[][] data;

    /**
     * Create a new PGM image
     * @param width the width of the image
     * @param height the height of the image
     */
    public PGM(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new int[width][height];
    }

    /**
     * Set the value of a pixel
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @param value the value to set
     */
    public void set(int x, int y, int value) {
        this.data[x][y] = value;
    }

    /**
     * Get the value of a pixel
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return the value of the pixel
     */
    public int get(int x, int y) {
        return this.data[x][y];
    }

    /**
     * Save the image to a PGM file
     * @param filename the name of the file to save
     * @return true if the save was successful, false otherwise
     */
    public boolean save(String filename) {
        try(FileOutputStream fos = new FileOutputStream(filename)){
            // write header
            String header = "P5\n" + this.width + " " + this.height + "\n255\n";
            fos.write(header.getBytes());

            // write data
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    fos.write((byte) this.data[x][y]);
                }
            }
        } catch (Exception e) {
            // if exception occurs, save is a failure
            return false;
        }

        // save is a success
        return true;
    }

    // save function for P2 format
    public boolean saveP2(String filename) {
        try(FileWriter fw = new FileWriter(filename)){
            // write header
            String header = "P2\n" + this.width + " " + this.height + "\n255\n";
            fw.write(header);

            // write data
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    fw.write((int) this.data[x][y] + " ");
                }
                fw.write("\n");
            }
        } catch (Exception e) {
            // if exception occurs, save is a failure
            return false;
        }

        // save is a success
        return true;
    }

    @Override
    public String toString() {
        // return the full data of the image, make line breaks at the end of each row
        String result = "";
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                result += this.data[x][y] + " ";
            }
            result += "\n";
        }
        return result;
    }

    // convert to bitmap
    public boolean saveAsBitmap(String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            // write header
            int padding = (4 - (this.width * 3) % 4) % 4;
            int size = 54 + (3 * this.width + padding) * this.height;
            fos.write(new byte[]{
                    0x42, 0x4D, // BM
                    (byte) size, (byte) (size >> 8), (byte) (size >> 16), (byte) (size >> 24), // size
                    0, 0, 0, 0, // reserved
                    54, 0, 0, 0, // offset
                    40, 0, 0, 0, // header size
                    (byte) this.width, (byte) (this.width >> 8), (byte) (this.width >> 16), (byte) (this.width >> 24), // width
                    (byte) this.height, (byte) (this.height >> 8), (byte) (this.height >> 16), (byte) (this.height >> 24), // height
                    1, 0, 24, 0, // planes, bpp
                    0, 0, 0, 0, // compression
                    0, 0, 0, 0, // image size
                    0, 0, 0, 0, // x pixels per meter
                    0, 0, 0, 0, // y pixels per meter
                    0, 0, 0, 0, // colors used
                    0, 0, 0, 0, // important colors
            });

            // write data
            for (int y = this.height - 1; y >= 0; y--) {
                for (int x = 0; x < this.width; x++) {
                    fos.write((byte) this.data[x][y]);
                    fos.write((byte) this.data[x][y]);
                    fos.write((byte) this.data[x][y]);
                }
                for (int p = 0; p < padding; p++) {
                    fos.write(0);
                }
            }
        } catch (Exception e) {
            // if exception occurs, save
            return false;
        }
        return true;
    }

    public void writeToPng(String filename){
        BufferedImage pngImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Set pixel values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int grayValue = data[x][y];
                pngImage.setRGB(x, y, grayValue << 16 | grayValue << 8 | grayValue);
            }
        }

        // Write the PNG file
        File outputFile = new File(filename);
        try {
            ImageIO.write(pngImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writePNGs(PGM[] pgms, String folderName){
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        for (int i = 0; i < pgms.length; i++) {
            pgms[i].writeToPng(folderName + "/image-" + i + ".png");
        }
    }
}