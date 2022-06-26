package model;

import model.enums.CaseType;
import model.enums.MovementType;
import model.enums.PlayerColor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Hold all the logic of the cases.
 */
public class Case {

    /**
     * Property change name when the type of the case change.
     */
    public static final String TYPE_CHANGED = "type_changed";

    /**
     * Property change name when the case selected state change.
     */
    public static final String IS_SELECTED_CHANGED = "is_selected_changed";

    /**
     * Property change name when the case constraint value is selected.
     */
    public static final String IS_CONSTRAINT_CHANGED = "is_constraint_changed";

    /**
     * Property change name when the case become reachable.
     */
    public static final String IS_REACHABLE_CHANGED = "is_reachable_changed";

    /**
     * Property change listeners boiler code.
     */
    transient private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
    }

    /**
     * Define the X position of the case in a graph.
     */
    private final int posX;

    /**
     * Define the Y position of the case in a graph.
     */
    private final int posY;

    /**
     * Define the type of case (see enum for more info).
     */
    private CaseType type = CaseType.EMPTY;

    /**
     * Define if the case is selected.
     */
    private boolean isSelected = false;

    /**
     * Define if the case in constraint.
     */
    private boolean isConstraint = false;

    /**
     * Define if the case is reachable.
     */
    private boolean isReachable = false;

    /**
     * Define if the pawn is in read.
     */
    private boolean isInRaid = false;

    /**
     * Define all the neighbour of this case in a "Jeu de dame" game.
     */
    private Map<Direction, Case> neighbour = new HashMap<>();

    public Case(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Add a neighbour to this case.
     * @param direction The direction of the neighbour.
     * @param c The case to be added.
     * @return If the case as correctly be added.
     */
    public boolean addNeighbour(Direction direction, Case c) {
        if (neighbour.containsKey(direction)) {
            return false;
        } else {
            neighbour.put(direction, c);
            return true;
        }
    }

    /**
     * Compute if this case constraint the player to play with it. Following the neighbours.
     * @param pColor The color of the player to who compute the constraint.
     * @return If the player is constraint.
     */
    public boolean computeConstraint(PlayerColor pColor) {
        if (pColor == PlayerColor.BLACK && type == CaseType.BLACK_LADY) {
            return computeLadyConstraint(Direction.TOP_RIGHT) ||
                    computeLadyConstraint(Direction.TOP_LEFT) ||
                    computeLadyConstraint(Direction.BOTTOM_RIGHT) ||
                    computeLadyConstraint(Direction.BOTTOM_LEFT);
        }

        if (pColor == PlayerColor.WHITE && type == CaseType.WHITE_LADY) {
            return computeLadyConstraint(Direction.TOP_RIGHT) ||
                    computeLadyConstraint(Direction.TOP_LEFT) ||
                    computeLadyConstraint(Direction.BOTTOM_RIGHT) ||
                    computeLadyConstraint(Direction.BOTTOM_LEFT);
        }

        for (var entry : neighbour.entrySet()) {
            Case c = entry.getValue();
            if (pColor == PlayerColor.WHITE) {
                if (computeColoredConstraint(entry,
                        c, CaseType.WHITE_LADY, CaseType.WHITE_PAWN, CaseType.BLACK_LADY, CaseType.BLACK_PAWN))
                    return true;
            }
            if (pColor == PlayerColor.BLACK) {
                if (computeColoredConstraint((Map.Entry<Direction, Case>) entry,
                        c, CaseType.BLACK_LADY, CaseType.BLACK_PAWN, CaseType.WHITE_LADY, CaseType.WHITE_PAWN))
                    return true;
            }
        }
        this.setConstraint(false);
        return false;
    }

    private boolean computeLadyConstraint(Direction direction) {
        Case c = getNeighbour(direction);
        while (c != null) {
            if (type == CaseType.WHITE_LADY) {
                if (c.getType() == CaseType.BLACK_LADY || c.getType() == CaseType.BLACK_PAWN) {
                    if (c.getNeighbour(direction) != null && c.getNeighbour(direction).getType() == CaseType.EMPTY) {
                        this.setConstraint(true);
                        return true;
                    } else {
                        return false;
                    }
                } else if (c.getType() != CaseType.EMPTY) {
                    return false;
                }
            } else {
                if (c.getType() == CaseType.WHITE_LADY || c.getType() == CaseType.WHITE_PAWN) {
                    if (c.getNeighbour(direction) != null && c.getNeighbour(direction).getType() == CaseType.EMPTY) {
                        this.setConstraint(true);
                        return true;
                    } else {
                        return false;
                    }
                } else if (c.getType() != CaseType.EMPTY) {
                    return false;
                }
            }
            c = c.getNeighbour(direction);
        }
        return false;
    }

    private boolean computeColoredConstraint(Map.Entry<Direction, Case> entry, Case c, CaseType whiteLady, CaseType whitePawn, CaseType blackLady, CaseType blackPawn) {
        if (this.getType() == whiteLady || this.getType() == whitePawn) {
            if (c.getType() == blackLady || c.getType() == blackPawn) {
                if (c.getNeighbour(entry.getKey()) != null
                        && c.getNeighbour(entry.getKey()).getType() == CaseType.EMPTY ) {
                    this.setConstraint(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compute if the player can select this case, following the player color.
     * @param pColor&
     * @return
     */
    public boolean computeSelected(PlayerColor pColor) {
        if (pColor == PlayerColor.WHITE) {
            if (this.getType() == CaseType.WHITE_LADY || this.getType() == CaseType.WHITE_PAWN) {
                this.setSelected(true);
                return true;
            }
        } else {
            if (this.getType() == CaseType.BLACK_PAWN || this.getType() == CaseType.BLACK_LADY) {
                this.setSelected(true);
                return true;
            }
        }
        this.setSelected(false);
        return false;
    }

    /**
     * Compute all the reachable cases in the neighbourhood of this case.
     * @param pColor Color of the current player.
     */
    public void computeReachable(PlayerColor pColor) {
        if (this.isConstraint) {
            if (this.type == CaseType.BLACK_LADY || this.type == CaseType.WHITE_LADY) {
                computeLadyReachableConstrained(Direction.TOP_RIGHT);
                computeLadyReachableConstrained(Direction.TOP_LEFT);
                computeLadyReachableConstrained(Direction.BOTTOM_LEFT);
                computeLadyReachableConstrained(Direction.BOTTOM_RIGHT);
            }
            if (pColor == PlayerColor.WHITE) {
                for (var entry : neighbour.entrySet()) {
                   Case nei = entry.getValue();
                   if (nei != null && (nei.getType() == CaseType.BLACK_LADY || nei.getType() == CaseType.BLACK_PAWN)) {
                       if (nei.getNeighbour(entry.getKey()).getType() == CaseType.EMPTY) {
                           nei.getNeighbour(entry.getKey()).setReachable(true);
                       }
                   }
                }
            } else {
                for (var entry : neighbour.entrySet()) {
                    Case nei = entry.getValue();
                    if (nei != null && (nei.getType() == CaseType.WHITE_PAWN || nei.getType() == CaseType.WHITE_LADY)) {
                        if (nei.getNeighbour(entry.getKey()) != null && nei.getNeighbour(entry.getKey()).getType() == CaseType.EMPTY) {
                            nei.getNeighbour(entry.getKey()).setReachable(true);
                        }
                    }
                }
            }
        } else {
            if (pColor == PlayerColor.BLACK) {
                if (type == CaseType.BLACK_LADY) {
                    computeLadyReachable(Direction.TOP_RIGHT);
                    computeLadyReachable(Direction.TOP_LEFT);
                    computeLadyReachable(Direction.BOTTOM_RIGHT);
                    computeLadyReachable(Direction.BOTTOM_LEFT);
                    return;
                }

                if (neighbour.get(Direction.BOTTOM_RIGHT) != null
                        && neighbour.get(Direction.BOTTOM_RIGHT).getType() == CaseType.EMPTY) {
                    neighbour.get(Direction.BOTTOM_RIGHT).setReachable(true);
                }
                if (neighbour.get(Direction.TOP_RIGHT) != null
                        && neighbour.get(Direction.TOP_RIGHT).getType() == CaseType.EMPTY) {
                    neighbour.get(Direction.TOP_RIGHT).setReachable(true);
                }
            } else {
                if (type == CaseType.WHITE_LADY) {
                    computeLadyReachable(Direction.TOP_RIGHT);
                    computeLadyReachable(Direction.TOP_LEFT);
                    computeLadyReachable(Direction.BOTTOM_RIGHT);
                    computeLadyReachable(Direction.BOTTOM_LEFT);
                    return;
                }

                if (neighbour.get(Direction.TOP_LEFT) != null
                        && neighbour.get(Direction.TOP_LEFT).getType() == CaseType.EMPTY) {
                    neighbour.get(Direction.TOP_LEFT).setReachable(true);
                }
                if (neighbour.get(Direction.BOTTOM_LEFT) != null
                        && neighbour.get(Direction.BOTTOM_LEFT).getType() == CaseType.EMPTY) {
                    neighbour.get(Direction.BOTTOM_LEFT).setReachable(true);
                }
            }
        }
    }

    private void computeLadyReachable(Direction direction) {
        Case c = getNeighbour(direction);
        while (c != null && c.getType() == CaseType.EMPTY) {
            c.setReachable(true);
            c = c.getNeighbour(direction);
        }
    }
    private void computeLadyReachableConstrained(Direction direction) {
        Case c = getNeighbour(direction);
        while (c != null) {
            if (type == CaseType.WHITE_LADY) {
                if (c.getType() == CaseType.BLACK_LADY || c.getType() == CaseType.BLACK_PAWN) {
                    if (c.getNeighbour(direction) != null && c.getNeighbour(direction).getType() == CaseType.EMPTY) {
                        c = c.getNeighbour(direction);
                        while (c != null) {
                            if (c.type == CaseType.EMPTY) {
                                c.setReachable(true);
                            } else {
                                return;
                            }
                            c = c.getNeighbour(direction);
                        }
                        return;
                    } else {
                        return;
                    }
                }
            } else {
                if (c.getType() == CaseType.WHITE_LADY || c.getType() == CaseType.WHITE_PAWN) {
                    if (c.getNeighbour(direction) != null && c.getNeighbour(direction).getType() == CaseType.EMPTY) {
                        c = c.getNeighbour(direction);
                        while (c != null) {
                            if (c.type == CaseType.EMPTY) {
                                c.setReachable(true);
                            } else {
                                return;
                            }
                            c = c.getNeighbour(direction);
                        }
                        return;
                    } else {
                        return;
                    }
                }
            }
            c = c.getNeighbour(direction);
        }
    }

    /**
     * Compute a pawn to eat another one.
     * @param destination Destination of the moving pawn.
     */
    public MovementType eat(Case destination) {
        Direction dir = null;
        MovementType mt = MovementType.MOVEMENT_WITHOUT_TAKE;

        if (destination.getPosX() < this.posX && destination.getPosY() < this.posY) {
            dir = Direction.TOP_LEFT;
        } else if (destination.getPosX() > this.posX && destination.getPosY() < this.posY) {
            dir = Direction.BOTTOM_LEFT;
        } else if (destination.getPosX() < this.posX && destination.getPosY() > this.posY) {
            dir = Direction.TOP_RIGHT;
        } else if (destination.getPosX() > this.posX && destination.getPosY() > this.posY) {
            dir = Direction.BOTTOM_RIGHT;
        }
        Case c = getNeighbour(dir);

        while (c != destination) {
            if (c.getType() != CaseType.EMPTY) {
                c.setType(CaseType.EMPTY);
                mt = MovementType.MOVEMENT_WITH_TAKE;
            }
            c = c.getNeighbour(dir);
        }
        return mt;
    }

    /**
     * Compute if the case is in the neighbourhood of this case.
     * @param c The case to be computed with.
     * @return Relative true / false.
     */
    public boolean isNeighbourOf(Case c) {
        if (getNeighbour(Direction.TOP_LEFT) == c) {
            return true;
        }
        if (getNeighbour(Direction.TOP_RIGHT) == c) {
            return true;
        }
        if (getNeighbour(Direction.BOTTOM_LEFT) == c) {
            return true;
        }
        if (getNeighbour(Direction.BOTTOM_RIGHT) == c) {
            return true;
        }
        return false;
    }

    public Case getNeighbour(Direction direction) {
        return neighbour.get(direction);
    }

    public boolean isSelected() {
        return isSelected;
    }

    private void setSelected(boolean selected) {
        boolean old = this.isSelected;
        this.isSelected = selected;
        propertyChangeSupport.firePropertyChange(IS_SELECTED_CHANGED, old, this.isSelected);
    }

    public boolean isConstraint() {
        return isConstraint;
    }

    private void setConstraint(boolean constraint) {
        boolean old = this.isConstraint;
        isConstraint = constraint;
        propertyChangeSupport.firePropertyChange(IS_CONSTRAINT_CHANGED, old, this.isConstraint);
    }

    public boolean isReachable() {
        return isReachable;
    }

    private void setReachable(boolean reachable) {
        boolean old = this.isReachable;
        isReachable = reachable;
        propertyChangeSupport.firePropertyChange(IS_REACHABLE_CHANGED, old, this.isReachable);
    }

    public CaseType getType() {
        return type;
    }

    public void setType(CaseType type) {
        CaseType old = this.type;

        this.type = type;

        if (posY == 0 && type == CaseType.WHITE_PAWN) {
            this.type = CaseType.WHITE_LADY;
        }

        if (posY == 9 && type == CaseType.BLACK_PAWN) {
            this.type = CaseType.BLACK_LADY;
        }

        propertyChangeSupport.firePropertyChange(TYPE_CHANGED, old, this.type);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isInRaid() {
        return isInRaid;
    }

    public void setInRaid(boolean inRaid) {
        isInRaid = inRaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case aCase = (Case) o;
        return posX == aCase.posX && posY == aCase.posY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posX, posY);
    }

    /**
     * Reset all state applied on this case.
     */
    public void resetState() {
        setReachable(false);
        setSelected(false);
    }

    /**
     * Reset all constraint applied on this case.
     */
    public void resetConstraint() {
        setConstraint(false);
    }

    /**
     * The different direction of neighbour.
     */
    public enum Direction {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
}
