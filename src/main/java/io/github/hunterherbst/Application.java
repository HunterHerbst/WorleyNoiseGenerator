package io.github.hunterherbst;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Worley Noise Generator!");
        System.out.println("Please enter the width of the image: ");
        int width = scan.nextInt();
        System.out.println("Please enter the height of the image: ");
        int height = scan.nextInt();
        System.out.println("Please enter the depth of the image: ");
        int depth = scan.nextInt();
        System.out.println("Please enter the number of points: ");
        int numPoints = scan.nextInt();
        scan.close();


        System.out.println("Generating 3D Worley noise...");
        Worley3D w = new Worley3D(width, height, depth, numPoints);
        w.generate();
        System.out.println("Saving 3D Worley noise...");
        PGM[] pgms = w.getPGMs();
        PGM.writePNGs(pgms, "worleyGif");
    }
}