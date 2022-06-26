package view;

import model.Game;
import model.Player;
import model.enums.PlayerColor;
import view.viewListener.BoardViewListener;
import view.viewListener.GameViewListener;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

public class GameView extends JPanel implements BoardViewListener, PropertyChangeListener {

    private List<GameViewListener> listeners = new LinkedList<>();

    public void addGameViewListener(GameViewListener gameViewListener) {
        this.listeners.add(gameViewListener);
    }

    public void removeGameViewListener(GameViewListener gameViewListener) {
        this.listeners.remove(gameViewListener);
    }

    private JPanel header = new JPanel() {{
        setLayout(new GridBagLayout());
    }};

    private final BoardView boardView;

    private final JLabel blackPlayerName = new JLabel();

    private final JLabel whitePlayerName = new JLabel();

    private final JButton stop = new JButton("Stop the game");

    private final JLabel cote = new JLabel("By Louis Legrand And Ludivine Biot");

    private final Game model;

    public GameView(Game game) {
        model = game;
        this.boardView = new BoardView(game.getBoard());
        this.setLayout(new BorderLayout());
        this.add(header, BorderLayout.NORTH);
        this.header.add(blackPlayerName);
        this.header.add(stop);
        this.header.add(whitePlayerName);
        this.add(boardView, BorderLayout.CENTER);
        this.add(cote, BorderLayout.SOUTH);
        setNames();
        this.boardView.addListener(this);
        this.stop.addActionListener((__) -> {
            this.boardView.removeListener(this);
            listeners.forEach(GameViewListener::onStopGame);
        });
        game.addPropertyChangeListener(this);
        whitePlayerName.setForeground(Color.ORANGE);
    }

    @Override
    public void onBoardViewClicked(int posX, int posY) {
        listeners.forEach(l -> l.onBoardAction(posX, posY));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Game.PLAYER_CHANGED)) {
            if (((Player)evt.getNewValue()).getColor() == PlayerColor.BLACK) {
                blackPlayerName.setForeground(Color.ORANGE);
                whitePlayerName.setForeground(Color.BLACK);
            } else {
                blackPlayerName.setForeground(Color.BLACK);
                whitePlayerName.setForeground(Color.ORANGE);
            }
            setNames();
        }
    }

    private void setNames() {
        String black = String.format("(Black pawn) %s: %d pawn and %d lady",
                model.getPlayerBlack().getName(),
                model.getBoard().getNumberOfBlackPawn(),
                model.getBoard().getNumberOfBlackLady());

        String white = String.format("(White pawn) %s: %d pawn and %d lady",
                model.getPlayerWhite().getName(),
                model.getBoard().getNumberOfWhitePawn(),
                model.getBoard().getNumberOfWhiteLady());

        blackPlayerName.setText(black);
        whitePlayerName.setText(white);
    }
}
