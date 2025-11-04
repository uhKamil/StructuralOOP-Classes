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

double Maximum(double a, double b) {
    if (a < b) {
        return b;
    }
    return a;
}

double TrainCollision(double X, double t1, double t2, double a1, double a2, double dev_v, double t_osc, double tau) {
    double t = 0;
    double x1 = 0;
    double x2 = X;
    double v2 = 0;
    double a_2 = 0;
    boolean Collision = false;
//    while (!Collision) {
        // Change of position of trains during acceleration in t1, t2
        for (double i = 0; i <= Maximum(t1, t2); i += tau) {
//            println(i);
            if (i <= t1) {
                // Derived by two integrations
                x1 += a1 * (Math.pow(t+tau, 2) - Math.pow(t, 2)) / 2;
            }
            if (i <= t2) {
                // Integrated once, v(t) is numerically integrated
                a_2 = a2 * Math.sin((Math.PI * t) / (2 * t2));
                v2 = -(a2 * 2 * t2) / Math.PI * Math.cos((Math.PI * t) / (2 * t2));
                x2 += v2 * tau;
            }
            if (x1 > x2) {
                Collision = true;
                break;
            }
//          println(t + " :" + x1 + " " + x2);
            PrintSituation(X, a1*t, a1, x1, v2, a_2, x2, t, x2-x1);
            delay(30);
            t += tau;
        }
        // Change of acceleration


//    }
    return t;
}


void PrintSituation(double X, double v1, double a1, double x1, double v2, double a2, double x2, double t, double collisionDistance) {
    gotoxy(1, 1);
    print("Train Collision Simulation:");
    gotoxy(1, 3);
    print("t [min] = " + String.format("%.6f", t/60));
    gotoxy(1, 4);
    print("P1");
    gotoxy(1, 5);
    print("[m/s]: " + String.format("%.6f", v1));
    gotoxy(1, 6);
    print("[km/h]: " + String.format("%.6f", v1*3.6));
    gotoxy(1, 7);
    print("a1 [m/s2] = " + String.format("%.6f", a1));
    gotoxy(1, 8);
    print("x1 [m] = " + String.format("%.6f", x1));
    gotoxy(24, 4);
    print("P2");
    gotoxy(24, 5);
    print("[m/s]: " + String.format("%.6f", v2));
    gotoxy(24, 6);
    print("[km/h]: " + String.format("%.6f", v2*3.6));
    gotoxy(24, 7);
    print("a2 [m/s2] = " + String.format("%.6f", a2));
    gotoxy(24, 8);
    print("x2 [m] = " + String.format("%.6f", x2));
}


void main() {
//    Scanner scanner = new Scanner(System.in);
    clrscr();
    cursor_hide();

    double X = 50000;
    double t1 = 60;
    double t2 = 120;
    double a1 = 0.5;
    double a2 = 0.7;
    double dev_v = 4;
    double t_osc = 180;
    double tau = 0.1;

    TrainCollision(X, t1, t2, a1, a2, dev_v, t_osc, tau);
}
