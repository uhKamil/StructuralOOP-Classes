import static java.lang.IO.*;

static long LCG(long a, long s, long c, long m) {
    // Linear congruential generator (LCG)
    return (a * s + c) % m;
}


static double PRNG(int n, int seed) {
    long initial_i = LCG(1242150, seed, 412510221, 11) + 10;
    // Calculate the average of an array
    double sum = initial_i;
    double average = 0;
    for (int i = 1; i <= n/2; i++) {
        long number = LCG(1242150, initial_i, 412510221, 11) + 10;
        long number2 = LCG(1242150, initial_i, 412510221, 21) + 50;
        print(number + "\n");
        print(number2 + "\n");
        initial_i = number;
        sum += (number + number2);
        if (i == n/2 && n % 2 == 1) {
            number2 = LCG(1242150, initial_i, 412510221, 21) + 50;
            print(number2 + "\n");
            sum += number2;
        }
    }
    average = sum / n;
    return average;
}

void main() {
    Scanner scanner = new Scanner(System.in);
    int N;
    int s;
    while (true) {
        print("Enter how many numbers I should generate: ");
        N = scanner.nextInt();
        if (N == 0) {
            break;
        }
        print("Enter the seed: ");
        s = scanner.nextInt();
        print("The average of the generated number is " + PRNG(N, s) + "\n");
    }
}
