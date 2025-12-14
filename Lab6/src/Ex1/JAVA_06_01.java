// Kamil Wolbach (280161)
package Ex1;

import static java.lang.IO.*;
import static term.term.*;

public class JAVA_06_01 {
    // Data structures //
    public static class TFrame {
        public int width, height;
    }

    public static class TPoint {
        public int x, y;
    }

    /**
     * Stores only the corners of the path.
     */
    public static class TCornerPath {
        TPoint[] points = new TPoint[100];
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

    public static TCornerPath createCornerPath() {
        TCornerPath p = new TCornerPath();
        for (int i = 0; i < p.points.length; i++) p.points[i] = new TPoint();
        return p;
    }

    public static void addPoint(TCornerPath path, TPoint p) {
        if (path.count < path.points.length) {
            path.points[path.count] = point(p.x, p.y);
            path.count++;
        }
    }

    public static TPoint getLastPoint(TCornerPath path) {
        if (path.count == 0) return null;
        return path.points[path.count - 1];
    }

    public static boolean isPointOnPath(TCornerPath path, TPoint p) {
        for (int i = 0; i < path.count - 1; i++) {
            TPoint p1 = path.points[i];
            TPoint p2 = path.points[i + 1];

            double xDiff = p2.x - p1.x;
            double yDiff = p2.y - p1.y;

            double steps = Math.max(xDiff, yDiff);
            double dx = xDiff / steps;
            double dy = yDiff / steps;

            double x = p1.x;
            double y = p1.y;

            int checkX, checkY;

            for (int j = 1; j <= Math.max(Math.abs(xDiff) + 1, Math.abs(yDiff) + 1); j++) {
                checkX = (int) Math.round(x);
                checkY = (int) Math.round(y);
                x += dx;
                y += dy;

                if (p.x == checkX && p.y == checkY) return true;
            }
        }
        return false;
    }

    /**
     * Draw the path by interpolating between corners
     */
    public static void drawPath(TCornerPath path, char c) {
        if (path.count < 2) return;
        for (int i = 0; i < path.count - 1; i++) {
            TPoint p1 = path.points[i];
            TPoint p2 = path.points[i + 1];
            drawSegment(p1, p2, c);
        }
    }

    private static void drawSegment(TPoint p1, TPoint p2, char c) {
        double xDiff = p2.x - p1.x;
        double yDiff = p2.y - p1.y;

        double steps = Math.max(Math.abs(xDiff), Math.abs(yDiff));
        double dx = xDiff / steps;
        double dy = yDiff / steps;

        double x = p1.x;
        double y = p1.y;

        for (int j = 1; j <= steps + 1; j++) {
            gotoxy((int) Math.round(x), (int) Math.round(y));
            print(c);
            x += dx;
            y += dy;
        }
    }

    public static boolean isSegmentValid(TPoint a, TPoint b) {
        int dx = Math.abs(b.x - a.x);
        int dy = Math.abs(b.y - a.y);
        // Accepted segment types: vertical, horizontal, diagonal
        return (dx == 0) || (dy == 0) || (dx == dy);
    }

    public static void addSegment(TCornerPath path, TPoint a, TPoint b) {
        if (!isSegmentValid(a, b)) return;
        boolean addA = true;

        if (path.count > 0) {
            TPoint lastPoint = getLastPoint(path);
            boolean skipFirst = (lastPoint != null && lastPoint.x == a.x && lastPoint.y == a.y);
            if (skipFirst) addA = false;
        }
        if (addA) addPoint(path, a);
        addPoint(path, b);
    }

    public static void AddSectionsToPath(TCornerPath path, TPoint... points) {
        if (points.length < 2) return;

        addPoint(path, points[0]);

        for (int i = 1; i < points.length; i++) {
            TPoint prev = points[i - 1];
            TPoint curr = points[i];

            if (isSegmentValid(prev, curr)) {
                TPoint last = path.points[path.count - 1];
                if (last.x != curr.x || last.y != curr.y) {
                    addPoint(path, curr);
                }
            }
        }
    }

