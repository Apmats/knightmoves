package me.apmats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class KnightMovementCalculationsTest {
    @Test
    public void unboundedSearchShouldCalculateShortestPath() {
        List<String> path = KnightMovementCalculations.findOptimalPath("A1", "B1", -1);
        List<String> expectedPath = Arrays.asList("A1", "C2", "A3", "B1");
        assertEquals("Expected path to be the shortest one", expectedPath, path);
    }

    @Test
    public void boundedSearchShouldCalculateShortestPathIfWithinMoveLimit() {
        List<String> path = KnightMovementCalculations.findOptimalPath("A1", "B1", 3);
        List<String> expectedPath = Arrays.asList("A1", "C2", "A3", "B1");
        assertEquals("Expected path to be the shortest one", expectedPath, path);
    }

    @Test
    public void boundedSearchShouldReturnNoPathIfMoveNumberIsExceeded() {
        List<String> path = KnightMovementCalculations.findOptimalPath("A1", "B1", 2);
        assertTrue("Expected no path to be found due to maximum allowed moves", path.isEmpty());
    }

    @Test
    public void boundedSearchOfLengthZeroShouldReturnASingleElementPathIfStartAndTargetAreTheSame() {
        List<String> path = KnightMovementCalculations.findOptimalPath("A1", "A1", 0);
        List<String> expectedPath = Arrays.asList("A1");
        assertEquals("Expected path to be simply the starting position if start and target match, when maximum moves allowed are zero", expectedPath, path);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnInvalidStartInput() {
        KnightMovementCalculations.findOptimalPath("A9", "B1", 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnInvalidTargetInput() {
        KnightMovementCalculations.findOptimalPath("A1", "N1", 5);
    }
}
