package model.BoardFactory;

import model.Case;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class to hold board factory logic.
 */
public abstract class BoardFactory {

    /**
     * Method to generate a graph computed with pawn.
     * @return The graph.
     */
    public abstract List<Case> generateGraph();

    /**
     * Generate an empty graph.
     * @return The empty graph.
     */
    protected List<Case> createGraph() {
        var cases = new LinkedList<Case>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                cases.add(new Case(x, y));
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                var c = cases.get(convertMatPosToArrayPos(i,j));
                if (i != 0 && j != 0) {
                    c.addNeighbour(Case.Direction.TOP_LEFT, cases.get(convertMatPosToArrayPos(i-1, j-1)));
                }
                if (i != 9 && j != 9) {
                    c.addNeighbour(Case.Direction.BOTTOM_RIGHT, cases.get(convertMatPosToArrayPos(i+1, j+1)));
                }
                if (i != 0 && j != 9) {
                    c.addNeighbour(Case.Direction.BOTTOM_LEFT, cases.get(convertMatPosToArrayPos(i-1, j+1)));
                }
                if (i != 9 && j != 0) {
                    c.addNeighbour(Case.Direction.TOP_RIGHT, cases.get(convertMatPosToArrayPos(i+1, j-1)));
                }
            }
        }
        return cases;
    }

    protected int convertMatPosToArrayPos(int posX, int posY) {
        return posY * 10 + posX;
    }

}
