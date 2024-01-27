package org.pokesplash.legendaryspawns.config;

public class Booster {
    private double spawnRate;
    private double spawnTime;
    private double boosterDuration;

    public Booster(double spawnRate, double spawnTime, double boosterDuration) {
        this.spawnRate = spawnRate;
        this.spawnTime = spawnTime;
        this.boosterDuration = boosterDuration;
    }

    public double getSpawnRate() {
        return spawnRate;
    }

    public double getSpawnTime() {
        return spawnTime;
    }

    public double getBoosterDuration() {
        return boosterDuration;
    }
}
