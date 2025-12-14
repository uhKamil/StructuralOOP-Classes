// Kamil Wolbach (280161)
package Ex6;

import java.io.*;

import static java.lang.IO.*;
import static term.term.*;

public class JAVA_06_06 {

    // ==========================================
    // Data Structures
    // ==========================================

    public static class Square {
        private double size;
        private double gravity;
        private double x, y;
        private double dx, dy;
        private int color;
        private boolean active = true;
        private boolean cleaned = false;
    }

    public static class Player {
        public enum moveDirection {UP, DOWN, LEFT, RIGHT}

        private int id;
        private int color;
        private int x, y;
        private int prevX, prevY;
        private int controlMode = 0;
        private Square currentTarget = null;
        private int score = 0;
        private moveDirection moveDir = null;
    }

    public static class SimulationModel {
        public Player[] players;
        private Square[] squares;
        private int width;
        private int height;
        private int count;
        private int squaresLeft;
    }

    public static class SimulationController {
        private SimulationModel model;
        public static final int roundLength = 60;
        public final int simulationSpeed = 80;
        public boolean gameActive = true;
        public KeyBinding[] controls;
    }

    public static class KeyBinding {
        public String[] keys;
        public int playerIndex;
    }

    public record discreteCoords(int sX, int sY) {
    }

    // ==========================================
    // Constructors
    // ==========================================

    public static Square createSquare(int boardWidth, int boardHeight, double gravity) {
        Square s = new Square();
        s.size = (Math.random() < 0.5) ? 1 : 2;
        s.gravity = gravity;
        s.x = 3 + (int) (Math.random() * (boardWidth - 3 - s.size));
        s.y = 3 + (int) (Math.random() * (boardHeight - 3 - s.size));
        randomizeVelocity(s, boardHeight);

        int[] palette = {1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15};
        s.color = palette[(int) (Math.random() * palette.length)];
        return s;
    }

    public static Player createPlayer(int id) {
        Player p = new Player();
        p.id = id;
        final int[] palette = {green, lime, magenta, cyan, red, ltgreen, yellow, white};
        p.color = palette[(int) (Math.random() * palette.length)];
        return p;
    }

    public static SimulationModel createModel(int count, int width, int height, Player[] players, double gravity) {
        SimulationModel model = new SimulationModel();
        model.width = width;
        model.height = height;
        model.count = count;
        model.squaresLeft = count;
        model.squares = new Square[count];
        model.players = players;

        for (int i = 0; i < count; i++) {
            model.squares[i] = createSquare(width, height, gravity);
        }

        if (players.length == 1) {
            setX(players[0], width / 2);
        } else {
            for (int i = 0; i < players.length; i++) {
                if (i % 2 == 0) {
                    setX(players[i], width / 4);
                    setPrevX(players[i], width / 4);
                } else {
                    setX(players[i], width * 3 / 4);
                    setPrevX(players[i], width * 3 / 4);
                }
                setY(players[i], height / 2);
                setPrevY(players[i], height / 2);
            }
        }
        return model;
    }

    public static SimulationController createController(int squareCount, Player[] players) {
        SimulationController controller = new SimulationController();
        controller.model = createModel(squareCount, 120, 30, players, 0.2);

        controller.controls = new KeyBinding[]{
                createKeyBinding(new String[]{"arrow_up", "arrow_dn", "arrow_lt", "arrow_rt"}, 0),
                createKeyBinding(new String[]{"w", "s", "a", "d"}, 1),
                createKeyBinding(new String[]{"i", "j", "k", "l"}, 2)
        };
        return controller;
    }

    public static KeyBinding createKeyBinding(String[] keys, int playerIndex) {
        KeyBinding b = new KeyBinding();
        b.keys = keys;
        b.playerIndex = playerIndex;
        return b;
    }

    // ==========================================
    // Logic functions for Squares
    // ==========================================

    public static void randomizeVelocity(Square s, int boardHeight) {
        s.dx = 0;
        // Assignment of potential energy
        s.dy = s.gravity * (boardHeight - s.y) / 5;
        // Assignment of kinetic energy
        while (s.dx == 0) s.dx = (int) (Math.random() * 3) - 1;
        s.dy += (double) (int) ((Math.random() * 3) - 2) / 2;
    }

