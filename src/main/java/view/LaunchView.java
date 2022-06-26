package view;

import view.viewListener.LaunchViewListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class LaunchView extends JPanel {

    private final List<LaunchViewListener> listeners = new LinkedList<>();

    public void addListener(LaunchViewListener listener) {
        this.listeners.add(listener);
    }

    public final void removeListener(LaunchViewListener listener) {
        this.listeners.remove(listener);
    }

    private final JLabel player1Label = new JLabel("Player 1 : ");

    private final JLabel player2Label = new JLabel("Player 2 : ");

    private final JButton validate = new JButton("Validate");

    private final JTextField player1TextField = new JTextField() {{
        setColumns(15);
    }};

    private final JTextField player2TextField = new JTextField() {{
        setColumns(15);
    }};

    public LaunchView() {
        this.setLayout(new GridBagLayout());
        this.add(player1Label);
        this.add(player1TextField);
        this.add(validate);
        this.add(player2Label);
        this.add(player2TextField);
        this.validate.addActionListener((__) -> listeners.forEach((l) ->
                l.onPlayerValidated(player1TextField.getText(), player2TextField.getText())));
    }
}
