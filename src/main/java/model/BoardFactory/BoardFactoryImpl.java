package model.BoardFactory;

import model.BoardFactory.BoardFactory;
import model.Case;
import model.enums.CaseType;

import java.util.LinkedList;
import java.util.List;

/**
 * Simple factory to create the graph of cases.
 */
public class BoardFactoryImpl extends BoardFactory {

    /**
     * Create a normal and all linked graph.
     * @return The graph.
     */
    @Override
    public List<Case> generateGraph() {
        var cases = createGraph();

        for (int x = 0; x < 4; x++) {
            if (x%2 == 0) {
                cases.get(convertMatPosToArrayPos(x,1)).setType(CaseType.BLACK_PAWN);
                cases.get(convertMatPosToArrayPos(x,3)).setType(CaseType.BLACK_PAWN);
                cases.get(convertMatPosToArrayPos(x,5)).setType(CaseType.BLACK_PAWN);
                cases.get(convertMatPosToArrayPos(x,7)).setType(CaseType.BLACK_PAWN);
                cases.get(convertMatPosToArrayPos(x,9)).setType(CaseType.BLACK_PAWN);
            } else {
                cases.get(convertMatPosToArrayPos(x,0)).setType(CaseType.BLACK_PAWN);
                cases.get(convertMatPosToArrayPos(x,2)).setType(CaseType.BLACK_PAWN);
                cases.get(convertMatPosToArrayPos(x,4)).setType(CaseType.BLACK_PAWN);
                cases.get(convertMatPosToArrayPos(x,6)).setType(CaseType.BLACK_PAWN);
                cases.get(convertMatPosToArrayPos(x,8)).setType(CaseType.BLACK_PAWN);
            }
        }

        for (int x = 6; x < 10; x++) {
            if (x%2 == 0) {
                cases.get(convertMatPosToArrayPos(x,1)).setType(CaseType.WHITE_PAWN);
                cases.get(convertMatPosToArrayPos(x,3)).setType(CaseType.WHITE_PAWN);
                cases.get(convertMatPosToArrayPos(x,5)).setType(CaseType.WHITE_PAWN);
                cases.get(convertMatPosToArrayPos(x,7)).setType(CaseType.WHITE_PAWN);
                cases.get(convertMatPosToArrayPos(x,9)).setType(CaseType.WHITE_PAWN);
            } else {
                cases.get(convertMatPosToArrayPos(x,0)).setType(CaseType.WHITE_PAWN);
                cases.get(convertMatPosToArrayPos(x,2)).setType(CaseType.WHITE_PAWN);
                cases.get(convertMatPosToArrayPos(x,4)).setType(CaseType.WHITE_PAWN);
                cases.get(convertMatPosToArrayPos(x,6)).setType(CaseType.WHITE_PAWN);
                cases.get(convertMatPosToArrayPos(x,8)).setType(CaseType.WHITE_PAWN);
            }
        }
        return cases;
    }

}