    public static void bounceX(Square s) {
        s.dx = -s.dx;
        if (s.dx == 0) s.dx = (Math.random() < 0.5) ? 1 : -1;
    }

    public static void bounceY(Square s) {
        s.dy = -s.dy;
    }

    public static void applyMove(Square s) {
        s.x += s.dx;
        s.dy += s.gravity;
        s.y += s.dy;
    }

    public static double getNextX(Square s) {
        return s.x + s.dx;
    }

    public static double getNextY(Square s) {
        return s.y + (s.gravity + s.dy);
    }

    public static double getX(Square s) {
        return s.x;
    }

    public static double getY(Square s) {
        return s.y;
    }

    public static void setX(Square s, double x) {
        s.x = x;
    }

    public static void setY(Square s, double y) {
        s.y = y;
    }

    public static double getSize(Square s) {
        return s.size;
    }

    public static int getColor(Square s) {
        return s.color;
    }

    public static void setColor(Square s, int color) {
        s.color = color;
    }

    public static boolean isActive(Square s) {
        return s.active;
    }

    public static void setActive(Square s, boolean active) {
        s.active = active;
    }

    public static boolean isCleaned(Square s) {
        return s.cleaned;
    }

    public static void setCleaned(Square s, boolean cleaned) {
        s.cleaned = cleaned;
    }

    // ==========================================
    // Logic functions for Player
    // ==========================================

    public static int getDistance(Player player, double targetX, double targetY) {
        return Math.abs(getX(player) - (int) targetX) + Math.abs(getY(player) - (int) targetY);
    }

