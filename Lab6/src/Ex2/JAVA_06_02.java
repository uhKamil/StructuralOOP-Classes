// Kamil Wolbach (280161)
package Ex2;

import static java.lang.IO.*;
import static term.term.*;

public class JAVA_06_02 {

    // ==========================================
    // Data Structures
    // ==========================================

    public static class TPoint {
        public int x, y;
    }

    public static class TSection {
        TPoint start, end;
    }

    public static class TSectionPath {
        TSection[] sections = new TSection[100];
        int count = 0;
        int print_direction = 0; // 0: clockwise, 1: anticlockwise
        int wrap_around = 0;     // 0: cuts, 1: wraps
    }

    public static class TFrame {
        public int width, height;
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

    public static TSection section(TPoint start, TPoint end) {
        TSection s = new TSection();
        s.start = point(start.x, start.y);
        s.end = point(end.x, end.y);
        return s;
    }

    public static TSectionPath createSectionPath() {
        return new TSectionPath();
    }

    // ==========================================
    // Logic functions
    // ==========================================

    public static int GetSectionLength(TSection s) {
        if (s == null) return 0;
        return Math.max(Math.abs(s.end.x - s.start.x), Math.abs(s.end.y - s.start.y)) + 1;
    }

    public static TPoint GetSectionNthPoint(TSection s, int n) {
        int len = GetSectionLength(s);
        if (n <= 0) return point(s.start.x, s.start.y);
        if (n >= len - 1) return point(s.end.x, s.end.y);

        int dx = Integer.compare(s.end.x, s.start.x);
        int dy = Integer.compare(s.end.y, s.start.y);

        return point(s.start.x + (n * dx), s.start.y + (n * dy));
    }

    public static boolean IsPointOnSection(TSection s, TPoint p) {
        // Bounding box
        int minX = Math.min(s.start.x, s.end.x);
        int maxX = Math.max(s.start.x, s.end.x);
        int minY = Math.min(s.start.y, s.end.y);
        int maxY = Math.max(s.start.y, s.end.y);

        if (p.x < minX || p.x > maxX || p.y < minY || p.y > maxY) return false;

        // Check if vectors start-end of the section and start-point are collinear (i.e., if their cross-product is 0)
        int crossProduct = (p.y - s.start.y) * (s.end.x - s.start.x) - (p.x - s.start.x) * (s.end.y - s.start.y);
        return crossProduct == 0;
    }

    public static boolean SectionsOverlap(TSection s1, TSection s2) {
        int len1 = GetSectionLength(s1);
        for (int i = 0; i < len1; i++) {
            TPoint p = GetSectionNthPoint(s1, i);
            if (IsPointOnSection(s2, p)) return true;
        }
        return false;
    }

    /**
     * Returns total number of points in the path.
     * Prevents double-counting common beginning/endpoints of adjacent sections.
     */
    public static int GetPathLength(TSectionPath path) {
        if (path.count == 0) return 0;
        int totalLen = 0;

        for (int i = 0; i < path.count; i++) {
            totalLen += GetSectionLength(path.sections[i]);
            if (i > 0) {
                TSection prev = path.sections[i - 1];
                TSection curr = path.sections[i];
                if (prev.end.x == curr.start.x && prev.end.y == curr.start.y) totalLen--;
            }
        }
        return totalLen;
    }

    public static TPoint GetPathNthPoint(TSectionPath path, int n) {
        if (path.count == 0) return null;

        int totalLen = GetPathLength(path);
        if (totalLen == 0) return path.sections[0].start;

        n = n % totalLen;
        while (n < 0) n += totalLen;

        int currentPos = 0;

        for (int i = 0; i < path.count; i++) {
            TSection s = path.sections[i];
            int sectionLength = GetSectionLength(s);

            boolean isConnected = false;
            if (i > 0) {
                TSection prev = path.sections[i - 1];
                if (prev.end.x == s.start.x && prev.end.y == s.start.y) {
                    isConnected = true;
                }
            }

            int currentLength = isConnected ? sectionLength - 1 : sectionLength;

            if (n < currentPos + currentLength) {
                int remainder = n - currentPos;
                int localIndex = isConnected ? remainder + 1 : remainder;
                return GetSectionNthPoint(s, localIndex);
            }
            currentPos += currentLength;
        }
        return path.sections[path.count - 1].end;
    }

