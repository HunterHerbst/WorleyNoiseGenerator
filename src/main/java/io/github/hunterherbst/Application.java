package io.github.hunterherbst;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        // Default packaged code in the JAR
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Welcome to Worley Noise Generator!");
//        System.out.println("Please enter the width of the image: ");
//        int width = scan.nextInt();
//        System.out.println("Please enter the height of the image: ");
//        int height = scan.nextInt();
//        System.out.println("Please enter the depth of the image: ");
//        int depth = scan.nextInt();
//        System.out.println("Please enter the number of points: ");
//        int numPoints = scan.nextInt();
//        scan.close();
//
//
//        System.out.println("Generating 3D Worley noise...");
//        Worley3D w = new Worley3D(width, height, depth, numPoints);
//        w.generate();
//        System.out.println("Saving 3D Worley noise...");
//        PGM[] pgms = w.getPGMs();
//        PGM.savePNGSequence(pgms, "worleyGif");

        // Example of how to use the Worley class
        Worley w = new Worley(100, 100, 10);
        w.getPGM().savePNG("worleyWrap.png");
    }
}