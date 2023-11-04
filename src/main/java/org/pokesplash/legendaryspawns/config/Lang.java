package org.pokesplash.legendaryspawns.config;

import com.google.gson.Gson;
import org.pokesplash.legendaryspawns.LegendarySpawns;
import org.pokesplash.legendaryspawns.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Lang {
	private String title;
	private String fillerMaterial;

	public Lang() {
		title = LegendarySpawns.MOD_ID;
		fillerMaterial = "minecraft:white_stained_glass_pane";
	}

	public String getTitle() {
		return title;
	}

	public String getFillerMaterial() {
		return fillerMaterial;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(LegendarySpawns.BASE_PATH, "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);
					title = lang.getTitle();
					fillerMaterial = lang.getFillerMaterial();
				});

		if (!futureRead.join()) {
			LegendarySpawns.LOGGER.info("No lang.json file found for " + LegendarySpawns.MOD_ID + ". Attempting to " +
					"generate " +
					"one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(LegendarySpawns.BASE_PATH, "lang.json", data);

			if (!futureWrite.join()) {
				LegendarySpawns.LOGGER.fatal("Could not write lang.json for " + LegendarySpawns.MOD_ID + ".");
			}
			return;
		}
		LegendarySpawns.LOGGER.info(LegendarySpawns.MOD_ID + " lang file read successfully.");
	}
}
