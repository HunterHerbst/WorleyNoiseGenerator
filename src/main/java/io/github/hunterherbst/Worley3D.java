package io.github.hunterherbst;

public class Worley3D {

    private int width;
    private int height;
    private int depth;
    private float[][][] data;
    private int numPoints;
    private int[][] points;

    public Worley3D(int width, int height, int depth, int numPoints) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.numPoints = numPoints;
        this.points = new int[numPoints][3];
        this.data = new float[width][height][depth];
        for (int i = 0; i < numPoints; i++) {
            this.points[i][0] = (int) (Math.random() * width);
            this.points[i][1] = (int) (Math.random() * height);
            this.points[i][2] = (int) (Math.random() * depth);
        }
        this.generate();
    }

    private static float distance(int x1, int y1, int z1, int x2, int y2, int z2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
    }

    public void generate() {
        // generate the worley noise by calculating the distance from each point to each pixel
        // and setting the pixel to the closest point
        // the range for these numbers should be 0 to 1
        for (int z = 0; z < this.depth; z++) {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    float minDistance = distance(x, y, z, this.points[0][0], this.points[0][1], this.points[0][2]);
                    for (int i = 1; i < this.numPoints; i++) {
                        float d = distance(x, y, z, this.points[i][0], this.points[i][1], this.points[i][2]);
                        if (d < minDistance) {
                            minDistance = d;
                        }
                    }
                    this.data[x][y][z] = minDistance;
                }
            }
        }

        // normalize the data
        float max = 0;
        for (int z = 0; z < this.depth; z++) {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    if (this.data[x][y][z] > max) {
                        max = this.data[x][y][z];
                    }
                }
            }
        }
        for (int z = 0; z < this.depth; z++) {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    this.data[x][y][z] /= max;
                }
            }
        }
    }

    public void invert() {
        for (int z = 0; z < this.depth; z++) {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    this.data[x][y][z] = 1 - this.data[x][y][z];
                }
            }
        }
    }



    public PGM[] getPGMs() {
        // each layer of depth gets its own PGM of Width x Height
        PGM[] pgms = new PGM[this.depth];
        for (int z = 0; z < this.depth; z++) {
            pgms[z] = new PGM(this.width, this.height);
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    pgms[z].set(x, y, (int) (this.data[x][y][z] * 255));
                }
            }
        }
        return pgms;
    }
}
