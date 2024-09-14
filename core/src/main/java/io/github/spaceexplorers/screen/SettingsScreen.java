package io.github.spaceexplorers.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.spaceexplorers.SpaceExplorers;
import io.github.spaceexplorers.entities.Settings;

public class SettingsScreen extends DefaultScreen {

    private Stage stage;

    private Label asteroidSpawnIntervalLabel;
    private Slider asteroidSpawnIntervalSlider;

    private Label maxNumberOfAsteroidsLabel;
    private Slider maxNumberOfAsteroidsSlider;

    private Label maxAsteroidSpeedLabel;
    private Slider maxAsteroidSpeedSlider;

    private Label safeZoneLabel;
    private Slider safeZoneSlider;

    private Label spawnZoneLabel;
    private Slider spawnZoneSlider;

    private Label forgetDistanceLabel;
    private Slider forgetDistanceSlider;

    private Label accelerationLabel;
    private Slider accelerationSlider;

    private Label maxSpeedLabel;
    private Slider maxSpeedSlider;

    private Label turnSpeedLabel;
    private Slider turnSpeedSlider;

    private Label bulletSpeedLabel;
    private Slider bulletSpeedSlider;


    protected SettingsScreen(SpaceExplorers game) {
        super(game);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load a Skin (for styling UI elements)
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create a table for layout
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50); // Optional padding

        maxNumberOfAsteroidsLabel = new Label("Asteroid maximum number: " + game.settings.getMaxNumberOfAsteroids(), skin);
        maxNumberOfAsteroidsSlider = new Slider(10, 1000, 10, false, skin);
        maxNumberOfAsteroidsSlider.setValue(game.settings.getMaxNumberOfAsteroids());

        table.add(maxNumberOfAsteroidsLabel).pad(10);
        table.row();
        table.add(maxNumberOfAsteroidsSlider).width(300).pad(10);

        asteroidSpawnIntervalLabel = new Label("Asteroid Spawn Interval: " + game.settings.getAsteroidSpawnInterval(), skin);
        asteroidSpawnIntervalSlider = new Slider(0.1f, 10, 0.1f, false, skin);
        asteroidSpawnIntervalSlider.setValue(game.settings.getAsteroidSpawnInterval());

        table.row();
        table.add(asteroidSpawnIntervalLabel).pad(10);
        table.row();
        table.add(asteroidSpawnIntervalSlider).width(300).pad(10);

        maxAsteroidSpeedLabel = new Label("Max Asteroid Speed: " + game.settings.getMaxAsteroidSpeed(), skin);
        maxAsteroidSpeedSlider = new Slider(10, 500, 10, false, skin);
        maxAsteroidSpeedSlider.setValue(game.settings.getMaxAsteroidSpeed());

        table.row();
        table.add(maxAsteroidSpeedLabel).pad(10);
        table.row();
        table.add(maxAsteroidSpeedSlider).width(300).pad(10);

        safeZoneLabel = new Label("Safe zone around Spacecraft: " + game.settings.getSafeZone(), skin);
        safeZoneSlider = new Slider(10, 500, 10, false, skin);
        safeZoneSlider.setValue(game.settings.getSafeZone());

        table.row();
        table.add(safeZoneLabel).pad(10);
        table.row();
        table.add(safeZoneSlider).width(300).pad(10);

        spawnZoneLabel = new Label("Asteroid spawn zone around Spacecraft: " + game.settings.getSpawnZone(), skin);
        spawnZoneSlider = new Slider(50, 1000, 10, false, skin);
        spawnZoneSlider.setValue(game.settings.getSpawnZone());

        table.row();
        table.add(spawnZoneLabel).pad(10);
        table.row();
        table.add(spawnZoneSlider).width(300).pad(10);

        forgetDistanceLabel = new Label("Distance after which asteroids are considered lost: " + game.settings.getForgetDistance(), skin);
        forgetDistanceSlider = new Slider(500, 2000, 100, false, skin);
        forgetDistanceSlider.setValue(game.settings.getForgetDistance());

        table.row();
        table.add(forgetDistanceLabel).pad(10);
        table.row();
        table.add(forgetDistanceSlider).width(300).pad(10);

