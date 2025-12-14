// Kamil Wolbach (280161)
package Ex1;

import static java.lang.IO.print;
import static term.term.*;

public class JAVA_06_01_backup3 {
    // Data structures //
    public static class TFrame {
        public int width, height;
    }

    public static class TPoint {
        public int x, y;
    }

    /**
     * Stores only the corners of the path
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

            double dx = xDiff / Math.max(xDiff, yDiff);
            double dy = yDiff / Math.max(xDiff, yDiff);

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

    public static void drawPath(TCornerPath path, char c) {
        for (int i = 0; i < path.count - 1; i++) {
            TPoint p1 = path.points[i];
            TPoint p2 = path.points[i + 1];

            double xDiff = p2.x - p1.x;
            double yDiff = p2.y - p1.y;

            double dx = xDiff / Math.max(xDiff, yDiff);
            double dy = yDiff / Math.max(xDiff, yDiff);

            double x = p1.x;
            double y = p1.y;

            for (int j = 1; j <= Math.max(Math.abs(xDiff) + 1, Math.abs(yDiff) + 1); j++) {
                gotoxy((int) Math.round(x), (int) Math.round(y));
                print(c);
                x += dx;
                y += dy;
            }
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

    /**
     * Prints string on a translated path, without changing the path structure itself
     */
    static void WriteStringOnPathWithOffset(String text, TCornerPath path, int startIdx, int direction, int wrap, TPoint offset) {
        if (path.count == 0) return;

        int totalSegmentLength = getSegmentLength(path);

        int currentIdx = startIdx % totalSegmentLength;
        if (currentIdx < 0) currentIdx += totalSegmentLength;

        for (int i = 0; i < text.length(); i++) {
            if (wrap == 0 && (currentIdx < 0 || currentIdx >= totalSegmentLength)) break;

            int actualIdx = (currentIdx % totalSegmentLength + totalSegmentLength) % totalSegmentLength;

            // Check in which segment the character should be printed
            int segmentIndicator = getSegmentIndex(path, actualIdx, 's');
            int finalIdx = getSegmentIndex(path, actualIdx, 'i');
            TPoint segment_p1, segment_p2;
            if (segmentIndicator < path.points.length - 1) {
                segment_p1 = path.points[segmentIndicator];
                segment_p2 = path.points[segmentIndicator + 1];
            } else {
                segment_p1 = path.points[segmentIndicator - 1];
                segment_p2 = path.points[segmentIndicator];
            }

            double xDiff = segment_p2.x - segment_p1.x;
            double yDiff = segment_p2.y - segment_p1.y;

            double dx = finalIdx * xDiff / Math.max(xDiff, yDiff);
            double dy = finalIdx * yDiff / Math.max(xDiff, yDiff);

            double x = segment_p1.x;
            double y = segment_p1.y;

            gotoxy((int) Math.round(x + dx + offset.x), (int) Math.round(y + dy + offset.y));
            print(text.charAt(i));

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

    public static int getSegmentIndex(TCornerPath path, int actualIdx, char param) {
        int segmentLengthSum = actualIdx;
        int segmentIndicator = 0;
        int finalIdx = segmentLengthSum;

        for (int j = 0; j < path.count - 1; j++) {
            TPoint p1 = path.points[j];
            TPoint p2 = path.points[j + 1];

            double xDiff = p2.x - p1.x;
            double yDiff = p2.y - p1.y;
            int segmentLength;

            if (j < path.count - 2) {
                segmentLength = (int) Math.max(Math.abs(xDiff), Math.abs(yDiff));
            } else segmentLength = (int) Math.max(Math.abs(xDiff) + 1, Math.abs(yDiff) + 1);
            segmentLengthSum -= segmentLength;
            if (finalIdx - segmentLength >= 0) finalIdx -= segmentLength;

            if (segmentLengthSum < 0) {
                segmentIndicator = j;
                break;
            }
        }

        if (param == 's') return segmentIndicator;
        return finalIdx; // final id for specific segment
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

    /**
     * Java-adjusted norm calculator
     */
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

        TCornerPath path1 = createCornerPath();
        addPoint(path1, point(1, 1));
        addSegment(path1, point(5, 5), point(9, 1));
        AddSectionsToPath(path1, point(15, 15), point(30, 20));
        TCornerPath path3 = createCornerPath();
        AddSectionsToPath(path3, point(35, 17), point(40, 15));
        addPath(path1, path3);

        boolean p1 = isPointOnPath(path1, point(4, 4)); // true

        drawPath(path1, '*');
        delay(3000);
        WriteStringOnPath("Hi there mate, how's life?", path1, 0);
        clrscr();
    }
}