package io.jbnu.test;

public class DefaultEnemy extends Enemy {
    public DefaultEnemy(float x, float y) {
        super(x, y, 32, 44, 100);
    }

    @Override
    public void update(float dt, Player player, Stage stage) {
        // TODO: Implement movement, tracking and attacks.
    }
}
