package controller;

import model.Game;
import view.GameView;
import view.LaunchView;
import view.viewListener.GameViewListener;
import view.viewListener.LaunchViewListener;

import javax.swing.*;

public class GameController implements GameViewListener, LaunchViewListener {

    private Game model;

    private final JFrame view = new JFrame();

    public GameController() {
        LaunchView lv = new LaunchView();
        lv.addListener(this);
        this.view.setContentPane(lv);
        this.view.setSize(1000, 1000);
        this.view.setResizable(false);
        this.view.setLocationRelativeTo(null);
        this.view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void init() {
        this.view.setVisible(true);
    }

    @Override
    public void onStopGame() {
        ((GameView)this.view.getContentPane()).removeGameViewListener(this);
        LaunchView lv = new LaunchView();
        lv.addListener(this);
        this.view.setContentPane(lv);
        this.model = null;
        this.view.revalidate();
    }

    @Override
    public void onBoardAction(int posX, int posY) {
        if (model != null) {
            model.caseAction(posX, posY);
        }
    }

    @Override
    public void onPlayerValidated(String nameP1, String nameP2) {
        this.model = new Game(nameP1, nameP2);
        GameView gv = new GameView(this.model);
        gv.addGameViewListener(this);
        ((LaunchView)this.view.getContentPane()).removeListener(this);
        this.view.setContentPane(gv);
        this.view.revalidate();
    }
}
