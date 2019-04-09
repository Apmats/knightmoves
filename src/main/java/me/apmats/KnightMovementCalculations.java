package me.apmats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class KnightMovementCalculations {

    // Our main is a simple example of how we would go about using the provided API
    // It covers the use case described by the assignment itself
    public static void main(String[] args) {
        Preconditions.checkArgument(args.length == 2, "2 arguments are expected");
        int desiredMaxPathLength = 3;
        List<String> optimalPath = findOptimalPath(args[0], args[1], desiredMaxPathLength);
        if (optimalPath.isEmpty()) {
            System.out.println("No path found between " + args[0] + " and " + args[1]);
            return;
        }
        System.out.println("Found a path between those 2 positions, the path is:");
        System.out.println(optimalPath.stream().collect(Collectors.joining("->")));
    }


    // This is our public API, might as well document it!

    /**
     * This method runs a search to find the shortest path between 2 given positions
     * 
     * @param start     the starting position as a string, following chess
     *                  convention of letter A-H followed by a number 1-8, such as
     *                  A5
     * @param target    the desired target position as a string
     * @param maxLength the maximum length of the path to be found, if negative then
     *                  the path length is unbounded   * @return a list of positions
     *                  corresponding to a path between the 2 positions, or an empty
     *                  list if no path is found
     * 
     */
    public static List<String> findOptimalPath(String start, String target, int maxMoves) {
        return seekPath(translateIntoCoords(start), translateIntoCoords(target), maxMoves).stream()
                .map(KnightMovementCalculations::translateIntoDisplayString).collect(Collectors.toList());
    }

    // An internal immutable utility class represention the board coordinates of a
    // position
    private static class Position {
        private int x, y;

        public Position(int x, int y) {
            Preconditions.checkArgument(coordsWithinBoardBounds(x, y), "Valids positions have coordinates between 1 and 8, instead we got: " + x + "," + "y");
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!Position.class.isAssignableFrom(obj.getClass())) {
                return false;
            }
            Position otherPosition = (Position) obj;
            return this.getX() == otherPosition.getX() && this.getY() == otherPosition.getY();
        }
    }

    // Translates a string representing a position into an internal object that
    // carries the corresponding coordinates
    private static Position translateIntoCoords(String str) {
        Preconditions.checkArgument(str.matches("[ABCDEFGH][12345678]"),
                "Input should be a chess board position, something  like A5, or more generally letter A throubh H and number 1 through 8, but we got: "
                        + str);
        int x = str.substring(0, 1).charAt(0) - 'A' + 1;
        int y = Integer.parseInt(str.substring(1));
        return new Position(x, y);
    }

    // Translates an internal position representation into a string we can display
    // to the user
    private static String translateIntoDisplayString(Position pos) {
        char firstPart = (char) ('A' + pos.getX() - 1);
        return String.valueOf(firstPart) + String.valueOf(pos.getY());
    }

    // Given a start and target position, this method runs a breadth first search to
    // find the shortest path to that position
    // If the path isn't found within maximum desired length an empty list is
    // returned
    // A negative path length just returns any path found, regardless of path length
    private static List<Position> seekPath(Position start, Position target, int maximumMoves) {
        if (start.equals(target))
            return Arrays.asList(start);
        boolean[][] visited = new boolean[8][8];
        Queue<List<Position>> paths = new LinkedList<>();
        paths.add(Arrays.asList(start));
        while (paths.peek() != null) {
            List<Position> currentPath = paths.poll();
            // Intuitively the break criterion here can be explained as follows:
            // a path length of X requires X-1 moves to follow
            // eg. a path of just a node A1 can be followed with 0 moves
            // and a path of two nodes A1->C2 can be followed with 1 move
            // When the current path length surpases the maximum moves allowed number
            // (ie. when it reaches maximumMoves+1)
            // then adding to the path would take more than maximum moves to achieve
            // because the new path would have maximumMoves+2 length and thus would require
            // maximuMoves+1 to follow.
            // We can then safely not follow these paths.
            if (maximumMoves >= 0 && currentPath.size() > maximumMoves)
                continue;
            Position currentPosition = currentPath.get(currentPath.size() - 1);
            for (Position move : getLegalMoves(currentPosition)) {
                if (visited[move.getX() - 1][move.getY() - 1])
                    continue;
                List<Position> newPath = new ArrayList<>();
                newPath.addAll(currentPath);
                newPath.add(move);
                if (move.equals(target))
                    return newPath;
                paths.add(newPath);
                visited[move.getX() - 1][move.getY() - 1] = true;
            }
        }
        return new ArrayList<>();
    }

    private static List<Position> getLegalMoves(Position start) {
        List<Position> legalMoves = new ArrayList<>();
        addIfLegalPosition(start.getX() + 2, start.getY() + 1, legalMoves);
        addIfLegalPosition(start.getX() + 1, start.getY() + 2, legalMoves);
        addIfLegalPosition(start.getX() + 2, start.getY() - 1, legalMoves);
        addIfLegalPosition(start.getX() + 1, start.getY() - 2, legalMoves);
        addIfLegalPosition(start.getX() - 2, start.getY() + 1, legalMoves);
        addIfLegalPosition(start.getX() - 1, start.getY() + 2, legalMoves);
        addIfLegalPosition(start.getX() - 2, start.getY() - 1, legalMoves);
        addIfLegalPosition(start.getX() - 1, start.getY() - 2, legalMoves);
        return legalMoves;
    }

    // We could generated all the permutations for the legal moves programmatically
    // However we'd have to keep these permutations in memory in a static variable
    // instead of recalculating them as done here
    // and this code overly complicates things when there's only a few permutations
    // to manually write down

    // private static List<Position> getLegalMoves(Position start) {
    // List<Position> legalMoves = new ArrayList<>();
    // List<Integer> ones = Arrays.asList(1, -1);
    // List<Integer> twos = Arrays.asList(2, -2);
    // List<List<Integer>> allPerms = new ArrayList<>();
    // allPerms.addAll(Lists.cartesianProduct(ones, twos));
    // allPerms.addAll(Lists.cartesianProduct(twos, ones));
    // allPerms.stream().forEach(
    // tupleList -> addIfLegalPosition(start.getX() + tupleList.get(0), start.getY()
    // + tupleList.get(1), legalMoves));
    // return legalMoves;
    // }

    private static void addIfLegalPosition(Integer x, Integer y, List<Position> legalMoves) {
        if (coordsWithinBoardBounds(x, y))
            legalMoves.add(new Position(x, y));
    }

    private static boolean coordsWithinBoardBounds(int x, int y) {
        if ((x >= 1 && x <= 8) && (y >= 1 && y <= 8))
            return true;
        return false;
    }

}