import static java.lang.IO.*;  // including package to be able to use simple print()
import static term.term.*;     // includes package term (createElements() functions were moved there)

// Model
static class Square {
    private int x, y;
    private int dx, dy;
    private final int size;
    private final int color;

    public Square(int boardWidth, int boardHeight) {
        this.size = (Math.random() < 0.5) ? 1 : 2;
        
        // Initial position
        this.x = 3 + (int) (Math.random() * (boardWidth - 3 - this.size));
        this.y = 3 + (int) (Math.random() * (boardHeight - 3 - this.size));

        // Pick a velocity 
        randomizeVelocity();

        int[] palette = {1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15};
        int colorIndex = (int) (Math.random() * palette.length);
        this.color = palette[colorIndex];
    }

    public void randomizeVelocity() {
        this.dx = 0;
        this.dy = 0;
        while (this.dx == 0 && this.dy == 0) {
            this.dx = (int) (Math.random() * 3) - 1;
            this.dy = (int) (Math.random() * 3) - 1;
        }
    }

    public void move(int boardWidth, int boardHeight) {
        int nextX = x + dx;
        int nextY = y + dy;

        boolean hitX = false;
        boolean hitY = false;
        
        if (nextX <= 1 || nextX + size >= boardWidth) {
            hitX = true;
        }
        if (nextY <= 1 || nextY + size >= boardHeight + 1) {
            hitY = true;
        }

        if (hitX || hitY) {
            randomizeVelocity();
            
            if (hitX) {
                // Hit the right wall -> go left. Hit the left wall -> go right
                if (nextX > boardWidth / 2) this.dx = -Math.abs(this.dx);
                else this.dx = Math.abs(this.dx);
            }
            if (hitY) {
                // Hit the lower wall -> go up. Hit the upper wall -> go down
                if (nextY > boardHeight / 2) this.dy = -Math.abs(this.dy);
                else this.dy = Math.abs(this.dy);
            }
            
            if (hitX && this.dx == 0) this.dx = (nextX > boardWidth / 2) ? -1 : 1;
            if (hitY && this.dy == 0) this.dy = (nextY > boardHeight / 2) ? -1 : 1;
            
        } else {
            x = nextX;
            y = nextY;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
    public int getColor() { return color; }
}

static class SimulationModel {
    private final Square[] squares;
    private final int count;
    private final int width;
    private final int height;

    public SimulationModel(int count, int width, int height) {
        this.width = width;
        this.height = height;
        this.count = count;
        this.squares = new Square[count];

        for (int i = 0; i < count; i++) {
            this.squares[i] = new Square(width, height);
        }
    }

    public void update() {
        for (int i = 0; i < count; i++) {
            Square s = squares[i];
            s.move(width, height);
        }
    }

    public Square[] getSquares() {
        return squares;
    }
}

// View
static class SimulationView {
    public void drawFrame(int width, int height) {
        setfgcolor(7);
        framexyc(1, 1, width, height, '#');
    }

    public void clearSquares(Square[] squares) {
        setfgcolor(0);
        for (Square s : squares) {
            framexyc(s.getX(), s.getY(), s.getX() + s.getSize() - 1, s.getY() + s.getSize() - 1, ' ');
        }
    }

    public void drawSquares(Square[] squares) {
        for (Square s : squares) {
            setfgcolor(s.getColor());
            framexyc(s.getX(), s.getY(), s.getX() + s.getSize() - 1, s.getY() + s.getSize() - 1, '*');
        }
    }
}

// Controller
static class SimulationController {
    private final SimulationModel model;
    private final SimulationView view;

    private final int WIDTH = 120;
    private final int HEIGHT = 30;
    private int simulationSpeed = 100;

    public SimulationController(int squareCount) {
        this.model = new SimulationModel(squareCount, WIDTH, HEIGHT);
        this.view = new SimulationView();
    }

    public void run() {
        clrscr();
        cursor_hide();
        view.drawFrame(WIDTH, HEIGHT);

        while (true) {
            if (keypressed()) {
                String key = readkeystr();
                if (key.equals("q")) {
                    break;
                }
                if (key.equals("+") || key.equals("=")) simulationSpeed = Math.max(10, simulationSpeed - 20);
                if (key.equals("-")) simulationSpeed = Math.min(400, simulationSpeed + 20);
            }

            view.clearSquares(model.getSquares());
            model.update();
            view.drawSquares(model.getSquares());

            setfgcolor(7);
            String ui_info = " Squares: " + model.getSquares().length + " | Speed delay: " + simulationSpeed + "ms | 'q' to exit ";
            gotoxy((WIDTH - ui_info.length()) / 2, HEIGHT);
            print(ui_info);

            System.out.flush();
            delay(simulationSpeed);
        }

        setfgcolor(7);
        clrscr();
        print("Simulation ended.");
    }
}

void main() {
    SimulationController app = new SimulationController(30);
    app.run();
}
