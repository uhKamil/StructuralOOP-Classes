import static java.lang.IO.*;

void gotoxy(int x, int y) {
    String GOTO_XY = "\u001b[%d;%dH";
    print(String.format(GOTO_XY, y, x));
}

void delay(int msec) {
    try {
        Thread.sleep(msec);
    } catch (InterruptedException e) {
    }
}

void clrscr() {
    String CLEAR_SCREEN = "\u001b[2J";
    print(String.format(CLEAR_SCREEN));
}

void cursor_hide() {
    String HIDE_CURSOR = "\u001b[?25l";
    print(String.format(HIDE_CURSOR));
}

void cursor_show() {
    String SHOW_CURSOR = "\u001b[?25h";
    print(String.format(SHOW_CURSOR));
}

double TankFillTimeSim(double vt, double v0, double x, double tau) {
    double t = 0;
    gotoxy(1, 1);
    print("NUMERICAL INTEGRATION MODEL");
    while (v0 <= vt) {
        double waterFlown = x * tau;
        v0 += waterFlown;
        t += tau;
        gotoxy(1, 2);
        print("Time resolution [s]: " + tau);
        gotoxy(1, 3);
        print("Time elapsed [s]: " + String.format("%.6f", t));
        gotoxy(1, 4);
        print("Current capacity [l]: " + String.format("%.6f", v0));
        gotoxy(1, 5);
        print("Maximum capacity [l]: " + String.format("%.6f", vt));
        delay((int) (tau * 1000));
    }
    return t;
}

double TankFillTimeLin(double vt, double v0, double x) {
    return (vt - v0) / x;
}


void main() {
//    Scanner scanner = new Scanner(System.in);
    clrscr();
    cursor_hide();
    
    double vt = 3;
    double v0 = 0;
    double x = 0.45;
    double tau = 0.1;
    
    double resultL = TankFillTimeLin(vt, v0, x);
    gotoxy(1, 7); print("LINEAR MODEL");
    gotoxy(1, 8); print(String.format("%.6f", resultL) + " s");
    double resultI = TankFillTimeSim(vt, v0, x, tau);
    
    gotoxy(1, 10); print("Difference for tau = " + tau + " s: " + String.format("%.6f", resultI - resultL) + " s");

    tau = 0.01;
    resultI = TankFillTimeSim(vt, v0, x, tau);
    gotoxy(1, 11); print("Difference for tau = " + tau + " s: " + String.format("%.6f", resultI - resultL) + " s");

    tau = 0.001;
    resultI = TankFillTimeSim(vt, v0, x, tau);
    gotoxy(1, 12); print("Difference for tau = " + tau + " s: " + String.format("%.6f", resultI - resultL) + " s");
    
    delay(5000);
}
