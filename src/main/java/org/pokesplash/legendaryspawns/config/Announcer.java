package org.pokesplash.legendaryspawns.config;

import com.google.gson.Gson;
import org.pokesplash.legendaryspawns.LegendarySpawns;
import org.pokesplash.legendaryspawns.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class Announcer {
	private boolean announceShinies;
	private String shinyMessage;
	private boolean announceLegendaries;
	private String legendaryMessage;
	private boolean announceUltrabeasts;
	private String ultrabeastMessage;
	private boolean announceParadox;
	private String paradoxMessage;


	public Announcer() {
		announceShinies = true;
		shinyMessage = "§aA §3Shiny §b{pokemon} §ahas spawned in a §b{biome} §aat §6{x} {y} {z} §anear §b{player}";
		announceLegendaries = true;
		legendaryMessage = "§aA §5Legendary §b{pokemon} §ahas spawned in a §b{biome} at §6{x} {y} {z} §anear §b{player}";
		announceUltrabeasts = true;
		ultrabeastMessage = "§aA §3Ultrabeast §b{pokemon} §ahas spawned in a §b{biome} §aat §6{x} {y} {z} §anear " +
				"§b{player}";
		announceParadox = true;
		paradoxMessage = "§aA §3Paradox §b{pokemon} §ahas spawned in a §b{biome} §aat §6{x} {y} {z} §anear " +
				"§b{player}";
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(LegendarySpawns.BASE_PATH,
				"announcer.json", el -> {
					Gson gson = Utils.newGson();
					Announcer cfg = gson.fromJson(el, Announcer.class);
					announceShinies = cfg.isAnnounceShinies();
					shinyMessage = cfg.getShinyMessage();
					announceLegendaries = cfg.isAnnounceLegendaries();
					legendaryMessage = cfg.getLegendaryMessage();
					announceUltrabeasts = cfg.isAnnounceUltrabeasts();
					ultrabeastMessage = cfg.getUltrabeastMessage();
					announceParadox = cfg.isAnnounceParadox();
					paradoxMessage = cfg.getParadoxMessage();
				});

		if (!futureRead.join()) {
			LegendarySpawns.LOGGER.info("No announcer.json file found for " + LegendarySpawns.MOD_ID + ". Attempting to generate" +
					" " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(LegendarySpawns.BASE_PATH,
					"announcer.json", data);

			if (!futureWrite.join()) {
				LegendarySpawns.LOGGER.fatal("Could not write Announcer for " + LegendarySpawns.MOD_ID + ".");
			}
			return;
		}
		LegendarySpawns.LOGGER.info(LegendarySpawns.MOD_ID + " announcer file read successfully");
	}

	public boolean isAnnounceShinies() {
		return announceShinies;
	}

	public boolean isAnnounceLegendaries() {
		return announceLegendaries;
	}

	public String getShinyMessage() {
		return shinyMessage;
	}

	public String getLegendaryMessage() {
		return legendaryMessage;
	}

	public boolean isAnnounceUltrabeasts() {
		return announceUltrabeasts;
	}

	public String getUltrabeastMessage() {
		return ultrabeastMessage;
	}

	public boolean isAnnounceParadox() {
		return announceParadox;
	}

	public String getParadoxMessage() {
		return paradoxMessage;
	}
}
