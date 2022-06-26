package model.BoardFactory.BoardTest;

import model.BoardFactory.BoardFactory;
import model.Case;
import model.enums.CaseType;

import java.util.List;

public class BoardFactoryMultipleChoiceRaid extends BoardFactory {
    @Override
    public List<Case> generateGraph() {
        var graph = createGraph();

        graph.get(convertMatPosToArrayPos(5,4)).setType(CaseType.WHITE_PAWN);
        graph.get(convertMatPosToArrayPos(5,6)).setType(CaseType.WHITE_PAWN);
        graph.get(convertMatPosToArrayPos(4,3)).setType(CaseType.BLACK_PAWN);
        graph.get(convertMatPosToArrayPos(4,5)).setType(CaseType.BLACK_PAWN);
        graph.get(convertMatPosToArrayPos(2,1)).setType(CaseType.BLACK_PAWN);

        return graph;
    }
}
