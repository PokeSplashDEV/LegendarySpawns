package org.pokesplash.legendaryspawns.config;

import com.google.gson.Gson;
import org.pokesplash.legendaryspawns.LegendarySpawns;
import org.pokesplash.legendaryspawns.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Announcer {
	private boolean announceShinies;
	private String shinySpawnMessage;
	private String shinyCaughtMessage;
	private boolean announceLegendaries;
	private String legendarySpawnMessage;
	private String legendaryCaughtMessage;
	private boolean announceUltrabeasts;
	private String ultrabeastSpawnMessage;
	private String ultrabeastCaughtMessage;
	private boolean announceParadox;
	private String paradoxSpawnMessage;
	private String paradoxCaughtMessage;
	private boolean spawnHoverable;
	private boolean captureHoverable;


	public Announcer() {
		announceShinies = true;
		shinySpawnMessage = "§aA §eShiny §b{pokemon} §ahas spawned in a §b{biome} §aat §6{x} {y} {z} §anear §b{player}";
		shinyCaughtMessage = "§b{player} §acaught a §eShiny §b{pokemon}";
		announceLegendaries = true;
		legendarySpawnMessage = "§aA §5Legendary §b{pokemon} §ahas spawned in a §b{biome} at §6{x} {y} {z} §anear §b{player}";
		legendaryCaughtMessage = "§b{player} §acaught a §5Legendary §b{pokemon}";
		announceUltrabeasts = true;
		ultrabeastSpawnMessage = "§aA §3Ultrabeast §b{pokemon} §ahas spawned in a §b{biome} §aat §6{x} {y} {z} §anear " +
				"§b{player}";
		ultrabeastCaughtMessage = "§b{player} §acaught an §3Ultrabeast §b{pokemon}";
		announceParadox = true;
		paradoxSpawnMessage = "§aA §3Paradox §b{pokemon} §ahas spawned in a §b{biome} §aat §6{x} {y} {z} §anear " +
				"§b{player}";
		paradoxCaughtMessage = "§b{player} §acaught an §3Paradox §b{pokemon}";
		captureHoverable = true;
		spawnHoverable = true;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(LegendarySpawns.BASE_PATH,
				"announcer.json", el -> {
					Gson gson = Utils.newGson();
					Announcer cfg = gson.fromJson(el, Announcer.class);
					announceShinies = cfg.isAnnounceShinies();
					shinySpawnMessage = cfg.getShinySpawnMessage();
					announceLegendaries = cfg.isAnnounceLegendaries();
					legendarySpawnMessage = cfg.getLegendarySpawnMessage();
					announceUltrabeasts = cfg.isAnnounceUltrabeasts();
					ultrabeastSpawnMessage = cfg.getUltrabeastSpawnMessage();
					announceParadox = cfg.isAnnounceParadox();
					paradoxSpawnMessage = cfg.getParadoxSpawnMessage();
					shinyCaughtMessage = cfg.getShinyCaughtMessage();
					legendaryCaughtMessage = cfg.getLegendaryCaughtMessage();
					ultrabeastCaughtMessage = cfg.getUltrabeastCaughtMessage();
					paradoxCaughtMessage = cfg.getParadoxCaughtMessage();
					spawnHoverable = cfg.isSpawnHoverable();
					captureHoverable = cfg.isCaptureHoverable();
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

	public String getShinySpawnMessage() {
		return shinySpawnMessage;
	}

	public String getLegendarySpawnMessage() {
		return legendarySpawnMessage;
	}

	public boolean isAnnounceUltrabeasts() {
		return announceUltrabeasts;
	}

	public String getUltrabeastSpawnMessage() {
		return ultrabeastSpawnMessage;
	}

	public boolean isAnnounceParadox() {
		return announceParadox;
	}

	public String getParadoxSpawnMessage() {
		return paradoxSpawnMessage;
	}

	public String getShinyCaughtMessage() {
		return shinyCaughtMessage;
	}

	public String getLegendaryCaughtMessage() {
		return legendaryCaughtMessage;
	}

	public String getUltrabeastCaughtMessage() {
		return ultrabeastCaughtMessage;
	}

	public String getParadoxCaughtMessage() {
		return paradoxCaughtMessage;
	}

	public boolean isSpawnHoverable() {
		return spawnHoverable;
	}

	public boolean isCaptureHoverable() {
		return captureHoverable;
	}

	public void setAnnounceShinies(boolean announceShinies) {
		this.announceShinies = announceShinies;
	}

	public void setAnnounceLegendaries(boolean announceLegendaries) {
		this.announceLegendaries = announceLegendaries;
	}

	public void setAnnounceUltrabeasts(boolean announceUltrabeasts) {
		this.announceUltrabeasts = announceUltrabeasts;
	}

	public void setAnnounceParadox(boolean announceParadox) {
		this.announceParadox = announceParadox;
	}

	private void write() {
		Gson gson = Utils.newGson();
		CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(LegendarySpawns.BASE_PATH,
				"announcer.json", gson.toJson(this));

		if (!futureWrite.join()) {
			LegendarySpawns.LOGGER.fatal("Could not write Announcer for " + LegendarySpawns.MOD_ID + ".");
		}
	}
}
