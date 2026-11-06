package io.jbnu.test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public interface Stage {
    float getWorldWidth();
    Rectangle getFlag();
    Array<Rectangle> getBlocks();
    Array<Enemy> getEnemies();

    default boolean isCleared(Player player) {
        return player.getBounds().overlaps(getFlag());
    }
}