    public static void think(SimulationModel model, Player player) {
        if (getControlMode(player) == 0) return;

        int targetX = -1;
        int targetY = -1;

        // --- Algorithm 1: Nearest Neighbor ---
        if (getControlMode(player) == 1) {
            Square bestSquare = findClosestSquare(model, player);
            if (bestSquare != null) {
                targetX = (int) getX(bestSquare);
                targetY = (int) getY(bestSquare);
            }
            if (getY(player) < targetY) setMoveDir(player, Player.moveDirection.DOWN);
            else if (getY(player) > targetY) setMoveDir(player, Player.moveDirection.UP);
            else if (getX(player) < targetX) setMoveDir(player, Player.moveDirection.RIGHT);
            else if (getX(player) > targetX) setMoveDir(player, Player.moveDirection.LEFT);
        }
        // --- Algorithm 2 ---
        else if (getControlMode(player) == 2) {
            boolean needNewTarget = (player.currentTarget == null) || (!isActive(player.currentTarget));
            if (needNewTarget) {
                player.currentTarget = findClosestSquare(model, player);
            }
            if (player.currentTarget != null) {
                targetX = (int) getX(player.currentTarget);
                targetY = (int) getY(player.currentTarget);
            }
            if (targetX != -1 && targetY != -1) {
                setMoveDir(player, null);
                int dx = targetX - getX(player);
                int dy = targetY - getY(player);
                if (dx == 0 && dy == 0) return;

                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) setMoveDir(player, Player.moveDirection.RIGHT);
                    else setMoveDir(player, Player.moveDirection.LEFT);
                } else {
                    if (dy > 0) setMoveDir(player, Player.moveDirection.DOWN);
                    else setMoveDir(player, Player.moveDirection.UP);
                }
            }
        }
    }

    public static int getId(Player p) {
        return p.id;
    }

    public static int getX(Player p) {
        return p.x;
    }

    public static void setX(Player p, int x) {
        p.x = x;
    }

    public static int getY(Player p) {
        return p.y;
    }

    public static void setY(Player p, int y) {
        p.y = y;
    }

    public static int getPrevX(Player p) {
        return p.prevX;
    }

    public static void setPrevX(Player p, int x) {
        p.prevX = x;
    }

    public static int getPrevY(Player p) {
        return p.prevY;
    }

    public static void setPrevY(Player p, int y) {
        p.prevY = y;
    }

    public static int getColor(Player p) {
        return p.color;
    }

    public static int getControlMode(Player p) {
        return p.controlMode;
    }

    public static void setControlMode(Player p, int mode) {
        p.controlMode = mode;
    }

    public static int getScore(Player p) {
        return p.score;
    }

    public static void setScore(Player p, int s) {
        p.score = s;
    }

    public static Player.moveDirection getMoveDir(Player p) {
        return p.moveDir;
    }

    public static void setMoveDir(Player p, Player.moveDirection dir) {
        p.moveDir = dir;
    }

    // ==========================================
    // Logic functions for Model
    // ==========================================

    public static boolean updateSquares(SimulationModel model) {
        boolean collisionEvent = false;

        for (int i = 0; i < model.count; i++) {
            Square s1 = model.squares[i];
            if (!isActive(s1)) continue;

            applyMove(s1);

            double currX = getX(s1);
            double currY = getY(s1);
            double size = getSize(s1);

            boolean collided = false;

            boolean hitWallLeft = currX <= 1;
            boolean hitWallRight = (currX + size >= model.width);
            boolean hitCeiling = currY <= 2;
            boolean hitFloor = (currY + size >= model.height + 1);

            if (hitWallLeft || hitWallRight || hitCeiling || hitFloor) {
                collisionEvent = true;
                collided = true;

                if (hitWallLeft) {
                    bounceX(s1);
                    setX(s1, 2);
                } else if (hitWallRight) {
                    bounceX(s1);
                    setX(s1, model.width - size - 1);
                }

                if (hitCeiling) {
                    bounceY(s1);
                    double excess = 2 - currY;
                    setY(s1, 2 + excess);
                } else if (hitFloor) {
                    bounceY(s1);
//                    setY(s1, model.height - size); // simplified collision (no handling of excess, so some energy might be lost)
                    double excess = (currY + size) - (model.height + 1);
                    if (model.height + 1 - size - excess >= model.height + 1) setY(s1, model.height - size - excess); // might cause some small loss of energy, but I think this case will rarely occur
                    else setY(s1, (model.height + 1) - size - excess); // no loss of energy
                }
            }

            // Square-square collision
            if (!collided) {
                for (int j = i + 1; j < model.count; j++) {
                    Square s2 = model.squares[j];
                    if (!isActive(s2)) continue;
                    if (checkIntersection(getX(s1), getY(s1), getSize(s1),
                            getX(s2), getY(s2), getSize(s2))) {
                        collisionEvent = true;
                        bounceX(s1);
                        bounceY(s1);
                        bounceX(s2);
                        bounceY(s2);
                    }
                }
            }
        }
        return collisionEvent;
    }

    public static int updatePlayer(SimulationModel model, Player plr) {
        setPrevX(plr, getX(plr));
        setPrevY(plr, getY(plr));

        if (getMoveDir(plr) == null) return 0;

        if (getMoveDir(plr) == Player.moveDirection.UP) setY(plr, getY(plr) - 1);
        else if (getMoveDir(plr) == Player.moveDirection.DOWN) setY(plr, getY(plr) + 1);
        else if (getMoveDir(plr) == Player.moveDirection.LEFT) setX(plr, getX(plr) - 1);
        else if (getMoveDir(plr) == Player.moveDirection.RIGHT) setX(plr, getX(plr) + 1);
        // Commenting this creates the snake effect
//        plr.setMoveDir(null);

        // Player-wall collision
        if (getX(plr) <= 1) setX(plr, 2);
        if (getY(plr) <= 1) setY(plr, 2);
        if (getX(plr) >= model.width) setX(plr, model.width - 1);
        if (getY(plr) >= model.height) setY(plr, model.height - 1);

        // Player-square collision
        for (int i = 0; i < model.count; i++) {
            Square s = model.squares[i];

            boolean playerCollided = (getX(plr) >= getX(s) && getX(plr) <= getX(s) + getSize(s) - 1) &&
                    (getY(plr) >= getY(s) && getY(plr) <= getY(s) + getSize(s) - 1);

            if (playerCollided && isActive(s)) {
                if (getSize(s) == 2) setScore(plr, getScore(plr) + 1);
                else if (getSize(s) == 1) setScore(plr, getScore(plr) + 4);

                setActive(s, false);
                setColor(s, 0);
                model.squaresLeft -= 1;
                return getId(plr);
            }
        }
        return 0;
    }

    private static boolean checkIntersection(double x1, double y1, double size1, double x2, double y2, double size2) {
        return x1 < x2 + size2 && x1 + size1 > x2 && y1 < y2 + size2 && y1 + size1 > y2;
    }

    public static Square findClosestSquare(SimulationModel model, Player plr) {
        double minDist = Double.MAX_VALUE;
        Square bestSquare = null;
        for (Square s : model.squares) {
            if (isActive(s)) {
                double dist = getDistance(plr, getX(s), getY(s));
                if (dist < minDist) {
                    minDist = dist;
                    bestSquare = s;
                }
            }
        }
        return bestSquare;
    }

    public static void playersThink(SimulationModel model) {
        for (Player plr : model.players) {
            think(model, plr);
        }
    }

    public static int[] getWinnerInfo(SimulationModel model) {
        int max = 0;
        int max_id = 1;
        int equalScore = 1;
        int i = 0;
        for (Player plr : model.players) {
            if (i == 0) max = getScore(plr);
            if (i > 0 && max != getScore(plr)) {
                equalScore = 0;
            }
            if (getScore(plr) > max) {
                max = getScore(plr);
                max_id = getId(plr);
            }
            i += 1;
        }
        return new int[]{max, max_id, equalScore};
    }

    // ==========================================
    // VIEW
    // ==========================================

    public static void drawFrame(int width, int height) {
        setfgcolor(7);
        framexyc(1, 1, width, height, '#');
    }

    public static void clearSquares(Square[] squares, int boardWidth, int boardHeight) {
        setfgcolor(0);
        for (Square s : squares) {
            if (!isCleaned(s)) {
                discreteCoords c = getDiscreteCoords(boardWidth, boardHeight, s);
                framexyc(c.sX(), c.sY(), (int) (c.sX() + getSize(s) - 1), (int) (c.sY() + getSize(s) - 1), ' ');
                if (!isActive(s)) {
                    setCleaned(s, true);
                }
            }
        }
    }

    private static discreteCoords getDiscreteCoords(int boardWidth, int boardHeight, Square s) {
        int sX, sY;
        if (getX(s) >= boardWidth - 1) sX = (int) (boardWidth - getSize(s));
        else sX = (int) getX(s);
        if (getY(s) >= boardHeight - 1) sY = (int) (boardHeight - getSize(s));
        else sY = (int) getY(s);
        return new discreteCoords(sX, sY);
    }

    public static void drawSquares(Square[] squares, int boardWidth, int boardHeight) {
        for (Square s : squares) {
            if (isActive(s)) {
                setfgcolor(getColor(s));
                discreteCoords c = getDiscreteCoords(boardWidth, boardHeight, s);
                framexyc(c.sX(), c.sY(), (int) (c.sX() + getSize(s) - 1), (int) (c.sY() + getSize(s) - 1), '*');
            }
        }
    }

    public static void drawPlayer(Player plr) {
        setfgcolor(getColor(plr));
        gotoxy(getX(plr), getY(plr));
        framexyc(getX(plr), getY(plr), getX(plr), getY(plr), '*');
    }

    public static void clearPlayer(Player plr) {
        setfgcolor(0);
        gotoxy(getPrevX(plr), getPrevY(plr));
        framexyc(getPrevX(plr), getPrevY(plr), getPrevX(plr), getPrevY(plr), ' ');
    }

    public static void renderClear(SimulationModel model) {
        clearSquares(model.squares, model.width, model.height);
        for (Player plr : model.players) clearPlayer(plr);
    }

    public static void renderDraw(SimulationModel model) {
        drawSquares(model.squares, model.width, model.height);
        for (Player plr : model.players) drawPlayer(plr);
    }

    public static void displayUserInfo(SimulationModel model) {
        int[] plr_info = getWinnerInfo(model);
        String plr_msg;
        if (plr_info[2] == 1) {
            plr_msg = "Players had the same amount of points (" + plr_info[0] + ")";
        } else {
            plr_msg = "Player " + plr_info[1] + " had the most points (" + plr_info[0] + ")";
        }
        gotoxy((model.width - plr_msg.length()) / 2, model.height / 2 + 1);
        println(plr_msg);
    }

    public static void updateScoreboard(SimulationModel model, char state) {
        drawFrame(model.width, model.height);
        String msg = state == 'w' ? "YOU WON" : "GAME OVER";
        gotoxy((model.width - msg.length()) / 2, model.height / 2);
        print(msg);
        displayUserInfo(model);
    }

    public static void uiInfo(SimulationModel model, long timeLeft) {
        setfgcolor(7);
        int plr_index = 1;
        for (Player plr : model.players) {
            String player_info = String.format(" %d ", getScore(plr));
            gotoxy((model.width - player_info.length()) * plr_index / (model.players.length + 1), 1);
            print(player_info);
            plr_index += 1;
        }
        String ui_info = String.format(" Time left: %d s | Press 'q' to quit ", timeLeft);
        gotoxy((model.width - ui_info.length()) / 2, model.height);
        print(ui_info);
    }

    // ==========================================
    // CONTROLLER
    // ==========================================

    public static long currentTime() {
        return System.currentTimeMillis();
    }

    public static void handleInput(SimulationController controller) {
        while (keypressed()) {
            String key = readkeystr();
            int i = 1;
            for (KeyBinding binding : controller.controls) {
                if (i > controller.model.players.length) break;
                int j = 0;
                for (String k : binding.keys) {
                    if (key.equals(k)) {
                        Player p = controller.model.players[binding.playerIndex];
                        if (getControlMode(p) == 0) {
                            if (j == 0) setMoveDir(p, Player.moveDirection.UP);
                            else if (j == 1) setMoveDir(p, Player.moveDirection.DOWN);
                            else if (j == 2) setMoveDir(p, Player.moveDirection.LEFT);
                            else setMoveDir(p, Player.moveDirection.RIGHT);
                        }
                    }
                    j += 1;
                }
                i += 1;
            }
            if (key.equals("q")) controller.gameActive = false;
        }
    }

    public static void runGame(SimulationController controller) {
        loadConfiguration(controller.model.players);
        clrscr();
        cursor_hide();

        drawFrame(controller.model.width, controller.model.height);
        long startTime = System.currentTimeMillis();

        while (roundTime(startTime, SimulationController.roundLength) && controller.gameActive) {
            handleInput(controller);
            renderClear(controller.model);

            boolean collision = updateSquares(controller.model);
            playersThink(controller.model);
            int eatEvent1 = updatePlayer(controller.model, controller.model.players[0]);
            int eatEvent2 = updatePlayer(controller.model, controller.model.players[1]);

            if (eatEvent1 == 1) playSound(600, 100);
            else if (eatEvent2 == 2) playSound(800, 100);
            else if (collision) playSound(400, 50);

            renderDraw(controller.model);
            long timeLeft = SimulationController.roundLength - (currentTime() - startTime) / 1000;

            uiInfo(controller.model, timeLeft);

            if (controller.model.squaresLeft <= 0) {
                controller.gameActive = false;
                updateScoreboard(controller.model, 'w');
            }
            System.out.flush();
            delay(controller.simulationSpeed);
        }
        if (controller.model.squaresLeft > 0) updateScoreboard(controller.model, 'l');
        delay(4000);
        clrscr();
    }

    public static boolean roundTime(long startTime, int roundLength) {
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

    private static void loadConfiguration(Player[] players) {
        //        String fileName = "conf.cfg";
        String fileName = "src" + File.separator + "Ex6" + File.separator + "conf.cfg";
        File file = new File(fileName);

        if (!file.exists()) {
            try {
                try (Writer writer = new FileWriter(file)) {
                    for (Player plr : players) {
                        writer.write("Player " + getId(plr) + ": 0\n");
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
                    String label = parts[0].trim();
                    String value = parts[1].trim();
                    if (label.toLowerCase().startsWith("player")) {
                        try {
                            String idStr = label.toLowerCase().replace("player", "").trim();
                            int plr_id = Integer.parseInt(idStr);
                            int mode = Integer.parseInt(value);
                            if (mode < 0 || mode > 2) mode = 0;

                            for (Player plr : players) {
                                if (getId(plr) == plr_id) {
                                    setControlMode(plr, mode);
                                    System.out.println("Player " + getId(plr) + " mode set to " + mode);
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid line: " + line);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Config read error. Starting with defaults.");
        }
    }

    void main() {
        Player player1 = createPlayer(1);
        Player player2 = createPlayer(2);
        Player[] players = {player1, player2};

        SimulationController controller = createController(10, players);
        runGame(controller);
    }
}