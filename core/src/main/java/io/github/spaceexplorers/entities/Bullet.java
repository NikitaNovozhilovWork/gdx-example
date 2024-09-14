package io.github.spaceexplorers.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Bullet {

    private Scene scene;
    private Vector3 position;
    private Vector3 velocity;
    private float timeToLive; // In seconds
    private BoundingBox boundingBox;

    public Bullet(Vector3 initialPosition, Vector3 velocity, float timeToLive) {
        this.scene = new Scene(getBulletModel());
        this.position = new Vector3(initialPosition);
        this.velocity = new Vector3(velocity);
        this.timeToLive = timeToLive;
        this.scene.modelInstance.transform.setToTranslation(position);

        // Initialize bounding box
        this.boundingBox = new BoundingBox();
        scene.modelInstance.calculateBoundingBox(boundingBox);
    }

    public void update(float deltaTime) {
        timeToLive -= deltaTime;
        position.add(velocity.x * deltaTime, velocity.y * deltaTime, velocity.z * deltaTime);
        scene.modelInstance.transform.setToTranslation(position);

        // Update bounding box position
        scene.modelInstance.calculateBoundingBox(boundingBox);
        boundingBox.set(boundingBox.min.add(position), boundingBox.max.add(position));
    }

    public boolean isExpired() {
        return timeToLive <= 0;
    }

    public Scene getScene() {
        return scene;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    private Model getBulletModel() {
        return new ModelBuilder().createBox(
            0.3f, 0.3f, 0.3f, // Small cube size
            new Material(PBRColorAttribute.createBaseColorFactor(Color.RED)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
    }
}
