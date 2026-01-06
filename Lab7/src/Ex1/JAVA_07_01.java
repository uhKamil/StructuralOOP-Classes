// Kamil Wolbach (280161)
import java.util.Scanner;

class LCGNumberGenerator {
    private long currentSeed;

    private static final long LCG_M = 4294967296L;
    private static final long LCG_A = 214013L;
    private static final long LCG_C = 2531011L;

    public LCGNumberGenerator(long initialSeed) {
        this.currentSeed = initialSeed;
    }

    public long next() {
        this.currentSeed = (LCG_A * this.currentSeed + LCG_C) % LCG_M;
        return this.currentSeed;
    }
}

void main() {
    Scanner scanner = new Scanner(System.in);

    final int MIN_A = 10;
    final int MIN_B = 50;
    final int SIZE_A = 11;

    while (true) {
        System.out.print("Enter how many numbers I should generate: ");
        if (!scanner.hasNextInt()) break;
        int n = scanner.nextInt();
        if (n == 0) break;

        System.out.print("Enter the seed: ");
        long s = scanner.nextLong();

        LCGNumberGenerator generator = new LCGNumberGenerator(s);
        int sum = 0;

        for (int i = 0; i < n; i++) {
            long rawValue = generator.next();
//            int r = (int) (rawValue % 32); // weaker bits
            int r = (int) (rawValue >> 27); // stronger bits moved to 5 least significant positions

            if (r <= 10) r = r + MIN_A;
            else r = r + MIN_B - SIZE_A;

            System.out.println(r);
            sum += r;
        }

        double average = (double) sum / n;
        System.out.println("The average of the random number set is " + average);
    }
}
