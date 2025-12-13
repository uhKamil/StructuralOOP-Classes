// Kamil Wolbach (280161)
package Ex2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static Ex2.JAVA_06_02.*;

class TSectionPathTest {
    @Test
    void testGetSectionLength() {
        TSection s1 = section(point(0, 0), point(3, 0));
        assertEquals(4, GetSectionLength(s1), "Horizontal length should be 4");

        TSection s2 = section(point(0, 0), point(0, 2));
        assertEquals(3, GetSectionLength(s2), "Vertical length should be 3");

        TSection s3 = section(point(0, 0), point(2, 2));
        assertEquals(3, GetSectionLength(s3), "Diagonal length should be 3");

        TSection s4 = section(point(1, 1), point(1, 1));
        assertEquals(1, GetSectionLength(s4), "Single point section length should be 1");
    }

    @Test
    void testGetSectionNthPoint() {
        TSection s = section(point(10, 10), point(12, 12));

        TPoint p0 = GetSectionNthPoint(s, 0);
        assertEquals(10, p0.x);
        assertEquals(10, p0.y);

        TPoint p1 = GetSectionNthPoint(s, 1);
        assertEquals(11, p1.x);
        assertEquals(11, p1.y);

        TPoint p2 = GetSectionNthPoint(s, 2);
        assertEquals(12, p2.x);
        assertEquals(12, p2.y);

        // Out-of-bounds check
        TPoint pOver = GetSectionNthPoint(s, 99);
        assertEquals(12, pOver.x);

        TPoint pUnder = GetSectionNthPoint(s, -5);
        assertEquals(10, pUnder.x);
    }

    @Test
    void testSectionsOverlap() {
        TSection s1 = section(point(0, 0), point(4, 0));
        TSection s2 = section(point(2, -2), point(2, 2)); // crosses s1 at (2,0))
        assertTrue(SectionsOverlap(s1, s2), "Sections crossing each other should overlap");

        TSection s3 = section(point(5, 0), point(6, 0));
        assertFalse(SectionsOverlap(s1, s3), "Disjoint sections should not overlap");

        TSection s4 = section(point(4, 0), point(4, 5)); // touches the end of s1
        assertTrue(SectionsOverlap(s1, s4), "Touching sections should overlap");
    }

    @Test
    void testGetPathLength() {
        TSectionPath path = createSectionPath();
        addSection(path, point(0, 0), point(0, 2));
        addSection(path, point(0, 2), point(2, 2));

        assertEquals(5, GetPathLength(path), "Connected path length incorrect (probably double counting?)");

        addSection(path, point(5, 5), point(6, 5));
        assertEquals(7, GetPathLength(path), "Total path length incorrect (ignores discontinuity of the path?)");
    }

    @Test
    void testGetPathNthPoint() {
        TSectionPath path = createSectionPath();
        addSection(path, point(0, 0), point(2, 0));
        addSection(path, point(10, 10), point(11, 10));

        // Test first segment points
        TPoint p1 = GetPathNthPoint(path, 0);
        assertNotNull(p1);
        assertEquals(0, p1.x);
        assertEquals(0, p1.y);
        TPoint p2 = GetPathNthPoint(path, 2);
        assertNotNull(p2);
        assertEquals(2, p2.x);
        assertEquals(0, p2.y);

        // Test second segment points
        TPoint p3 = GetPathNthPoint(path, 3);
        assertNotNull(p3);
        assertEquals(10, p3.x, "Index 3 should be start of second section");
        assertEquals(10, p3.y);
        TPoint p4 = GetPathNthPoint(path, 4);
        assertNotNull(p4);
        assertEquals(11, p4.x);
        assertEquals(10, p4.y);
    }
}