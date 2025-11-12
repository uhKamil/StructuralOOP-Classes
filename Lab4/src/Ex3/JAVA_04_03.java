import static java.lang.IO.print;

public double vt = 3;
public double v0 = 0;
public double x = 0.45;
public double tau = 0.1;

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

interface ODE {
    // Ordinary Differential Equation dV/dt = f(t, V)
    double evaluate(double t, double v);
}

// Runge-Kitta implementation (suitable for non-linear flow of water)
double TankFillTimeSim(double vt, double v0, double x, double tau) {
//    ODE flowRate = (t, v) -> x; // this case is plain as it always returns x (linear flow)
    ODE flowRate = (t, v) -> 0.45 * Math.exp(-0.1 * t); // this is a scenario where tap would be set off
    
    double t = 0;
    gotoxy(1, 1);
    print("4TH ORDER RUNGE-KITTA");
    while (v0 <= vt) {
        double tau2 = (vt-v0) / x;
        double h = Math.min(tau, tau2);
        
        double k1 = flowRate.evaluate(t, v0);
        double k2 = flowRate.evaluate(t + h / 2, v0 + (h * k1) / 2);
        double k3 = flowRate.evaluate(t + h / 2, v0 + (h * k2) / 2);
        double k4 = flowRate.evaluate(t + h, v0 + h * k3);
        double deltaV = (h / 6) * (k1 + 2 * k2 + 2 * k3 + k4);
        
        v0 += deltaV;
        t += h;
        
        gotoxy(1, 2);
        print("Time resolution [s]: " + h);
        gotoxy(1, 3);
        print("Time elapsed [s]: " + String.format("%.6f", t));
        gotoxy(1, 4);
        print("Current capacity [l]: " + String.format("%.6f", v0));
        gotoxy(1, 5);
        print("Maximum capacity [l]: " + String.format("%.6f", vt));
        delay((int) (h * 1000));
        
        // don't make this too small or the loop won't end
        if (h <= 1E-8) {
            break;
        }
    }
    return t;
}

double TankFillTimeAnalytic(double vt, double v0, double x) {
//    return (vt - v0) / x; // linear flow
    return -10 * Math.log((v0 - vt + 4.5) / 4.5); // 0.45 * Math.exp(-0.1 * t) scenario
}

void main() {
    clrscr();
    cursor_hide();
    
    double resultL = TankFillTimeAnalytic(vt, v0, x);
    gotoxy(1, 7);
    print("ANALYTIC COMPUTATION");
    gotoxy(1, 8);
    print(String.format("%.6f", resultL) + " s");
    double resultI = TankFillTimeSim(vt, v0, x, tau);

    gotoxy(1, 10); print("Difference for tau = " + tau + " s: " + String.format("%.6f", resultI - resultL) + " s");
    delay(5000);
}
