package io.github.hunterherbst;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class GIF {

    private static final int DEFAULT_DELAY = 5;
    private int width;
    private int height;
    private GifFrame[] frames;

    public GIF(int width, int height, int numFrames, int delay) {
        this.width = width;
        this.height = height;
        this.frames = new GifFrame[numFrames];

        for (int i = 0; i < numFrames; i++) {
            frames[i] = new GifFrame(width, height, delay);
        }
    }
    public GIF(int width, int height, int numFrames) {
        this(width, height, numFrames, DEFAULT_DELAY);
    }

    public GIF(int width, int height, GifFrame[] frames) {
        this.width = width;
        this.height = height;
        this.frames = frames;
    }

    public GIF(PNG[] pngs){
        this.width = pngs[0].getWidth();
        this.height = pngs[0].getHeight();
        this.frames = new GifFrame[pngs.length];
        for (int i = 0; i < pngs.length; i++) {
            frames[i] = new GifFrame(width, height, DEFAULT_DELAY);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    frames[i].set(x, y, pngs[i].getPixel(x, y).getRGBA());
                }
            }
        }
    }

    public void setFrame(int index, GifFrame frame) {
        frames[index] = frame;
    }

    public GifFrame getFrame(int index) {
        return frames[index];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumFrames() {
        return frames.length;
    }

    public GifFrame[] getFrames() {
        return frames;
    }

    /**
     * NOTE: THIS DOESN'T WORK PROPERLY. SAVE AS A PNG SEQUENCE AND STITCH IT YOURSELF WITH A TOOL LIKE FFmpeg.
     * Save the GIF to a file
     * @param filename the name of the file to save
     */
    public void save(String filename) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filename);
            GIFOutputStream gifOutputStream = new GIFOutputStream(outputStream);

            gifOutputStream.writeGifHeader(width, height);

            for (int i = 0; i < frames.length; i++) {
                gifOutputStream.writeGifFrame(frames[i], i == 0);
            }

            gifOutputStream.writeGifTrailer();

            gifOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsPNGSequence(String folderName) {
       File folder = new File(folderName);
       if (!folder.exists()) {
           folder.mkdir();
       }

         for (int i = 0; i < frames.length; i++) {
              frames[i].save(folderName + "/frame" + i + ".png");
         }
    }

    class GifFrame extends PNG {
        private int delay;

        public GifFrame(int width, int height, int delay) {
            super(width, height);
            this.delay = delay;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }
        public int getDelay() {
            return delay;
        }

        public void set(int x, int y, int rgba) {
            int r = (rgba >> 16) & 0xFF;
            int g = (rgba >> 8) & 0xFF;
            int b = rgba & 0xFF;
            int a = (rgba >> 24) & 0xFF;
            super.setPixel(x, y, new Pixel(r / 255f, g / 255f, b / 255f, a / 255f));
        }
    }

    class GIFOutputStream {

        // DEFAULT VALUES
        private static final int GLOBAL_COLOR_TABLE_FLAG = 0;       // 1 means there will be a GCT
        private static final int COLOR_RESOLUTION = 7;              // 7 means 8 bits per primary color
        private static final int SORT_FLAG = 0;                     // 1 means the GCT is sorted
        private static final int DEFAULT_X_POS = 0;                 // X position of the image
        private static final int DEFAULT_Y_POS = 0;                 // Y position of the image
        private static final boolean DEFUALT_INTERLACING = false;   // true if the image is interlaced

        private final OutputStream outputStream;

        public GIFOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        public byte[] createLocalColorTable(Pixel[][] pixels) {
            // Create a local color table for the image
            // This is a simplified implementation; actual color table creation requires more complex logic
            // Here, we simply add all unique pixel values to the color table
            List<Pixel> uniquePixels = new ArrayList<>();
            for (Pixel[] row : pixels) {
                for (Pixel pixel : row) {
                    if (!uniquePixels.contains(pixel)) {
                        uniquePixels.add(pixel);
                    }
                }
            }

            // Convert unique pixel values to byte array
            byte[] colorTable = new byte[uniquePixels.size() * 3];
            for (int i = 0; i < uniquePixels.size(); i++) {
                Pixel pixel = uniquePixels.get(i);
                colorTable[i * 3] = (byte) (pixel.getR() * 255);
                colorTable[i * 3 + 1] = (byte) (pixel.getG() * 255);
                colorTable[i * 3 + 2] = (byte) (pixel.getB() * 255);
            }

            return colorTable;
        }

        public void writeGifHeader(int width, int height) throws IOException {
            outputStream.write("GIF89a".getBytes());
            outputStream.write((byte) (width & 0xFF));
            outputStream.write((byte) ((width >> 8) & 0xFF));
            outputStream.write((byte) (height & 0xFF));
            outputStream.write((byte) ((height >> 8) & 0xFF));

            // Do Flags
            int gctByte = (GLOBAL_COLOR_TABLE_FLAG << 7) | (COLOR_RESOLUTION << 4);
            int sortByte = (SORT_FLAG << 7);

            outputStream.write((byte) (gctByte | sortByte));
            outputStream.write((byte) 0);
            outputStream.write((byte) 0);
        }

        public void writeGifFrame(GifFrame frame, boolean firstFrame) throws IOException {
            if(!firstFrame)
                outputStream.write(",".getBytes());

            int width = frame.getWidth();
            int height = frame.getHeight();
            outputStream.write((byte) (DEFAULT_X_POS & 0xFF));
            outputStream.write((byte) ((DEFAULT_X_POS >> 8) & 0xFF));
            outputStream.write((byte) (DEFAULT_Y_POS & 0xFF));
            outputStream.write((byte) ((DEFAULT_Y_POS >> 8) & 0xFF));
            outputStream.write((byte) (width & 0xFF));
            outputStream.write((byte) ((width >> 8) & 0xFF));
            outputStream.write((byte) (height & 0xFF));
            outputStream.write((byte) ((height >> 8) & 0xFF));

            // Write local color table
            byte[] colorTable = createLocalColorTable(frame.getPixels());
            outputStream.write((byte) (0x80 | colorTable.length / 3 - 1)); // 0x80 is the local color table flag
            outputStream.write(colorTable);

            outputStream.write((byte) (DEFUALT_INTERLACING ? 0x40 : 0x00)); // 0x07 is the size of the color table (2^8 = 256 colors

            // Do Flags (I think this is just for the initial header of the gif)
//            int gctByte = (GLOBAL_COLOR_TABLE_FLAG << 7) | (COLOR_RESOLUTION << 4);
//            int sortByte = (SORT_FLAG << 7);
//
//            outputStream.write((byte) (gctByte | sortByte));
//            outputStream.write((byte) 0);
//            outputStream.write((byte) 0);


//            outputStream.write((byte) 0x08);
            LZWEncoder encoder = new LZWEncoder(width, height, frame.getPixels(), 8);
            encoder.encode(outputStream);
            outputStream.write((byte) 0x00); // Block terminator

//            outputStream.write((byte) 0x21);                                // Extension introducer
//            outputStream.write((byte) 0xF9);                                // Graphic control label
//            outputStream.write((byte) 0x04);                                // Block size
//            outputStream.write((byte) 0x05);                                // Flags (e.g., dispose = 1, transparent = 0)
//            outputStream.write((byte) (frame.getDelay() & 0xFF));           // Delay time
//            outputStream.write((byte) ((frame.getDelay() >> 8) & 0xFF));
//            outputStream.write((byte) 0);                                   // Transparent color index
//            outputStream.write((byte ) 0);                                  // Block terminator

            // Graphic Control Extension
            outputStream.write((byte) 0x21); // Extension Introducer
            outputStream.write((byte) 0xF9); // Graphic Control Label
            outputStream.write((byte) 0x04); // Block Size
            int packedField = 0x00; // Initialize with no flags set
            packedField |= 0x08; // Set disposal method to "no disposal"
            packedField |= ((frame.getDelay() / 10) & 0xFF); // Set delay time
            packedField |= (((frame.getDelay() / 10) >> 8) & 0xFF);
            outputStream.write((byte) packedField);
            outputStream.write((byte) 0x00); // Transparent color index
            outputStream.write((byte) 0x00); // Block terminator
        }

        public void writeGifTrailer() throws IOException {
            outputStream.write(";".getBytes());
        }

        public void close() throws IOException {
            outputStream.close();
        }

        class LZWEncoder {
            private final int width;
            private final int height;
            private final Pixel[][] pixels;
            private final int initialCodeSize;
            private final List<Integer> encodedData;

            public LZWEncoder(int width, int height, Pixel[][] pixels, int initialCodeSize) {
                this.width = width;
                this.height = height;
                this.pixels = pixels;
                this.initialCodeSize = initialCodeSize;
                this.encodedData = new ArrayList<>();
            }

            public void encode(OutputStream outputStream) throws IOException {
                // Perform LZW compression on pixels and add encoded data to encodedData list
                // This is a simplified implementation; actual LZW encoding requires more complex logic
                // Here, we simply add all pixel values to the encoded data list without compression
                for (Pixel[] row : pixels) {
                    for (Pixel pixel : row) {
                        encodedData.add(pixel.getRGBA());
                    }
                }

                for(int data : encodedData) {
                    outputStream.write(data & 0xFF);
                    outputStream.write((data >> 8) & 0xFF);
                    outputStream.write((data >> 16) & 0xFF);
                    outputStream.write((data >> 24) & 0xFF);
                }
            }
        }

    }
}
