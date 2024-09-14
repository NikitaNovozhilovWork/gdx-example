package io.github.spaceexplorers.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Asteroid {

    private Scene scene;
    private Vector3 position;
    private float rotation; // Rotation around Y axis (yaw)
    private float speed; // Direction and speed
    private BoundingBox boundingBox;
    private boolean destroyed = false;

    public Asteroid(Vector3 initialPosition, float rotation, float speed) {
        this.scene = new Scene(new GLTFLoader().load(Gdx.files.internal("model/Asteroid.gltf")).scene);
        this.position = new Vector3(initialPosition);
        this.rotation = rotation;
        this.speed = speed;
        this.boundingBox = new BoundingBox();
        scene.animationController.setAnimation("AIdle", -1);
        scene.modelInstance.calculateBoundingBox(boundingBox);
    }

    // Update the asteroid's position and check if it's time to remove it
    public void update(float deltaTime) {
        position.x += Math.sin(Math.toRadians(rotation)) * speed * deltaTime;
        position.z += Math.cos(Math.toRadians(rotation)) * speed * deltaTime;

        // Update spacecraft transform in the scene
        scene.modelInstance.transform.setToTranslation(position);

        // Update bounding box position
        scene.modelInstance.calculateBoundingBox(boundingBox);
        boundingBox.set(boundingBox.min.add(position), boundingBox.max.add(position));
    }

    public Vector3 getPosition() {
        return position;
    }

    public Scene getScene() {
        return scene;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