    static void addPath(TCornerPath dest, TCornerPath src) {
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

    static void WriteStringOnPath(String text, TCornerPath path, int startIdx) {
        WriteStringOnPathWithOffset(text, path, startIdx, path.print_direction, path.wrap_around, point(0, 0));
    }

    static void WriteStringOnPath(String text, TCornerPath path, int startIdx, int direction, int wrap) {
        WriteStringOnPathWithOffset(text, path, startIdx, direction, wrap, point(0, 0));
    }

    static void WriteStringOnPathWithOffset(String text, TCornerPath path, int startIdx, int direction, int wrap, TPoint offset) {
        if (path.count < 2) return;

        int totalSegmentLength = getSegmentLength(path);
        if (totalSegmentLength == 0) return;

        int currentIdx = startIdx % totalSegmentLength;
        if (currentIdx < 0) currentIdx += totalSegmentLength;

        for (int i = 0; i < text.length(); i++) {
            if (wrap == 0 && (currentIdx < 0 || currentIdx >= totalSegmentLength)) break;
            int actualIdx = (currentIdx % totalSegmentLength + totalSegmentLength) % totalSegmentLength;

            int segmentIndex = getSegmentIndex(path, actualIdx);
            if (segmentIndex >= path.count - 1) segmentIndex = path.count - 2;

            TPoint p1 = path.points[segmentIndex];
            TPoint p2 = path.points[segmentIndex + 1];

            int distanceIntoSegment = getDistanceIntoSegment(path, actualIdx, segmentIndex);

            int dx = p2.x - p1.x;
            int dy = p2.y - p1.y;
            int segmentLen = Math.max(Math.abs(dx), Math.abs(dy));

            if (segmentLen == 0) continue;

            double xPos = p1.x + ((double) dx / segmentLen) * distanceIntoSegment;
            double yPos = p1.y + ((double) dy / segmentLen) * distanceIntoSegment;

            printAt((int) Math.round(xPos + offset.x), (int) Math.round(yPos + offset.y), String.valueOf(text.charAt(i)));

            if (direction == 0) currentIdx++;
            else currentIdx--;
        }
    }

    public static int getSegmentLength(TCornerPath path) {
        int totalSegmentLength = 0;

        for (int i = 0; i < path.count - 1; i++) {
            TPoint p1 = path.points[i];
            TPoint p2 = path.points[i + 1];

            double xDiff = p2.x - p1.x;
            double yDiff = p2.y - p1.y;

            if (i < path.count - 2) {
                totalSegmentLength += (int) Math.max(Math.abs(xDiff), Math.abs(yDiff));
            } else totalSegmentLength += (int) Math.max(Math.abs(xDiff) + 1, Math.abs(yDiff) + 1);
        }
        return totalSegmentLength;
    }

    /**
     * Finds the index of the segment's starting point containing actualIdx
     */
    public static int getSegmentIndex(TCornerPath path, int actualIdx) {
        int currentLenSum = 0;
        for (int i = 0; i < path.count - 1; i++) {
            TPoint p1 = path.points[i];
            TPoint p2 = path.points[i + 1];
            int segLen = Math.max(Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));

            if (actualIdx < currentLenSum + segLen) {
                return i;
            }
            currentLenSum += segLen;
        }
        return path.count - 2;
    }

