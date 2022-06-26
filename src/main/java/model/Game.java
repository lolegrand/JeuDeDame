package model;

import model.enums.MovementType;
import model.enums.PlayerColor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Hold all the game logics.
 */
public class Game {

    /**
     * Property name when the current player change.
     */
    public static final String PLAYER_CHANGED = "player_changed";

    /**
     * Property change boiler code to implement listener (modern way).
     */
    transient private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
    }

    /**
     * The player with black pawn.
     */
    private final Player playerBlack;

    /**
     * The player with white pawn.
     */
    private final Player playerWhite;

    /**
     * The currently playing player.
     */
    private Player currentPlayer;

    /**
     * The board of the game.
     */
    private final Board board;

    public Game(String playerOne, String playerTwo) {
        this.playerBlack = new Player(playerOne, PlayerColor.BLACK);
        this.playerWhite = new Player(playerTwo, PlayerColor.WHITE);
        this.board = new Board();
        this.currentPlayer = playerWhite;
    }

    /**
     * Put an action on the game. The actions depend on the currently state of the game. It is the only entry point
     * for the user to use the model.
     * @param posX X position of the action.
     * @param posY Y position of the action.
     */
    public void caseAction(int posX, int posY) {
        MovementType movementType = MovementType.NO_MOVEMENT;
        if (currentPlayer == playerWhite) {
            movementType = this.board.computeSelected(PlayerColor.WHITE, posX, posY);
        } else if (currentPlayer == playerBlack) {
            movementType = this.board.computeSelected(PlayerColor.BLACK, posX, posY);
        }
        
        switch (movementType) {
            case NO_MOVEMENT -> {

            }
            case MOVEMENT_WITHOUT_TAKE -> {
                switchPlayer();
                if (currentPlayer == playerWhite) {
                    this.board.computeConstraint(PlayerColor.WHITE);
                } else {
                    this.board.computeConstraint(PlayerColor.BLACK);
                }
            }
            case MOVEMENT_WITH_TAKE -> {
                if (currentPlayer == playerWhite) {
                    this.board.computeConstraint(PlayerColor.WHITE);
                } else {
                    this.board.computeConstraint(PlayerColor.BLACK);
                }

                if (!board.isBoardConstraint()) {
                    switchPlayer();
                }

                if (currentPlayer == playerWhite) {
                    this.board.computeConstraint(PlayerColor.WHITE);
                } else {
                    this.board.computeConstraint(PlayerColor.BLACK);
                }
            }
        }
    }

    private void switchPlayer() {
        Player old = this.currentPlayer;
        if (currentPlayer == playerWhite) {
            currentPlayer = playerBlack;
        } else {
            currentPlayer = playerWhite;
        }
        propertyChangeSupport.firePropertyChange(PLAYER_CHANGED, old, currentPlayer);
    }

    public Player getPlayerBlack() {
        return playerBlack;
    }

    public Player getPlayerWhite() {
        return playerWhite;
    }

    public Board getBoard() {
        return board;
    }
}
