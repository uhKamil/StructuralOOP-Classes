// Kamil Wolbach (280161)
import static java.lang.IO.*;  //including package to be able to use simple print()
import static term.term.*;     //includes package term (createElements() functions were moved there)

public static class SimulationManager {
    private Square[] squares;
    private int count;
    private int N_STEPS;

    private final int WIDTH = 120;
    private final int HEIGHT = 30;

    public SimulationManager(int squareCount, int initialSteps) {
        this.count = squareCount;
        this.N_STEPS = initialSteps;
        this.squares = new Square[count];

        for (int i = 0; i < count; i++) {
            this.squares[i] = new Square(WIDTH, HEIGHT);
        }
    }

    public void run() {
        int steps = this.N_STEPS;

        while (true) {
            if (keypressed()) {
                String key = readkeystr();
                if (key.equals("q")) {
                    break;
                } else if ((key.equals("+") || key.equals("=")) && steps < Math.min(WIDTH, HEIGHT) / 2) {
                    steps++;
                    gotoxy(1, 1);
                    setfgcolor(7);
                    print("Steps: " + steps + "       ");
                } else if (key.equals("-") && steps > 0) {
                    steps--;
                    gotoxy(1, 1);
                    setfgcolor(7);
                    print("Steps: " + steps + "       ");
                }
            }

            for (Square square : this.squares) {
                square.clear();
            }

            for (Square square : this.squares) {
                square.move(steps, WIDTH, HEIGHT); 
            }

            for (Square square : this.squares) {
                square.draw();
            }

            System.out.flush();
            delay(200);
        }
        clrscr();
    }
}

public static class Square {
    private int x, y;
    private final int color;
    private int dx, dy;
    private final char sign;

    private static final int[][] DIRECTIONS = {
            {0, -1}, {0, 1}, {-1, 0}, {1, 0},
    };
    private static final int[] COLORS = {1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 15};

    public Square(int width, int height) {
        this.x = 2 + (int) (Math.random() * (width - 2));
        this.y = 2 + (int) (Math.random() * (height - 2));

        this.color = COLORS[(int) (Math.random() * COLORS.length)];
                
        int directionIndex = (int) (Math.random() * (DIRECTIONS.length));
        this.dx = DIRECTIONS[directionIndex][0];
        this.dy = DIRECTIONS[directionIndex][1];

        this.sign = '*';
    }

    public void draw() {
        setfgcolor(this.color);
        framexyc(this.x - 1, this.y - 1, this.x + 1, this.y + 1, this.sign);
    }

    public void clear() {
        setfgcolor(7);
        framexyc(this.x - 1, this.y - 1, this.x + 1, this.y + 1, ' ');
    }

    public void move(int steps, int width, int height) {
        int newX = this.x + steps * this.dx;
        int newY = this.y + steps * this.dy;

        if (newX < 2 || newX > width - 2) {
            this.dx = -this.dx;
        }
        if (newY < 2 || newY > height - 2) {
            this.dy = -this.dy;
        }

        this.x += steps * this.dx;
        this.y += steps * this.dy;
    }
}

void main() {
    final int INITIAL_SQUARES = 100;
    final int INITIAL_STEPS = 2;

    clrscr();
    print("\nPlanned Squares");
    print("\nInitial squares: " + INITIAL_SQUARES + ", Initial steps: " + INITIAL_STEPS);
    print("\nUse '+' and '-' to change steps. Press 'q' to quit.");
    delay(2000);
    cursor_hide();
    clrscr();

    SimulationManager manager = new SimulationManager(INITIAL_SQUARES, INITIAL_STEPS);
    manager.run();

    cursor_show();
    setfgcolor(7);
    print("\nThe simulation is ended.");
}