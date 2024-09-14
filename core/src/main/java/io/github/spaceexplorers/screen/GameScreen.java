package io.github.spaceexplorers.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.spaceexplorers.SpaceExplorers;
import io.github.spaceexplorers.entities.Asteroid;
import io.github.spaceexplorers.entities.Bullet;
import io.github.spaceexplorers.entities.Spacecraft;
import io.github.spaceexplorers.enums.CameraMode;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreen extends DefaultScreen {

    //Scene
    private SceneManager sceneManager;

    //UI
    private Stage stage;
    private ProgressBar healthBar;
    private Label healthLabel;
    private ProgressBar speedBar;
    private Label speedLabel;
    private Label scoreLabel;
    private Label gameOverLabel;
    private Label restartLabel;
    private boolean isGameOver = false;
    private int score = 0;

    //Sound
    Sound shootSound;
    Music mainMusic;

    //Game objects
    private Spacecraft spacecraft;
    private List<Asteroid> asteroids = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();

    private float timeSinceLastAsteroid;
    private CameraMode cameraMode = CameraMode.BACK;

    public GameScreen(SpaceExplorers game) {
        super(game);

        shootSound = Gdx.audio.newSound(Gdx.files.internal("sound/221560__alaskarobotics__laser-shot.wav"));
        mainMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/753910__logicmoon__tokio-2189.mp3"));
        mainMusic.setLooping(true);

        this.timeSinceLastAsteroid = game.settings.getAsteroidSpawnInterval() - 1f;
    }

    @Override
    public void show() {
        mainMusic.play();
        initializeScene();
        initializeUI();
    }

    private void initializeUI() {
        // Setup the Stage and the Skin for UI elements
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load a Skin (a Skin defines the appearance of UI elements)
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json")); // LibGDX provides a default skin

        // Create a health bar using a ProgressBar
        healthBar = new ProgressBar(0, 100, 1, false, skin);
        healthBar.setValue(spacecraft.getHealth());
        healthBar.setAnimateDuration(0.25f);

        // Create a label for the health value
        healthLabel = new Label("Health: " + spacecraft.getHealth(), skin);
        healthLabel.setFontScale(1.2f);

        // Create a speed bar using a ProgressBar
        speedBar = new ProgressBar(-game.settings.getMaxSpeed(), game.settings.getMaxSpeed(), 1, false, skin);
        speedBar.setValue(spacecraft.getSpeed());
        speedBar.setAnimateDuration(0.25f);

        // Create a label for the health value
        speedLabel = new Label("Speed: " + spacecraft.getSpeed(), skin);
        speedLabel.setFontScale(1.2f);

        // Use a Table to layout the UI elements
        Table statusTable = new Table();
        statusTable.add(healthBar).width(200).padLeft(10).height(30);
        statusTable.row(); // Move to the next row
        statusTable.add(healthLabel).left().padLeft(20).padTop(-30);
        statusTable.row(); // Move to the next row
        // Set the width of the health bar
        statusTable.add(speedBar).width(200).padLeft(10).height(30);
        statusTable.row(); // Move to the next row
        statusTable.add(speedLabel).left().padLeft(20).padTop(-30);

        // Create a label to display the score
        scoreLabel = new Label("Score: " + score, skin);
        scoreLabel.setColor(Color.RED);
        scoreLabel.setFontScale(1.5f);

        // Create the main table to contain both the health and score sections
        Table layoutTable = new Table();
        layoutTable.top(); // Align to the top of the screen
        layoutTable.setFillParent(true); // Make the table cover the screen

        // Add the health bar on the left side
        layoutTable.add(statusTable).expandX().left().pad(10);

        // Add the score on the right side
        layoutTable.add(scoreLabel).expandX().right().padRight(30).padTop(10);

        // Create the Game Over label (hidden initially)
        gameOverLabel = new Label("Game Over! Final Score: " + score, skin);
        gameOverLabel.setFontScale(2f);
        gameOverLabel.setVisible(false); // Hide until game is over
        restartLabel = new Label("To restart the game press Enter or Space", skin);
        restartLabel.setFontScale(2f);
        restartLabel.setVisible(false); // Hide until game is over


        // Create another table to display the final result (Game Over)
        Table gameOverTable = new Table();
        gameOverTable.center(); // Center the Game Over text
        gameOverTable.setFillParent(true); // Make it cover the screen
        gameOverTable.add(gameOverLabel);
        gameOverTable.row();
        gameOverTable.add(restartLabel);

        // Add the main layout table to the stage
        stage.addActor(layoutTable);
        stage.addActor(gameOverTable); // Overlay the game over message
    }

    private void initializeScene() {
        // Initialize SceneManager
        sceneManager = new SceneManager();

        // Set up a basic camera
        PerspectiveCamera camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 1f;
        camera.far = 1000f;
        camera.position.set(0, 0, 20f);
        camera.lookAt(0f, 0f, 0f);
        sceneManager.setCamera(camera);

        FirstPersonCameraController cameraController = new FirstPersonCameraController(camera);
        Gdx.input.setInputProcessor(cameraController);

        // setup light
        DirectionalLightEx light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        Cubemap environmentCubemap = iblBuilder.buildEnvMap(1024);
        Cubemap diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        Cubemap specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        Texture brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        SceneSkybox skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);

        // Load the glTF spacecraft model using GltfImporter
        spacecraft = new Spacecraft(new Vector3(0, 0, 0));
        sceneManager.addScene(spacecraft.getScene());
    }

    @Override
    public void render(float delta) {
        handleEscape();
        if (!isGameOver) {
            // Handle spacecraft movement input
            handleInput(delta);

            // Update spacecraft position
            updateSpacecraftPosition(delta);

            // Update and render asteroids
            spawnAndUpdateAsteroids(delta);

            // Update and render bullets
            updateBullets(delta);

            // Check for collisions between bullets and asteroids
            checkCollisions();

            updateUI();
        } else {
            handleRestart();
        }

        // Render the scene using SceneManager
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.update(delta);
        sceneManager.render();

        // Update and render the UI
        stage.act(delta);
        stage.draw(); // Draw the UI on top of the 3D game
    }

    private void handleRestart() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    private void updateUI() {
        // Update the health bar and label based on the spacecraft's health
        healthBar.setValue(spacecraft.getHealth());
        healthLabel.setText("Health: " + spacecraft.getHealth());
        speedBar.setValue(spacecraft.getSpeed());
        speedLabel.setText("Speed: " + spacecraft.getSpeed());
        scoreLabel.setText("Score: " + score);

        if (spacecraft.getHealth() <= 0) {
            gameOver();
        }
    }

    private void handleEscape() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    private void handleInput(float deltaTime) {
        // Accelerate with W, Decelerate with S
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            spacecraft.setSpeed(Math.min(spacecraft.getSpeed() + game.settings.getAcceleration() * deltaTime, game.settings.getMaxSpeed()));
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            spacecraft.setSpeed(Math.max(spacecraft.getSpeed() - game.settings.getAcceleration() * deltaTime, -game.settings.getMaxSpeed()));
        }

        // Turn left with A, right with D
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            spacecraft.setRotation(spacecraft.getRotation() + game.settings.getTurnSpeed() * deltaTime);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            spacecraft.setRotation(spacecraft.getRotation() - game.settings.getTurnSpeed() * deltaTime);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            fireBullet();
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            cameraMode = CameraMode.FRONT;
        } else if (cameraMode != CameraMode.BACK) {
            cameraMode = CameraMode.BACK;
        }
    }

    private void fireBullet() {
        // Bullet starts at the spacecraft's position and moves in the spacecraft's direction
        Vector3 bulletPosition = new Vector3(spacecraft.getPosition());
        Vector3 bulletVelocity = new Vector3(
                (float) Math.sin(Math.toRadians(spacecraft.getRotation())),
                0,
                (float) Math.cos(Math.toRadians(spacecraft.getRotation()))
        ).nor().scl(game.settings.getBulletSpeed() + spacecraft.getSpeed());

        Bullet bullet = new Bullet(bulletPosition, bulletVelocity, 2f); // 2 seconds lifetime
        bullets.add(bullet);
        sceneManager.addScene(bullet.getScene());
        shootSound.play();
    }

    private void updateSpacecraftPosition(float deltaTime) {
        spacecraft.update(deltaTime);

        // Update camera to follow the spacecraft
        if (cameraMode == CameraMode.BACK) {
            sceneManager.camera.position.set(
                spacecraft.getPosition().x - (float) (10 * Math.sin(Math.toRadians(spacecraft.getRotation()))),
                spacecraft.getPosition().y + 5,
                spacecraft.getPosition().z - (float) (10 * Math.cos(Math.toRadians(spacecraft.getRotation())))
            );
        } else {
            sceneManager.camera.position.set(
                spacecraft.getPosition().x - (float) (-10 * Math.sin(Math.toRadians(spacecraft.getRotation()))),
                spacecraft.getPosition().y + 5,
                spacecraft.getPosition().z - (float) (-10 * Math.cos(Math.toRadians(spacecraft.getRotation())))
            );
        }
        sceneManager.camera.up.set(Vector3.Y);
        sceneManager.camera.lookAt(spacecraft.getPosition());
        sceneManager.camera.update();
    }

    private void spawnAndUpdateAsteroids(float deltaTime) {
        timeSinceLastAsteroid += deltaTime;
        if (asteroids.size() < game.settings.getMaxNumberOfAsteroids()
            && timeSinceLastAsteroid > game.settings.getAsteroidSpawnInterval()) {
            // Spawn a new asteroid every `asteroidSpawnInterval` seconds
            timeSinceLastAsteroid = 0f;

            // Random asteroid in spawn zone
            Asteroid asteroid = getNewAsteroid();
            asteroids.add(asteroid);
            sceneManager.addScene(asteroid.getScene());
        }

        // Update all asteroids and remove expired ones
        Iterator<Asteroid> iterator = asteroids.iterator();
        while (iterator.hasNext()) {
            Asteroid asteroid = iterator.next();
            asteroid.update(deltaTime);

            if (spacecraft.getPosition().dst(asteroid.getPosition()) > game.settings.getForgetDistance()) {
                sceneManager.removeScene(asteroid.getScene());
                iterator.remove();
            }
        }
    }

    private Asteroid getNewAsteroid() {
        float asteroidDistance = (float) (game.settings.getSafeZone() + ((game.settings.getSpawnZone() - game.settings.getSafeZone()) * Math.random()));
        float asteroidPositionAngle = (float) Math.random() * 360;
        Vector3 asteroidVector = new Vector3(
            spacecraft.getPosition().x + (float) (asteroidDistance * Math.sin(Math.toRadians(asteroidPositionAngle))),
            0,
            spacecraft.getPosition().z + (float) (asteroidDistance * Math.cos(Math.toRadians(asteroidPositionAngle)))
        );
        // to have 30 degrees spread: 180 + asteroidPositionAngle + ((float) Math.random() * 30) - 15
        return new Asteroid(asteroidVector, 165 + asteroidPositionAngle + ((float) Math.random() * 30), 10);
    }

    private void updateBullets(float deltaTime) {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update(deltaTime);

            // Remove bullet if its time to live is up
            if (bullet.isExpired()) {
                sceneManager.removeScene(bullet.getScene());
                iterator.remove();
            }
        }
    }

    private void checkCollisions() {
        BoundingBox spacecraftBox = spacecraft.getBoundingBox();

        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            BoundingBox asteroidBox = asteroid.getBoundingBox();

            Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                BoundingBox bulletBox = bullet.getBoundingBox();


                // Check if the bullet's bounding box intersects the asteroid's bounding box
                if (bulletBox.intersects(asteroidBox)) {
                    score++;
                    asteroid.setDestroyed(true);
                    // Collision detected - remove both bullet and asteroid
                    bulletIterator.remove();
                    sceneManager.removeScene(bullet.getScene());
                    asteroidIterator.remove();
                    sceneManager.removeScene(asteroid.getScene());
                    break; // Break inner loop since the bullet is already removed
                }
            }

            if (!asteroid.isDestroyed() && asteroidBox.intersects(spacecraftBox)) {
                asteroid.setDestroyed(true);
                asteroidIterator.remove();
                sceneManager.removeScene(asteroid.getScene());
                spacecraft.setHealth(spacecraft.getHealth() - 20);
            }
        }
    }

    private void gameOver() {
        isGameOver = true; // Stop game updates

        // Display the Game Over message and final score
        gameOverLabel.setText("Game Over! Final Score: " + score);
        gameOverLabel.setVisible(true); // Show the Game Over message
        restartLabel.setVisible(true);
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
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
        shootSound.dispose();
        sceneManager.dispose();
        mainMusic.dispose();
        stage.dispose();
    }
}
