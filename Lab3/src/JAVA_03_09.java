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
long fibonacci_iterative(long n) {
    long result = 0;
    long f0 = 0;
    long f1 = 1;
    if (n == 0) {
        return f0;
    } else if (n == 1) {
        return f1;
    }
    for (int i = 2; i <= n; i++) {
        result = f0 + f1;
        f0 = f1;
        f1 = result;
    }
    return result;
}

long fibonacci_recursive(long n) {
    if (n == 0) {
        return 0;
    } else if (n == 1) {
        return 1;
    }
    return fibonacci_recursive(n - 1) + fibonacci_recursive(n - 2);
}

long fibonacci_TR(long n, long prev, long curr) {
    if (n == 0) {
        return prev;
    }
    return fibonacci_TR(n - 1, curr, prev + curr);
}

long computingExperiment(int n, int method, int times, long t0) {
    for (int i = 1; i <= times; i++) {
        if (method == 1) {
            fibonacci_iterative(n);
        } else if (method == 2) {
            fibonacci_recursive(n);
        } else {
            fibonacci_TR(n, 0, 1);
        }
    }
    return (System.nanoTime() - t0) ;
}

void main() {
    Scanner scanner = new Scanner(System.in);
    int number;
    long t0;
    long t1;
    print("The Fibonacci Experiment\n");
    while (true) {
        print("\nEnter the number and I'll compute the element of Fibonacci sequence: ");
        number = scanner.nextInt();
        cursor_hide();
        print("Computing with the iterative method\n");
        t0 = System.nanoTime();
        long result1 = fibonacci_iterative(number);
        t1 = System.nanoTime();
        print("The result is " + result1 + ". The time taken to compute this was " + (t1 - t0) + " ns.\n");
        print("Computing with the recursive method\n");
        t0 = System.nanoTime();
        long result2 = fibonacci_recursive(number);
        t1 = System.nanoTime();
        print("The result is " + result2 + ". The time taken to compute this was " + (t1 - t0) + " ns.\n");
        print("Computing with the tail recursive method\n");
        t0 = System.nanoTime();
        long result3 = fibonacci_TR(number, 0, 1);
        t1 = System.nanoTime();
        print("The result is " + result3 + ". The time taken to compute this was " + (t1 - t0) + " ns.\n");
        print("\nMeasuring time for 10 computations\n");
        for (int m = 1; m <= 3; m++) {
            t0 = System.nanoTime();
            long t = computingExperiment(number, m, 10, t0);
            print("Using method " + m + " the time of computation for 10 computations was " + String.format("%.6f", (double) t / 1000000) + " ms.\n");
        }
        print("\nMeasuring time for 1000 computations\n");
        t0 = System.nanoTime();
        long t = computingExperiment(number, 1, 1000, t0);
        print("Using method 1 the time of computation for 1000 computations was " + String.format("%.6f", (double) t / 1000000) + " ms.\n");
        t0 = System.nanoTime();
        t = computingExperiment(number, 3, 1000, t0);
        print("Using method 3 the time of computation for 1000 computations was " + String.format("%.6f", (double) t / 1000000) + " ms.\n");
    }
}