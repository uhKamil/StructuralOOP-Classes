// Kamil Wolbach (280161)
import static java.lang.IO.*;

final double X = 50000; // a single straight track of length X
final double t1 = 60; // time of Train 1's acceleration 
final double t2 = 120; // time of Train 2's acceleration 
final double a1 = 0.5; // constant for the acceleration equation (Train 1)
final double a2 = 0.7; // constant for the acceleration equation (Train 2)
final double dev_v = 4; // constant for the acceleration equation (for Train 2 after t2)
final double t_osc = 180; // constant for the acceleration equation (for Train 2 after t2)
final double tau = 0.1;

void gotoxy(int x, int y) {
    String GOTO_XY = "\u001b[%d;%dH";
    print(String.format(GOTO_XY, y, x));
}

void delay(int msec) {
    try {
        Thread.sleep(msec);
    } catch (InterruptedException _) {
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

static class Train {
    int id;
    double a; // train's acceleration
    double v; // train's speed
    double x; // train's position on the track
    double t; // train's initial acceleration time
    
    public Train(int id, double a, double v, double x, double t) {
        this.id = id;
        this.a = a;
        this.v = v;
        this.x = x;
        this.t = t;
    }
    
}

double Maximum(double a, double b) {
    if (a < b) {
        return b;
    }
    return a;
}

double TrainCollision(Train Train1, Train Train2, double tau) {
    double t = 0;
    double a_1 = Train1.a, a_2 = Train2.a;
    double v1 = Train1.v, v2 = Train2.v;
    double x1 = Train1.x, x2 = Train2.x;
    boolean COLLISION = false;
    
//    while (!COLLISION) {
        // Change trains' position during acceleration in t1, t2
        for (double i = 0; i <= Maximum(t1, t2); i += tau) {
//            println(i);
            if (i <= t1) {
                // Derived by two integrations
                v1 = a_1 * t;
                x1 = a1 * Math.pow(t, 2) / 2;
            }
            if (i <= t2) {
                a_2 = a2 * Math.sin((Math.PI * t) / (2 * t2));
                // Derived by two integrations
                v2 = (2 * a2 * t2) / Math.PI * (1 - Math.cos((Math.PI * t) / (2 * t2)));
                x2 = -(4 * a2 * Math.pow(t2, 2)) / Math.pow(Math.PI, 2) * Math.sin((Math.PI * t) / (2 * t2)) + X;
            }
            if (x1 > x2) {
                COLLISION = true;
                break;
            }
//          println(t + " :" + x1 + " " + x2);
            PrintSituation(X, a_1, v1, x1, a_2, v2, x2, t, x2-x1);
            delay(30);
            t += tau;
        }
        // Change of acceleration


//    }
    return t;
}


void PrintSituation(double X, double a1, double v1, double x1, double a2, double v2, double x2, double t, double collisionDistance) {
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
    clrscr();
    cursor_hide();
    
    Train Train1 = new Train(1, a1, 0, 0, t1);
    Train Train2 = new Train(2, 0, 0, X, t2);
    
    TrainCollision(Train1, Train2, tau);
}
