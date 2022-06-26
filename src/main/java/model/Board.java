package model;

import model.BoardFactory.BoardFactoryImpl;
import model.BoardFactory.BoardTest.BoardFactoryLadyMovement;
import model.BoardFactory.BoardTest.BoardFactoryLadyTest;
import model.BoardFactory.BoardTest.BoardFactoryMultipleChoiceRaid;
import model.enums.CaseType;
import model.enums.MovementType;
import model.enums.PlayerColor;

import java.util.Collections;
import java.util.List;

/**
 * Hold all the board logic. (A board is a graph of tile).
 */
public class Board {

    /**
     * All cases in the graph (to compose a board).
     */
    private final List<Case> cases;

    /**
     * Compute if one of the tile in the board is constraint.
     */
    private boolean isBoardConstraint = false;

    public Board() {
        this.cases = new BoardFactoryImpl().generateGraph();
        this.computeConstraint(PlayerColor.WHITE);
    }

    /**
     * Entry point to compute all the board logic.
     * @param pColor Color of the current player.
     * @param posX Position of the next tile selection (Y).
     * @param posY Position of the next tile selection (X).
     * @return The type of movement computed by the selection.
     */
    public MovementType computeSelected(PlayerColor pColor, int posX, int posY) {
        Case c = getCase(posX, posY);
        MovementType mt = MovementType.MOVEMENT_WITHOUT_TAKE;
        if (c.isReachable()) {
            Case selectedCase = getSelected();
            assert selectedCase != null;
            mt = selectedCase.eat(c);
            if (mt == MovementType.MOVEMENT_WITH_TAKE) {
                c.setInRaid(true);
            }
            c.setType(selectedCase.getType());
            selectedCase.setType(CaseType.EMPTY);
            this.cases.forEach(Case::resetState);
            return mt;
        }

        this.cases.forEach(Case::resetState);
        boolean isOk = c.computeSelected(pColor);
        if (isOk) {
            if (!isBoardConstraint) {
                getCase(posX, posY).computeReachable(pColor);
            } else {
                if (c.isConstraint()) {
                    getCase(posX, posY).computeReachable(pColor);
                }
            }
            return MovementType.NO_MOVEMENT;
        }
        return MovementType.NO_MOVEMENT;
    }

    /**
     * Compute the constraint on the board.
     * @param pColor Color of the last player.
     * @return if a constraint has been detected.
     */
    public boolean computeConstraint(PlayerColor pColor) {
        this.cases.forEach(Case::resetState);
        this.cases.forEach(Case::resetConstraint);
        this.isBoardConstraint = false;
        if (getPawnInRaid() != null) {
            var isConstraint = getPawnInRaid().computeConstraint(pColor);
            if (isConstraint) {
                this.isBoardConstraint = true;
            } else {
                this.cases.forEach(c -> c.setInRaid(false));
            }
            return this.isBoardConstraint;
        }

        for (Case aCase : cases) {
            var isConstraint = aCase.computeConstraint(pColor);
            if (isConstraint) {
                this.isBoardConstraint = true;
            }
        }
        return this.isBoardConstraint ;
    }

    /**
     * Get a case in the graph following if coordination.
     * @param posX Position of the case in X.
     * @param posY Position of the case in Y.
     * @return The case (or null if not in the graph).
     */
    public Case getCase(int posX, int posY) {
        return cases.get(posX * 10 + posY);
    }

    /**
     * Get all the cases in the graph (as an unmodifiable collection).
     * @return An unmodifiable list of cases.
     */
    public List<Case> getCases() {
        return Collections.unmodifiableList(cases);
    }

    public boolean isBoardConstraint() {
        return isBoardConstraint;
    }

    private Case getSelected() {
        for (Case aCase : cases) {
            if (aCase.isSelected()) {
                return aCase;
            }
        }
        return null;
    }

    private Case getPawnInRaid() {
        for (Case aCase : cases) {
            if (aCase.isInRaid()) {
                return aCase;
            }
        }
        return null;
    }

    public int getNumberOfWhitePawn() {
        int n = 0;
        for (Case aCase : cases) {
            if (aCase.getType() == CaseType.WHITE_PAWN) {
                n++;
            }
        }
        return n;
    }

    public int getNumberOfBlackPawn() {
        int n = 0;
        for (Case aCase : cases) {
            if (aCase.getType() == CaseType.BLACK_PAWN) {
                n++;
            }
        }
        return n;
    }

    public int getNumberOfWhiteLady() {
        int n = 0;
        for (Case aCase : cases) {
            if (aCase.getType() == CaseType.WHITE_LADY) {
                n++;
            }
        }
        return n;
    }

    public int getNumberOfBlackLady() {
        int n = 0;
        for (Case aCase : cases) {
            if (aCase.getType() == CaseType.BLACK_LADY) {
                n++;
            }
        }
        return n;
    }

}
