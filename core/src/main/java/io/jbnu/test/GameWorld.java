package io.jbnu.test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
    public static final float WORLD_WIDTH = 2600;

    private final Player player = new Player();
    private final Rectangle flag = new Rectangle(2420, 344, 20, 100);
    private final Array<Rectangle> blocks = new Array<>();
    private boolean gameOver;

    public GameWorld() {
        // x, y, 너비, 높이. 바닥도 같은 충돌 블록입니다.
        blocks.add(new Rectangle(0, 0, 620, 60));
        blocks.add(new Rectangle(760, 0, 540, 60));
        blocks.add(new Rectangle(1440, 0, 1160, 60));
        blocks.add(new Rectangle(300, 60, 80, 64));
        blocks.add(new Rectangle(900, 60, 96, 80));
        blocks.add(new Rectangle(1080, 180, 160, 24));
        blocks.add(new Rectangle(1650, 60, 120, 70));
        blocks.add(new Rectangle(1790, 60, 120, 140));
        blocks.add(new Rectangle(1930, 60, 120, 210));
        blocks.add(new Rectangle(2100, 270, 110, 24));
        blocks.add(new Rectangle(2280, 320, 210, 24));
    }

    public void update(float dt) {
        if (gameOver || dt <= 0) return;

        player.update(dt, blocks, WORLD_WIDTH);

        // 깃발에 닿으면 게임 로직을 멈추고 마지막 화면을 유지합니다.
        if (player.getBounds().overlaps(flag)) gameOver = true;
    }

    public Player getPlayer() {
        return player;
    }

    public Rectangle getFlag() {
        return flag;
    }

    public Array<Rectangle> getBlocks() {
        return blocks;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
