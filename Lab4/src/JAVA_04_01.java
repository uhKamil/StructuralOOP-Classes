import static java.lang.IO.*;

static int LCG(int a, int s, int c, int m) {
    // Inspired by the Linear congruential generator (LCG)
    return (a * s + c) % m;
}


static double PRNG(int n, int seed) {
    int initial_i = LCG(1000, seed, 5, 256424);
    print(initial_i + "\n");
    // Calculator for the average of an array
    double sum = initial_i;
    double average = 0;
    for (int i = 1; i <= n - 1; i++) {
        int number = LCG(1000, initial_i, 5, 256424);
        print(number + "\n");
        initial_i = number;
        sum += number;
        average = sum / (i + 1);
    }
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
