// Kamil Wolbach (280161)
import java.util.Random;
import java.security.SecureRandom;
import static term.term.*;

static final int MODE_STANDARD = 0;
static final int MODE_RESEED_LCG = 1;
static final int MODE_SECURE = 2;
static final int MODE_RESEED_SECURE = 3;

static int[][] RandMtx(int width, int height, double n, int mode) {
    int[][] matrix = new int[height][width]; // automatically sets all values to 0

    Random rand;
    long k = 123456;

    if (mode == MODE_SECURE || mode == MODE_RESEED_SECURE) {
        rand = new SecureRandom();
    } else {
        rand = new Random(123456);
    }

    for (int i = 0; i < n; i++) {
        int x, y;

        if (mode == MODE_STANDARD || mode == MODE_SECURE) {
            x = rand.nextInt(width);
            y = rand.nextInt(height);
        } else {
            rand.setSeed(k++);
            x = rand.nextInt(width);
            rand.setSeed(k++);
            y = rand.nextInt(height);
        }

        if (x < width && y < height) {
            matrix[y][x]++;
        }
    }

    return matrix;
}

static void visualizeMatrix(int[][] matrix) {
    int height = matrix.length;
    int width = matrix[0].length;

    double minVal = Integer.MAX_VALUE;
    double maxVal = 0;

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            if (matrix[y][x] < minVal) minVal = matrix[y][x];
            if (matrix[y][x] > maxVal) maxVal = matrix[y][x];
        }
    }

    if (maxVal == minVal) maxVal++; // so we don't get ArithmeticException (zero division error)

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            long value = matrix[y][x];

            // Scaling function
            int green = (int) (255.0 * ((value - minVal) / (maxVal - minVal)));

            // Narrowing down to 0-255
            if (green < 0) green = 0;
            if (green > 255) green = 255;

            setfgcolor_rgb(10, green, 10);
            gotoxy(x + 1, y + 1);
            write('█');
        }
    }

    setfgcolor_rgb(255, 255, 255);
    gotoxy(1, height + 2);
}


void main() {
    int width = 100;
    int height = 30;

    // Part A: Basic visualization of randomness //
    // Matrix 1: Standard Random
    clrscr();
    cursor_hide();
    int[][] matrixA = RandMtx(width, height, 5000000, MODE_STANDARD);

    clrscr();
    visualizeMatrix(matrixA);
    delay(3000);

    // Matrix 2: LCG with Reseed (Coordinate Test)
    clrscr();
    int[][] matrixB = RandMtx(width, height, 5000000, MODE_RESEED_LCG);

    clrscr();
    visualizeMatrix(matrixB);
    delay(3000);

    // Matrix 3: SecureRandom
    clrscr();
    int[][] matrixC = RandMtx(width, height, 100000, MODE_SECURE);

    clrscr();
    visualizeMatrix(matrixC);
    delay(3000);

    // Matrix 4: SecureRandom with Reseed (Coordinate Test)
    clrscr();
    int[][] matrixD = RandMtx(width, height, 100000, MODE_RESEED_SECURE);

    clrscr();
    visualizeMatrix(matrixD);
}