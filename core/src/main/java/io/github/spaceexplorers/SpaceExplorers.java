package io.github.spaceexplorers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.spaceexplorers.entities.Settings;
import io.github.spaceexplorers.screen.MainMenuScreen;

public class SpaceExplorers extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public Settings settings = new Settings();

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
