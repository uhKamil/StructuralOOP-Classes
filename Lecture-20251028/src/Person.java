public class Person {
    int age;
    String name;
    void sayHello(String name) {
        System.out.println("Hello " + name);
    }
    Person(int age, String name) {
        this.age = age;
        this.name = name;
    }
}
