/**
 * Rekord Point3D.
 * Idealny do przenoszenia niezmiennych danych.
 * Automatycznie generuje konstruktor, metody dostępu (x(), y(), z()),
 * equals(), hashCode() i toString().
 */
public record PointRecord(double x, double y, double z) {

    // Rekordy mogą (i powinny) mieć metody biznesowe!
    // Umożliwiają one obliczenia oparte na stanie.
    public double calculateDistanceToOrigin() {
        // Metoda korzysta z automatycznych pól rekordu (x, y, z)
        return Math.sqrt(x * x + y * y + z * z);
    }

    // Opcjonalnie: można dodać kompaktowy konstruktor do walidacji.
    // Taki konstruktor nie ma parametru i nie wymaga przypisania (jest to zrobione za kulisami).
    public PointRecord {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            throw new IllegalArgumentException("Współrzędne nie mogą być NaN.");
        }
    }

    // --- PRZYKŁAD UŻYCIA ---
    public static void main(String[] args) {
        PointRecord p2 = new PointRecord(3.0, 4.0, 0.0);
        PointRecord p3 = new PointRecord(3.0, 4.0, 0.0); // Identyczne dane

        System.out.println("Rekord:");
        System.out.println("Punkt: " + p2); // Używa automatycznego toString()
        System.out.println("Współrzędna X: " + p2.x()); // Używa automatycznego gettera x()
        System.out.printf("Odległość od początku: %.2f\n", p2.calculateDistanceToOrigin());

        // TEST RÓWNOŚCI WARTOŚCIOWEJ (automatycznie generowany equals)
        System.out.println("p2 i p3 są równe (wartościowo)? " + p2.equals(p3)); // Zwraca true
    }
}
