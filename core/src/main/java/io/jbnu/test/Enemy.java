package io.jbnu.test;

import com.badlogic.gdx.math.Rectangle;

/** Shared enemy state. Movement, attacks and contact damage are added by subclasses. */
public abstract class Enemy {
    private final Rectangle bounds;
    private final int maxHealth;
    private int health;

    protected Enemy(float x, float y, float width, float height, int maxHealth) {
        if (width <= 0 || height <= 0 || maxHealth <= 0) {
            throw new IllegalArgumentException("Enemy dimensions and health must be positive.");
        }
        bounds = new Rectangle(x, y, width, height);
        this.maxHealth = maxHealth;
        health = maxHealth;
    }

    public abstract void update(float dt, Player player, Stage stage);

    public void takeDamage(int amount) {
        if (amount <= 0) return;
        health = amount >= health ? 0 : health - amount;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isAlive() {
        return health > 0;
    }
}
