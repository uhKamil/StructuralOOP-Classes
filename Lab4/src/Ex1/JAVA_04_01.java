import java.util.Scanner;
import static java.lang.IO.*;

public static class LCGNumberGenerator {

    private long currentSeed;

    private static final long LCG_M = 4294967296L;
    private static final long LCG_A = 214013L;
    private static final long LCG_C = 2531011L;

    private static final int MIN_A = 10;
    private static final int MAX_A = 20;
    private static final int MIN_B = 50;
    private static final int MAX_B = 70;

    private static final int Size_A = MAX_A - MIN_A + 1;
    private static final int Size_B = MAX_B - MIN_B + 1;
    private static final int Size = Size_A + Size_B;

    public LCGNumberGenerator(long initialSeed) {
        this.currentSeed = initialSeed;
    }

    /**
     * Based on Linear congruential generator (LCG). It generates the next seed
     * The algorithm is given as such: X_n+1 = (A * X_n + C) mod M.
     *
     * @return The next seed (state) in the LCG sequence.
     */
    private long LCG() {
        // Linear congruential generator (LCG)
        this.currentSeed = (LCG_A * this.currentSeed + LCG_C) % LCG_M;
        return this.currentSeed;
    }

    public void PRNG(int n) {
        int Sum = 0;
        for (int i = 1; i <= n; i++) {
            long newState = LCG();
            int r = (int) (newState % 32);

            if (r <= 10) {
                r = r + MIN_A;
            } else {
                r = r + MIN_B - Size_A;
            }
            println(r);
            Sum += r;
        }
        double average = (double) Sum / n;
        println("The average of the random number set is " + average);
    }
}

    void main() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            print("Enter how many numbers I should generate: ");
            int n = scanner.nextInt();
            if (n == 0) {
                break;
            }
            print("Enter the seed: ");
            long s = scanner.nextLong();

            LCGNumberGenerator generator = new LCGNumberGenerator(s);
            generator.PRNG(n);
        }
    }