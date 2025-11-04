/**
 * Klasyczna klasa Point3D.
 * Służy do modelowania obiektu z możliwością dodania złożonego zachowania,
 * dziedziczenia lub mutowalności (choć tu została ustawiona na immutable).
 */
public class PointClass {

    // 1. Pola są prywatne i finalne (dla niezmienności – dobra praktyka).
    private final double x;
    private final double y;
    private final double z;

    // 2. Konstruktor: ręcznie definiuje się, jak zainicjować stan obiektu.
    public PointClass(double x, double y, double z) {
        // Opcjonalnie: można tu dodać walidację danych.
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // 3. Gettery: ręcznie definiuje się metody dostępu do pól.
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    // 4. Metoda Biznesowa/Behawioralna: unikalna funkcjonalność klasy.
    public double calculateDistanceToOrigin() {
        // Obliczenie odległości punktu od początku układu współrzędnych (0, 0, 0).
        return Math.sqrt(x * x + y * y + z * z);
    }

    // 5. Przeciążenie equals() i hashCode():
    // Wymagane, aby dwa punkty o identycznych współrzędnych były traktowane jako równe
    // w strukturach danych takich jak HashMapy lub Set. Musi być zaimplementowane RĘCZNIE.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointClass that = (PointClass) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        // Złożona (ale konieczna) logika tworzenia hasha z wielu pól.
        return java.util.Objects.hash(x, y, z);
    }

    // 6. toString(): Ręczna definicja, jak obiekt ma się wyświetlać.
    @Override
    public String toString() {
        return "Point3DClass{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    // --- PRZYKŁAD UŻYCIA ---
    public static void main(String[] args) {
        PointClass p1 = new PointClass(3.0, 4.0, 0.0);

        System.out.println("Klasa:");
        System.out.println("Punkt: " + p1);
        System.out.println("Współrzędna X: " + p1.getX());
        System.out.printf("Odległość od początku: %.2f\n", p1.calculateDistanceToOrigin());
    }
}
