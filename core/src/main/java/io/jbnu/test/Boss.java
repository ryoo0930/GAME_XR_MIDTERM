package io.jbnu.test;

/** Common entry point for boss-specific attack patterns. */
public abstract class Boss extends Enemy {
    protected Boss(float x, float y) {
        super(x, y, 64, 88, 500);
    }

    @Override
    public final void update(float dt, Player player, Stage stage) {
        if (!isAlive() || dt <= 0) return;
        updatePattern(dt, player, stage);
    }

    protected abstract void updatePattern(float dt, Player player, Stage stage);
}
