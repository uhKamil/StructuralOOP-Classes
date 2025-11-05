import static java.lang.IO.*;

static int LCG(int a, int s, int c, int m) {
    // Inspired by the Linear congruential generator (LCG)
    return (a * s + c) % m;
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
    double angle = a % 360;
    int result = 1; // 0 - negative SumCurr; 1 - positive SumCurr
//    if (180 - angle > 0 && (angle > 90 && angle < 180)) {
//        // cos(180deg - A) = -cos(A)
//        println("v1");
//        angle = 180 - angle;
//        result = 0;
//    }    
//    else if (180 + angle < 0 && (angle > -180 && angle < -90)) {
//        // cos(180deg - A) = -cos(A)
//        println("v2");
//        angle = 180 - angle;
//        result = 0;
//    }
//    else if (degToRad(a) > Math.PI) {
//        // cos(180deg + A) = -cos(A)
//        println("v3");
//        angle -= 180;
//        result = 0;
//    }
//    else if (degToRad(a) < -Math.PI) {
//        // cos(180deg + A) = -cos(A)
//        println("v4");
//        angle += 180;
//        result = 0;
//    }
    double SumPrev = 0;
    double SumCurr = 1;
    int sign = -1;
    int e = 2;
    while (Math.abs(SumPrev - SumCurr) > eps) {
        SumPrev = SumCurr;
        SumCurr += sign * Math.pow(degToRad(angle), e) / Factorial(e);
        sign *= -1;
        e += 2;
    }
    if (result == 0) {
        return -SumCurr;
    }
    return SumCurr;
}

double[] TestCos(int N) {
    int initial = LCG(12551, 124150, 60, 100000);
    long TotalTime0 = 0;
    long TotalTime1 = 0;
    double averageDiff = 0;
    for (int i = 1; i <= N; i++) {
        initial = LCG(12551, initial, 60, 100000);
        double number = (double) (20 * initial) / 100000 - 10;
        long t0 = System.nanoTime();
        double result0 = cosTaylor(number * 180 / Math.PI, 0.0001);
        TotalTime0 += (System.nanoTime() - t0);
        t0 = System.nanoTime();
        double result1 = Math.cos(number);
        TotalTime1 += (System.nanoTime() - t0);
//        if (i % 10 == 0) {println(result0 + " " + result1);}
        if (i < 100) {println(result0 + " " + result1 + " " + Math.abs(result1 - result0));}
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
    print(cosTaylor(angle, epsilon) + " " + Math.cos(degToRad(angle)));
  // The testing part
    println("\nNow let's test this computation for 10 thousand random values and compare performance with Math.cos(x)");
    for (int i = 1; i <= 10; i++) {
        double[] testResult = TestCos(10000);
        println(testResult[0] + " microseconds for cosTaylor() method. " + testResult[1] + " microseconds for the" +
                " Math.cos(x) method. The average difference between results is " + testResult[2]);
    }
}
