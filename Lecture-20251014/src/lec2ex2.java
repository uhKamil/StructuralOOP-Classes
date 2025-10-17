public class lec2ex2 {
    public static void main(String[] args) {
        int [] numbers = {10, 20, 30, 40, 50};
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
