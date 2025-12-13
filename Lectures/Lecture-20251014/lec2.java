public class lec2 {
    // Lecture2Ex1
    public static class ForLoopExample {
        static void main(String[] args) {
            // The for loop
            for (int i = 1; i <= 10; i++) {
                System.out.println("Number: " + i);
            }

            // Way 1 for the while loop
            int i = 1;
            while (i <= 5) {
                System.out.println("This will print as long as the condition is true.");
                i++;
            }

            // Way 2 for the while loop
            int j = 1;
            do {
                System.out.println("This will print at least once.");
                j++;
            }
            while (j <= 5);

            int[] numbers = {10, 20, 30, 40, 50};
            System.out.println(numbers);
            /* Linia System.out.println(numbers); wydrukuje coś w stylu [I@6d06d69c,
            czyli reprezentację obiektu tablicy typu int[] (nie jej zawartość).
            To jest domyślna metoda toString() dla tablic w Javie, pokazująca typ i hashcode obiektu. */
            String[] fruits = {"Apple", "Banana", "Cherry", "Date"};

            for (String fruit : fruits) {
                System.out.println("Fruit: " + fruit);
            }

            for (int number : numbers) {
                System.out.println("Number: " + number);
            }
        }
    }

    // Lecture2Ex2
    public static class WhileLoopExample {
        static void main(String[] args) {
            int i = 1;
            while (i <= 5) {
                System.out.println("Number: " + i);
                i++;
            }
        }
    }

    // Lecture2Ex3
    public static class DoWhileLoopExample {
        static void main(String[] args) {
            int i = 1;
            do {
                System.out.println("Number: " + i);
                i++;
            } while (i <= 5);
        }
    }

    // Lecture2Ex4
    public static class ForEachLoopExample {
        static void main(String[] args) {
            String[] fruits = {"Apple", "Banana", "Cherry"};
            for (String fruit : fruits) {
                System.out.println("Fruit: " + fruit);
            }
        }
    }

    // Lecture2Ex5
    public static class ForEachLoopExample2 {
        static void main(String[] args) {
            for (int i = 1; i <= 10; i++) {
                if (i == 5) break; // exits the loop when i is 5
                System.out.println("i = " + i);
            }
        }
    }
    
    // Lecture2Ex6
    public static class ForEachLoopExample3 {
        static void main(String[] args) {
            for (int i = 1; i <= 5; i++) {
                if (i == 3) continue; // skip printing 3
                System.out.println("i = " + i);
            }
        }
    }

    //        Lecture2Ex7
    int[] numbers = {10, 20, 30, 40, 50};

    // Lecture2Ex8
    public static class ForLoopArray {
        static void main(String[] args) {
            int[] numbers = {10, 20, 30, 40, 50};
            for (int i = 0; i < numbers.length; i++) {
                System.out.println("Element at index " + i + ": " + numbers[i]);
            }
        }
    }

    // Lecture2Ex9
    public static class WhileLoopArray {
        static void main(String[] args) {
            int[] numbers = {10, 20, 30, 40, 50};
            int i = 0;
            while (i < numbers.length) {
                System.out.println("Element at index " + i + ": " + numbers[i]);
                i++;
            }
        }
    }

    // Lecture2Ex10
    public static class DoWhileLoopArray {
        static void main(String[] args) {
            int[] numbers = {10, 20, 30, 40, 50};
            int i = 0;
            do {
                System.out.println("Element at index " + i + ": " + numbers[i]);
                i++;
            } while (i < numbers.length);
        }
    }

    // Lecture2Ex11
    public static class ForEachArray {
        static void main(String[] args) {
            int[] numbers = {10, 20, 30, 40, 50};
            for (int number : numbers) {
                System.out.println("Number: " + number);
            }
        }
    }

    // Lecture2Ex12
    public static class StringArrayExample {
        static void main(String[] args) {
            String[] colors = {"Red", "Green", "Blue"};
            for (String color : colors) {
                System.out.println("Color: " + color);
            }
        }
    }

    // Lecture2Ex13
    public static class SimpleIfExample {
        static void main(String[] args) {
            int number = 10;
            if (number > 0) {
                System.out.println("The number is positive.");
            }
        }
    }

    // Lecture2Ex13
    public static class IfElseExample {
        static void main(String[] args) {
            int number = -5;
            if (number >= 0) {
                System.out.println("Non-negative number.");
            } else {
                System.out.println("Negative number.");
            }
        }
    }

    // Lecture2Ex14
    public static class IfElseIfExample {
        static void main(String[] args) {
            int score = 85;
            if (score >= 90) {
                System.out.println("Grade: A");
            } else if (score >= 80) {
                System.out.println("Grade: B");
            } else if (score >= 70) {
                System.out.println("Grade: C");
            } else {
                System.out.println("Grade: F");
            }
        }
    }

    // Lecture2Ex15
    public static class LogicalOperatorsExample {
        static void main(String[] args) {
            int age = 25;
            boolean hasTicket = true;
            if (age >= 18 && hasTicket) {
                System.out.println("You can enter the concert.");
            }
            boolean isMember = false;
            if (age >= 18 || isMember) {
                System.out.println("You are eligible for access.");
            }
            boolean isBlocked = false;
            if (!isBlocked) {
                System.out.println("Access granted.");
            }
        }
    }

    // Lecture2Ex16
    public static class ArrayConditionExample {
        static void main(String[] args) {
            int[] numbers = {5, 12, 8, 20, 3};
            for (int number : numbers) {
                if (number > 10 && number % 2 == 0) {
                    System.out.println(number + " is even and greater than 10.");
                } else if (number <= 10) {
                    System.out.println(number + " is 10 or less.");
                }
            }
        }
    }
}