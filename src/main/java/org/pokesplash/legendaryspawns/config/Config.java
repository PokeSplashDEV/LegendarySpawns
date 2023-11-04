package org.pokesplash.legendaryspawns.config;

import com.google.gson.Gson;
import org.pokesplash.legendaryspawns.LegendarySpawns;
import org.pokesplash.legendaryspawns.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class Config {
	private double timer;
	private double spawnChance;
	private int numberOfIvs;
	private int radius;
	private HashMap<String, ArrayList<String>> pokemon;

	public Config() {
		timer = 20;
		spawnChance = 0.3;
		numberOfIvs = 3;
		radius = 50;
		pokemon = new HashMap<>();
		ArrayList<String> biomes = new ArrayList<>();
		biomes.add("minecraft:desert");
		pokemon.put("xerneas", biomes);
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(LegendarySpawns.BASE_PATH,
				"config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					timer = cfg.getTimer();
					spawnChance = cfg.getSpawnChance();
					pokemon = cfg.getPokemon();
					numberOfIvs = cfg.getNumberOfIvs();
					radius = cfg.getRadius();
				});

		if (!futureRead.join()) {
			LegendarySpawns.LOGGER.info("No config.json file found for " + LegendarySpawns.MOD_ID + ". Attempting to generate" +
					" " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(LegendarySpawns.BASE_PATH,
					"config.json", data);

			if (!futureWrite.join()) {
				LegendarySpawns.LOGGER.fatal("Could not write config for " + LegendarySpawns.MOD_ID + ".");
			}
			return;
		}
		LegendarySpawns.LOGGER.info(LegendarySpawns.MOD_ID + " config file read successfully");
	}

	public double getTimer() {
		return timer;
	}

	public double getSpawnChance() {
		return spawnChance;
	}

	public int getNumberOfIvs() {
		return numberOfIvs;
	}

	public int getRadius() {
		return radius;
	}

	public HashMap<String, ArrayList<String>> getPokemon() {
		return pokemon;
	}
}