    public static boolean isSegmentValid(TPoint a, TPoint b) {
        int dx = Math.abs(b.x - a.x);
        int dy = Math.abs(b.y - a.y);
        return (dx == 0) || (dy == 0) || (dx == dy);
    }

    public static void addSection(TSectionPath path, TPoint start, TPoint end) {
        if (!isSegmentValid(start, end)) return;
        if (path.count >= path.sections.length) return;
        path.sections[path.count] = section(start, end);
        path.count++;
    }

    /** Creates connected sections */
    public static void AddPointsAsSections(TSectionPath path, TPoint... points) {
        if (points.length < 2) return;
        for (int i = 0; i < points.length - 1; i++) addSection(path, points[i], points[i + 1]);
    }

    public static void AddSectionsToPath(TSectionPath path, TSection... sections) {
        for (TSection s : sections) addSection(path, s.start, s.end);
    }

    public static void drawPath(TSectionPath path, char c) {
        for (int i = 0; i < path.count; i++) {
            TSection s = path.sections[i];
            int len = GetSectionLength(s);
            for (int j = 0; j < len; j++) {
                TPoint p = GetSectionNthPoint(s, j);
                gotoxy(p.x, p.y);
                print(c);
            }
        }
    }

    public static void WriteStringOnPath(String text, TSectionPath path, int startIdx, int direction, int wrap) {
        if (path.count == 0) return;

        int totalLen = GetPathLength(path);
        if (totalLen == 0) return;

        int currentIdx = startIdx;
        currentIdx = currentIdx % totalLen;
        if (currentIdx < 0) currentIdx += totalLen;

        for (int i = 0; i < text.length(); i++) {
            if (wrap == 0 && (currentIdx < 0 || currentIdx >= totalLen)) break;

            TPoint p = GetPathNthPoint(path, currentIdx);
            if (p != null) {
                gotoxy(p.x, p.y);
                print(String.valueOf(text.charAt(i)));
            }

            if (direction == 0) currentIdx++;
            else currentIdx--;
        }
    }

    static int norm(int val, int max) {
        if (max == 0) return 0;
        return (val % max + max) % max;
    }

    static int calculateTip(int startPos, int length, int direction, int pathLen) {
        int tip;
        if (direction == 0) tip = startPos + length - 2;
        else tip = startPos - length + 2;
        return norm(tip, pathLen);
    }

    static int dist(int a, int b, int max) {
        int d = Math.abs(a - b);
        return Math.min(d, max - d);
    }

    public boolean handleStart() {
        while (keypressed()) {
            String key = readkeystr();
            if (key.equals("s")) return true;
        }
        return false;
    }

    void main() {
        clrscr();
        cursor_hide();

        TFrame terminal = frame(120, 30);
        TFrame bee = frame(20, 15);

        TSectionPath path1 = createSectionPath();
        TSectionPath path2 = createSectionPath();

        AddPointsAsSections(path1,
                point(1, 1),
                point(terminal.width, 1),
                point(terminal.width, terminal.height),
                point(1, terminal.height),
                point(1, 1)
        );
        path1.wrap_around = 1;

        AddPointsAsSections(path2,
                point((terminal.width - bee.width) / 2, (terminal.height - bee.height) / 2),
                point((terminal.width + bee.width) / 2, (terminal.height - bee.height) / 2),
                point((terminal.width + bee.width) / 2, (terminal.height + bee.height) / 2),
                point((terminal.width - bee.width) / 2, (terminal.height + bee.height) / 2),
                point((terminal.width - bee.width) / 2, (terminal.height - bee.height) / 2)
        );
        path2.wrap_around = 1;
        path2.print_direction = 0;

        int path1Len = GetPathLength(path1);
        int path2Len = GetPathLength(path2);

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
        WriteStringOnPath(bee_ready, path2, bee1_pos, 0, 0);
        WriteStringOnPath(bee_ready, path2, bee2_pos, 0, 0);
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

                WriteStringOnPath("*", path1, game_pos, game_dir, 1);
                WriteStringOnPath("*", path1, press_pos, press_dir, 1);
            }

            WriteStringOnPath("*", path2, bee1_pos, 0, 0);
            WriteStringOnPath("*", path2, bee2_pos, 0, 0);

            setfgcolor(yellow);
            WriteStringOnPath(bee_ready, path2, next_bee1_pos, 0, 0);
            WriteStringOnPath(bee_ready, path2, next_bee2_pos, 0, 0);

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