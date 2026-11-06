package io.jbnu.test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
    public static final int STAGE_COUNT = 3;

    private Player player = new Player();
    private Stage stage = new Stage1();
    private boolean gameOver;
    private int currentStage = 1;

    public void update(float dt) {
        if (gameOver || dt <= 0) return;

        player.update(dt, stage.getBlocks(), stage.getWorldWidth());

        if (player.getBounds().overlaps(stage.getFlag())) {
            if (currentStage < STAGE_COUNT) {
                currentStage++;
                stage = currentStage == 2 ? new Stage2() : new Stage3();
                player = new Player();
            } else {
                gameOver = true;
            }
        }
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public float getWorldWidth() {
        return stage.getWorldWidth();
    }

    public Player getPlayer() {
        return player;
    }

    public Rectangle getFlag() {
        return stage.getFlag();
    }

    public Array<Rectangle> getBlocks() {
        return stage.getBlocks();
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
