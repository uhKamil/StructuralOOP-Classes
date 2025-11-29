import static java.lang.IO.*;
import static term.term.*;

// Model
static class Square {
    private int x, y;
    private int dx, dy;
    private final int size;
    private int color;
    private boolean active = true;
    private boolean cleaned = false;

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

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setColor(int color) {
        this.color = color;
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

    public boolean isCleaned() {
        return cleaned;
    }

    public void setCleaned(boolean cleaned) {
        this.cleaned = cleaned;
    }
}

static class Player {
    public int x = SimulationController.WIDTH / 2;
    public int y = SimulationController.HEIGHT / 2;
    public int prev_x = x, prev_y = y;

    public void movePlayer(moveDirection dir) {
        prev_x = x;
        prev_y = y;
        if (dir == moveDirection.UP) {
            y -= 1;
        } else if (dir == moveDirection.DOWN) {
            y += 1;
        } else if (dir == moveDirection.LEFT) {
            x -= 1;
        } else {
            x += 1;
        }
    }

    public enum moveDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}

static Player createPlayer() {
    return new Player();
}

static class SimulationModel {
    private final Square[] squares;
    private final int width;
    private final int height;
    private final int count;
    private int squaresLeft;

    private int score = 0;

    public SimulationModel(int count, int width, int height) {
        this.width = width;
        this.height = height;
        this.count = count;
        this.squaresLeft = count;
        this.squares = new Square[count];

        for (int i = 0; i < count; i++) {
            this.squares[i] = new Square(width, height);
        }
    }

