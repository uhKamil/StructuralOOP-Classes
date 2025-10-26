import static java.lang.IO.*;  //including package to be able to use simple print()

//clear the terminal window

void clrscr() {
    String CLEAR_SCREEN = "\u001b[2J";
    print(String.format(CLEAR_SCREEN));
}

void cursor_hide() {
    String HIDE_CURSOR = "\u001b[?25l";
    print(String.format(HIDE_CURSOR));
}

void cursor_show() {
    String SHOW_CURSOR = "\u001b[?25h";
    print(String.format(SHOW_CURSOR));
}

void delay(int msec) {
    try {
        Thread.sleep(msec);
    } catch (InterruptedException e) {
    }
}

// procedures
long factorialIterative(long n) {
    long a = 1;
    for (int i = 2; i <= n; i++) {
        a *= i;
    }
    return a;
}

long factorialRecursion(long n) {
    if (n == 0) {
        return 1;
    }
    return n * factorialRecursion(n - 1);
}

long factorialTR(long n, long a) {
    if (n <= 0) {
        return a;
    }
    return factorialTR(n - 1, n * a);
}

long computingExperiment(int n, int method, int times, long t0) {
    for (int i = 1; i <= times; i++) {
        if (method == 1) {
            factorialIterative(n);
        } else if (method == 2) {
            factorialRecursion(n);
        } else {
            factorialTR(n, 1);
        }
    }
    return (System.nanoTime() - t0) ;
}


void main() {
    Scanner scanner = new Scanner(System.in);
    int number;
    long t0;
    long t1;
    print("The Factorial Experiment\n");
    while (true) {
        print("\nEnter the number and I'll compute its factorial: ");
        number = scanner.nextInt();
        cursor_hide();
        print("Computing with the iterative method\n");
        t0 = System.nanoTime();
        long result1 = factorialIterative(number);
        t1 = System.nanoTime();
        print("The result is " + result1 + ". The time taken to compute this was " + (t1 - t0) + " ns.\n");
        print("Computing with the recursive method\n");
        t0 = System.nanoTime();
        long result2 = factorialRecursion(number);
        t1 = System.nanoTime();
        print("The result is " + result2 + ". The time taken to compute this was " + (t1 - t0) + " ns.\n");
        print("Computing with the tail recursive method\n");
        t0 = System.nanoTime();
        long result3 = factorialTR(number, 1);
        t1 = System.nanoTime();
        print("The result is " + result3 + ". The time taken to compute this was " + (t1 - t0) + " ns.\n");
        print("\nMeasuring time for 1000 computations\n");
        for (int m = 1; m <= 3; m++) {
            t0 = System.nanoTime();
            long t = computingExperiment(number, m, 1000, t0);
            print("Using method " + m + " the time of computation for 1000 computations was " + String.format("%.6f", (double) t / 1000000) + " ms.\n");
        }
        print("\nMeasuring time for 10000 computations\n");
        for (int m = 1; m <= 3; m++) {
            t0 = System.nanoTime();
            long t = computingExperiment(number, m, 10000, t0);
            print("Using method " + m + " the time of computation for 10000 computations was " + String.format("%.6f", (double) t / 1000000) + " ms.\n");
        }
    }
}