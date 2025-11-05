import static java.lang.IO.*;

static int LCG(int a, int s, int c, int m) {
    // Inspired by the Linear congruential generator (LCG)
    return (a * s + c) % m;
}


// This one generates the array, the main version won't 
static int[] PRNG(int n, int seed) {
    int[] values = new int[n];
    int initial_i = LCG(1000, seed, 5, 256424);
    values[0] = initial_i;
    // Calculator for the average of an array (it doesn't use the info about the whole array)
    double sum = values[0]; double average = 0;
    for (int i = 1; i <= values.length-1; i++) {
        values[i] = LCG(1000, initial_i, 5, 256424);
        initial_i = values[i];
        sum += values[i];
        average = sum / (i+1);
    }
    print("The average of the array is " + average + "\n");
    return values;
}

void main() {
    Scanner scanner = new Scanner(System.in);
    int N; int s;
    while (true) {
        print("Enter how many numbers I should generate: ");
        N = scanner.nextInt();
        if (N == 0) {
            break;
        }
        print("Enter the seed: ");
        s = scanner.nextInt();
        print(Arrays.toString(PRNG(N, s)) + "\n");
    }
}
