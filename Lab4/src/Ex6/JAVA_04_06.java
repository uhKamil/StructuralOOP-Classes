import static java.lang.IO.*;
import static term.term.*;

final double tau = 0.01;

class Clock {
    int h;
    int m;
    double s;

    Clock(int h, int m, double s) {
        this.h = h;
        this.m = m;
        this.s = s;
    }

    /**
     * @return Time in seconds
     */
    double TimeSeconds(int h, int m, double s) {
        return h * 3600 + m * 60 + s;
    }

    /**
     * Returns the hour hand angle in degrees for the given elapsed time since midnight.
     *
     * @param t time in seconds
     * @return Hour hand angle in degrees in range [0, 360).
     * Examples:
     * <ul>
     *   <li>12:00:00 -> t = 43200 -> 0°</li>
     *   <li>15:00:00 -> t = 54000 -> 90°</li>
     * </ul>
     */
    double HourHand(double t) {
        return ((double) 360 / (3600 * 12) * t % (3600 * 12)) % 360;
    }

    /**
     * Returns the minute hand angle in degrees for the given elapsed time since midnight.
     *
     * @param t time in seconds
     * @return Hour hand angle in degrees in range [0, 360).
     * Examples:
     * <ul>
     *   <li>12:00:00 -> t = 43200 -> 0°</li>
     *   <li>12:30:00 -> t = 45000 -> 180°</li>
     * </ul>
     */
    double MinuteHand(double t) {
        return ((double) 360 / 3600 * t % 3600) % 360;
    }

    /**
     * Returns time in a suitable hour:min:sec format
     * where sec is a double and hour and min are integers.
     * @param t time in seconds
     */
    void ComputeTime(double t) {
        h = (int) Math.floor(Hour(t));
        m = (int) Math.floor(Minute(t));
        s = Second(t);
    }
    
    double Hour(double t) { return (t / 3600) % 24;}
    double Minute(double t) { return (t / 60) % 60;}
    double Second(double t) { return t % 60;}

    /**
     * Returns the time difference in hr:min:sec where t1 < t2
     *
     * @param t1 time in seconds
     * @param t2 time in seconds
     */
    double[] TimeDifference(double t1, double t2) {
        double diff = t2 - t1;
        return new double[]{Hour(diff), Minute(diff), Second(diff)};
    }

    double[] IntersectionHourMin(double t) {
        // Initialization
        double HourHand = HourHand(t);
        double MinuteHand = MinuteHand(t);
        double AngleDifference = HourHand - MinuteHand;
        long t0 = System.nanoTime();
        long t1;
        while (Math.abs(AngleDifference) > tau) {
            AngleDifference = HourHand(t) - MinuteHand(t);
            ComputeTime(t);
            t += tau;
            gotoxy(1, 1);
            t1 = System.nanoTime();
            print("Computing" + ".".repeat((int) ((t1 - t0) / 1E9) % 4) + "   ");
//            println(t + " " + h + ":" + m + ":" + s + " " + HourHand + " " + MinuteHand + " " + (HourHand - MinuteHand));
        }
        return new double[]{h, m, s};
    }
}

void main() {
    Scanner scanner = new Scanner(System.in);
    print("Enter initial hour: ");
    int h0 = scanner.nextInt();
    print("Enter initial minutes: ");
    int m0 = scanner.nextInt();
    print("Enter initial seconds: ");
    int s0 = scanner.nextInt();

    Clock clock = new Clock(h0, m0, s0);
    double time = clock.TimeSeconds(h0, m0, s0);

    clrscr();
    cursor_hide();
    double[] overlap = clock.IntersectionHourMin(time);
    gotoxy(1, 2);
    print("The hands will overlap at " + (int) overlap[0] + ":"
            + (int) overlap[1] + ":" + String.format("%.2f", overlap[2]));

    double[] timeDiff = clock.TimeDifference(time, clock.TimeSeconds(clock.h, clock.m, clock.s));
    gotoxy(1, 3);
    print("The time difference is " + (int) timeDiff[0] + ":"
            + (int) timeDiff[1] + ":" + String.format("%.2f", timeDiff[2]));
}