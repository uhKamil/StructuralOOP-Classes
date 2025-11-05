import static java.lang.IO.*;

private static final int N = 10000; // number of tests

public static class LCGNumberGenerator {

    private long currentSeed;

    private static final long LCG_M = 4294967296L;
    private static final long LCG_A = 214013L;
    private static final long LCG_C = 2531011L;

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

    public double PRNG(int limit) {
        long newState = LCG();
        return newState % limit;
    }
}

double degToRad(double deg) {
    return (deg * Math.PI / 180) % (2 * Math.PI);
}

int Factorial(int a) {
    int s = 1;
    for (int i = 2; i <= a; i++) {
        s *= i;
    }
    return s;
}

double cosTaylor(double a, double eps) {
    // Normalization to the segment [-180, 180)
    double angle = (a + 180) % 360; // [-360, 360)
    angle += 360; // [0, 720)
    angle %= 360; // [0, 360)
    angle -= 180; // [-180, 180)
    int resultMultiplier = 1;
    // Symmetry of the cosine function cos(-x) = cos(x)
    if (angle < 0) angle = -angle;
    // Trigonometrical property cos(180-x) = -cos(x)
    if (angle > 90) {
        angle = 180 - angle;
        resultMultiplier = -1;
    }
    double SumPrev = 0;
    double SumCurr = 1;
    int termSign = -1;
    int e = 2;
    double rad = degToRad(angle);
    while (Math.abs(SumPrev - SumCurr) > eps) {
        SumPrev = SumCurr;
        SumCurr += termSign * Math.pow(rad, e) / Factorial(e);
        termSign *= -1;
        e += 2;
    }
    return resultMultiplier * SumCurr;
}


double[] TestCos(int N) {
    LCGNumberGenerator generator = new LCGNumberGenerator(12345L);
    long TotalTime0 = 0;
    long TotalTime1 = 0;
    double averageDiff = 0;
    for (int i = 1; i <= N; i++) {
        double number = (generator.PRNG(1000) / 1000) - 0.5;
        long t0 = System.nanoTime();
        double result0 = cosTaylor(number * 180 / Math.PI, 0.0001);
        TotalTime0 += (System.nanoTime() - t0);
        t0 = System.nanoTime();
        double result1 = Math.cos(number);
        TotalTime1 += (System.nanoTime() - t0);
        averageDiff += Math.abs(result1 - result0);
    }
    averageDiff /= N;
    return new double[]{(double) TotalTime0 / 1000, (double) TotalTime1 / 1000, averageDiff};
}

void main() {
    Scanner scanner = new Scanner(System.in);
    print("Enter the desired angle in degrees: ");
    double angle = scanner.nextDouble();
    print("Enter the desired accuracy: ");
    double epsilon = scanner.nextDouble();
    double TaylorResult = cosTaylor(angle, epsilon);
    double LibraryResult = Math.cos(degToRad(angle));
    print(TaylorResult + " " + LibraryResult + " " + Math.abs(TaylorResult - LibraryResult));
    // The testing part
    println("\nNow let's test this computation for 10 thousand random values and compare performance with Math.cos(x)");
    double[] testResult = TestCos(N);
    println(testResult[0] + " microseconds for cosTaylor() method. " + testResult[1] + " microseconds for the" +
            " Math.cos(x) method.\nThe average difference between results is " + testResult[2]);
}
