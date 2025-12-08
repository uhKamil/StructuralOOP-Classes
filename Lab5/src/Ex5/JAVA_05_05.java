import static java.lang.IO.*;
import static term.term.*;

static class Square {
    private final int size;
    private int x, y;
    private int dx, dy;
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

    public boolean isActive() {
        return active;
    }

    public boolean isCleaned() {
        return cleaned;
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setCleaned(boolean cleaned) {
        this.cleaned = cleaned;
    }
}

static class Player {
    public enum moveDirection {UP, DOWN, LEFT, RIGHT}

    private final int id;
    private final int color;
    private int x;
    private int y;
    private int prevX;

    private int prevY;
    private int controlMode = 0;

    private Square currentTarget = null;
    private int score = 0;

    private moveDirection moveDir = null;

    public Player(int id) {
        this.id = id;

        final int[] palette = {green, lime, magenta, cyan, red, ltgreen, yellow, white}; // excluded dark colors
        int colorIndex = (int) (Math.random() * palette.length);
        this.color = palette[colorIndex];
    }

    private int getDistance(int targetX, int targetY) {
        return Math.abs(this.getX() - targetX) + Math.abs(this.getY() - targetY);
    }

    public void think(SimulationModel model) {
        if (getControlMode() == 0) return;

        int targetX = -1;
        int targetY = -1;

        // --- Algorithm 1: Nearest Neighbor ---
        if (getControlMode() == 1) {
            Square bestSquare = model.findClosestSquare(this);
            if (bestSquare != null) {
                targetX = bestSquare.getX();
                targetY = bestSquare.getY();
            }

            // Prioritise X moves
//            if (this.x < targetX) this.move_dir = moveDirection.RIGHT;
//            else if (this.x > targetX) this.move_dir = moveDirection.LEFT;
//            else if (this.y < targetY) this.move_dir = moveDirection.DOWN;
//            else if (this.y > targetY) this.move_dir = moveDirection.UP;            

            // Prioritise Y moves
            if (this.getY() < targetY) this.setMoveDir(moveDirection.DOWN);
            else if (this.getY() > targetY) this.setMoveDir(moveDirection.UP);
            else if (this.getX() < targetX) this.setMoveDir(moveDirection.RIGHT);
            else if (this.getX() > targetX) this.setMoveDir(moveDirection.LEFT);
        }

        // --- Algorithm 2: Chase ---
        else if (getControlMode() == 2) {
            boolean needNewTarget = (currentTarget == null) || (!currentTarget.isActive());

            if (needNewTarget) {
                currentTarget = model.findClosestSquare(this);
            }

            if (currentTarget != null) {
                targetX = currentTarget.getX();
                targetY = currentTarget.getY();
            }

            if (targetX != -1 && targetY != -1) {
                this.setMoveDir(null);

                int dx = targetX - this.getX();
                int dy = targetY - this.getY();

                if (dx == 0 && dy == 0) return;

                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) this.setMoveDir(moveDirection.RIGHT);
                    else this.setMoveDir(moveDirection.LEFT);
                } else {
                    if (dy > 0) this.setMoveDir(moveDirection.DOWN);
                    else this.setMoveDir(moveDirection.UP);
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPrevX() {
        return prevX;
    }

    public void setPrevX(int prevX) {
        this.prevX = prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    public void setPrevY(int prevY) {
        this.prevY = prevY;
    }

    public int getColor() {
        return color;
    }

    public int getControlMode() {
        return controlMode;
    }

    public int getScore() {
        return score;
    }

    public moveDirection getMoveDir() {
        return moveDir;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMoveDir(moveDirection moveDir) {
        this.moveDir = moveDir;
    }

    public void setControlMode(int controlMode) {
        this.controlMode = controlMode;
    }
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

        if (players.length == 1) players[0].setX(width / 2);
        else {
            for (int i = 0; i < players.length; i++) {
                if (i % 2 == 0) {
                    players[i].setX(width / 4);
                    players[i].setPrevX(width / 4);
                    players[i].setY(height / 2);
                    players[i].setPrevY(height / 2);
                } else {
                    players[i].setX(width * 3 / 4);
                    players[i].setPrevX(width * 3 / 4);
                    players[i].setY(height / 2);
                    players[i].setPrevY(height / 2);
                }
            }
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
        plr.setPrevX(plr.getX());
        plr.setPrevY(plr.getY());

        if (plr.getMoveDir() == null) return 0;

        if (plr.getMoveDir() == Player.moveDirection.UP) plr.setY(plr.getY() - 1);
        else if (plr.getMoveDir() == Player.moveDirection.DOWN) plr.setY(plr.getY() + 1);
        else if (plr.getMoveDir() == Player.moveDirection.LEFT) plr.setX(plr.getX() - 1);
        else if (plr.getMoveDir() == Player.moveDirection.RIGHT) plr.setX(plr.getX() + 1);
        // Commenting this creates the snake effect
//        plr.setMoveDir(null);

        // Player-wall collision
        if (plr.getX() <= 1) plr.setX(2);
        if (plr.getY() <= 1) plr.setY(2);
        if (plr.getX() >= width) plr.setX(width - 1);
        if (plr.getY() >= height) plr.setY(height - 1);

        // Player-square collision
        for (int i = 0; i < count; i++) {
            Square s = squares[i];

            boolean playerCollided = (plr.getX() >= s.getX() && plr.getX() <= s.getX() + s.getSize() - 1) &&
                    (plr.getY() >= s.getY() && plr.getY() <= s.getY() + s.getSize() - 1);

            if (playerCollided && s.active) {
                if (s.getSize() == 2) plr.setScore(plr.getScore() + 1);
                else if (s.getSize() == 1) plr.setScore(plr.getScore() + 4);
                s.setActive(false);
                s.setColor(0);
                squaresLeft -= 1;
                return plr.getId();
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
            if (i == 0) max = plr.getScore();
            if (i > 0 && max != plr.getScore()) {
                equalScore = 0;
            }
            if (plr.getScore() > max) {
                max = plr.getScore();
                max_id = plr.getId();
            }
            i += 1;
        }
        return new int[]{max, max_id, equalScore};
    }

    public Square findClosestSquare(Player plr) {
        double minDist = Double.MAX_VALUE;
        Square bestSquare = null;
        for (Square s : squares) {
            if (s.active) {
                double dist = plr.getDistance(s.x, s.y);
                if (dist < minDist) {
                    minDist = dist;
                    bestSquare = s;
                }
            }
        }
        return bestSquare;
    }

    public void playersThink(SimulationModel model, Player[] players) {
        for (Player plr : players) {
            plr.think(model);
        }
    }
}

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
        setfgcolor(plr.getColor());
        gotoxy(plr.getX(), plr.getY());
        framexyc(plr.getX(), plr.getY(), plr.getX(), plr.getY(), '*');
    }

    public void clearPlayer(Player plr) {
        setfgcolor(0);
        gotoxy(plr.getPrevX(), plr.getPrevY());
        framexyc(plr.getPrevX(), plr.getPrevY(), plr.getPrevX(), plr.getPrevY(), ' ');
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
        } else {
            plr_msg = "Player " + plr_info[1] + " had the most points (" + plr_info[0] + ")";
        }
        gotoxy((model.width - plr_msg.length()) / 2, model.height / 2 + 1);
        println(plr_msg);
    }

    public void updateScoreboard(SimulationModel model, char state) {
        drawFrame(model.width, model.height);
        String msg = state == 'w' ? "YOU WON" : "GAME OVER";
        gotoxy((model.width - msg.length()) / 2, model.height / 2);
        print(msg);
        displayUserInfo(model);
    }

    public void uiInfo(SimulationModel model, Player[] players, long timeLeft) {
        setfgcolor(7);
        int plr_index = 1;
        for (Player plr : players) {
            String player_info = String.format(" %d ", plr.getScore());
            gotoxy((model.width - player_info.length()) * plr_index / (players.length + 1), 1);
            print(player_info);
            plr_index += 1;
        }
        String ui_info = String.format(" Time left: %d s | Press 'q' to quit ",
                timeLeft);
        gotoxy((model.width - ui_info.length()) / 2, model.height);
        print(ui_info);
    }
}

static class SimulationController {
    private final SimulationModel model;
    private final SimulationView view;

    public static final int roundLength = 60; // round duration in seconds
    public final int simulationSpeed = 80;
    private boolean gameActive = true;

    static class KeyBinding {
        public String[] keys;
        public int playerIndex;

        public KeyBinding(String[] keys, int playerIndex) {
            this.keys = keys;
            this.playerIndex = playerIndex;
        }
    }

    private final KeyBinding[] controls = {
        new KeyBinding(new String[]{"arrow_up", "arrow_dn", "arrow_lt", "arrow_rt"}, 0),
        new KeyBinding(new String[]{"w", "s", "a", "d"}, 1),
        new KeyBinding(new String[]{"i", "j", "k", "l"}, 2)
    };

    public SimulationController(int squareCount, Player[] players) {
        this.model = new SimulationModel(squareCount, 120, 30, players);
        this.view = new SimulationView();
    }

    public long currentTime() {
        return System.currentTimeMillis();
    }

    public void handleInput(Player[] players) {
        while (keypressed()) {
            String key = readkeystr();
            int i = 1;
            for (KeyBinding binding : controls) {
                if (i > players.length) break;
                int j = 0;
                for (String k : binding.keys) {
                    if (key.equals(k)) {
                        if (players[binding.playerIndex].getControlMode() == 0) {
                            if (j == 0) players[binding.playerIndex].setMoveDir(Player.moveDirection.UP);
                            else if (j == 1) players[binding.playerIndex].setMoveDir(Player.moveDirection.DOWN);
                            else if (j == 2) players[binding.playerIndex].setMoveDir(Player.moveDirection.LEFT);
                            else players[binding.playerIndex].setMoveDir(Player.moveDirection.RIGHT);
                        }
                    }
                    j += 1;
                }
                i += 1;
            }
            if (key.equals("q")) gameActive = false;
        }
    }

    public void run() {
        loadConfiguration(model.players);

        clrscr();
        cursor_hide();

        view.drawFrame(model.width, model.height);
        long startTime = System.currentTimeMillis();

        while (roundTime(startTime, roundLength) && gameActive) {
            handleInput(model.players);
            view.renderClear(model);

            boolean collision = model.updateSquares();
            model.playersThink(model, model.players);
            int eatEvent1 = model.updatePlayer(model.players[0]);
            int eatEvent2 = model.updatePlayer(model.players[1]);

            if (eatEvent1 == 1) playSound(600, 100);
            else if (eatEvent2 == 2) playSound(800, 100);
            else if (collision) playSound(400, 50);

            view.renderDraw(model);
            long timeLeft = roundLength - (currentTime() - startTime) / 1000;

            view.uiInfo(model, model.players, timeLeft);

            if (!model.getSquaresLeft()) {
                gameActive = false;
                view.updateScoreboard(model, 'w');
            }
            System.out.flush();
            delay(simulationSpeed);
        }
        if (model.getSquaresLeft()) view.updateScoreboard(model, 'l');
        delay(4000);
        clrscr();
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

    private void loadConfiguration(Player[] players) {
//        String fileName = "conf.cfg";
        String fileName = "Ex5" + File.separator + "conf.cfg";
        File file = new File(fileName);

        if (!file.exists()) {
            try {
                try (Writer writer = new FileWriter(file)) {
                    for (Player plr : players) {
                        writer.write("Player " + plr.getId() + ": 0\n");
                    }
                }
                System.out.println("Created default conf.cfg");
            } catch (IOException e) {
                System.out.println("Error creating config: " + e.getMessage());
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(":");
                if (parts.length > 1) {
                    String label = parts[0].trim(); // should throw sth like "Player 1"
                    String value = parts[1].trim(); // should only throw a number

                    if (label.toLowerCase().startsWith("player")) {
                        try {
                            String idStr = label.toLowerCase().replace("player", "").trim();

                            int plr_id = Integer.parseInt(idStr);
                            int mode = Integer.parseInt(value);

                            if (mode < 0 || mode > 2) {
                                mode = 0;
                            }

                            for (Player plr : players) {
                                if (plr.getId() == plr_id) {
                                    plr.setControlMode(mode);
                                    System.out.println("Player " + plr.getId() + " mode set to " + mode);
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid line: " + line);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Config read error. Starting with defaults. Details: " + e.getMessage());
        }
    }
}

void main() {
    Player player1 = new Player(1);
    Player player2 = new Player(2);
    Player[] players = {player1, player2};

    new SimulationController(10, players).run();
}
