// Kamil Wolbach (280161)
import static java.lang.IO.*;
import static term.term.*;

// Data structures //

public static class TFrame {
    public int width, height;
}

public static class TPoint {
    public int x, y;
}

public static class TPath {
    TPoint[] points = new TPoint[2000];
    int count = 0;
    int print_direction = 0; // 0: clockwise, 1: anticlockwise
    int wrap_around = 0;     // 0: cuts, 1: wraps
}

public static TFrame frame(int w, int h) {
    TFrame frame = new TFrame();
    frame.width = w;
    frame.height = h;
    return frame;
}

public static TPoint point(int x, int y) {
    TPoint p = new TPoint();
    p.x = x;
    p.y = y;
    return p;
}

public static TPath createPath() {
    TPath p = new TPath();
    for (int i = 0; i < p.points.length; i++) p.points[i] = new TPoint();
    return p;
}

public static void addPoint(TPath path, TPoint p) {
    if (path.count < path.points.length) {
        path.points[path.count] = point(p.x, p.y);
        path.count++;
    }
}

public static TPoint getLastPoint(TPath path) {
    if (path.count == 0) return null;
    return path.points[path.count - 1];
}

public static boolean isPointOnPath(TPath path, TPoint p) {
    for (int i = 0; i < path.count; i++) {
        if (path.points[i].x == p.x && path.points[i].y == p.y) return true;
    }
    return false;
}

public static void drawPath(TPath path, char c) {
    for (int i = 0; i < path.count; i++) {
        TPoint p = path.points[i];
        gotoxy(p.x, p.y);
        print(c);
    }
}

public static boolean isSegmentValid(TPoint a, TPoint b) {
    int dx = Math.abs(b.x - a.x);
    int dy = Math.abs(b.y - a.y);
    // Accepted segment types: vertical, horizontal, diagonal
    return (dx == 0) || (dy == 0) || (dx == dy);
}

/**
 * Adds a segment of connected points to the given path.
 */
public static void addSegment(TPath path, TPoint a, TPoint b) {
    if (!isSegmentValid(a, b)) return;

    TPoint lastPoint = getLastPoint(path);
    boolean skipFirst = (lastPoint != null && lastPoint.x == a.x && lastPoint.y == a.y);

    // Decide the direction of writing the segment
    int stepX = Integer.compare(b.x, a.x);
    int stepY = Integer.compare(b.y, a.y);

    int currentX = a.x;
    int currentY = a.y;

    while (true) {
        if (segmentConditions(currentX, currentY, path, a, b, skipFirst)) addPoint(path, point(currentX, currentY));
        if (currentX == b.x && currentY == b.y) break;

        currentX += stepX;
        currentY += stepY;
    }
}

/**
 * Checks whether a certain point should be added to the segment.
 */
public static boolean segmentConditions(int currentX, int currentY, TPath path, TPoint a, TPoint b, boolean skipFirst) {
    boolean shouldAdd = currentX != a.x || currentY != a.y || !skipFirst;

    if (shouldAdd) {
        if (path.count > 0 && currentX == b.x && currentY == b.y) {
            if (path.points[0].x == b.x && path.points[0].y == b.y) shouldAdd = false;
        }
    }

    return shouldAdd;
}

public static void AddSectionsToPath(TPath path, TPoint... points) {
    if (points.length == 0) return;

    // if points are odd, then the last point should be added to the path but not
    // to the segment; if points are even, then add each to separate segments

    for (int i = 0; i < points.length - 1; i++) {
        TPoint p1 = points[i];
        TPoint p2 = points[i + 1];

        if (isSegmentValid(p1, p2)) {
            addSegment(path, p1, p2);
        } else {
            TPoint last = getLastPoint(path);
            if (last == null || last.x != p1.x || last.y != p1.y) {
                addPoint(path, p1);
            }
        }
    }

    TPoint finalP = points[points.length - 1];
    TPoint lastInPath = getLastPoint(path);
    if (lastInPath == null || lastInPath.x != finalP.x || lastInPath.y != finalP.y) {
        if (points.length > 1 && !isSegmentValid(points[points.length - 2], finalP)) addPoint(path, finalP);
        else if (points.length == 1) addPoint(path, finalP);
    }
}

static void addPath(TPath dest, TPath src) {
    if (src.count == 0) return;

    TPoint destLast = getLastPoint(dest);

    for (int i = 0; i < src.count; i++) {
        boolean add = true;
        TPoint p = src.points[i];

        // The first point of src overlaps the last point of dest
        if (i == 0 && destLast != null && p.x == destLast.x && p.y == destLast.y) {
            add = false;
        }

        // The last point of src overlaps the first point of dest
        if (i == src.count - 1 && dest.count > 0) {
            if (p.x == dest.points[0].x && p.y == dest.points[0].y) {
                add = false;
            }
        }

        if (add) {
            addPoint(dest, point(p.x, p.y));
        }
    }
}

static void WriteStringOnPath(String text, TPath path, int startIdx) {
    WriteStringOnPathWithOffset(text, path, startIdx, path.print_direction, path.wrap_around, point(0, 0));
}

static void WriteStringOnPath(String text, TPath path, int startIdx, int direction, int wrap) {
    WriteStringOnPathWithOffset(text, path, startIdx, direction, wrap, point(0, 0));
}

/**
 * Prints string on a translated path, without changing the path structure itself
 */