        accelerationLabel = new Label("Spacecraft acceleration: " + game.settings.getAcceleration(), skin);
        accelerationSlider = new Slider(10, 100, 1, false, skin);
        accelerationSlider.setValue(game.settings.getAcceleration());

        table.row();
        table.add(accelerationLabel).pad(10);
        table.row();
        table.add(accelerationSlider).width(300).pad(10);

        maxSpeedLabel = new Label("Spacecraft maximum speed: " + game.settings.getMaxSpeed(), skin);
        maxSpeedSlider = new Slider(20, 200, 10, false, skin);
        maxSpeedSlider.setValue(game.settings.getMaxSpeed());

        table.row();
        table.add(maxSpeedLabel).pad(10);
        table.row();
        table.add(maxSpeedSlider).width(300).pad(10);

        turnSpeedLabel = new Label("Spacecraft turning speed: " + game.settings.getTurnSpeed(), skin);
        turnSpeedSlider = new Slider(20, 200, 10, false, skin);
        turnSpeedSlider.setValue(game.settings.getTurnSpeed());

        table.row();
        table.add(turnSpeedLabel).pad(10);
        table.row();
        table.add(turnSpeedSlider).width(300).pad(10);

        bulletSpeedLabel = new Label("Bullet speed: " + game.settings.getBulletSpeed(), skin);
        bulletSpeedSlider = new Slider(20, 200, 10, false, skin);
        bulletSpeedSlider.setValue(game.settings.getBulletSpeed());

        table.row();
        table.add(bulletSpeedLabel).pad(10);
        table.row();
        table.add(bulletSpeedSlider).width(300).pad(10);



        TextButton saveButton = new TextButton("Save", skin);
        saveButton.addListener(e -> {
            if (saveButton.isPressed()) {
                Settings settings = game.settings;
                settings.setMaxNumberOfAsteroids(Math.round(maxNumberOfAsteroidsSlider.getValue()));
                settings.setAsteroidSpawnInterval(asteroidSpawnIntervalSlider.getValue());
                settings.setMaxAsteroidSpeed(maxAsteroidSpeedSlider.getValue());
                settings.setSafeZone(safeZoneSlider.getValue());
                settings.setSpawnZone(spawnZoneSlider.getValue());
                settings.setForgetDistance(forgetDistanceSlider.getValue());
                settings.setAcceleration(accelerationSlider.getValue());
                settings.setMaxSpeed(maxSpeedSlider.getValue());
                settings.setTurnSpeed(turnSpeedSlider.getValue());
                settings.setBulletSpeed(bulletSpeedSlider.getValue());
                game.setScreen(new MainMenuScreen(game)); // Go to the main screen
            }
            return true;
        });

        // Add Back button to the table
        table.row();
        table.add(saveButton).padTop(20);

        // Create a button to go back to the main menu or the game
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(e -> {
            if (backButton.isPressed()) {
                game.setScreen(new MainMenuScreen(game)); // Go back to Main Menu
            }
            return true;
        });

        // Add Back button to the table
        table.row();
        table.add(backButton).padTop(20);

        // Add table to stage
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        asteroidSpawnIntervalLabel.setText("Asteroid Spawn Interval: " + asteroidSpawnIntervalSlider.getValue());
        maxAsteroidSpeedLabel.setText("Max Asteroid Speed: " + maxAsteroidSpeedSlider.getValue());
        safeZoneLabel.setText("Safe zone around Spacecraft: " + safeZoneSlider.getValue());
        spawnZoneLabel.setText("Asteroid spawn zone around Spacecraft: " + spawnZoneSlider.getValue());
        forgetDistanceLabel.setText("Distance after which asteroids are considered lost: " + forgetDistanceSlider.getValue());
        accelerationLabel.setText("Spacecraft acceleration: " + accelerationSlider.getValue());
        maxSpeedLabel.setText("Spacecraft maximum speed: " + maxSpeedSlider.getValue());
        turnSpeedLabel.setText("Spacecraft turning speed: " + turnSpeedSlider.getValue());
        bulletSpeedLabel.setText("Bullet speed: " + bulletSpeedSlider.getValue());

        // Clear the screen and render the stage (UI)
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
