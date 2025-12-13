// Kamil Wolbach (280161)
package Ex1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static Ex1.JAVA_06_01.*;

class TCornerPathTest {
    @Test
    void testPointCreation() {
        TPoint p = point(10, 20);

        assertNotNull(p, "The point shouldn't be null");
        assertEquals(10, p.x, "The X coordinate should be 10");
        assertEquals(20, p.y, "The Y coordinate should be 20");
    }

    @Test
    void testAddPointToCornerPath() {
        TCornerPath path = createCornerPath();
        addPoint(path, point(5, 5));

        assertEquals(1, path.count, "The point count should be 1");
        assertEquals(5, path.points[0].x);
    }

    @Test
    void testIsPointOnPath_Positive() {
        TCornerPath path = createCornerPath();
        addPoint(path, point(0, 0));
        addPoint(path, point(0, 10));

        TPoint pointToCheck = point(0, 5);
        boolean result = isPointOnPath(path, pointToCheck);

        assertTrue(result, "The point (0,5) should exist on the segment (0,0)-(0,10)");
    }

    @Test
    void testIsPointOnPath_Negative() {
        TCornerPath path = createCornerPath();
        addPoint(path, point(0, 0));
        addPoint(path, point(10, 0));

        TPoint pointToCheck = point(5, 5);
        boolean result = isPointOnPath(path, pointToCheck);

        assertFalse(result, "The point (5,5) should not exist on the segment (0,0)-(0,10)");
    }

    @Test
    void testIsSegmentValid() {
        TPoint p1 = point(0, 0);

        assertTrue(isSegmentValid(p1, point(0, 5)), "Correct vertical segment");
        assertTrue(isSegmentValid(p1, point(5, 0)), "Correct horizontal segment");
        assertTrue(isSegmentValid(p1, point(5, 5)), "Correct diagonal segment");
        assertFalse(isSegmentValid(p1, point(1, 2)), "(0,0)-(1,2) is not a dx=dy diagonal segment, so it should be discarded");
    }

    @Test
    void testComplicatedPath() {
        TCornerPath path1 = createCornerPath();
        addPoint(path1, point(1, 1));
        addSegment(path1, point(5, 5), point(9, 1));
        AddSectionsToPath(path1, point(15, 15), point(30, 20));
        TCornerPath path3 = createCornerPath();
        AddSectionsToPath(path3, point(35, 17), point(40, 15));
        addPath(path1, path3);

        boolean result = isPointOnPath(path1, point(9, 2));
        assertTrue(result, "The point (9,2) should exist on the path");
        assertTrue((getSegmentLength(path1) > path1.count), "There can't be more database points on the path than the amount of corners");
    }
}