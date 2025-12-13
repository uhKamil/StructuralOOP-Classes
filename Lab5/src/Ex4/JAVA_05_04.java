// Kamil Wolbach (280161)
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
    public int id;
    public int x, y;
    public int prev_x, prev_y;
    public int color;

    public String keyUp, keyDown, keyLeft, keyRight;
    public int score = 0;
    public moveDirection move_dir = null;

    public int[] palette = {1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15};


    public void handleInput(String key) {
        if (key.equals(keyUp)) move_dir = moveDirection.UP;
        else if (key.equals(keyDown)) move_dir = moveDirection.DOWN;
        else if (key.equals(keyLeft)) move_dir = moveDirection.LEFT;
        else if (key.equals(keyRight)) move_dir = moveDirection.RIGHT;
    }

    public enum moveDirection {UP, DOWN, LEFT, RIGHT}
}

static Player createPlayer(int id, int x, String keyUp, String keyDown, String keyLeft, String keyRight) {
    Player plr = new Player();
    plr.id = id;
    plr.x = x;
    plr.y = SimulationController.HEIGHT / 2;
    plr.prev_x = plr.x;
    plr.prev_y = plr.y;
    plr.keyUp = keyUp;
    plr.keyDown = keyDown;
    plr.keyLeft = keyLeft;
    plr.keyRight = keyRight;
    int colorIndex = (int) (Math.random() * plr.palette.length);
    plr.color = plr.palette[colorIndex];
    return plr;
}

static class SimulationModel {
    public final Player[] players;
    private final Square[] squares;
    private final int width;
    private final int height;
    private final int count;
    private int squaresLeft;

    public SimulationModel(int count, int width, int height, Player[] players) {
        this.width = width;
        this.height = height;
        this.count = count;
        this.squaresLeft = count;
        this.squares = new Square[count];
        this.players = players;

        for (int i = 0; i < count; i++) {
            this.squares[i] = new Square(width, height);
        }
    }

    public boolean updateSquares() {
        boolean collisionEvent = false;
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
                    collisionEvent = true;
                    if (hitWallX) s1.bounceX();
                    if (hitWallY) s1.bounceY();
                }

