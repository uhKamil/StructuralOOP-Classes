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

    // Invert horizontal moving direction
    public void bounceX() {
        this.dx = -this.dx;
        if (this.dx == 0) this.dx = (Math.random() < 0.5) ? 1 : -1;
    }

    // Invert vertical moving direction
    public void bounceY() {
        this.dy = -this.dy;
        if (this.dy == 0) this.dy = (Math.random() < 0.5) ? 1 : -1;
    }

    public void applyMove() {
        x += dx;
        y += dy;
    }

    public int getNextX() {
        return x + dx;
    }

    public int getNextY() {
        return y + dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public int getColor() {
        return color;
    }
}

static class SimulationModel {
    private final Square[] squares;
    private final int count;
    private final int width;
    private final int height;

    private long totalWallHits = 0;
    private long totalSquareHits = 0;

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
            Square s1 = squares[i];
            boolean collided = false;

            int nextX = s1.getNextX();
            int nextY = s1.getNextY();

            // Wall collision
            boolean hitWallX = (nextX <= 1 || nextX + s1.getSize() >= width);
            boolean hitWallY = (nextY <= 1 || nextY + s1.getSize() >= height + 1);

            if (hitWallX || hitWallY) {
                totalWallHits++;
                collided = true;
                if (hitWallX) s1.bounceX();
                if (hitWallY) s1.bounceY();
            }

            // Square collision
            if (!collided) {
                for (int j = i + 1; j < count; j++) {
                    Square s2 = squares[j];
                    if (checkIntersection(nextX, nextY, s1.getSize(), s2.getNextX(), s2.getNextY(), s2.getSize())) {
                        totalSquareHits++;
                        collided = true;
                        s1.bounceX();
                        s1.bounceY();
                        s2.bounceX();
                        s2.bounceY();
                    }
                }
            }

            if (!collided) {
                s1.applyMove();
            }
        }
    }

    private boolean checkIntersection(int x1, int y1, int size1, int x2, int y2, int size2) {
        return x1 < x2 + size2 && x1 + size1 > x2 && y1 < y2 + size2 && y1 + size1 > y2;
    }

    public Square[] getSquares() {
        return squares;
    }

    public long getWallHits() {
        return totalWallHits;
    }

    public long getSquareHits() {
        return totalSquareHits;
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

    public long currentTime() {
        return System.currentTimeMillis();
    }

    public void run() {
        clrscr();
        cursor_hide();
        view.drawFrame(WIDTH, HEIGHT);

        long startTime = System.currentTimeMillis();

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

            long elapsedSeconds = (currentTime() - startTime) / 1000;

            setfgcolor(7);
            String ui_info = String.format(" Time: %d s | Wall Hits: %d | Square Hits: %d | Press 'q' to quit ",
                    elapsedSeconds, model.getWallHits(), model.getSquareHits());
            gotoxy((WIDTH - ui_info.length()) / 2, HEIGHT);
            print(ui_info);

            System.out.flush();
            delay(simulationSpeed);
        }

        setfgcolor(7);
        clrscr();
        System.out.println("Simulation ended. Final Stats:");
        System.out.println("Total Time: " + String.format("%.3f", (double) (currentTime() - startTime) / 1000) + " s");
        System.out.println("Wall Hits: " + model.getWallHits());
        System.out.println("Square Hits: " + model.getSquareHits());
    }
}

void main() {
    SimulationController app = new SimulationController(30);
    app.run();
}
