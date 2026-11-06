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
        Array<Enemy> enemies = stage.getEnemies();
        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            if (enemy.isAlive()) enemy.update(dt, player, stage);
            if (!enemy.isAlive()) enemies.removeIndex(i);
        }

        if (stage.isCleared(player)) nextStage();
    }

    private void nextStage() {
        if (currentStage == STAGE_COUNT) {
            gameOver = true;
            return;
        }
        currentStage++;
        stage = currentStage == 2 ? new Stage2() : new Stage3();
        player = new Player();
    }

    public Array<Enemy> getEnemies() {
        return stage.getEnemies();
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
