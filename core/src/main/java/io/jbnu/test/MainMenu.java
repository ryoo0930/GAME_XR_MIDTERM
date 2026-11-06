package io.jbnu.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** Text-only menu with a camera independent of the scrolling game world. */
public class MainMenu implements Disposable {
    public enum Action { NONE, START, QUIT }

    private static final float WIDTH = 1280;
    private static final float HEIGHT = 720;
    private static final String[] MAIN_ITEMS = {"Game Start", "Option", "Quit"};
    private final FitViewport viewport = new FitViewport(WIDTH, HEIGHT);
    private final BitmapFont font = new BitmapFont();
    private final GlyphLayout layout = new GlyphLayout();
    private final Vector2 mouse = new Vector2();
    private final Rectangle[] buttons = {new Rectangle(), new Rectangle(), new Rectangle()};
    private boolean options;
    private int selected;
    private int lastMouseX = -1;
    private int lastMouseY = -1;

    public MainMenu() {
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void show() {
        options = false;
        selected = 0;
        lastMouseX = Gdx.input.getX();
        lastMouseY = Gdx.input.getY();
    }

    public Action update() {
        int count = options ? 2 : MAIN_ITEMS.length;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && options) {
            options = false;
            selected = 1;
            return Action.NONE;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            selected = (selected + count - 1) % count;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            selected = (selected + 1) % count;
        }

        updateButtonBounds(count);
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();
        mouse.set(mouseX, mouseY);
        viewport.unproject(mouse);
        boolean insideViewport = mouse.x >= 0 && mouse.x <= WIDTH && mouse.y >= 0 && mouse.y <= HEIGHT;
        boolean clicked = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        boolean mouseMoved = mouseX != lastMouseX || mouseY != lastMouseY;
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        int hovered = -1;
        if (insideViewport) {
            for (int i = 0; i < count; i++) {
                if (buttons[i].contains(mouse)) hovered = i;
            }
        }
        if (hovered >= 0 && (mouseMoved || clicked)) selected = hovered;
        boolean activate = Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
            || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || (clicked && hovered >= 0);
        if (!activate) return Action.NONE;

        if (options) {
            if (selected == 0) {
                if (Gdx.graphics.isFullscreen()) Gdx.graphics.setWindowedMode((int) WIDTH, (int) HEIGHT);
                else Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                options = false;
                selected = 1;
            }
            return Action.NONE;
        }
        if (selected == 0) return Action.START;
        if (selected == 2) return Action.QUIT;
        options = true;
        selected = 0;
        return Action.NONE;
    }

    private String label(int index) {
        if (!options) return MAIN_ITEMS[index];
        return index == 0 ? "Fullscreen: " + (Gdx.graphics.isFullscreen() ? "ON" : "OFF") : "Back";
    }

    private void updateButtonBounds(int count) {
        font.getData().setScale(2.5f);
        for (int i = 0; i < count; i++) {
            layout.setText(font, label(i));
            buttons[i].set((WIDTH - layout.width) / 2 - 30, 365 - i * 80 - layout.height - 15,
                layout.width + 60, layout.height + 30);
        }
    }

    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.045f, 0.06f, 0.09f, 1);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        drawCentered(batch, options ? "Option" : "GameXR Mid Term Project", 555, 4, Color.WHITE);
        int count = options ? 2 : MAIN_ITEMS.length;
        for (int i = 0; i < count; i++) {
            drawCentered(batch, label(i), 365 - i * 80, 2.5f, i == selected ? Color.CYAN : Color.LIGHT_GRAY);
        }
        drawCentered(batch, "Arrow Keys / Enter  |  Mouse Click", 70, 1.2f, Color.GRAY);
        batch.end();
    }

    private void drawCentered(SpriteBatch batch, String text, float y, float scale, Color color) {
        font.getData().setScale(scale);
        font.setColor(color);
        layout.setText(font, text);
        font.draw(batch, layout, (WIDTH - layout.width) / 2, y);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
