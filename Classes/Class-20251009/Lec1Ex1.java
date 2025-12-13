//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//  void main() {
//    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
//    // to see how IntelliJ IDEA suggests fixing it.
//    IO.println(String.format("Hello and welcome!"));
//
//    for (int i = 1; i <= 5; i++) {
//        //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
//        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
//        IO.println("i = " + i);
//    }
//}

public class Lec1Ex1 {
    public static void main(String[] args) {
        // Primitive types of data in Java
        byte b = 100;               // 1 byte, range -128 to 127
        short s = 10000;            // 2 bytes
        int i = 1000000;            // 4 bytes
        long l = 1000000000000L;    // 8 bytes (notice the 'L' at the end)

        float f = 5.75f;
        double d = 19.99;

        char c = 'a';
        boolean boo = true;

        // String
        String str = "Hello there!";
        IO.println(b);
        IO.println("Hello and welcome!");
        IO.println(str);

        // Better print
        System.out.println("byte " + b);
        System.out.println("short " + s);
        System.out.println("int " + i);
        System.out.println("long " + l);
        System.out.println("float " + f);
        System.out.println("double " + d);
        System.out.println("char " + c);
        System.out.println("boolean " + boo);
    }
}