package io.jbnu.test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Stage2 implements Stage {
    private final Rectangle flag = new Rectangle(2420, 344, 20, 100);
    private final Array<Rectangle> blocks = new Array<>();

    public Stage2() {
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

    @Override
    public float getWorldWidth() {
        return 2600;
    }

    @Override
    public Rectangle getFlag() {
        return flag;
    }

    @Override
    public Array<Rectangle> getBlocks() {
        return blocks;
    }
}
