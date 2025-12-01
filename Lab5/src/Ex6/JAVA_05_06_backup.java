import java.awt.*;

import static java.lang.IO.*;
import static term.term.*;

// Data structures //

public static class TPoint {
    public int x, y;
}

public static class TPath {
    TPoint[] points = new TPoint[2000];
    int count = 0;
    int print_direction = 0; // 0: front, 1: back
    int wrap_around = 0;     // 0: cuts, 1: wraps
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
 * Adds a segment of connected points to the given path. The segment is defined
 * by two points, A and B, which must form a valid segment (vertical, horizontal, or diagonal).
 * <p>
 * Points are iteratively added from A to B. If the last point in the path
 * already matches the starting point of the segment (A), the starting
 * point is skipped to avoid duplicates. If the ending point of the
 * segment matches the first point in the path, it is also skipped.
 * 
 * @param path the path to which the segment should be added
 * @param a the start point of the segment
 * @param b the end point of the segment
 */
public static void addSegment(TPath path, TPoint a, TPoint b) {
    if (!isSegmentValid(a, b)) return;

    TPoint lastPoint = getLastPoint(path);
    boolean skipFirst = (lastPoint != null && lastPoint.x == a.x && lastPoint.y == a.y);

    // Decide the first direction to start writing the segment
    int stepX = Integer.compare(b.x, a.x);
    int stepY = Integer.compare(b.y, a.y);

    int currentX = a.x;
    int currentY = a.y;

    while (true) {
        boolean shouldAdd = true;
        if (currentX == a.x && currentY == a.y && skipFirst) shouldAdd = false;
        
        if (path.count > 0 && currentX == b.x && currentY == b.y) {
            if (path.points[0].x == b.x && path.points[0].y == b.y) shouldAdd = false;
        }

        if (shouldAdd) addPoint(path, point(currentX, currentY));
        if (currentX == b.x && currentY == b.y) break;

        currentX += stepX;
        currentY += stepY;
    }
}

public static void addSectionsToPath(TPath path, TPoint... points) {
    if (points.length == 0) return;

    // if points are odd, then the last point should be added to the path but not
    // to the segment; if points are even, then add each to separate segments

    for (int i = 0; i < points.length - 1; i++) {
        TPoint p1 = points[i];
        TPoint p2 = points[i+1];

        if (isSegmentValid(p1, p2)) {
            addSegment(path, p1, p2);
        } else {
            TPoint last = getLastPoint(path);
            if (last == null || last.x != p1.x || last.y != p1.y) {
                addPoint(path, p1);
            }
        }
    }
    
    TPoint finalP = points[points.length-1];
    TPoint lastInPath = getLastPoint(path);
    if (lastInPath == null || lastInPath.x != finalP.x || lastInPath.y != finalP.y) {
        if (points.length > 1 && !isSegmentValid(points[points.length-2], finalP)) addPoint(path, finalP);
        else if (points.length == 1) addPoint(path, finalP); // Tylko 1 punkt podany
    }
}



void main() {
    clrscr();
    TPath path1 = createPath();
//    addPoint(path1, point(1, 1));
//    addPoint(path1, point(2, 2));
//    addPoint(path1, point(3, 3));
//    drawPath(path1, 'a');
//
//    addSegment(path1, point(5, 5), point(9, 9));
//    drawPath(path1, 'b');

    addSectionsToPath(path1, point(1,1), point(5,5), point(10,5));
    drawPath(path1, 'c');
    clrscr();
}
