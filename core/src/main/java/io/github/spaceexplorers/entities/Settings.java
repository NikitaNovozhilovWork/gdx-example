package io.github.spaceexplorers.entities;

public class Settings {

    //Asteroid settings
    private int maxNumberOfAsteroids = 100;
    private float asteroidSpawnInterval = 1f; // Time between spawns
    private float maxAsteroidSpeed = 100f;
    private float safeZone = 20f; // how far from the spacecraft new asteroids will spawn
    private float spawnZone = 50f; // max distance from the spacecraft for asteroid spawn
    private float forgetDistance = 1000f;

    //Spacecraft settings
    private float acceleration = 20f;
    private float maxSpeed = 50f;
    private float turnSpeed = 30f;
    private float bulletSpeed = 50f;

    public float getSafeZone() {
        return safeZone;
    }

    public void setSafeZone(float safeZone) {
        this.safeZone = safeZone;
    }

    public float getAsteroidSpawnInterval() {
        return asteroidSpawnInterval;
    }

    public void setAsteroidSpawnInterval(float asteroidSpawnInterval) {
        this.asteroidSpawnInterval = asteroidSpawnInterval;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getTurnSpeed() {
        return turnSpeed;
    }

    public void setTurnSpeed(float turnSpeed) {
        this.turnSpeed = turnSpeed;
    }

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public float getMaxAsteroidSpeed() {
        return maxAsteroidSpeed;
    }

    public void setMaxAsteroidSpeed(float maxAsteroidSpeed) {
        this.maxAsteroidSpeed = maxAsteroidSpeed;
    }

    public float getSpawnZone() {
        return spawnZone;
    }

    public void setSpawnZone(float spawnZone) {
        this.spawnZone = spawnZone;
    }

    public int getMaxNumberOfAsteroids() {
        return maxNumberOfAsteroids;
    }

    public void setMaxNumberOfAsteroids(int maxNumberOfAsteroids) {
        this.maxNumberOfAsteroids = maxNumberOfAsteroids;
    }

    public float getForgetDistance() {
        return forgetDistance;
    }

    public void setForgetDistance(float forgetDistance) {
        this.forgetDistance = forgetDistance;
    }
}