    public void update(Player plr) {
        for (int i = 0; i < count; i++) {
            Square s1 = squares[i];
            if (s1.active) {
                boolean collided = false;

                int nextX = s1.getNextX();
                int nextY = s1.getNextY();

                // Square-wall collision
                boolean hitWallX = (nextX <= 1 || nextX + s1.getSize() >= width);
                boolean hitWallY = (nextY <= 1 || nextY + s1.getSize() >= height + 1);

                if (hitWallX || hitWallY) {
                    collided = true;
                    if (hitWallX) s1.bounceX();
                    if (hitWallY) s1.bounceY();
                }

                // Square-square collision
                if (!collided) {
                    for (int j = i + 1; j < count; j++) {
                        Square s2 = squares[j];
                        if (checkIntersection(nextX, nextY, s1.getSize(), s2.getNextX(), s2.getNextY(), s2.getSize()) && s2.active) {
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

        // Player-wall collision
        if (plr.x <= 1) {
            plr.x = 2;
        }
        if (plr.y <= 1) {
            plr.y = 2;
        }
        if (plr.x >= width) {
            plr.x = width - 1;
        }
        if (plr.y >= height) {
            plr.y = height - 1;
        }

        // Player-square collision
        for (int i = 0; i < count; i++) {
            Square s = squares[i];

            boolean playerCollided = (plr.x >= s.getX() && plr.x <= s.getX() + s.getSize() - 1) &&
                    (plr.y >= s.getY() && plr.y <= s.getY() + s.getSize() - 1);

            if (playerCollided && s.active) {
                if (s.getSize() == 2) score += 1;
                else if (s.getSize() == 1) score += 4;
                s.setActive(false);
                s.setColor(0);
                squaresLeft -= 1;
            }
        }
    }

    private boolean checkIntersection(int x1, int y1, int size1, int x2, int y2, int size2) {
        return x1 < x2 + size2 && x1 + size1 > x2 && y1 < y2 + size2 && y1 + size1 > y2;
    }

    public Square[] getSquares() {
        return squares;
    }

    public long getScore() {
        return score;
    }

    public boolean getSquaresLeft() {
        return squaresLeft > 0;
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
            if (!s.isCleaned()) {
                framexyc(s.getX(), s.getY(), s.getX() + s.getSize() - 1, s.getY() + s.getSize() - 1, ' ');
                if (!s.active) {
                    s.setCleaned(true);
                }
            }
        }
    }

    public void drawSquares(Square[] squares) {
        for (Square s : squares) {
            if (s.active) {
                setfgcolor(s.getColor());
                framexyc(s.getX(), s.getY(), s.getX() + s.getSize() - 1, s.getY() + s.getSize() - 1, '*');
            }
        }
    }

    public void drawPlayer(Player player) {
        setfgcolor(7);
        gotoxy(player.x, player.y);
        framexyc(player.x, player.y, player.x, player.y, '*');
    }

    public void clearPlayer(Player player) {
        setfgcolor(0);
        gotoxy(player.prev_x, player.prev_y);
        framexyc(player.prev_x, player.prev_y, player.prev_x, player.prev_y, ' ');
    }
}

// Controller
static class SimulationController {
    private final SimulationModel model;
    private final SimulationView view;

    public static final int WIDTH = 120;
    public static final int HEIGHT = 30;
    public static final int roundLength = 60; // round duration in seconds
    private int simulationSpeed = 80;
    private boolean gameActive = true;

    public SimulationController(int squareCount) {
        this.model = new SimulationModel(squareCount, WIDTH, HEIGHT);
        this.view = new SimulationView();
    }

    public long currentTime() {
        return System.currentTimeMillis();
    }

    public boolean handleInput(Player plr) {
        if (keypressed()) {
            String key = readkeystr();
            if (key.equals("q")) {
                return false;
            }
            if (key.equals("+") || key.equals("=")) simulationSpeed = Math.max(10, simulationSpeed - 20);
            if (key.equals("-")) simulationSpeed = Math.min(400, simulationSpeed + 20);
            if (key.equals("arrow_up")) plr.movePlayer(Player.moveDirection.UP);
            else if (key.equals("arrow_dn")) plr.movePlayer(Player.moveDirection.DOWN);
            else if (key.equals("arrow_rt")) plr.movePlayer(Player.moveDirection.RIGHT);
            else if (key.equals("arrow_lt")) plr.movePlayer(Player.moveDirection.LEFT);
        }
        return true;
    }

    public void run() {
        clrscr();
        cursor_hide();
        view.drawFrame(WIDTH, HEIGHT);

        long startTime = System.currentTimeMillis();
        Player player = createPlayer();

        while (roundTime(startTime, roundLength) && gameActive) {
            if (!handleInput(player)) break;
            view.clearSquares(model.getSquares());
            view.clearPlayer(player);
            model.update(player);
            view.drawSquares(model.getSquares());
            view.drawPlayer(player);

            long elapsedSeconds = (currentTime() - startTime) / 1000;

            setfgcolor(7);
            String player_info = String.format(" %d ", roundLength - elapsedSeconds);
            gotoxy((WIDTH - player_info.length()) / 2, 1);
            print(player_info);
            String ui_info = String.format(" Time: %d s | Score: %d | Press 'q' to quit ",
                    elapsedSeconds, model.getScore());
            gotoxy((WIDTH - ui_info.length()) / 2, HEIGHT);
            print(ui_info);

            if (!model.getSquaresLeft()) {
                gameActive = false;
                view.drawFrame(WIDTH, HEIGHT);
                String win_info = "YOU WON!";
                gotoxy((WIDTH - win_info.length()) / 2, HEIGHT / 2);
                println(win_info);
                delay(4000);
                clrscr();
            }

            System.out.flush();
            delay(simulationSpeed);
        }
        if (gameActive) {
            setfgcolor(7);
            view.drawFrame(WIDTH, HEIGHT);
            String lose_info = "GAME OVER";
            gotoxy((WIDTH - lose_info.length()) / 2, HEIGHT / 2);
            print(lose_info);
            delay(4000);
            clrscr();
        }
    }

    public boolean roundTime(long startTime, int roundLength) {
        return ((currentTime() - startTime) / 1000) < roundLength;
    }
}

void main() {
    SimulationController app = new SimulationController(10);
    app.run();
}
