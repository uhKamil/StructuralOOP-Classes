import static java.lang.IO.*;
import static term.term.*;

import term.*;

final double X = 50000; // a single straight track of length X
final double t1 = 60; // time of Train 1's acceleration 
final double t2 = 120; // time of Train 2's acceleration 
final double a1 = 0.5; // constant for the acceleration equation (Train 1)
final double a2 = 0.7; // constant for the acceleration equation (Train 2)
final double dev_v = 4; // constant for the acceleration equation (for Train 2 after t2)
final double t_osc = 180; // constant for the acceleration equation (for Train 2 after t2)
final double tau = 0.1;

final int W = 80; // Track of Doom's length

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

boolean KeyQ() {
    if (keypressed()) {
        String key = readkeystr();
        return key.equals("q");
    }
    return false;
}

void TrainCollision(Train Train1, Train Train2, double tau) {
    double t = 0;
    boolean collision = false; // to print the status only once after the collision occurs
    while (true) {
        // Change trains' position during acceleration in t1, t2
        if (!collision) {
            calculatePosition(Train1, t);
            calculatePosition(Train2, t);
            PrintSituation(Train1, Train2, t);
            TrackOfDoom(W, X, Train1.x, Train2.x);
        }
        if (CheckCollision(Train1, Train2)) {
            gotoxy(1, 14);
            setfgcolor(ltgrey);
            print("Collision detected! Press 'q' key to quit...");
            collision = true;
        }
        else {
            t += tau;
        }
        if (KeyQ()) {break;}
        delay(10);
    }
}

void calculatePosition(Train train, double t) {
    double Kv_2 = (2 * a2 * t2) / Math.PI;
    double K_osc = 2 * Math.PI / t_osc;
    double sin_t2 = Math.sin((Math.PI * t) / (2 * t2));

    if (train.id == 1) {
        if (t <= t1) {
            train.a = a1;
            // Derived by two integrations
            train.v = train.a * t;
            train.x = a1 * Math.pow(t, 2) / 2;
        } else {
            train.a = 0;
            train.v = a1 * t1;
            train.x = a1 * t1 * (t - t1) + a1 * Math.pow(t1, 2) / 2;
        }
    } else {
        if (t <= t2) {
            // Derived by one derivative and one integration
            train.a = a2 * sin_t2;
            train.v = -Kv_2 * (1 - Math.cos((Math.PI * t) / (2 * t2)));
            train.x = (4 * a2 * Math.pow(t2, 2)) / Math.pow(Math.PI, 2) * sin_t2 - Kv_2 * t + X;
        } else {
            double K_x_sin_const = (4 * a2 * Math.pow(t2, 2)) / Math.pow(Math.PI, 2);
            double Kv_t2_const = (2 * a2 * Math.pow(t2, 2)) / Math.PI;

            train.a = -dev_v * K_osc * Math.sin(K_osc * (t - t2));
            train.v = -dev_v * Math.cos(K_osc * (t - t2)) - Kv_2;
            train.x = -dev_v / K_osc * Math.sin(K_osc * (t - t2)) - Kv_2 * (t - t2) + (X + K_x_sin_const - Kv_t2_const);
        }
    }
}

void PrintSituation(Train Train1, Train Train2, double t) {
    double a1 = Train1.a, a2 = Train2.a;
    double v1 = Train1.v, v2 = Train2.v;
    double x1 = Train1.x, x2 = Train2.x;
    gotoxy(1, 1);
    print("Train Collision Simulation:");
    gotoxy(1, 3);
    print("t [min] = " + String.format("%.6f", t / 60));
    gotoxy(1, 4);
    print("P1");
    gotoxy(1, 5);
    print("v1 [km/h]: " + String.format("%.6f", v1 * 3.6));
    gotoxy(1, 6);
    print("v1 [m/s]: " + String.format("%.6f", v1));
    gotoxy(1, 7);
    print("a1 [m/s2] = " + String.format("%.6f", a1));
    gotoxy(1, 8);
    print("x1 [m] = " + String.format("%.6f", x1));
    gotoxy(24, 4);
    print("P2");
    gotoxy(24, 5);
    print("v2 [km/h]: " + String.format("%.6f", v2 * 3.6));
    gotoxy(24, 6);
    print("v2 [m/s]: " + String.format("%.6f", v2));
    gotoxy(24, 7);
    print("a2 [m/s2] = " + String.format("%.6f", a2));
    gotoxy(24, 8);
    print("x2 [m] = " + String.format("%.6f", x2));
    gotoxy(1, 10);
    print("Distance to collision: " + String.format("%.4f", (x2 - x1) / 1000) + " km    ");
    gotoxy(1, 14);
    print("Press 'q' key to quit...");
}


boolean CheckCollision(Train Train1, Train Train2) {
    return Train1.x >= Train2.x;
}

void TrackOfDoom(int W, double X, double x1, double x2) {
    gotoxy(1, 11);
    print("Track:");
    gotoxy(1, 12);
    print("=".repeat(W));

    int coord1 = (int) ((int) x1 / (X / W)) + 1;
    int coord2 = (int) ((int) x2 / (X / W)) + 1;
    if (x2 == X) {
        // Edge case: we don't want to print x2 outside the track
        coord2 -= 1;
    }
    if (Math.abs(x1 - x2) > X / W) {
        gotoxy(coord1, 12);
        print(">");
        gotoxy(coord2, 12);
        print("<");
    } else if (x1 > x2) {
        gotoxy(coord1, 12);
        setfgcolor(red);
        print("x");
    } else {
        gotoxy(coord1, 12);
        print("x");
    }
}

void main() {
    clrscr();
    cursor_hide();

    Train Train1 = new Train(1, a1, 0, 0, t1);
    Train Train2 = new Train(2, 0, 0, X, t2);

    TrainCollision(Train1, Train2, tau);
}
