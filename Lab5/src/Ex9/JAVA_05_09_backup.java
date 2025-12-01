import java.util.Random;
import java.security.SecureRandom;

import static term.term.*;

static class Matrix {
    final double numberOfGenerations;
    int width, height;
    int[][] matrix;
    Random random;
    
    long currentSeed;
    boolean reseed;

    Matrix(int width, int height, int seed, double n, boolean useSecure, boolean reseed) {
        this.width = width;
        this.height = height;
        this.matrix = new int[height][width]; // automatically sets all values to 0
        this.numberOfGenerations = n;
        this.random = new Random(seed);

        this.reseed = reseed;
        this.currentSeed = seed;

        if (useSecure) {
            this.random = new SecureRandom();
        } else {
            this.random = new Random(seed);
        }
    }

    public void generate2DPoint() {
        int x, y;
        if (reseed) {
            random.setSeed(currentSeed++);
            x = random.nextInt(0, width);
            random.setSeed(currentSeed++);
            y = random.nextInt(0, height);
        }
        else {
            x = random.nextInt(0, width);
            y = random.nextInt(0, height);
        }
        matrix[y][x] += 1;
    }

    public long getMax() {
        long max = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix[y][x] > max) max = matrix[y][x];
            }
        }
        return max;
    }

    public long getMin() {
        long min = Integer.MAX_VALUE;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix[y][x] < min) min = matrix[y][x];
            }
        }
        return min;
    }

    public void drawMatrix() {
        long maxVal = getMax();
        long minVal = getMin();

        if (maxVal == minVal) maxVal++; // so we don't get ArithmeticException (zero division error)

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                long value = matrix[y][x];
                int green = (int) (255.0 * ((value - minVal) / (double) (maxVal - minVal)));
                
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                
                setfgcolor_rgb(10, green, 10);
                gotoxy(x + 1, y + 1);
                write('█');
            }
        }
    }
}

void main() {
    // Part A: Basic visualization of randomness //
    // Matrix A: Random (LCG)
    Matrix m1 = new Matrix(100, 30, 123456, 5E6, false, false);
    for (int i = 1; i <= m1.numberOfGenerations; i++) {
        m1.generate2DPoint();
    }
    clrscr();
    m1.drawMatrix();
    // Matrix 2: Random with reseeding
    clrscr();
    Matrix m2 = new Matrix(100, 30, 123456, 5E6, false, true);
    for (int i = 1; i <= m2.numberOfGenerations; i++) {
        m2.generate2DPoint();
    }
    m2.drawMatrix();
    // Matrix 3: Secure random with reseeding
    clrscr();
    Matrix m3 = new Matrix(100, 30, 123456, 1E5, true, true);
    for (int i = 1; i <= m3.numberOfGenerations; i++) {
        m3.generate2DPoint();
    }
    m3.drawMatrix();
}