                // Square-square collision
                if (!collided) {
                    for (int j = i + 1; j < count; j++) {
                        Square s2 = squares[j];
                        if (checkIntersection(nextX, nextY, s1.getSize(), s2.getNextX(), s2.getNextY(), s2.getSize()) && s2.active) {
                            collided = true;
                            collisionEvent = true;
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
        return collisionEvent;
    }

    public int updatePlayer(Player plr) {
        plr.prev_x = plr.x;
        plr.prev_y = plr.y;

        if (plr.move_dir == null) return 0;
        
        if (plr.move_dir == Player.moveDirection.UP) plr.y--;
        else if (plr.move_dir == Player.moveDirection.DOWN) plr.y++;
        else if (plr.move_dir == Player.moveDirection.LEFT) plr.x--;
        else if (plr.move_dir == Player.moveDirection.RIGHT) plr.x++;
        // Commenting this creates the snake effect
//        plr.move_dir = null;

        // Player-wall collision
        if (plr.x <= 1) plr.x = 2;
        if (plr.y <= 1) plr.y = 2;
        if (plr.x >= width) plr.x = width - 1;
        if (plr.y >= height) plr.y = height - 1;

        // Player-square collision
        for (int i = 0; i < count; i++) {
            Square s = squares[i];

            boolean playerCollided = (plr.x >= s.getX() && plr.x <= s.getX() + s.getSize() - 1) &&
                    (plr.y >= s.getY() && plr.y <= s.getY() + s.getSize() - 1);

            if (playerCollided && s.active) {
                if (s.getSize() == 2) plr.score += 1;
                else if (s.getSize() == 1) plr.score += 4;
                s.setActive(false);
                s.setColor(0);
                squaresLeft -= 1;
                return plr.id;
            }
        }
        return 0;
    }

    private boolean checkIntersection(int x1, int y1, int size1, int x2, int y2, int size2) {
        return x1 < x2 + size2 && x1 + size1 > x2 && y1 < y2 + size2 && y1 + size1 > y2;
    }

    public Square[] getSquares() {
        return squares;
    }

    public boolean getSquaresLeft() {
        return squaresLeft > 0;
    }

    public int[] getWinnerInfo() {
        int max = 0;
        int max_id = 1;
        int equalScore = 1;
        int i = 0;
        for (Player plr : players) {
            if (i == 0) max = plr.score;
            if (i > 0 && max != plr.score) {
                equalScore = 0;
            }
            if (plr.score > max) {
                max = plr.score;
                max_id = plr.id;
            }
            i += 1;
        }
        return new int[]{max, max_id, equalScore};
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

    public void drawPlayer(Player plr) {
        setfgcolor(plr.color);
        gotoxy(plr.x, plr.y);
        framexyc(plr.x, plr.y, plr.x, plr.y, '*');
    }

    public void clearPlayer(Player plr) {
        setfgcolor(0);
        gotoxy(plr.prev_x, plr.prev_y);
        framexyc(plr.prev_x, plr.prev_y, plr.prev_x, plr.prev_y, ' ');
    }

    public void renderClear(SimulationModel model) {
        clearSquares(model.getSquares());
        for (Player plr : model.players) clearPlayer(plr);
    }

    public void renderDraw(SimulationModel model) {
        drawSquares(model.getSquares());
        for (Player plr : model.players) drawPlayer(plr);
    }

    public void displayUserInfo(SimulationModel model) {
        int[] plr_info = model.getWinnerInfo();
        String plr_msg;
        if (plr_info[2] == 1) {
            plr_msg = "Players had the same amount of points (" + plr_info[0] + ")";
        }
        else {
            plr_msg = "Player " + plr_info[1] + " had the most points (" + plr_info[0] + ")";
        }
        gotoxy((SimulationController.WIDTH - plr_msg.length()) / 2, SimulationController.HEIGHT / 2 + 1);
        println(plr_msg);
    }
}

// Controller
static class SimulationController {
    private final SimulationModel model;
    private final SimulationView view;

    public static final int WIDTH = 120;
    public static final int HEIGHT = 30;
    public static final int roundLength = 60; // round duration in seconds
    public final int simulationSpeed = 80;
    private boolean gameActive = true;

    Player player1 = createPlayer(1, SimulationController.WIDTH / 4, "arrow_up", "arrow_dn", "arrow_lt", "arrow_rt");
    Player player2 = createPlayer(2, SimulationController.WIDTH * 3 / 4, "w", "s", "a", "d");

    Player[] players = {player1, player2};

    public SimulationController(int squareCount) {
        this.model = new SimulationModel(squareCount, WIDTH, HEIGHT, players);
        this.view = new SimulationView();
    }

    public long currentTime() {
        return System.currentTimeMillis();
    }

    public void handleInput() {
        while (keypressed()) {
            String key = readkeystr();
            for (Player plr : players) {
                plr.handleInput(key);
            }
            if (key.equals("q")) gameActive = false;
        }
    }

    public void run() {
        clrscr();
        cursor_hide();
        view.drawFrame(WIDTH, HEIGHT);

        long startTime = System.currentTimeMillis();

        while (roundTime(startTime, roundLength) && gameActive) {
            handleInput();
            view.renderClear(model);

            boolean collision = model.updateSquares();
            int eatEvent1 = model.updatePlayer(player1);
            int eatEvent2 = model.updatePlayer(player2);

            if (eatEvent1 == 1) playSound(600, 100);
            else if (eatEvent2 == 2) playSound(800, 100);
            else if (collision) playSound(400, 50);

            view.renderDraw(model);
            long timeLeft = roundLength - (currentTime() - startTime) / 1000;

            setfgcolor(7);
            int plr_index = 1;
            for (Player plr : players) {
                String player_info = String.format(" %d ", plr.score);
                gotoxy((WIDTH - player_info.length()) * plr_index / (players.length + 1), 1);
                print(player_info);
                plr_index += 1;
            }
            String ui_info = String.format(" Time left: %d s | Press 'q' to quit ",
                    timeLeft);
            gotoxy((WIDTH - ui_info.length()) / 2, HEIGHT);
            print(ui_info);

            if (!model.getSquaresLeft()) {
                gameActive = false;
                view.drawFrame(WIDTH, HEIGHT);
                String win_info = "YOU WON!";
                gotoxy((WIDTH - win_info.length()) / 2, HEIGHT / 2);
                println(win_info);
                view.displayUserInfo(model);
                delay(4000);
                clrscr();
            }

            System.out.flush();
            delay(simulationSpeed);
        }
        if (model.getSquaresLeft()) {
            setfgcolor(7);
            view.drawFrame(WIDTH, HEIGHT);
            String lose_info = "GAME OVER";
            gotoxy((WIDTH - lose_info.length()) / 2, HEIGHT / 2);
            print(lose_info);
            view.displayUserInfo(model);
            delay(4000);
            clrscr();
        }
    }

    public boolean roundTime(long startTime, int roundLength) {
        return ((currentTime() - startTime) / 1000) < roundLength;
    }

    public static void playSound(final int freq, final int durationMs) {
        new Thread(() -> {
            try {
                sound(freq, durationMs);
            } catch (Exception _) {
            }
        }).start();
    }
}

void main() {
    new SimulationController(10).run();
}
