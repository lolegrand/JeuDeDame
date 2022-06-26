package model;

import model.enums.PlayerColor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Hold player logic. (Simple bean to store information)
 */
public class Player implements Serializable {

    /**
     * Name of the player.
     */
    private final String name;

    /**
     * Color of the player.
     */
    private final PlayerColor color;

    public Player(String name, PlayerColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return this.color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return color == player.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
