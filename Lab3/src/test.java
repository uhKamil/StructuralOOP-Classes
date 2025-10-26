import static java.lang.IO.*;

void delay(int msec) {
    try {
        Thread.sleep(msec);
    } catch (InterruptedException e) {
    }
}


void main() {
    System.out.println("test");
    delay(5000);
}
