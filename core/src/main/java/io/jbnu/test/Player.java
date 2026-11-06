package io.jbnu.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player {
    private static final float MOVE_SPEED = 260;
    private static final float JUMP_SPEED = 650;
    private static final float GRAVITY = 1700;

    private final Rectangle player = new Rectangle(80, 60, 32, 44);
    private float velocityX, velocityY;
    private boolean grounded;

    public void update(float dt, Array<Rectangle> blocks, float worldWidth) {
        // 좌우 이동과 지상 점프
        velocityX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) velocityX -= MOVE_SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) velocityX += MOVE_SPEED;
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && grounded) {
            velocityY = JUMP_SPEED;
            grounded = false;
        }

        // 중력: 아래쪽 속도는 음수이며 최대 낙하 속도를 제한합니다.
        velocityY = Math.max(velocityY - GRAVITY * dt, -900);

        // X축과 Y축을 나누어 이동하고 충돌을 보정합니다.
        moveX(dt, blocks, worldWidth);
        moveY(dt, blocks);

        // 낭떠러지에 떨어지면 시작점으로 복귀합니다.
        if (player.y < -100) {
            player.setPosition(80, 60);
            velocityY = 0;
            grounded = false;
        }
    }

    private void moveX(float dt, Array<Rectangle> blocks, float worldWidth) {
        player.x += velocityX * dt;
        for (Rectangle block : blocks) {
            if (!player.overlaps(block)) continue;
            if (velocityX > 0) player.x = block.x - player.width;
            else if (velocityX < 0) player.x = block.x + block.width;
        }
        player.x = MathUtils.clamp(player.x, 0, worldWidth - player.width);
    }

    private void moveY(float dt, Array<Rectangle> blocks) {
        float dy = velocityY * dt;
        player.y += dy;
        grounded = false;

        for (Rectangle block : blocks) {
            if (!player.overlaps(block)) continue;
            if (dy < 0) { // 내려오던 중: 블록 위에 착지
                player.y = block.y + block.height;
                velocityY = 0;
                grounded = true;
            } else if (dy > 0) { // 올라가던 중: 블록 아래에 머리 충돌
                player.y = block.y - player.height;
                velocityY = 0;
            }
        }
    }

    public Rectangle getBounds() {
        return player;
    }
}
