// Kamil Wolbach (280161)
package Ex2;

import static java.lang.IO.print;
import static term.term.*;

public class JAVA_06_02 {
    // Data structures //
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

    public static boolean isSegmentValid(TPoint a, TPoint b) {
        int dx = Math.abs(b.x - a.x);
        int dy = Math.abs(b.y - a.y);
        return (dx == 0) || (dy == 0) || (dx == dy);
    }

    public static int GetSectionLength(TSection s) {
        if (s == null) return 0;
        return Math.max(Math.abs(s.end.x - s.start.x), Math.abs(s.end.y - s.start.y)) + 1;
    }

    public static TPoint GetSectionNthPoint(TSection s, int n) {
        int len = GetSectionLength(s);
        if (n <= 0) return point(s.start.x, s.start.y);
        if (n >= len) return point(s.end.x, s.end.y);

        int dx = Integer.compare(s.end.x, s.start.x);
        int dy = Integer.compare(s.end.y, s.start.y);

        return point(s.start.x + (n * dx), s.start.y + (n * dy));
    }

    public static boolean IsPointOnSection(TSection s, TPoint p) {
        int minX = Math.min(s.start.x, s.end.x);
        int maxX = Math.max(s.start.x, s.end.x);
        int minY = Math.min(s.start.y, s.end.y);
        int maxY = Math.max(s.start.y, s.end.y);

        if (p.x < minX || p.x > maxX || p.y < minY || p.y > maxY) return false;

        // Check if vectors start-end of the section and start-point are collinear (i.e., if their cross-product is 0)
        int crossProduct = (p.y - s.start.y) * (s.end.x - s.start.x) - (p.x - s.start.x) * (s.end.y - s.start.y);
        return crossProduct == 0;
    }

    /**
     * Returns true if two sections have at least one identical point.
     */
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
                if (prev.end.x == curr.start.x && prev.end.y == curr.start.y) {
                    totalLen--;
                }
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

            boolean connectedToPrev = false;
            if (i > 0) {
                TSection prev = path.sections[i - 1];
                if (prev.end.x == s.start.x && prev.end.y == s.start.y) {
                    connectedToPrev = true;
                }
            }

            int currentLength = connectedToPrev ? sectionLength - 1 : sectionLength;

            if (n < currentPos + currentLength) {
                int remainder = n - currentPos;
                int localIndex = connectedToPrev ? remainder + 1 : remainder;
                return GetSectionNthPoint(s, localIndex);
            }
            currentPos += currentLength;
        }
        return path.sections[path.count - 1].end;
    }

    public static void addSection(TSectionPath path, TPoint start, TPoint end) {
        if (!isSegmentValid(start, end)) return;
        if (path.count >= path.sections.length) return;

        path.sections[path.count] = section(start, end);
        path.count++;
    }

    public static void AddSectionsToPath(TSectionPath path, TSection... sections) {
        for (TSection s : sections) {
            addSection(path, s.start, s.end);
        }
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

    public static void WriteStringOnPath(String text, TSectionPath path, int startIdx) {
        if (path.count == 0) return;

        int direction = path.print_direction;
        int wrap = path.wrap_around;

        int currentIdx = startIdx;
        int totalLen = GetPathLength(path);

        for (int i = 0; i < text.length(); i++) {
            if (wrap == 0 && (currentIdx < 0 || currentIdx >= totalLen)) break;

            TPoint p = GetPathNthPoint(path, currentIdx);
            if (p != null) {
                gotoxy(p.x, p.y);
                print(text.charAt(i));
            }

            if (direction == 0) currentIdx++;
            else currentIdx--;
        }
    }

    void main() {
        clrscr();
        cursor_hide();

        TSectionPath path = createSectionPath();
        addSection(path, point(1, 1), point(5, 5));
        addSection(path, point(10, 5), point(15, 5));
        TSection s1 = section(point(15, 5), point(15, 10));
        AddSectionsToPath(path, s1);
        drawPath(path, '#');

        String msg = "Hello world!";
        WriteStringOnPath(msg, path, 0);
        clrscr();
    }
}