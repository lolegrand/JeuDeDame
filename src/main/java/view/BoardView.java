package view;

import model.Board;
import model.Case;
import model.enums.CaseType;
import view.viewListener.BoardViewListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BoardView extends JPanel implements PropertyChangeListener {

    private Board board;

    private final List<BoardViewListener> listeners = new LinkedList<>();

    public void addListener(BoardViewListener boardViewListener) {
        this.listeners.add(boardViewListener);
    }

    public void removeListener(BoardViewListener boardViewListener) {
        this.listeners.remove(boardViewListener);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphic2d = (Graphics2D) g;

        graphic2d.setColor(Color.WHITE);
        graphic2d.fillRect(0,0 , getWidth(), getHeight());

        int odd = 0;

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                var c = board.getCase(x,y);

                odd = (x + y) % 2 ;

                if (odd == 1) {
                    graphic2d.setColor(Color.LIGHT_GRAY);
                    graphic2d.fillRect(c.getPosX() * (getSize().width / 10),
                            c.getPosY() * (getSize().height / 10),
                            (getSize().width / 10),
                            (getSize().height / 10));
                }

                if (c.isConstraint()) {
                    graphic2d.setColor(Color.ORANGE);
                    graphic2d.fillRect(c.getPosX() * (getSize().width / 10),
                            c.getPosY() * (getSize().height / 10),
                            (getSize().width / 10),
                            (getSize().height / 10));
                }

                if (c.isSelected()) {
                    graphic2d.setColor(Color.GREEN);
                    graphic2d.fillRect(c.getPosX() * (getSize().width / 10),
                            c.getPosY() * (getSize().height / 10),
                            (getSize().width / 10),
                            (getSize().height / 10));

                }

                if (c.isReachable()) {
                    graphic2d.setColor(Color.RED);
                    graphic2d.fillRect(c.getPosX() * (getSize().width / 10),
                            c.getPosY() * (getSize().height / 10),
                            (getSize().width / 10),
                            (getSize().height / 10));
                }

                if (c.getType() != CaseType.EMPTY) {
                    if (c.getType() == CaseType.BLACK_LADY) {
                        try {
                            BufferedImage image = ImageIO.read(getClass().getResource("/crown_black.png"));
                            Image tmp = image.getScaledInstance(getSize().width / 10,
                                    getSize().height / 10, Image.SCALE_SMOOTH);
                            graphic2d.drawImage(tmp, c.getPosX() * (getSize().width / 10),
                                    c.getPosY() * (getSize().height / 10),null);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (c.getType() == CaseType.WHITE_LADY) {
                        try {
                            BufferedImage image = ImageIO.read(getClass().getResource("/crown_red.png"));
                            Image tmp = image.getScaledInstance(getSize().width / 10,
                                    getSize().height / 10, Image.SCALE_SMOOTH);
                            graphic2d.drawImage(tmp, c.getPosX() * (getSize().width / 10),
                                    c.getPosY() * (getSize().height / 10),null);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        switch (c.getType()) {
                            case BLACK_PAWN, BLACK_LADY -> graphic2d.setColor(Color.BLACK);
                            case WHITE_PAWN, WHITE_LADY -> graphic2d.setColor(Color.RED);
                        }
                        graphic2d.fillOval(c.getPosX() * (getSize().width / 10),
                                c.getPosY() * (getSize().height / 10),
                                (getSize().width / 10),
                                (getSize().height / 10));
                    }
                }
            }
        }

        for (int i = 1; i < 10; i++) {
            graphic2d.setColor(Color.BLACK);
            graphic2d.drawLine(0, (getSize().height / 10) * i, getSize().width, (getSize().height / 10) * i);
            graphic2d.drawLine((getSize().width / 10) * i, 0, (getSize().width / 10) * i, getSize().height);
        }
    }

    public BoardView (Board board) {
        this.board = board;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / (getWidth()/10);
                int y = e.getY() / (getHeight()/10);
                listeners.forEach(l -> l.onBoardViewClicked(x, y));
            }
        });
        this.board.getCases().forEach(c -> c.addPropertyChangeListener(this));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Case.IS_CONSTRAINT_CHANGED) ||
                evt.getPropertyName().equals(Case.IS_REACHABLE_CHANGED) ||
                    evt.getPropertyName().equals(Case.IS_SELECTED_CHANGED) ||
                        evt.getPropertyName().equals(Case.TYPE_CHANGED)) {
            repaint();
        }
    }
}
