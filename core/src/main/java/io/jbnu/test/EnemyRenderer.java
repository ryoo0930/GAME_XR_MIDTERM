package io.jbnu.test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/** Rendering extension point; no enemy textures have been assigned yet. */
public class EnemyRenderer implements Disposable {
    public void draw(SpriteBatch batch, Enemy enemy) {
        if (!enemy.isAlive()) return;
        // TODO: Select enemy textures and draw at enemy.getBounds().
    }

    @Override
    public void dispose() {
        // Dispose enemy textures here once they are introduced.
    }
}
