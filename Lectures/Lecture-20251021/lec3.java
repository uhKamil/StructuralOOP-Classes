public class lec3 {
    // Lecture3_Ex_1
    public static class NoDecomposition {
        static void main(String[] args) {
            int[] arr = {1, 2, 3, 4, 5};
            int sum = 0;
            for (int i : arr) {
                sum += i;
            }
            System.out.println(sum);
        }
    }

    public static class ProblemDecompositionExample {
        static void main(String[] args) {
            int[] numbers = {10, 20, 30, 40, 50};

            // The problem is decomposed into smaller parts (functions)
            int sum = calculateSum(numbers);
            double average = calculateAverage(sum, numbers.length);
            System.out.println("Average = " + average);
        }

        // Function 1: calculates the sum
        public static int calculateSum(int[] arr) {
            int total = 0;
            for (int value : arr) {
                total += value;
            }
            return total;
        }

        // Function 2: calculates the average
        public static double calculateAverage(int sum, int count) {
            return (double) sum / count;
        }
    }

    // Lecture3_Ex_2
    // Example: Factorial using iteration
    public static class IterationExample {
        static void main(String[] args) {
            int n = 5;
            System.out.println("Factorial of " + n + " = " + factorialIterative(n));
        }

        // Iterative function to calculate factorial
        public static int factorialIterative(int n) {
            int result = 1;
            for (int i = 1; i <= n; i++) {
                result *= i;
            }
            return result;
        }
    }

    // Lecture3_Ex_3
    // Example: Factorial using recursion
    public static class RecursionExample {
        static void main(String[] args) {
            int n = 5;
            System.out.println("Factorial of " + n + " = " + factorialRecursive(n));
        }

        // Recursive function to calculate factorial
        public static int factorialRecursive(int n) {
            if (n == 0) {
                return 1; // base case
            } else {
                return n * factorialRecursive(n - 1); // recursive call
            }
        }
    }

    // Lecture3_Ex_4
    // Example: Sum of digits using both iteration and recursion
    public static class SumOfDigitsExample {
        static void main(String[] args) {
            int number = 12345;
            System.out.println("Iterative sum: " + sumDigitsIterative(number));
            System.out.println("Recursive sum: " + sumDigitsRecursive(number));
        }

        // Function using iteration
        public static int sumDigitsIterative(int n) {
            int sum = 0;
            while (n > 0) {
                sum += n % 10;
                n /= 10;
            }
            return sum;
        }

        // Function using recursion
        public static int sumDigitsRecursive(int n) {
            if (n == 0) return 0;          // base case
            return (n % 10) + sumDigitsRecursive(n / 10); // recursive step
        }
    }

    // Lecture3_Ex_5
    public static class MaxValueExample {
        static void main(String[] args) {
            int[] numbers = {3, 17, 9, 22, 5};
            System.out.println("Maximum value: " + findMax(numbers));
        }

        // Iterative function to find maximum
        public static int findMax(int[] arr) {
            int max = arr[0];
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                }
            }
            return max;
        }
    }

    //Lecture3_Ex_6
    // Example: Fibonacci sequence using recursion
    public static class FibonacciExample {
        static void main(String[] args) {
            int n = 6;
            System.out.println("Fibonacci(" + n + ") = " + fibonacci(n));
        }

        // Recursive function to compute Fibonacci numbers
        public static int fibonacci(int n) {
            if (n == 0) return 0;      // base case
            if (n == 1) return 1;      // base case
            return fibonacci(n - 1) + fibonacci(n - 2); // recursive calls
        }
    }

    //Lecture3_Ex_7
    public static class FibonacciIterative {
        static void main(String[] args) {
            int n = 6;
            System.out.println("Fibonacci(" + n + ") = " + fibonacciIterative(n));
        }

        public static int fibonacciIterative(int n) {
            if (n == 0) return 0;
            if (n == 1) return 1;

            int a = 0, b = 1, c = 0;

            for (int i = 2; i <= n; i++) {
                c = a + b;
                a = b;
                b = c;
            }
            return c;
        }
    }


    // Lecture3_Ex_8 immutable and mutable
    public static class StringExample {
        static void main(String[] args) {
            String text = "Ala";
            text.concat(" ma kota"); // próba "dopisywania"
            System.out.println(text); // wypisze: Ala
        }
    }

    public static class StringBuilderExample {
        static void main(String[] args) {
            StringBuilder text = new StringBuilder("Ala");
            text.append(" ma kota"); // modyfikuje ten sam obiekt
            System.out.println(text); // Ala ma kota
        }
    }


    //Lecture3_Ex_9
    public static class BubbleSortExample {
        public static void bubbleSort(int[] array) {
            int n = array.length;
            boolean swapped;

            for (int i = 0; i < n - 1; i++) {
                swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    if (array[j] > array[j + 1]) {
                        int temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                        swapped = true;
                    }
                }
                if (!swapped) {
                    break;
                }
            }
        }

        public static void printArray(int[] array) {
            for (int value : array) {
                System.out.print(value + " ");
            }
            System.out.println();
        }

        static void main(String[] args) {
            int[] numbers = {64, 34, 25, 12, 22, 11, 90};

            System.out.println("Original array:");
            printArray(numbers);
            bubbleSort(numbers);
            
            System.out.println("Sorted array:");
            printArray(numbers);
        }
    }
}