static void WriteStringOnPathWithOffset(String text, TPath path, int startIdx, int direction, int wrap, TPoint offset) {
    if (path.count == 0) return;

    int currentIdx = startIdx % path.count;
    if (currentIdx < 0) currentIdx += path.count;

    for (int i = 0; i < text.length(); i++) {
        if (wrap == 0 && (currentIdx < 0 || currentIdx >= path.count)) break;

        int actualIdx = (currentIdx % path.count + path.count) % path.count;

        TPoint p = path.points[actualIdx];
        printAt(p.x + offset.x, p.y + offset.y, String.valueOf(text.charAt(i)));

        // Move the string
        if (direction == 0) currentIdx++;
        else currentIdx--;
    }
}

static void printAt(int x, int y, String s) {
    gotoxy(x, y);
    print(s);
}

public boolean handleStart() {
    while (keypressed()) {
        String key = readkeystr();
        if (key.equals("s")) return true;
    }
    return false;
}

/**
 * Calculates the text's tip depending on the direction it's moving towards
 */
static int calculateTip(int startPos, int length, int direction, int pathLen) {
    int tip;
    if (direction == 0) {
        // Clockwise text [pos, pos + length]
        tip = startPos + length - 2;
    } else {
        // Anticlockwise text [pos - length, pos]
        tip = startPos - length + 2;
    }
    return norm(tip, pathLen);
}

static int norm(int val, int max) {
    return (val % max + max) % max;
}

// Two-sided shortest distance
static int dist(int a, int b, int max) {
    int d = Math.abs(a - b);
    return Math.min(d, max - d);
}

void main() {
    clrscr();
    cursor_hide();

    TFrame terminal = frame(120, 30);
    TFrame bee = frame(20, 15);

    TPath path1 = createPath();
    TPath path2 = createPath();

    AddSectionsToPath(path1,
            point(1, 1),
            point(terminal.width, 1),
            point(terminal.width, terminal.height),
            point(1, terminal.height),
            point(1, 1)
    );
    path1.wrap_around = 1;

    AddSectionsToPath(path2,
            point((terminal.width - bee.width) / 2, (terminal.height - bee.height) / 2),
            point((terminal.width + bee.width) / 2, (terminal.height - bee.height) / 2),
            point((terminal.width + bee.width) / 2, (terminal.height + bee.height) / 2),
            point((terminal.width - bee.width) / 2, (terminal.height + bee.height) / 2),
            point((terminal.width - bee.width) / 2, (terminal.height - bee.height) / 2)
    );
    path2.wrap_around = 1;
    path2.print_direction = 0;

    drawPath(path1, '*');
    drawPath(path2, '*');

    String bee_ready = " Bee ready! ";
    String gameStart = ">> Welcome to our game! <<";
    String pressS = ">> Press s to start <<";

    int bee1_pos = 0;
    int bee2_pos = path2.count / 2;

    int game_pos = terminal.width / 2;
    int game_dir = 0;

    int press_pos = terminal.width + terminal.height + (terminal.width / 2);
    int press_dir = 1;

    while (true) {
        WriteStringOnPath(" ".repeat(bee_ready.length()), path2, bee1_pos);
        WriteStringOnPath(" ".repeat(bee_ready.length()), path2, bee2_pos);

        String blankGame = " ".repeat(gameStart.length());
        String blankPress = " ".repeat(pressS.length());

        WriteStringOnPath(blankGame, path1, game_pos, game_dir, 1);
        WriteStringOnPath(blankPress, path1, press_pos, press_dir, 1);

        bee1_pos = (bee1_pos + 1) % path2.count;
        bee2_pos = (bee2_pos + 1) % path2.count;

        int next_game_pos = (game_dir == 0) ? game_pos + 1 : game_pos - 1;
        int next_press_pos = (press_dir == 0) ? press_pos + 1 : press_pos - 1;

        int gameTip = calculateTip(next_game_pos, gameStart.length(), game_dir, path1.count);
        int pressTip = calculateTip(next_press_pos, pressS.length(), press_dir, path1.count);

        int gameAnchor = norm(next_game_pos, path1.count);
        int pressAnchor = norm(next_press_pos, path1.count);

        boolean anchorsCrash = dist(gameAnchor, pressAnchor, path1.count) == 0;
        boolean tipsCrash = dist(gameTip, pressTip, path1.count) == 0;

        if (anchorsCrash || tipsCrash) {
            game_dir = 1 - game_dir;
            press_dir = 1 - press_dir;

            // String indices inversion
            if (game_dir == 1) {
                game_pos = norm(game_pos + gameStart.length() - 1, path1.count);
            } else {
                game_pos = norm(game_pos - gameStart.length() + 1, path1.count);
            }
            if (press_dir == 1) {
                press_pos = norm(press_pos + pressS.length() - 1, path1.count);
            } else {
                press_pos = norm(press_pos - pressS.length() + 1, path1.count);
            }

        } else {
            game_pos = norm(next_game_pos, path1.count);
            press_pos = norm(next_press_pos, path1.count);
        }

        setfgcolor(yellow);
        WriteStringOnPath(bee_ready, path2, bee1_pos);
        WriteStringOnPath(bee_ready, path2, bee2_pos);

        setfgcolor(cyan);
        WriteStringOnPath(gameStart, path1, game_pos, game_dir, 1);
        WriteStringOnPath(pressS, path1, press_pos, press_dir, 1);
        setfgcolor(black);

        if (handleStart()) break;
        delay(100);
    }
    setfgcolor(7);
    clrscr();
}