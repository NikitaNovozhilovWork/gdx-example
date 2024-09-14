package io.github.spaceexplorers.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.spaceexplorers.SpaceExplorers;

public class MainMenuScreen extends DefaultScreen implements Screen {

    private Stage stage;

    public MainMenuScreen(SpaceExplorers game) {
        super(game);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load a Skin (for styling UI elements)
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create a table for layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label main = new Label("Welcome, Space Explorer", skin);
        main.setFontScale(1.5f);
        table.add(main).pad(10);

        Label description = new Label("Controls:", skin);
        description.setFontScale(1.2f);
        table.row();
        table.add(description).pad(10);

        Label wasd = new Label("WS/AD - acceleration/yaw", skin);
        wasd.setFontScale(1.2f);
        table.row();
        table.add(wasd).pad(10);

        Label shoot = new Label("Space/Left mouse button - shoot", skin);
        shoot.setFontScale(1.2f);
        table.row();
        table.add(shoot).pad(10);

        Label lookBack = new Label("Middle mouse button - look back", skin);
        lookBack.setFontScale(1.2f);
        table.row();
        table.add(lookBack).pad(10);


        // Create a "Play" button
        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(e -> {
            if (playButton.isPressed()) {
                game.setScreen(new GameScreen(game)); // Go to the gameplay screen
            }
            return true;
        });

        // Create a "Settings" button
        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(e -> {
            if (settingsButton.isPressed()) {
                game.setScreen(new SettingsScreen(game)); // Go to the settings screen
            }
            return true;
        });

        // Add buttons to the table
        table.row();
        table.add(playButton).pad(10);
        table.row();
        table.add(settingsButton).pad(10);

        // Add table to stage
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
