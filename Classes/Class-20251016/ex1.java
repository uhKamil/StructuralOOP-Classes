public class ex1 {
    // creating a function (method)
    static void printName() {
        System.out.println("My name is John Doe");
    }
    static void printName2(String name) {
        System.out.println("My name is " + name);
    }
    static void main(String[] args) {
        // calling a function (method)
        printName(); printName2("Kamil");
    }
}
