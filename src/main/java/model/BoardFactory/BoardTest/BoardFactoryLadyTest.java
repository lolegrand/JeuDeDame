package model.BoardFactory.BoardTest;

import model.BoardFactory.BoardFactory;
import model.Case;
import model.enums.CaseType;

import java.util.List;

public class BoardFactoryLadyTest extends BoardFactory {
    @Override
    public List<Case> generateGraph() {
        var cases = createGraph();
        cases.get(convertMatPosToArrayPos(5,4)).setType(CaseType.WHITE_LADY);

        cases.get(convertMatPosToArrayPos(7,6)).setType(CaseType.BLACK_PAWN);

        cases.get(convertMatPosToArrayPos(3,2)).setType(CaseType.BLACK_PAWN);

        cases.get(convertMatPosToArrayPos(6,3)).setType(CaseType.BLACK_PAWN);

        cases.get(convertMatPosToArrayPos(3,6)).setType(CaseType.BLACK_PAWN);
        cases.get(convertMatPosToArrayPos(1,8)).setType(CaseType.BLACK_PAWN);

        return cases;
    }
}
