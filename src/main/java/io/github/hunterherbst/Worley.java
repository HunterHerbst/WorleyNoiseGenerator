package io.github.hunterherbst;

public class Worley {

    private int width;
    private int height;
    private float[][] data;
    private int numPoints;
    private int[][] points;

    public Worley(int width, int height, int numPoints) {
        this.width = width;
        this.height = height;
        this.numPoints = numPoints;
        this.points = new int[numPoints][2];
        this.data = new float[width][height];
        for (int i = 0; i < numPoints; i++) {
            this.points[i][0] = (int) (Math.random() * width);
            this.points[i][1] = (int) (Math.random() * height);
        }
        this.generate();
    }

    // create new worley from just data set
    public Worley(float[][] data, int numPoints, int[][] points) {
        this.width = data.length;
        this.height = data[0].length;
        this.numPoints = numPoints;
        this.points = points;
        this.data = data;
    }

    private static float distance(int x1, int y1, int x2, int y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public void generate() {
        // generate the worley noise by calculating the distance from each point to each pixel
        // and setting the pixel to the closest point
        // the range for these numbers should be 0 to 1
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                float minDistance = distance(x, y, this.points[0][0], this.points[0][1]);
                for (int i = 1; i < this.numPoints; i++) {
                    float d = distance(x, y, this.points[i][0], this.points[i][1]);
                    if (d < minDistance) {
                        minDistance = d;
                    }
                }
                this.data[x][y] = minDistance;
            }
        }

        // normalize the data
        float max = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (this.data[x][y] > max) {
                    max = this.data[x][y];
                }
            }
        }
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.data[x][y] /= max;
            }
        }
    }

    public void invert() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.data[x][y] = 1 - this.data[x][y];
            }
        }
    }

    public boolean saveAsPGM(String filename) {
        // use PGM object and write data to that to save
        PGM pgm = new PGM(this.width, this.height);
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if(this.data[x][y] > 1) {
                    System.err.println("Error: data out of range");
                }
                pgm.set(x, y, (int) (this.data[x][y] * 255));
            }
        }
        return pgm.save(filename);
    }

    public boolean saveAsPGM(String filename, boolean invert) {
        if(invert) {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    this.data[x][y] = 1 - this.data[x][y];
                }
            }
        }
        return saveAsPGM(filename);
    }

    public PGM getPGM() {
        PGM pgm = new PGM(this.width, this.height);
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if(this.data[x][y] > 1) {
                    System.err.println("Error: data out of range");
                }
                pgm.set(x, y, (int) (this.data[x][y] * 255));
            }
        }
        return pgm;
    }
}
