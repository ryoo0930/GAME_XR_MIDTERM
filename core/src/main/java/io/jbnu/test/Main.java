package io.jbnu.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture playerTexture, blockTexture, flagTexture;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private GameWorld world;
    private BitmapFont font;

    @Override
    public void create() {
        world = new GameWorld();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(1.5f);
        playerTexture = new Texture(Gdx.files.internal("player.png"));
        blockTexture = new Texture(Gdx.files.internal("block.png"));
        flagTexture = new Texture(Gdx.files.internal("flag.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        viewport = new FitViewport(800, 600, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    @Override
    public void render() {
        // 큰 프레임 시간으로 인해 한 번에 너무 멀리 이동하는 것을 제한합니다.
        float dt = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f);
        update(dt);

        ScreenUtils.clear(1f, 1f, 1f, 1f);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Rectangle block : world.getBlocks()) {
            batch.draw(blockTexture, block.x, block.y, block.width, block.height);
        }
        Rectangle flag = world.getFlag();
        batch.draw(flagTexture, flag.x, flag.y, flag.width, flag.height);
        Rectangle player = world.getPlayer().getBounds();
        batch.draw(playerTexture, player.x, player.y, player.width, player.height);
        String stageLabel = world.isGameOver()
            ? "All stages clear!"
            : "Stage " + world.getCurrentStage() + " / " + GameWorld.STAGE_COUNT;
        font.draw(batch, stageLabel,
            camera.position.x - viewport.getWorldWidth() / 2 + 20,
            camera.position.y + viewport.getWorldHeight() / 2 - 20);
        batch.end();
    }

    private void update(float dt) {
        if (world.isGameOver() || dt <= 0) return;

        world.update(dt);

        // 카메라는 X축으로만 따라갑니다.
        Rectangle player = world.getPlayer().getBounds();
        camera.position.x = MathUtils.clamp(
            player.x + player.width / 2, 400, world.getWorldWidth() - 400);
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        playerTexture.dispose();
        blockTexture.dispose();
        flagTexture.dispose();
    }
}
