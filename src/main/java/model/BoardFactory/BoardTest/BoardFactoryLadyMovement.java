package model.BoardFactory.BoardTest;

import model.BoardFactory.BoardFactory;
import model.Case;
import model.enums.CaseType;

import java.util.List;

public class BoardFactoryLadyMovement extends BoardFactory {
    @Override
    public List<Case> generateGraph() {
        var cases = createGraph();
        cases.get(convertMatPosToArrayPos(5,5)).setType(CaseType.WHITE_LADY);
        return cases;
    }
}