    /**
     * Calculates relative index within the specific segment
     */
    public static int getDistanceIntoSegment(TCornerPath path, int actualIdx, int segmentIndex) {
        int currentLenSum = 0;
        for (int i = 0; i < segmentIndex; i++) {
            TPoint p1 = path.points[i];
            TPoint p2 = path.points[i + 1];
            currentLenSum += Math.max(Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
        }
        return actualIdx - currentLenSum;
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

    static int calculateTip(int startPos, int length, int direction, int pathLen) {
        int tip;
        if (direction == 0) tip = startPos + length - 2;
        else tip = startPos - length + 2;
        return norm(tip, pathLen);
    }

    static int norm(int val, int max) {
        return (val % max + max) % max;
    }

    static int dist(int a, int b, int max) {
        int d = Math.abs(a - b);
        return Math.min(d, max - d);
    }

    void main() {
        clrscr();
        cursor_hide();

        TFrame terminal = frame(120, 30);
        TFrame bee = frame(20, 15);

        TCornerPath path1 = createCornerPath();
        TCornerPath path2 = createCornerPath();

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

        int path1Len = getSegmentLength(path1);
        int path2Len = getSegmentLength(path2);

        String bee_ready = "Bee ready!";
        String gameStart = ">> Welcome to our game! <<";
        String pressS = ">> Press s to start <<";

        String game_cleaner = "*".repeat(gameStart.length());
        String press_cleaner = "*".repeat(pressS.length());

        int bee1_pos = 0;
        int bee2_pos = path2Len / 2;

        int game_pos = terminal.width / 2;
        int game_dir = 0;

        int press_pos = terminal.width + terminal.height + (terminal.width / 2);
        int press_dir = 1;

        setfgcolor(7);
        drawPath(path1, '*');
        drawPath(path2, '*');

        setfgcolor(yellow);
        WriteStringOnPath(bee_ready, path2, bee1_pos);
        WriteStringOnPath(bee_ready, path2, bee2_pos);
        setfgcolor(cyan);
        WriteStringOnPath(gameStart, path1, game_pos, game_dir, 1);
        WriteStringOnPath(pressS, path1, press_pos, press_dir, 1);

        while (true) {
            int next_bee1_pos = (bee1_pos + 1) % path2Len;
            int next_bee2_pos = (bee2_pos + 1) % path2Len;

            int next_game_pos = (game_dir == 0) ? game_pos + 1 : game_pos - 1;
            int next_press_pos = (press_dir == 0) ? press_pos + 1 : press_pos - 1;

            int next_game_dir = game_dir;
            int next_press_dir = press_dir;
            int final_game_pos, final_press_pos;

            int gameTip = calculateTip(next_game_pos, gameStart.length(), game_dir, path1Len);
            int pressTip = calculateTip(next_press_pos, pressS.length(), press_dir, path1Len);
            int gameAnchor = norm(next_game_pos, path1Len);
            int pressAnchor = norm(next_press_pos, path1Len);

            boolean collision = (dist(gameAnchor, pressAnchor, path1Len) <= 1) || (dist(gameTip, pressTip, path1Len) <= 1);

            setfgcolor(7);
            if (collision) {
                next_game_dir = 1 - game_dir;
                next_press_dir = 1 - press_dir;

                if (next_game_dir == 1) final_game_pos = norm(game_pos + gameStart.length() - 1, path1Len);
                else final_game_pos = norm(game_pos - gameStart.length() + 1, path1Len);
                if (next_press_dir == 1) final_press_pos = norm(press_pos + pressS.length() - 1, path1Len);
                else final_press_pos = norm(press_pos - pressS.length() + 1, path1Len);

                WriteStringOnPath(game_cleaner, path1, game_pos, game_dir, 1);
                WriteStringOnPath(press_cleaner, path1, press_pos, press_dir, 1);
            } else {
                final_game_pos = norm(next_game_pos, path1Len);
                final_press_pos = norm(next_press_pos, path1Len);

                WriteStringOnPath("*", path1, game_pos);
                WriteStringOnPath("*", path1, press_pos);
            }

            WriteStringOnPath("*", path2, bee1_pos);
            WriteStringOnPath("*", path2, bee2_pos);

            setfgcolor(yellow);
            WriteStringOnPath(bee_ready, path2, next_bee1_pos);
            WriteStringOnPath(bee_ready, path2, next_bee2_pos);

            setfgcolor(cyan);
            WriteStringOnPath(gameStart, path1, final_game_pos, next_game_dir, 1);
            WriteStringOnPath(pressS, path1, final_press_pos, next_press_dir, 1);
            setfgcolor(black);

            bee1_pos = next_bee1_pos;
            bee2_pos = next_bee2_pos;

            game_pos = final_game_pos;
            press_pos = final_press_pos;
            game_dir = next_game_dir;
            press_dir = next_press_dir;

            if (handleStart()) break;
            delay(50);
        }
        setfgcolor(7);
        clrscr();
    }
}