package io.jbnu.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import java.util.EnumMap;

/** Owns shared motion textures so changing stages does not reload player assets. */
public class PlayerRenderer implements Disposable {
    private static final int FRAME_COUNT = 6;
    // Use one scale for every frame; resizing individual silhouettes causes pulsing.
    private static final float IDLE_HEIGHT = 475;
    private final EnumMap<Player.Motion, Texture> textures = new EnumMap<>(Player.Motion.class);
    private final EnumMap<Player.Motion, Animation<TextureRegion>> animations =
        new EnumMap<>(Player.Motion.class);
    private final EnumMap<Player.Motion, float[]> pivots = new EnumMap<>(Player.Motion.class);
    private final EnumMap<Player.Motion, Float> baselines = new EnumMap<>(Player.Motion.class);

    public PlayerRenderer() {
        // Alpha-weighted torso centers measured in each 362x724 frame (rows 280..430).
        // Canvas centers differ from body centers and otherwise create backward drift.
        load(Player.Motion.IDLE, "Idle", 0.16f, true, 615,
            new float[]{197.2f, 190.8f, 178.7f, 169.3f, 158.4f, 149.0f});
        load(Player.Motion.WALK, "Walk", 0.12f, true, 623,
            new float[]{206.4f, 194.8f, 186.1f, 183.4f, 172.6f, 165.7f});
        load(Player.Motion.RUN, "Running", 0.08f, true, 599,
            new float[]{200.5f, 190.7f, 188.6f, 184.4f, 173.5f, 169.3f});
        // Keep the vertical pose changes within the jump artwork.
        load(Player.Motion.JUMP, "Jump", 0.12f, false, 623,
            new float[]{227.0f, 199.4f, 185.5f, 176.7f, 165.8f, 196.7f});
        load(Player.Motion.RUN_JUMP, "RunningJump", 0.12f, false, 623,
            new float[]{206.9f, 200.2f, 186.3f, 176.4f, 171.6f, 167.0f});
    }

    private void load(Player.Motion motion, String name, float frameDuration, boolean loop,
                      float baseline, float[] framePivots) {
        Texture texture = new Texture(Gdx.files.internal("player/" + name + ".png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(motion, texture);
        TextureRegion[] frames = TextureRegion.split(
            texture, texture.getWidth() / FRAME_COUNT, texture.getHeight())[0];
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
        animations.put(motion, animation);
        pivots.put(motion, framePivots);
        baselines.put(motion, baseline);
    }

    public void draw(SpriteBatch batch, Player player) {
        Player.Motion motion = player.getMotion();
        Animation<TextureRegion> animation = animations.get(motion);
        int frameIndex = animation.getKeyFrameIndex(player.getAnimationTime());
        TextureRegion frame = animation.getKeyFrames()[frameIndex];
        Rectangle bounds = player.getBounds();
        float scale = bounds.height / IDLE_HEIGHT;
        float width = frame.getRegionWidth() * scale;
        float height = frame.getRegionHeight() * scale;
        float pivot = pivots.get(motion)[frameIndex];
        if (!player.isFacingRight()) pivot = frame.getRegionWidth() - pivot;
        float x = bounds.x + bounds.width / 2 - pivot * scale;
        float y = bounds.y - (frame.getRegionHeight() - baselines.get(motion)) * scale;
        if (player.isFacingRight()) batch.draw(frame, x, y, width, height);
        else batch.draw(frame, x + width, y, -width, height);
    }

    @Override
    public void dispose() {
        for (Texture texture : textures.values()) texture.dispose();
    }
}
