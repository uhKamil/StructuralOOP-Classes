import static java.lang.IO.*;
import static term.term.*;

public static class TrainSimulation {

    static class Track {
        double lengthX;
        int widthW;

        public Track(double lengthX, int widthW) {
            this.lengthX = lengthX;
            this.widthW = widthW;
        }
    }

    static class Train {
        int id;                  // Unique identifier of the train
        double x;                // Position [m]
        double prev_x;           // Position in the previous step of the simulation (for collision logic's sake)
        double v;                // Current velocity [m/s]
        double a;                // Acceleration [m/s^2]
        double max_v;            // Maximum speed [m/s]
        int direction;           // 1 (right) or -1 (left)
        boolean about_to_crash;  // Whether to display 'X' in the same position before the collision
        boolean crashed;         // Whether the train collided
        String symbol;           // Display the train as a symbol on the track

        public Train(int id, double startX, double acc, double maxV, int dir, String sym) {
            this.id = id;
            this.x = startX;
            this.prev_x = startX;
            this.v = 0;
            this.a = acc;
            this.max_v = maxV;
            this.direction = dir;
            this.about_to_crash = false;
            this.crashed = false;
            this.symbol = sym;
        }
    }

    static class TSimulation {
        Train[] trains;
        Track track;
        double time;
        double dt;
        int stepsPerRender;
    }
    
    static TSimulation initSimulation() {
        TSimulation sim = new TSimulation();
        sim.track = new Track(50000, 80);
        sim.time = 0;
        sim.dt = 0.01; // Simulation step
        // For one frame, these many steps will be calculated (increasing it will make the simulation faster)
        sim.stepsPerRender = 30; 

        sim.trains = new Train[]{
                new Train(1, 500, 0.5, 30, -1, "1"),
                new Train(2, 2500, 0.2, 20, 1, "2"),
                new Train(3, 50000, 0.7, 40, -1, "3"),
                new Train(4, 10000, 0.4, 25, 1, "4"),
                new Train(5, 40000, 0.6, 35, -1, "5"),
                new Train(6, 25000, 0.9, 50, 1, "6")
        };
        return sim;
    }

    static void updatePhysics(TSimulation sim) {
        for (Train t : sim.trains) {
            t.prev_x = t.x;
            
            if (t.crashed) {
                t.v = 0;
                t.a = 0;
                continue;
            }

            // Linear increase of speed up to maxV
            if (t.v < t.max_v) {
                t.v += t.a * sim.dt;
                if (t.v > t.max_v) t.v = t.max_v;
            }

            t.x += t.v * t.direction * sim.dt;
        }
        sim.time += sim.dt;
    }

    static void checkCollisions(TSimulation sim) {
        for (int i = 0; i < sim.trains.length; i++) {
            Train t1 = sim.trains[i];

            // Check boundary collisions (track ends)
            if (!t1.crashed && (t1.x <= 0 || t1.x >= sim.track.lengthX)) {
                t1.crashed = true;
                t1.x = (t1.x <= 0) ? 0 : sim.track.lengthX;
                t1.v = 0;
            }

            // Check collisions with other trains
            for (int j = i + 1; j < sim.trains.length; j++) {
                Train t2 = sim.trains[j];
                
                if (trainPosition(sim, t1) == trainPosition(sim, t2)) {
                    t1.about_to_crash = true;
                    t2.about_to_crash = true;
                }
                if (t1.prev_x < t2.prev_x != t1.x < t2.x) {
                    t1.crashed = true;
                    t1.v = 0;
                    t2.crashed = true;
                    t2.v = 0;
                }
            }
        }
    }
    
    static int trainPosition(TSimulation sim, Train t) {
        int pos = (int) ((t.x / sim.track.lengthX) * (sim.track.widthW - 1));

        if (pos < 0) pos = 0;
        if (pos >= sim.track.widthW) pos = sim.track.widthW - 1;
        
        return pos;
    }

    static void render(TSimulation sim) {
        gotoxy(1, 1);
        print("Multiple Train Collision Simulation:\nTime: " + String.format("%.2f", sim.time / 60) + " min");

        int row = 3;
        for (Train t : sim.trains) {
            gotoxy(1, row);
            String status = t.crashed ? "CRASH" : "OK";

            if (t.crashed) setfgcolor(red);
            else setfgcolor(white);

            print(String.format("T%d [%s]: x=%7.1f m, v=%5.1f m/s %s",
                    t.id, t.symbol, t.x, t.v * t.direction, status));
            row++;
        }

        char[] visualTrack = new char[sim.track.widthW];
        for (int i = 0; i < sim.track.widthW; i++) visualTrack[i] = '=';

        for (Train t : sim.trains) {
            int pos = trainPosition(sim, t);
            if (t.crashed) {
                visualTrack[pos] = 'X';
            }
            else if (t.about_to_crash) {
                visualTrack[pos] = 'x';
            } else {
                if (visualTrack[pos] != 'X') {
                    visualTrack[pos] = t.symbol.charAt(0);
                }
            }
        }

        gotoxy(1, row + 1);
        setfgcolor(ltgrey);
        print("Track: ");

        for (char c : visualTrack) {
            if (c == 'X') setfgcolor(red);
            else setfgcolor(green);
            print(String.valueOf(c));
        }
        setfgcolor(ltgrey);

        gotoxy(1, row + 3);
        print("Press 'q' to quit.");
    }

    static boolean checkExitKey() {
        if (keypressed()) {
            String key = readkeystr();
            return key.equals("q");
        }
        return false;
    }
    
    static boolean allTrainsCollided(TSimulation sim) {
        int trainsCollided = 0;
        for (Train t : sim.trains) {
            if (t.crashed) trainsCollided += 1;
        }
        return trainsCollided == sim.trains.length;
    }
}

void main() {
    clrscr();
    cursor_hide();
    TrainSimulation.TSimulation sim = TrainSimulation.initSimulation();

    while (true) {
        for (int i = 0; i < sim.stepsPerRender; i++) {
            TrainSimulation.updatePhysics(sim);
            TrainSimulation.checkCollisions(sim);
        }
        TrainSimulation.render(sim);
        if (TrainSimulation.checkExitKey() || TrainSimulation.allTrainsCollided(sim)) break;
        delay(15);
    }

    setfgcolor(ltgrey);
    cursor_show();
    print("\nSimulation finished.");
}
