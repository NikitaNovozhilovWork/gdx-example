package io.github.spaceexplorers.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Spacecraft {

    private Scene scene;
    private Vector3 position;
    private float rotation; // Rotation around Y axis (yaw)
    private float speed;
    private BoundingBox boundingBox;
    private int health = 100;

    public Spacecraft(Vector3 initialPosition) {
        this.scene = new Scene(new GLTFLoader().load(Gdx.files.internal("model/SpaceCraft.gltf")).scene);
        this.position = initialPosition;
        this.rotation = 0f;
        this.speed = 0f;
        this.boundingBox = new BoundingBox();
        scene.animationController.setAnimation("SIdle", -1);
        scene.modelInstance.transform.setToRotation(Vector3.Z, 90);
        scene.modelInstance.calculateBoundingBox(boundingBox);
    }

    public void update(float deltaTime) {
        position.x += Math.sin(Math.toRadians(rotation)) * speed * deltaTime;
        position.z += Math.cos(Math.toRadians(rotation)) * speed * deltaTime;

        // Update spacecraft transform in the scene
        scene.modelInstance.transform.setToTranslation(position);
        scene.modelInstance.transform.rotate(Vector3.Y, rotation);

        // Update bounding box position
        scene.modelInstance.calculateBoundingBox(boundingBox);
        boundingBox.set(boundingBox.min.add(position), boundingBox.max.add(position));
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
